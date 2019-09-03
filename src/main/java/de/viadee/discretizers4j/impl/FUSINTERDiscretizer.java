package de.viadee.discretizers4j.impl;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import de.viadee.discretizers4j.AbstractSupervisedDiscretizer;
import de.viadee.discretizers4j.DiscretizationTransition;
import de.viadee.discretizers4j.Interval;

/**
 * Implementation of the FUSINTER discretization algorithm described by [Zighed,
 * Rabaséda, Rakotomalala 1998]
 */
public class FUSINTERDiscretizer extends AbstractSupervisedDiscretizer {
	private final double lambda;
	private final double alpha;

	/** Number of possible classifications */
	private int m;

	/** Number of instances */
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
	 * Implementation of FUSINTER, 1. sort, 2. equalClassIntervals 3. merge if
	 * entropy improves
	 *
	 * @param keyValuePairs List of Values and Labels
	 * @return list of Intervals determined to have the highest entropy
	 */
	@Override
	protected List<DiscretizationTransition> fitCreateTransitions(
			List<AbstractMap.SimpleImmutableEntry<Double, Double>> keyValuePairs) {

		m = keyValuePairs.stream().map(AbstractMap.SimpleImmutableEntry::getValue).sorted().distinct().toArray().length;
		n = keyValuePairs.size();

		final List<Interval> equalClassSplits = equalClassSplit(keyValuePairs);
		final List<Interval> evaluatedIntervals = evaluateIntervals(equalClassSplits, keyValuePairs);
		final List<Double> actualCutPoints = new ArrayList<>(100);
		for (int i = 0; i < evaluatedIntervals.size() - 1; i++) {
			actualCutPoints.add((keyValuePairs.get(evaluatedIntervals.get(i).getEnd()).getKey()
					+ keyValuePairs.get(evaluatedIntervals.get(i + 1).getBegin()).getKey()) / 2D);
		}

		return getDiscretizationTransitionsFromCutPoints(actualCutPoints, keyValuePairs.get(0).getKey(),
				keyValuePairs.get(keyValuePairs.size() - 1).getKey());
	}

	private List<Interval> evaluateIntervals(List<Interval> equalClassSplits,
			final List<AbstractMap.SimpleImmutableEntry<Double, Double>> keyValuePairs) {
		boolean improvement = true;

		while (equalClassSplits.size() > 1 && improvement) {
			int deleteIndex = 0;
			double maxMergeCriterion = 0.0;
			final double oldCriterion = determineDiscretizationCriterion(equalClassSplits);
			for (int i = 0; i < equalClassSplits.size() - 1; i++) {
				final List<Interval> possibleMergedInterval = mergeInterval(equalClassSplits, i, keyValuePairs);
				final double mergeCriterion = oldCriterion - determineDiscretizationCriterion(possibleMergedInterval);
				if (mergeCriterion > maxMergeCriterion) {
					deleteIndex = i;
					maxMergeCriterion = mergeCriterion;
				}
			}

			if (maxMergeCriterion > 0) {
				equalClassSplits = mergeInterval(equalClassSplits, deleteIndex, keyValuePairs);
			} else {
				improvement = false;
			}
		}

		return equalClassSplits;
	}

	/**
	 * determines the Entropy of the given Intervals with the quadratic entropy
	 * formula
	 *
	 * @param intervals to be evaluated
	 * @return double from 0 to 1 with 0 being perfect entropy
	 */
	private double determineDiscretizationCriterion(List<Interval> intervals) {
		double criterion = 0;
		double quotient = 0;
		double intervalClassSum;
		final double mTimesLambda = m * lambda;
		for (final Interval interval : intervals) {
			intervalClassSum = 0;
			final int intervalsize = interval.getSize();
			for (int i = 0; i < m; i++) {
				quotient = (interval.getClassDist()[i] + lambda) / (intervalsize + mTimesLambda);
				intervalClassSum += (quotient * (1 - quotient));
			}
			double intervalSum = alpha * (intervalsize / (double) n) * intervalClassSum;
			intervalSum += ((1 - alpha) * ((mTimesLambda) / intervalsize));
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
		final List<Interval> temp = new ArrayList<>(intervals.subList(0, intervals.size()));
		final int mergeBegin = temp.get(i).getBegin();
		final int mergeEnd = temp.get(i + 1).getEnd();
		temp.add(i, new Interval(mergeBegin, mergeEnd, keyValuePairs));
		temp.remove(i + 1);
		temp.remove(i + 1);

		return temp;
	}

}