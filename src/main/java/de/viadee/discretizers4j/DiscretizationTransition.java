package de.viadee.discretizers4j;

/**
 * The relation between original and discretized value
 */
public final class DiscretizationTransition {
    private final DiscretizationOrigin discretizationOrigin;
    private final Double discretizedValue;

    /**
     * Constructs the instance
     *
     * @param discretizationOrigin the discretizationOrigin
     * @param discretizedValue     the discretizedValue
     */
    public DiscretizationTransition(DiscretizationOrigin discretizationOrigin,
                                    Double discretizedValue) {
        this.discretizationOrigin = discretizationOrigin;
        this.discretizedValue = discretizedValue;
    }

    /**
     * @return the discretization origin
     */
    public DiscretizationOrigin getDiscretizationOrigin() {
        return discretizationOrigin;
    }

    /**
     * @return the discretized value
     */
    public Double getDiscretizedValue() {
        return discretizedValue;
    }

    @Override
    public String toString() {
        return "DiscretizationTransition From " + discretizationOrigin.toString() +
                " to class " + FormatTools.roundToTwo(discretizedValue);
    }
}
