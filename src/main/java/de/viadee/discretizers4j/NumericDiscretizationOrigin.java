package de.viadee.discretizers4j;

import java.io.Serializable;

/**
 * Represents a numeric discretization origin
 */
public class NumericDiscretizationOrigin extends DiscretizationOrigin {
    private final Number maxValue;
    private Number minValue;
    private boolean isFirst = false;
    private boolean isLast = false;

    /**
     * Constructs the instance
     *
     * @param minValue range min value
     * @param maxValue range max value
     */
    public NumericDiscretizationOrigin(Number minValue, Number maxValue) {
        super(DiscretizationType.NUMERIC);
        if (minValue == null || maxValue == null) {
            throw new IllegalArgumentException("Both min and max value must not be null");
        }
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public boolean canDiscretize(Serializable originalValue) {
        if (!(originalValue instanceof Number)) {
            throw new IllegalArgumentException("Non-number type passed to numeric discretizer");
        }
        final Number value = (Number) originalValue;

        final boolean matchesLower = isFirst || value.doubleValue() >= minValue.doubleValue();
        final boolean matchesUpper = isLast || value.doubleValue() <= maxValue.doubleValue();
        return (matchesLower && matchesUpper);
    }

    @Override
    public String outputFormat() {
        return "IN " + formatRangeNotation();
    }

    /**
     * @return the min range value
     */
    public Number getMinValue() {
        return minValue;
    }

    /**
     * @param minValue the new min value
     */
    public void setMinValue(Number minValue) {
        this.minValue = minValue;
    }

    /**
     * @return the max range value
     */
    public Number getMaxValue() {
        return maxValue;
    }

    @Override
    public String toString() {
        return formatRangeNotation();
    }

    private String formatRangeNotation() {
        return ((isFirst) ? "]" : "[") +
                FormatTools.roundToTwo(minValue) +
                ", " +
                FormatTools.roundToTwo(maxValue) +
                ((isLast) ? "[" : ")");
    }

    /**
     * @param first true, if this is the first element and the lower boundary should be infinitely open
     */
    public void setFirst(boolean first) {
        isFirst = first;
    }

    /**
     * @param last true, if this is the last element and the upper boundary should be infinitely open
     */
    public void setLast(boolean last) {
        isLast = last;
    }

    /**
     * @return is lower boundary open
     */
    public boolean isFirst() {
        return isFirst;
    }

    /**
     * @return is upper boundary open
     */
    public boolean isLast() {
        return isLast;
    }
}
