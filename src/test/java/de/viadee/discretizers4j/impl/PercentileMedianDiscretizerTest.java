package de.viadee.discretizers4j.impl;

import de.viadee.discretizers4j.DiscretizationTransition;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class PercentileMedianDiscretizerTest {

    @Test
    void testErrorThrownOnNonNumbers() {
        PercentileMedianDiscretizer percentileMedianDiscretizer = new PercentileMedianDiscretizer(3, false);
        assertThrows(IllegalArgumentException.class, () ->
                percentileMedianDiscretizer.fit(new Serializable[]{1, 2, "thisshouldbreakeverything"}));
        assertThrows(IllegalArgumentException.class, () ->
                percentileMedianDiscretizer.fit(new Serializable[]{"thisshouldbreakeverything", "thiseither"}));

    }

    @Test
    void testFittingAllowedOnceOnly() {
        PercentileMedianDiscretizer percentileMedianDiscretizer = new PercentileMedianDiscretizer(3, false);
        percentileMedianDiscretizer.fit(new Integer[]{1, 2, 3});
        assertThrows(IllegalArgumentException.class, () ->
                percentileMedianDiscretizer.fit(new Integer[]{1, 2, 3}));
    }

    @Test
    void testDiscretizationOdd() {
        PercentileMedianDiscretizer percentileMedianDiscretizer = new PercentileMedianDiscretizer(3, false);
        percentileMedianDiscretizer.fit(new Double[]{1D, 2D, 3D, 0.2, 1.3, 2.4});
        Double[] discretization = percentileMedianDiscretizer.apply(new Double[]{1D, 1.9, 2.9});
        assertArrayEquals(new Double[]{0D, 1D, 2D}, discretization);
    }

    @Test
    void testDiscretizationEven() {
        PercentileMedianDiscretizer percentileMedianDiscretizer = new PercentileMedianDiscretizer(3, false);
        percentileMedianDiscretizer.fit(IntStream.range(1, 21).boxed().toArray(Integer[]::new));
        Double[] discretization = percentileMedianDiscretizer.apply(new Double[]{4D, 2D, 10D, 20D});
        assertArrayEquals(new Double[]{0D, 0D, 1D, 2D}, discretization);
    }

    @Test
    void testClassReduction() {
        PercentileMedianDiscretizer percentileMedianDiscretizer = new PercentileMedianDiscretizer(3, false);
        percentileMedianDiscretizer.fit(new Integer[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3});

        List<DiscretizationTransition> list = new ArrayList<>(percentileMedianDiscretizer.getTransitions());
        assertEquals(2, list.size());

    }
}
