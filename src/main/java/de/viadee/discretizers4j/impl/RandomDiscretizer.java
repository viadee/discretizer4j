package de.viadee.discretizers4j.impl;

import de.viadee.discretizers4j.AbstractDiscretizer;
import de.viadee.discretizers4j.DiscretizationTransition;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Reference discretizer to establish a baseline to compare advanced, supervised
 * approaches with.
 */
public class RandomDiscretizer extends AbstractDiscretizer {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final Random random = new Random();

    public RandomDiscretizer() {
        super(false);
    }

    @Override
    protected List<DiscretizationTransition> fitCreateTransitions(Serializable[] values, Double[] labels) {

        final List<Double> valuesDistinct = Stream.of(values).distinct().map(v -> ((Number) v).doubleValue()).sorted()
                .collect(Collectors.toList());

        final int numberCuts = random.nextInt(valuesDistinct.size() + 1);
        final Double[] cutPoints = new Double[numberCuts];

        Collections.shuffle(valuesDistinct);
        for (int i = 0; i < numberCuts; i++) {
            cutPoints[i] = valuesDistinct.get(i);
        }

        final ManualDiscretizer manualDiscretizer = new ManualDiscretizer(cutPoints);

        return manualDiscretizer.fitCreateTransitions(values, null);
    }
}
