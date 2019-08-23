package de.viadee.discretizers4j.impl;

import de.viadee.discretizers4j.DiscretizationTransition;
import de.viadee.discretizers4j.NumericDiscretizationOrigin;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MDLPDiscretizerTest {

    @Test
    void testMDLPBasicDiscretization() {
        Number[][] values = new Number[][]{
                {1.0, 0},
                {2.0, 0},
                {3.0, 0},
                {4.0, 0},
                {5.0, 0},
                {6.0, 1},
                {7.0, 1},
                {8.0, 1},
                {9.0, 1},
                {10.0, 1}
        };
        Serializable[] serializables = new Serializable[values.length];
        Double[] doubles = new Double[values.length];
        for (int i = 0; i < values.length; i++) {
            serializables[i] = values[i][0];
            doubles[i] = values[i][1].doubleValue();
        }
        MDLPDiscretizer mdlpDiscretizer = new MDLPDiscretizer();


        mdlpDiscretizer.fit(serializables, doubles);
        List<DiscretizationTransition> list = new ArrayList<>(mdlpDiscretizer.getTransitions());

        NumericDiscretizationOrigin discOrigin = ((NumericDiscretizationOrigin) list.get(0).getDiscretizationOrigin());
        assertEquals(1D, discOrigin.getMinValue());
        assertEquals(5D, discOrigin.getMaxValue());
        assertEquals(3.0, list.get(0).getDiscretizedValue().doubleValue());
        assertTrue(discOrigin.isFirst());
        assertFalse(discOrigin.isLast());

        NumericDiscretizationOrigin discOrigin2 = ((NumericDiscretizationOrigin) list.get(1).getDiscretizationOrigin());
        assertEquals(6D, discOrigin2.getMinValue());
        assertEquals(10D, discOrigin2.getMaxValue());
        assertEquals(8.0, list.get(1).getDiscretizedValue().doubleValue());
        assertFalse(discOrigin2.isFirst());
        assertTrue(discOrigin2.isLast());
    }

    @Test
    void testMDLPNoDiscretization() {
        Number[][] values = new Number[][]{
                {1.0, 0},
                {2.0, 0},
                {3.0, 0},
                {4.0, 0},
                {5.0, 0},
                {6.0, 0},
                {7.0, 0},
                {8.0, 0},
                {9.0, 0},
                {10.0, 0}
        };
        Serializable[] serializables = new Serializable[values.length];
        Double[] doubles = new Double[values.length];
        for (int i = 0; i < values.length; i++) {
            serializables[i] = values[i][0];
            doubles[i] = values[i][1].doubleValue();
        }
        MDLPDiscretizer mdlpDiscretizer = new MDLPDiscretizer();


        mdlpDiscretizer.fit(serializables, doubles);
        List<DiscretizationTransition> list = new ArrayList<>(mdlpDiscretizer.getTransitions());

        NumericDiscretizationOrigin discOrigin = ((NumericDiscretizationOrigin) list.get(0).getDiscretizationOrigin());
        assertEquals(1D, discOrigin.getMinValue());
        assertEquals(10D, discOrigin.getMaxValue());
        assertEquals(5.5, list.get(0).getDiscretizedValue().doubleValue());
        assertTrue(discOrigin.isFirst());
        assertTrue(discOrigin.isLast());
    }

    @Test
    void testMDLPBasicNonBinaryClassification() {
        Number[][] values = new Number[][]{
                {1.0, 0},
                {2.0, 0},
                {3.0, 0},
                {4.0, 0},
                {5.0, 0},
                {6.0, 1},
                {7.0, 1},
                {8.0, 1},
                {9.0, 1},
                {10.0, 1},
                {11.0, 2},
                {12.0, 2},
                {13.0, 2},
                {14.0, 2},
                {15.0, 2},
        };
        Serializable[] serializables = new Serializable[values.length];
        Double[] doubles = new Double[values.length];
        for (int i = 0; i < values.length; i++) {
            serializables[i] = values[i][0];
            doubles[i] = values[i][1].doubleValue();
        }
        MDLPDiscretizer mdlpDiscretizer = new MDLPDiscretizer();


        mdlpDiscretizer.fit(serializables, doubles);
        List<DiscretizationTransition> list = new ArrayList<>(mdlpDiscretizer.getTransitions());

        NumericDiscretizationOrigin discOrigin = ((NumericDiscretizationOrigin) list.get(2).getDiscretizationOrigin());
        assertEquals(11D, discOrigin.getMinValue());
        assertEquals(15D, discOrigin.getMaxValue());
        assertEquals(13.0, list.get(2).getDiscretizedValue().doubleValue());
        assertFalse(discOrigin.isFirst());
        assertTrue(discOrigin.isLast());
    }

    @Test
    void testMDLPEqualDistribution() {
        Number[][] values = new Number[][]{
                {1.0, 1},
                {1.0, 0},
                {2.0, 1},
                {2.0, 0},
                {3.0, 1},
                {3.0, 0},
                {4.0, 1},
                {4.0, 0}
        };
        Serializable[] serializables = new Serializable[values.length];
        Double[] doubles = new Double[values.length];
        for (int i = 0; i < values.length; i++) {
            serializables[i] = values[i][0];
            doubles[i] = values[i][1].doubleValue();
        }

        MDLPDiscretizer mdlpDiscretizer = new MDLPDiscretizer();
        mdlpDiscretizer.fit(serializables, doubles);
        List<DiscretizationTransition> list = new ArrayList<>(mdlpDiscretizer.getTransitions());
        assertEquals(1, list.size());
    }

    @Test
    void testDiscretizationBig() {
        Number[][] values = new Number[][]{
                {1, 1},
                {2, 1},
                {3, 1},
                {4, 1},
                {5, 1},
                {6, 1},
                {7, 1},
                {8, 1},
                {9, 1},
                {10, 1},
                {11, 1},
                {12, 1},
                {13, 1},
                {14, 1},
                {15, 1},
                {16, 1},
                {17, 1},
                {18, 1},
                {19, 1},
                {20, 1},
                {21, 0},
                {22, 0},
                {23, 0},
                {24, 0},
                {25, 0},
                {26, 0},
                {27, 0},
                {28, 1},
                {29, 0},
                {30, 1},
                {31, 0},
                {32, 0},
                {33, 1},
                {34, 1},
                {35, 0},
                {36, 0},
                {37, 0},
                {38, 1},
                {39, 1},
                {40, 1},
                {41, 1},
                {42, 1},
                {43, 1},
                {44, 1},
                {45, 1},
                {46, 1},
                {47, 1},
                {48, 1},
                {49, 1},
                {50, 0},
                {51, 0},
                {52, 0},
                {53, 0},
                {54, 1},
                {55, 1},
                {56, 1},
                {57, 1},
                {58, 1},
                {59, 1},
                {60, 1}
        };
        Serializable[] serializables = new Serializable[values.length];
        Double[] doubles = new Double[values.length];
        for (int i = 0; i < values.length; i++) {
            serializables[i] = values[i][0];
            doubles[i] = values[i][1].doubleValue();
        }
        MDLPDiscretizer mdlpDiscretizer = new MDLPDiscretizer();


        mdlpDiscretizer.fit(serializables, doubles);
        List<DiscretizationTransition> list = new ArrayList<>(mdlpDiscretizer.getTransitions());

        NumericDiscretizationOrigin discOrigin = ((NumericDiscretizationOrigin) list.get(0).getDiscretizationOrigin());
        assertEquals(1D, discOrigin.getMinValue());
        assertEquals(20D, discOrigin.getMaxValue());
        assertTrue(discOrigin.isFirst());
        assertFalse(discOrigin.isLast());

        NumericDiscretizationOrigin discOrigin2 = ((NumericDiscretizationOrigin) list.get(2).getDiscretizationOrigin());
        assertEquals(28D, discOrigin2.getMinValue());
        assertEquals(60D, discOrigin2.getMaxValue());
        assertFalse(discOrigin2.isFirst());
        assertTrue(discOrigin2.isLast());
    }

    /**
     * Tests the complete discretization, values taken from FUSINTER paper [Zighed, Rabaséda, Rakotomalala 1998]
     * Zighed et al describe the MDLP discretization and give several examples of its cut points. These are based
     * on the SIPINA-W software.
     */
    @Test
    void testIntervalReduction() {
        Number[][] values = new Number[][]{
                {1.0, 1},
                {2.0, 0},
                {3.0, 1},
                {4.0, 1},
                {5.0, 1},
                {6.0, 1},
                {7.0, 1},
                {8.0, 1},
                {9.0, 1},
                {10.0, 1},
                {11.0, 1},
                {12.0, 1},
                {13.0, 1},
                {14.0, 1},
                {15.0, 0},
                {16.0, 1},
                {17.0, 0},
                {18.0, 1},
                {19.0, 0},
                {20.0, 1},
                {21.0, 0},
                {22.0, 1},
                {23.0, 0},
                {24.0, 1},
                {25.0, 0},
                {26.0, 1},
                {27.0, 0},
                {28.0, 0},
                {29.0, 0},
                {30.0, 0},
                {31.0, 0},
                {32.0, 0},
                {33.0, 0},
                {34.0, 0},
                {35.0, 0},
                {36.0, 0},
                {37.0, 0},
                {38.0, 1},
                {39.0, 0},
                {40.0, 1},
        };
        Serializable[] serializables = new Serializable[values.length];
        Double[] doubles = new Double[values.length];
        for (int i = 0; i < values.length; i++) {
            serializables[i] = values[i][0];
            doubles[i] = values[i][1].doubleValue();
        }

        MDLPDiscretizer mdlpDiscretizer = new MDLPDiscretizer();
        mdlpDiscretizer.fit(serializables, doubles);
        List<DiscretizationTransition> list = new ArrayList<>(mdlpDiscretizer.getTransitions());
        assertEquals(2, list.size());
    }

    @Test
    void testNegativeValues() {
        Number[][] values = new Number[][]{
                {-1.0, 0},
                {-2.0, 0},
                {-3.0, 0},
                {-4.0, 0},
                {-5.0, 0},
                {-6.0, 1},
                {-7.0, 1},
                {-8.0, 1},
                {-9.0, 1},
                {-10.0, 1},
                {-11.0, 2},
                {-12.0, 2},
                {-13.0, 2},
                {-14.0, 2},
                {-15.0, 2},
        };
        Serializable[] serializables = new Serializable[values.length];
        Double[] doubles = new Double[values.length];
        for (int i = 0; i < values.length; i++) {
            serializables[i] = values[i][0];
            doubles[i] = values[i][1].doubleValue();
        }
        MDLPDiscretizer mdlpDiscretizer = new MDLPDiscretizer();


        mdlpDiscretizer.fit(serializables, doubles);
        List<DiscretizationTransition> list = new ArrayList<>(mdlpDiscretizer.getTransitions());

        NumericDiscretizationOrigin discOrigin = ((NumericDiscretizationOrigin) list.get(2).getDiscretizationOrigin());
        assertEquals(-5D, discOrigin.getMinValue());
        assertEquals(-1D, discOrigin.getMaxValue());
        assertEquals(-3.0, list.get(2).getDiscretizedValue().doubleValue());
        assertFalse(discOrigin.isFirst());
        assertTrue(discOrigin.isLast());

    }


}