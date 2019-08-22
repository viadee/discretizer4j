package de.viadee.discretizers4j;

import de.viadee.discretizers4j.impl.UniqueValueDiscretizer;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Abstract discretizer managing both the discretization and un-discretization for all discretizers that choose to work
 * with the {@link DiscretizationTransition}
 */
public abstract class AbstractDiscretizer implements Discretizer {
    private final boolean isSupervised;

    private List<DiscretizationTransition> discretizationTransitions;

    /**
     * Constructs the instance
     *
     * @param isSupervised true, if this is a supervised discretizer
     */
    protected AbstractDiscretizer(boolean isSupervised) {
        this.isSupervised = isSupervised;
    }

    @Override
    public Collection<DiscretizationTransition> getTransitions() {
        return discretizationTransitions;
    }

    /**
     * Fits the discretizer and passes all values that it might get asked to discretize
     *
     * @param values the domain
     */
    public void fit(Serializable[] values) {
        this.fit(values, null);
    }

    @Override
    public void fit(Serializable[] values, Double[] labels) {
        if (values == null || values.length == 0) {
            // all values are single class values
            throw new IllegalArgumentException("No values for fitting procedure passed");
        }

        if (isSupervised && labels == null) {
            throw new IllegalArgumentException("Labels need to be specified for supervised discretizers");
        }

        if (isSupervised && labels.length != values.length) {
            throw new IllegalArgumentException("Labels need to be of same length as column values");
        }

        if (discretizationTransitions != null) {
            throw new IllegalArgumentException("Discretizer has already been fitted");
        }

        if(Stream.of(values).anyMatch(v -> !(v instanceof Number)) && !(this instanceof UniqueValueDiscretizer)) {
            throw new IllegalArgumentException("Non-Numeric values can only be discretized with UniqueValue");
        }

        if(labels == null) {
            Arrays.sort(values);
            this.discretizationTransitions = fitCreateTransitions(values, labels);
        } else {
            List<AbstractMap.SimpleImmutableEntry<Double, Double>> keyValuePairs = IntStream.range(0, values.length)
                    .mapToObj(i -> new AbstractMap.SimpleImmutableEntry<>(((Number) values[i]).doubleValue(), labels[i]))
                    .sorted(Comparator.comparing(AbstractMap.SimpleImmutableEntry::getKey))
                    .collect(Collectors.toList());
            this.discretizationTransitions = fitCreateTransitions(keyValuePairs);
        }

        final long distinctDiscretizedValues = discretizationTransitions.stream()
                .map(DiscretizationTransition::getDiscretizedValue).distinct().count();
        if (distinctDiscretizedValues != discretizationTransitions.size()) {
            this.discretizationTransitions = null;
            // This could be required for some scenarios.. Not yet, though
            throw new IllegalArgumentException("Discretization targets are ambiguous");
        }

        // In case all origins are numeric we need to set open lower and upper boundaries
        if (!this.discretizationTransitions.isEmpty() && this.discretizationTransitions.stream()
                .allMatch(d -> d.getDiscretizationOrigin() instanceof NumericDiscretizationOrigin)) {
            DiscretizationTransition minDisc = this.discretizationTransitions.get(0);
            DiscretizationTransition maxDisc = this.discretizationTransitions.get(0);
            for (final DiscretizationTransition current : this.discretizationTransitions) {
                if (((NumericDiscretizationOrigin) current.getDiscretizationOrigin()).getMinValue().doubleValue() <
                        ((NumericDiscretizationOrigin) minDisc.getDiscretizationOrigin()).getMinValue().doubleValue())
                    minDisc = current;
                if (((NumericDiscretizationOrigin) current.getDiscretizationOrigin()).getMaxValue().doubleValue() >
                        ((NumericDiscretizationOrigin) maxDisc.getDiscretizationOrigin()).getMaxValue().doubleValue())
                    maxDisc = current;
            }
            ((NumericDiscretizationOrigin) minDisc.getDiscretizationOrigin()).setFirst(true);
            ((NumericDiscretizationOrigin) maxDisc.getDiscretizationOrigin()).setLast(true);
        }
    }

    /**
     * Fits on the data
     *
     * @param values the values to be fitted on
     * @param labels the labels. != null, iff supervised
     * @return a {@link Collection} containing the {@link DiscretizationTransition}s
     */
    protected abstract List<DiscretizationTransition> fitCreateTransitions(Serializable[] values, Double[] labels);

    /**
     * Fits on the data
     *
     * @param keyValuePairs the values and labels to be fitted
     * @return a {@link Collection} containing the {@link DiscretizationTransition}s
     */
    protected abstract List<DiscretizationTransition> fitCreateTransitions(List<AbstractMap.SimpleImmutableEntry<Double, Double>> keyValuePairs);

    @Override
    public Double apply(Serializable serializable) {
        final DiscretizationTransition discretizationTransition = discretizationTransitions.stream()
                .filter(d -> d.getDiscretizationOrigin().canDiscretize(serializable))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Could not find transition for " + serializable));
        return discretizationTransition.getDiscretizedValue();
    }
}
