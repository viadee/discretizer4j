package de.viadee.discretizers4j.impl;

import de.viadee.discretizers4j.DiscretizationTransition;
import de.viadee.discretizers4j.NumericDiscretizationOrigin;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EqualSizeDiscretizerTest {

    @Test
    void testPKIDForSimpleValues() {
        Number[] values = {
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15
        };

        EqualSizeDiscretizer equalSizeDiscretizer = new EqualSizeDiscretizer();
        equalSizeDiscretizer.fit(values);
        Collection<DiscretizationTransition> transitionList = equalSizeDiscretizer.getTransitions();

        assertEquals(4, transitionList.size());
    }

    @Test
    void testPKIDWithReduction() {
        Number[] values = {
                0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 10, 11, 12, 13, 14, 15
        };

        EqualSizeDiscretizer equalSizeDiscretizer = new EqualSizeDiscretizer();
        equalSizeDiscretizer.fit(values);
        Collection<DiscretizationTransition> transitionList = equalSizeDiscretizer.getTransitions();

        assertEquals(3, transitionList.size());
    }

    @Test
    void testESDForSimpleValues() {
        Number[] values = {
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10
        };

        EqualSizeDiscretizer equalSizeDiscretizer = new EqualSizeDiscretizer(5);
        equalSizeDiscretizer.fit(values);
        Collection<DiscretizationTransition> transitionList = equalSizeDiscretizer.getTransitions();

        assertEquals(2, transitionList.size());
    }

    @Test
    void testESDWithClassSizeHigherThanLength() {
        Number[] values = {
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10
        };

        EqualSizeDiscretizer equalSizeDiscretizer = new EqualSizeDiscretizer(100);
        equalSizeDiscretizer.fit(values);
        Collection<DiscretizationTransition> transitionList = equalSizeDiscretizer.getTransitions();

        assertEquals(1, transitionList.size());
    }

    @Test
    void testESDWithClassSizeNegative() {
        Number[] values = {
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10
        };

        EqualSizeDiscretizer equalSizeDiscretizer = new EqualSizeDiscretizer(-5);
        equalSizeDiscretizer.fit(values);
        Collection<DiscretizationTransition> transitionList = equalSizeDiscretizer.getTransitions();

        assertEquals(1, transitionList.size());
    }

    @Test
    void testESD() {
        Number[] values = {
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
                10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
                20, 21, 22, 23, 24, 25, 26, 27, 28, 29
        };

        EqualSizeDiscretizer equalSizeDiscretizer = new EqualSizeDiscretizer(5);
        equalSizeDiscretizer.fit(values);

        List<DiscretizationTransition> list = new ArrayList<>(equalSizeDiscretizer.getTransitions());

        NumericDiscretizationOrigin discOrigin = ((NumericDiscretizationOrigin) list.get(0).getDiscretizationOrigin());
        assertEquals(0D, discOrigin.getMinValue());
        assertEquals(4.5, discOrigin.getMaxValue());
        assertEquals(0D, list.get(0).getDiscretizedValue().doubleValue());
        assertTrue(discOrigin.isFirst());
        assertFalse(discOrigin.isLast());

        NumericDiscretizationOrigin discOrigin1 = ((NumericDiscretizationOrigin) list.get(1).getDiscretizationOrigin());
        assertEquals(4.5, discOrigin1.getMinValue());
        assertEquals(9.5, discOrigin1.getMaxValue());
        assertEquals(1D, list.get(1).getDiscretizedValue().doubleValue());
        assertFalse(discOrigin1.isFirst());
        assertFalse(discOrigin1.isLast());

        NumericDiscretizationOrigin discOrigin5 = ((NumericDiscretizationOrigin) list.get(5).getDiscretizationOrigin());
        assertEquals(24.5, discOrigin5.getMinValue());
        assertEquals(29D, discOrigin5.getMaxValue());
        assertEquals(5D, list.get(5).getDiscretizedValue().doubleValue());
        assertFalse(discOrigin5.isFirst());
        assertTrue(discOrigin5.isLast());
    }

}