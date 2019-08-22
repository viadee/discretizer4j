package de.viadee.discretizers4j;

import java.io.Serializable;
import java.util.Collection;
import java.util.function.Function;

/**
 * Represents functionality every column needs to provide: discretization.
 * <p>
 * Discretization is used by Anchors tabular to perturb instances and find similar neighbours.
 * <p>
 * This interface supports both unsupervised and supervised discretizers. In supervised cases, labels are passed to
 * {@link #fit(Serializable[], Double[])}
 */
public interface Discretizer extends Function<Serializable, Double>, Serializable {

    /**
     * Applies this discretizer to the passed data
     *
     * @param data the data to discretize
     * @return the discretized data
     */
    default Double[] apply(Serializable[] data) {
        Double[] discretizedData = new Double[data.length];
        for (int i = 0; i < data.length; i++) {
            discretizedData[i] = this.apply(data[i]);
        }

        return discretizedData;
    }

    /**
     * Fits the discretizer and passes all values that it might get asked to discretize
     *
     * @param values the domain
     * @param labels the labels or null, iff unsupervised. Caution: labels are required to be discretized first
     */
    void fit(Serializable[] values, Double[] labels);

    /**
     * This method returns the relation for a certain discretized value.
     * <p>
     * This allows to unApply a discretization and obtain the original value
     *
     * @param discretizedValue the value to get the relation for
     * @return the {@link DiscretizationTransition}
     */
    default DiscretizationTransition getTransition(Double discretizedValue) {
        return getTransitions().stream().filter(d -> discretizedValue.equals(d.getDiscretizedValue())).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Could not find transition for discretized value " +
                        discretizedValue));
    }

    /**
     * This method returns all stored transitions
     *
     * @return the {@link DiscretizationTransition}s
     */
    Collection<DiscretizationTransition> getTransitions();
}
