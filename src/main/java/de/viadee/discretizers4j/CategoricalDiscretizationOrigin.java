package de.viadee.discretizers4j;

import java.io.Serializable;

/**
 * Represents a categorical discretization origin
 */
public class CategoricalDiscretizationOrigin extends DiscretizationOrigin {
    private final Serializable value;

    /**
     * Constructs the instance
     *
     * @param value the original value
     */
    public CategoricalDiscretizationOrigin(Serializable value) {
        super(DiscretizationType.CATEGORICAL);
        if (value == null) {
            throw new IllegalArgumentException("Discretization origin value may not be null");
        }
        this.value = value;
    }

    @Override
    public boolean canDiscretize(Serializable originalValue) {
        return value.equals(originalValue);
    }

    @Override
    public String outputFormat() {
        return "= '" + getValue() + "'";
    }

    /**
     * @return the original value
     */
    public Serializable getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "[" + value + "]";
    }
}
