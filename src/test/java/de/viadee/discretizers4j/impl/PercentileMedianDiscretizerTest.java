package de.viadee.discretizers4j.impl;

import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        assertArrayEquals(new Double[]{0.6, 1.65, 2.7}, discretization);
    }

    @Test
    void testDiscretizationEven() {
        PercentileMedianDiscretizer percentileMedianDiscretizer = new PercentileMedianDiscretizer(3, false);
        percentileMedianDiscretizer.fit(IntStream.range(1, 21).boxed().toArray(Integer[]::new));
        Double[] discretization = percentileMedianDiscretizer.apply(new Double[]{4D, 2D, 10D, 20D});
        assertArrayEquals(new Double[]{4D, 4D, 11D, 17.5}, discretization);
    }

    @Test
    void testClassReduction() {
        PercentileMedianDiscretizer failingDiscretizer = new PercentileMedianDiscretizer(3, false);
        assertThrows(IllegalArgumentException.class, () ->
                failingDiscretizer.fit(new Integer[]{0, 0, 0, 0, 0, 0, 0, 0, 5, 10, 15}));

        PercentileMedianDiscretizer percentileMedianDiscretizer = new PercentileMedianDiscretizer(3, true);
        percentileMedianDiscretizer.fit(new Integer[]{0, 0, 0, 0, 0, 0, 0, 0, 5, 10, 15});

        Double[] discretization = percentileMedianDiscretizer.apply(new Double[]{0D, 5D, 10D, 15D});
        assertArrayEquals(new Double[]{0D, 10D, 10D, 10D}, discretization);
    }
}
