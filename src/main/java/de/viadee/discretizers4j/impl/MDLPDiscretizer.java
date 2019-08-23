package de.viadee.discretizers4j.impl;

import de.viadee.discretizers4j.AbstractSupervisedDiscretizer;
import de.viadee.discretizers4j.DiscretizationTransition;
import de.viadee.discretizers4j.Interval;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MDLPDiscretizer extends AbstractSupervisedDiscretizer {

    private List<AbstractMap.SimpleImmutableEntry<Double, Double>> keyValuePairs;
    private List<Integer> potentialCutPoints = new ArrayList<>(200);
    private List<Integer> actualIntervalEnds = new ArrayList<>(20);
    private Double[] targetValues;

    /**
     * Constructs the MDLP Discretizer, MDLP works without any Parameters.
     */
    public MDLPDiscretizer() {
        super(true);
    }

    /**
     * Implementation of MDLP,
     * 1. Sort all values. 2. Determine all useful cut points (useful if the class changes at this point)
     * 3. Search for cut point which splits the values into the best "bi-partition".
     * 4. Repeat [3] for each part until no improvement is possible (recursive)
     *
     * @param keyValuePairs List of Values and Labels
     * @return list of DiscretizationTransition determined to have the highest Ameva value
     */
    @Override
    protected List<DiscretizationTransition> fitCreateTransitions(List<AbstractMap.SimpleImmutableEntry<Double, Double>> keyValuePairs) {
        this.keyValuePairs = keyValuePairs;
        targetValues = keyValuePairs.stream().map(AbstractMap.SimpleImmutableEntry::getValue).sorted().distinct().toArray(Double[]::new);

        List<Interval> initialSplit = equalClassSplit(keyValuePairs);


        for (Interval interval : initialSplit) {
            potentialCutPoints.add(interval.getEnd());
        }
        determineIntervals(0, keyValuePairs.size() - 1);

        List<Interval> evaluatedIntervals = new ArrayList<>();
        Collections.sort(actualIntervalEnds);
        int begin = 0;
        for (Integer end : actualIntervalEnds) {
            evaluatedIntervals.add(new Interval(begin, end, keyValuePairs));
            begin = end + 1;
        }
        evaluatedIntervals.add(new Interval(begin, keyValuePairs.size() - 1, keyValuePairs));

        return evaluatedIntervals.stream().map(Interval::toDiscretizationTransition).collect(Collectors.toList());
    }

    /**
     * (RECURSIVE) determine the best cut points for value from index begin to end in keyValuePairs.
     *
     * @param begin begin index of Interval
     * @param end   end index of Interval
     */
    private void determineIntervals(int begin, int end) {
        double mdlpcMax = 0;
        int valueMax = -1;
        int posMax = -1;
        List<Integer> reducedPotentialIntervalEnds = potentialCutPoints.stream()
                .filter(intervalEnd -> intervalEnd >= begin
                        && intervalEnd <= end)
                .collect(Collectors.toList());
        for (int i = 0; i < reducedPotentialIntervalEnds.size(); i++) {
            double mdlpc = determineMDLPCCriterion(begin, end, reducedPotentialIntervalEnds.get(i));
            if (mdlpcMax < mdlpc) {
                mdlpcMax = mdlpc;
                posMax = i;
                valueMax = reducedPotentialIntervalEnds.get(i);
            }
        }

        if (mdlpcMax > 0) {
            actualIntervalEnds.add(valueMax);
            determineIntervals(begin, reducedPotentialIntervalEnds.get(posMax));
            determineIntervals(reducedPotentialIntervalEnds.get(posMax) + 1, end);
        }
    }

    private double determineMDLPCCriterion(Integer begin, Integer end, Integer i) {
        Interval completeInterval = new Interval(begin, end, keyValuePairs);
        Interval leftInterval = new Interval(begin, i, keyValuePairs);
        long leftCD = Arrays.stream(leftInterval.getClassDist()).filter(label -> label != 0).count();
        Interval rightInterval = new Interval(i + 1, end, keyValuePairs);
        long rightCD = Arrays.stream(rightInterval.getClassDist()).filter(label -> label != 0).count();

        double entropyComplete = computeEntropy(completeInterval);
        double entropyLeft = computeEntropy(leftInterval);
        double entropyRight = computeEntropy(rightInterval);

        double gain = entropyComplete -
                ((leftInterval.getSize() / (double) completeInterval.getSize()) * entropyLeft
                        + (rightInterval.getSize() / (double) completeInterval.getSize()) * entropyRight);

        double delta = log2(Math.pow(3, targetValues.length) - 2)
                - (targetValues.length * entropyComplete
                - leftCD * entropyLeft
                - rightCD * entropyRight);
        return gain - (log2(completeInterval.getSize() - 1.0)) / (double) completeInterval.getSize()
                - delta / (double) completeInterval.getSize();
    }

    /**
     * determines the entropy of an interval
     *
     * @param interval {@link Interval} to be evaluated
     * @return double value of "Shannons Entropy".
     */
    private double computeEntropy(Interval interval) {
        double entropy = 0;
        for (int i = 0; i < targetValues.length; i++) {
            entropy += (interval.getClassDist()[i] / (double) interval.getSize())
                    * log2((interval.getClassDist()[i] / (double) interval.getSize()));
        }
        return -1 * entropy;
    }


    private double log2(double value) {
        if (value == 0D) {
            return 0D;
        }
        return Math.log(value) / Math.log(2);
    }
}
