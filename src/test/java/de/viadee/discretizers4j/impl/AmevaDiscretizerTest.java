package de.viadee.discretizers4j.impl;

import de.viadee.discretizers4j.DiscretizationTransition;
import de.viadee.discretizers4j.NumericDiscretizationOrigin;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class AmevaDiscretizerTest {

    @Test
    void AmevaTestBasicDiscretization() {
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
        AmevaDiscretizer amevaDiscretizer = new AmevaDiscretizer();


        amevaDiscretizer.fit(serializables, doubles);
        List<DiscretizationTransition> list = new ArrayList<>(amevaDiscretizer.getTransitions());

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
    void AmevaTestNoDiscretization() {
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
        AmevaDiscretizer amevaDiscretizer = new AmevaDiscretizer();


        amevaDiscretizer.fit(serializables, doubles);
        List<DiscretizationTransition> list = new ArrayList<>(amevaDiscretizer.getTransitions());

        NumericDiscretizationOrigin discOrigin = ((NumericDiscretizationOrigin) list.get(0).getDiscretizationOrigin());
        assertEquals(1D, discOrigin.getMinValue());
        assertEquals(10D, discOrigin.getMaxValue());
        assertEquals(5.5, list.get(0).getDiscretizedValue().doubleValue());
        assertTrue(discOrigin.isFirst());
        assertTrue(discOrigin.isLast());
    }

    @Test
    void testAmevaBasicNonBinaryClassification() {
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
        AmevaDiscretizer amevaDiscretizer = new AmevaDiscretizer();


        amevaDiscretizer.fit(serializables, doubles);
        List<DiscretizationTransition> list = new ArrayList<>(amevaDiscretizer.getTransitions());

        NumericDiscretizationOrigin discOrigin = ((NumericDiscretizationOrigin) list.get(2).getDiscretizationOrigin());
        assertEquals(11D, discOrigin.getMinValue());
        assertEquals(15D, discOrigin.getMaxValue());
        assertEquals(13.0, list.get(2).getDiscretizedValue().doubleValue());
        assertFalse(discOrigin.isFirst());
        assertTrue(discOrigin.isLast());
    }

    @Test
    void testEqualDistribution() {
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

        AmevaDiscretizer amevaDiscretizer = new AmevaDiscretizer();
        amevaDiscretizer.fit(serializables, doubles);
        List<DiscretizationTransition> list = new ArrayList<>(amevaDiscretizer.getTransitions());
        assertEquals(1, list.size());
    }

    @Test
    void testAmevaValueDetermination() throws Exception {
        Number[][] values = new Number[][]{
                {1.0, 0},
                {2.0, 0},
                {3.0, 1},
                {4.0, 1},
                {5.0, 1},
                {6.0, 1},
                {7.0, 1},
                {8.0, 0},
                {9.0, 0},
                {10.0, 0},
                {11.0, 1},
                {12.0, 1},
                {13.0, 1},
                {14.0, 0},
                {15.0, 0},
                {16.0, 0},
                {17.0, 0},
                {18.0, 0},
                {19.0, 0},
                {20.0, 1},
                {21.0, 1},
                {22.0, 1},
                {23.0, 1},
                {24.0, 1},
                {25.0, 0},
                {26.0, 1},
                {27.0, 0},
                {28.0, 0},
                {29.0, 0},
                {30.0, 0}
        };
        AmevaDiscretizer amevaDiscretizer = new AmevaDiscretizer();
        List<AbstractMap.SimpleImmutableEntry<Number, Double>> keyValuePairs = Arrays.stream(values).map(value -> new AbstractMap.SimpleImmutableEntry<>(value[0], value[1].doubleValue()))
                .sorted(Comparator.comparing(entry -> entry.getKey().doubleValue()))
                .collect(Collectors.toList());

        Field keyValuePairsField = AmevaDiscretizer.class.getDeclaredField("keyValuePairs");
        keyValuePairsField.setAccessible(true);
        keyValuePairsField.set(amevaDiscretizer, keyValuePairs);

        List<Double> actualCutPoints = new ArrayList<>();
        actualCutPoints.add(7.5);
        actualCutPoints.add(19.5);

        Field actualCutPointsField = AmevaDiscretizer.class.getDeclaredField("actualCutPoints");
        actualCutPointsField.setAccessible(true);
        actualCutPointsField.set(amevaDiscretizer, actualCutPoints);

        Double[] targetValues = new Double[2];
        targetValues[0] = 0.0;
        targetValues[1] = 1.0;

        Field targetValuesField = AmevaDiscretizer.class.getDeclaredField("targetValues");
        targetValuesField.setAccessible(true);
        targetValuesField.set(amevaDiscretizer, targetValues);

        long[] targetValueDistribution = new long[2];
        targetValueDistribution[0] = 17;
        targetValueDistribution[1] = 13;

        Field targetValueDistributionField = AmevaDiscretizer.class.getDeclaredField("targetValueDistribution");
        targetValueDistributionField.setAccessible(true);
        targetValueDistributionField.set(amevaDiscretizer, targetValueDistribution);


        double ameva = amevaDiscretizer.determineAmeva(24.5);

        assertEquals(3.04, ameva, 0.01);

    }

}
