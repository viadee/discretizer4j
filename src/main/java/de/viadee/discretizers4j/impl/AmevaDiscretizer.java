package de.viadee.discretizers4j.impl;

import de.viadee.discretizers4j.AbstractSupervisedDiscretizer;
import de.viadee.discretizers4j.DiscretizationTransition;
import de.viadee.discretizers4j.Interval;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Implementation of the Ameva discretization algorithm described by [Gonzales-Abril, Cuberos, Velasco, Ortega 2009]
 */
public class AmevaDiscretizer extends AbstractSupervisedDiscretizer {

    private Double[] targetValues;
    private long[] targetValueDistribution;
    private List<Double> bCutPoints = new ArrayList<>();
    private List<Double> actualCutPoints = new ArrayList<>();
    private List<AbstractMap.SimpleImmutableEntry<Double, Double>> keyValuePairs;

    /**
     * Constructs the Ameva Discretizer, Ameva works without any Parameters.
     */
    public AmevaDiscretizer() {
        super(true);
    }

    /**
     * Implementation of Ameva, 1. sort values and get all CutPoints, 2. Consecutively add new cut points and determine
     * Ameva value (variation of contingency coefficient) of the tentative cut points. Get best cut point of this
     * iteration. If the ameva value does not increase with new cut points break and return Discretization
     *
     * @param keyValuePairs List of Values and Labels
     * @return list of DiscretizationTransition determined to have the highest Ameva value
     */
    @Override
    protected List<DiscretizationTransition> fitCreateTransitions(List<AbstractMap.SimpleImmutableEntry<Double, Double>> keyValuePairs) {
        this.keyValuePairs = keyValuePairs;
        targetValues = keyValuePairs.stream().map(AbstractMap.SimpleImmutableEntry::getValue).sorted().distinct().toArray(Double[]::new);
        targetValueDistribution = new long[targetValues.length];
        for (int i = 0; i < targetValues.length; i++) {
            int finalI = i;
            targetValueDistribution[i] = keyValuePairs.stream().map(AbstractMap.SimpleImmutableEntry::getValue)
                    .filter(label -> label.equals(targetValues[finalI])).count();
        }

        bCutPoints = IntStream.range(0, keyValuePairs.size() - 1)
                .mapToObj(i -> (((keyValuePairs.get(i).getKey()) + (keyValuePairs.get(i + 1).getKey())) / 2))
                .sorted().distinct()
                .collect(Collectors.toList());

        double globalAmeva = 0.0;
        double ameva = createNewCutPoint(globalAmeva);

        // ameva will be above globalAmeva until the (local) maximum of the contingency coefficient is found.
        // to prevent an infinite looping in while loop, length of potential cut points is tested for emptiness, to break the loop if
        // all potential cut points have been added.
        while (ameva > globalAmeva && !bCutPoints.isEmpty()) {
            globalAmeva = ameva;
            ameva = createNewCutPoint(globalAmeva);
        }

        return getDiscretizationTransitionsFromCutPoints(actualCutPoints, keyValuePairs.get(0).getKey(), keyValuePairs.get(keyValuePairs.size() - 1).getKey());
    }

    /**
     * tentatively adds new cutPoints to the actualCutpoints and accepts the cutPoints with the highest ameva.
     *
     * @param currentAmeva ameva of the current actualCutpoints, used to compare to new value
     * @return the new ameva value, after the best potential cut point has been added to actualCutpoints
     */
    private double createNewCutPoint(double currentAmeva) {
        double maxAmeva = currentAmeva;
        int maxPos = 0;
        for (int i = 0; i < bCutPoints.size(); i++) {
            double potentialCutPoint = bCutPoints.get(i);
            double ameva = determineAmeva(potentialCutPoint);

            if (ameva > maxAmeva) {
                maxPos = i;
                maxAmeva = ameva;
            }
        }
        if (maxAmeva != currentAmeva) {
            actualCutPoints.add(bCutPoints.get(maxPos));
            bCutPoints.remove(maxPos);
        }

        return maxAmeva;
    }

    /**
     * determines the ameva the intervals would have if this cut point would be added. Uses variation of contingency
     * coefficient.
     *
     * @param potentialCutPoint double value of new cut point, used to generate Intervals
     * @return ameva of potential Intervals if cut point would be added
     */
    double determineAmeva(double potentialCutPoint) {
        List<Double> potentialCutPoints = new ArrayList<>(actualCutPoints);
        potentialCutPoints.add(potentialCutPoint);
        Collections.sort(potentialCutPoints);
        List<Interval> intervals = getIntervalsFromCutPoints(potentialCutPoints);

        double chiSquared = 0.0;
        for (int i = 0; i < targetValues.length; i++) {
            for (Interval interval : intervals) {
                double dividend = Math.pow(interval.getClassDist()[i], 2);
                double divisor = targetValueDistribution[i] * (double) interval.getSize();
                chiSquared += dividend / divisor;
            }
        }
        chiSquared = (keyValuePairs.size()) * (-1 + chiSquared);

        return chiSquared / (intervals.size() * (targetValues.length - 1));
    }

    /**
     * creates {@link Interval} from intervals.
     *
     * @param cutPoints list of cut points. Have to be in values.
     * @return list of Intervals
     */
    private List<Interval> getIntervalsFromCutPoints(List<Double> cutPoints) {
        int lowerBoundary = 0;
        int z = 0;
        List<Interval> createdIntervals = new ArrayList<>();
        for (Double cp : cutPoints) {
            while (z < keyValuePairs.size() && keyValuePairs.get(z).getKey() < cp) {
                z++;
            }
            createdIntervals.add(new Interval(lowerBoundary, z - 1, keyValuePairs));
            lowerBoundary = z;
        }
        if (createdIntervals.get(createdIntervals.size() - 1).getEnd() != keyValuePairs.get(keyValuePairs.size() - 1).getKey()) {
            createdIntervals.add(new Interval(lowerBoundary, keyValuePairs.size() - 1, keyValuePairs));
        }
        return createdIntervals;
    }
}
