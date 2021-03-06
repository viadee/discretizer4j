package de.viadee.discretizers4j;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class AbstractSupervisedDiscretizer extends AbstractDiscretizer {

    /**
     * Constructs the instance
     *
     * @param isSupervised true, if this is a supervised discretizer
     */
    protected AbstractSupervisedDiscretizer(boolean isSupervised) {
        super(isSupervised);
    }

    /**
     * generates initial Intervals. Values with same class are merged to a Interval. If a value has several classes, all index with
     * this value will be a separate Interval.
     *
     * @param keyValuePairs, Array of Attribute Class.
     * @return initial List of Intervals
     */
    protected List<Interval> equalClassSplit(final List<AbstractMap.SimpleImmutableEntry<Double, Double>> keyValuePairs) {
        final List<Interval> resultDiscTrans = new ArrayList<>();
        int lowerLimit = 0;
        int amountSameValue = 0;
        for (int i = 1; i < keyValuePairs.size(); i++) {
            final Number currentKey = keyValuePairs.get(i).getKey();
            final Double currentValue = keyValuePairs.get(i).getValue();

            if (!currentKey.equals(keyValuePairs.get(i - 1).getKey())) {
                amountSameValue = 0;
                if (!currentValue.equals(keyValuePairs.get(i - 1).getValue())) {
                    resultDiscTrans.add(new Interval(lowerLimit, i - 1, keyValuePairs));
                    lowerLimit = i;
                }
            } else {
                amountSameValue++;
                if (!currentValue.equals(keyValuePairs.get(i - amountSameValue).getValue())) {
                    if (!resultDiscTrans.isEmpty() && resultDiscTrans.get(resultDiscTrans.size() - 1).getEnd() != i - amountSameValue - 1) {
                        resultDiscTrans.add(new Interval(lowerLimit, i - 1 - amountSameValue, keyValuePairs));
                    }

                    if (i != keyValuePairs.size() - 1 && !keyValuePairs.get(i + 1).getKey().equals(keyValuePairs.get(i).getKey())) {
                        lowerLimit = i - amountSameValue;
                        resultDiscTrans.add(new Interval(lowerLimit, i, keyValuePairs));
                        lowerLimit = i;
                        amountSameValue = 0;
                    }
                }
            }
        }
        resultDiscTrans.add(new Interval(lowerLimit, keyValuePairs.size() - 1, keyValuePairs));

        return resultDiscTrans;
    }

    @Override
    protected final List<DiscretizationTransition> fitCreateTransitions(Serializable[] values, Double[] labels) {
        final List<AbstractMap.SimpleImmutableEntry<Double, Double>> keyValuePairs = IntStream.range(0, values.length)
                .mapToObj(i -> new AbstractMap.SimpleImmutableEntry<>(((Number) values[i]).doubleValue(), labels[i]))
                .sorted(Comparator.comparing(AbstractMap.SimpleImmutableEntry::getKey))
                .collect(Collectors.toList());
        return fitCreateTransitions(keyValuePairs);
    }

    /**
     * Fits on the data
     *
     * @param keyValuePairs the values and labels to be fitted
     * @return a {@link Collection} containing the {@link DiscretizationTransition}s
     */
    protected abstract List<DiscretizationTransition> fitCreateTransitions(List<AbstractMap.SimpleImmutableEntry<Double, Double>> keyValuePairs);
}
