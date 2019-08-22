package de.viadee.discretizers4j.impl;

import de.viadee.discretizers4j.AbstractDiscretizer;
import de.viadee.discretizers4j.DiscretizationTransition;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.List;

/**
 * Implementation of the PKID and ESD discretization algorithms described by [Yang and Webb 2009]
 */
public class EqualSizeDiscretizer extends AbstractDiscretizer {
    @Override
    protected List<DiscretizationTransition> fitCreateTransitions(List<AbstractMap.SimpleImmutableEntry<Double, Double>> keyValuePairs) {
        return null;
    }
    private int classSize;

    /**
     * creates a {@link PercentileMedianDiscretizer} which creates intervals, that are the same size as there are
     * number of intervals
     */
    public EqualSizeDiscretizer() {
        this(0);
    }

    /**
     * creates a {@link PercentileMedianDiscretizer} which creates intervals, that are the same size. In actual datasets
     * a minimum size of 30 is recommended by [Weiss, 2002]
     * @param classSize size of Intervals to be created. Might be changed slightly if values length is not divisible by it
     */
    public EqualSizeDiscretizer(int classSize) {
        super(false);
        this.classSize = classSize;
    }

    /**
     * creates the {@link DiscretizationTransition} of the values. Each transition will have the same size. If no size
     * is given as a parameter in the constructor, a PKID will be created.
     * @param values the values to be fitted on
     * @param labels the labels. != null, iff supervised
     * @return list of Transisitions, created by {@link PercentileMedianDiscretizer}. All will have the same length.
     */
    @Override
    protected List<DiscretizationTransition> fitCreateTransitions(Serializable[] values, Double[] labels) {
        if(classSize == 0) {
            classSize = (int) Math.sqrt(values.length);
        } else if (classSize >= values.length) {
            classSize = values.length;
        }
        PercentileMedianDiscretizer percentileMedianDiscretizer = new PercentileMedianDiscretizer((values.length/ classSize));
        return percentileMedianDiscretizer.fitCreateTransitions(values, null);
    }
}
