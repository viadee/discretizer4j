package de.viadee.discretizers4j;

/**
 * Specifies the possible data types that can be discretized
 */
public enum DiscretizationType {
    /**
     * Categorical - unique or custom discretizations
     */
    CATEGORICAL,
    /**
     * Numericals - a range gets "shrunken" to a smaller range or value
     */
    NUMERIC
}
