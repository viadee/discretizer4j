package de.viadee.discretizers4j.impl;

import de.viadee.discretizers4j.AbstractDiscretizer;
import de.viadee.discretizers4j.DiscretizationTransition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Discretizer partitioning data into n specified classes using their mean values as a class label
 */
public class PercentileMedianDiscretizer extends AbstractDiscretizer {
    private int classCount;

    /**
     * Constructs the instance
     *
     * @param classCount count of classes
     */
    public PercentileMedianDiscretizer(int classCount) {
        super(false);
        this.classCount = classCount;
    }

    @Override
    protected List<DiscretizationTransition> fitCreateTransitions(Serializable[] values, Double[] labels) {

        List<Number> numbers = Stream.of(values).map(i -> (Number) i)
                .sorted(Comparator.comparingDouble(Number::doubleValue))
                .collect(Collectors.toList());

        List<Double> actualCutPoints = new ArrayList<>();

        final int classes = Math.min(classCount, numbers.size());
        final int countPerClass = numbers.size() / classes;
        int backlog = numbers.size() % classes;
        int endIndex = countPerClass - 1;
        for (int currentClass = 1; currentClass < classes; currentClass++) {
            if (backlog > 0) {
                endIndex++;
                backlog--;
            }
            actualCutPoints.add(
                    (((Number) values[endIndex]).doubleValue()
                            + ((Number) values[endIndex + 1]).doubleValue()) / 2);
            endIndex = endIndex + countPerClass;
        }

        return getDiscretizationTransitionsFromCutPoints(actualCutPoints, ((Number) values[0]).doubleValue(), ((Number) values[values.length - 1]).doubleValue());
    }
}