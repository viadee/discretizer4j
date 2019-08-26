package de.viadee.discretizers4j;

import java.io.Serializable;

/**
 * The left hand side of the origin
 * <p>
 * Represents the values pre-discretization
 */
public abstract class DiscretizationOrigin {
    private final DiscretizationType featureType;

    /**
     * @param featureType the {@link DiscretizationType}
     */
    DiscretizationOrigin(DiscretizationType featureType) {
        if (featureType == null) {
            throw new IllegalArgumentException("Discretization feature type may not be null");
        }
        this.featureType = featureType;
    }

    /**
     * @return the {@link DiscretizationType}
     */
    public DiscretizationType getFeatureType() {
        return featureType;
    }

    /**
     * May be queried to find out whether a discretizer is applicable
     *
     * @param originalValue the value to be discretized
     * @return true, if the origin matches the value
     */
    public abstract boolean canDiscretize(Serializable originalValue);

    /**
     * Prints the discretization (range) in a pretty format
     *
     * @return a human readable string
     */
    public abstract String outputFormat();
}
