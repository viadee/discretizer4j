package de.viadee.discretizers4j.impl;

import de.viadee.discretizers4j.DiscretizationTransition;
import de.viadee.discretizers4j.NumericDiscretizationOrigin;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RandomDiscretizerTest {

    @Test
    void testFitCreateTransitionsInts() {

        //Given
        Number[] values = {
                0, 1, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15
        };

        for (int i = 0; i < 100; i++) {
            RandomDiscretizer randomDiscretizer = new RandomDiscretizer();

            //When
            randomDiscretizer.fit(values);
            Collection<DiscretizationTransition> transitionList = randomDiscretizer.getTransitions();

            //Then
            Assertions.assertTrue(transitionList.size() <= values.length + 1 && transitionList.size() > 0);
        }
    }

    @Test
    void testFitCreateTransitionsIntsAllSame() {

        //Given
        Number[] values = {
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1
        };

        for (int i = 0; i < 100; i++) {
            RandomDiscretizer randomDiscretizer = new RandomDiscretizer();

            //When
            randomDiscretizer.fit(values);
            Collection<DiscretizationTransition> transitionList = randomDiscretizer.getTransitions();

            //Then
            assertEquals(1, transitionList.size());

            assertEquals(1D, ((NumericDiscretizationOrigin) randomDiscretizer
                    .getTransition(1D).getDiscretizationOrigin()).getMinValue());
            assertEquals(1D, ((NumericDiscretizationOrigin) randomDiscretizer
                    .getTransition(1D).getDiscretizationOrigin()).getMaxValue());
        }
    }

    @Test
    void testFitCreateTransitionsIntsAllSameButOne() {

        //Given
        Number[] values = {
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2
        };

        for (int i = 0; i < 100; i++) {
            RandomDiscretizer randomDiscretizer = new RandomDiscretizer();

            //When
            randomDiscretizer.fit(values);
            Collection<DiscretizationTransition> transitionList = randomDiscretizer.getTransitions();

            //Then
            Assertions.assertTrue(transitionList.size() == 1 || transitionList.size() == 2 || transitionList.size() == 3);
        }
    }

    @Test
    void testFitCreateTransitionsDoubles() {

        //Given
        Number[] values = {
                0.01, 0.1, 0.18, 0.27, 0.29, 0.31, 0.49, 0.55, 0.58, 0.71, 0.87, 0.88, 0.91, 0.96, 0.99
        };

        for (int i = 0; i < 100; i++) {
            RandomDiscretizer randomDiscretizer = new RandomDiscretizer();

            //When
            randomDiscretizer.fit(values);
            Collection<DiscretizationTransition> transitionList = randomDiscretizer.getTransitions();

            //Then
            Assertions.assertTrue(transitionList.size() <= values.length + 1 && transitionList.size() > 0);
        }
    }

    @Test
    void testFitCreateTransitionsStrings() {

        //Given
        String[] values = {
                "A", "B", "C", "D", "E",
        };
        RandomDiscretizer randomDiscretizer = new RandomDiscretizer();

        //Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> randomDiscretizer.fit(values));
    }
}