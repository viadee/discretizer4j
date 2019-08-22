package de.viadee.discretizers4j.impl;

import de.viadee.discretizers4j.AbstractSupervisedDiscretizer;
import de.viadee.discretizers4j.DiscretizationTransition;
import de.viadee.discretizers4j.Interval;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Implementation of the FUSINTER discretization algorithm described by [Zighed, Rabaséda, Rakotomalala 1998]
 */
public class FUSINTERDiscretizer extends AbstractSupervisedDiscretizer {
    @Override
    protected List<DiscretizationTransition> fitCreateTransitions(List<AbstractMap.SimpleImmutableEntry<Double, Double>> keyValuePairs) {
        return null;
    }
    private final double lambda;
    private final double alpha;

    // Number of possible classifications
    private int m;
    // Number of instances
    private int n;

    /**
     * Generates a FUSINTER discretizer with parameters suggested by the authors
     */
    public FUSINTERDiscretizer() {
        this(1.0, 0.975);
    }

    /**
     * Generates a FUSINTER discretizer with custom parameters
     *
     * @param lambda lambda value
     * @param alpha  alpha value
     */
    public FUSINTERDiscretizer(double lambda, double alpha) {
        super(true);
        this.lambda = lambda;
        this.alpha = alpha;
    }

    /**
     * Implementation of FUSINTER, 1. sort, 2. equalClassIntervals 3. merge if entropy improves
     *
     * @param labels Array of Doubles, classifications of instances
     * @param values Array of Numbers expected. FUSINTER is only possible with continuous variables
     * @return list of Intervals determined to have the highest entropy
     */
    @Override
    protected List<DiscretizationTransition> fitCreateTransitions(Serializable[] values, Double[] labels) {

        if (Stream.of(values).anyMatch(v -> !(v instanceof Number))) {
            throw new IllegalArgumentException("Only numeric values allowed for this discretizer");
        }

        m = Arrays.stream(labels).sorted().distinct().toArray(Double[]::new).length;
        n = values.length;

        final List<AbstractMap.SimpleImmutableEntry<Double, Double>> keyValuePairs = IntStream
                .range(0, values.length)
                .mapToObj(i -> new AbstractMap.SimpleImmutableEntry<>(((Number) values[i]).doubleValue(), labels[i]))
                .sorted(Comparator.comparing(AbstractMap.SimpleImmutableEntry::getKey))
                .collect(Collectors.toList());

        List<Interval> equalClassSplits = equalClassSplit(keyValuePairs);
        List<Interval> evaluatedIntervals;
        evaluatedIntervals = evaluateIntervals(equalClassSplits, keyValuePairs);

        return evaluatedIntervals.stream().map(Interval::toDiscretizationTransition).collect(Collectors.toList());
    }

    private List<Interval> evaluateIntervals(List<Interval> equalClassSplits,
                                             final List<AbstractMap.SimpleImmutableEntry<Double, Double>> keyValuePairs) {
        boolean improvement = true;

        while (equalClassSplits.size() > 1 && improvement) {
            int deleteIndex = 0;
            double maxMergeCrit = 0.0;
            double oldCrit = determineDiscretizationCriterion(equalClassSplits);
            for (int i = 0; i < equalClassSplits.size() - 1; i++) {
                List<Interval> possibleMergedInterval = mergeInterval(equalClassSplits, i, keyValuePairs);
                final double mergeCrit = oldCrit - determineDiscretizationCriterion(possibleMergedInterval);
                if (mergeCrit > maxMergeCrit) {
                    deleteIndex = i;
                    maxMergeCrit = mergeCrit;
                }
            }

            if (maxMergeCrit > 0) {
                equalClassSplits = mergeInterval(equalClassSplits, deleteIndex, keyValuePairs);
            } else {
                improvement = false;
            }
        }

        return equalClassSplits;
    }

    /**
     * determines the Entropy of the given Intervals with the quadratic entropy formula
     *
     * @param intervals to be evaluated
     * @return double from 0 to 1 with 0 being perfect entropy
     */
    private double determineDiscretizationCriterion(List<Interval> intervals) {
        double criterion = 0;
        for (Interval interval : intervals) {
            double intervalClassSum = 0;
            for (int i = 0; i < m; i++) {
                double quotient = (interval.getClassDist()[i] + lambda) / (interval.getSize() + m * lambda);
                intervalClassSum += (quotient * (1 - quotient));
            }
            double intervalSum = alpha * ((interval.getSize()) / (double) n) * intervalClassSum;
            intervalSum += ((1 - alpha) * ((m * lambda) / (interval.getSize())));

            criterion += intervalSum;
        }

        return criterion;
    }

    /**
     * merges Interval i and i+1,
     *
     * @param intervals     list of Interval to be reduced by one Element
     * @param i             index of element that will be merged
     * @param keyValuePairs list of values used for index
     * @return new List with Interval i and i+1 merged
     */
    private List<Interval> mergeInterval(List<Interval> intervals, int i,
                                         final List<AbstractMap.SimpleImmutableEntry<Double, Double>> keyValuePairs) {
        List<Interval> temp = new ArrayList<>(intervals.subList(0, intervals.size()));
        int mergeBegin = temp.get(i).getBegin();
        int mergeEnd = temp.get(i + 1).getEnd();
        temp.add(i, new Interval(mergeBegin, mergeEnd, keyValuePairs));
        temp.remove(i + 1);
        temp.remove(i + 1);

        return temp;
    }

}