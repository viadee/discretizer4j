package de.viadee.discretizers4j.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import de.viadee.discretizers4j.DiscretizationTransition;
import de.viadee.discretizers4j.NumericDiscretizationOrigin;

class FUSINTERDiscretizerTest {

	/**
	 * Tests Steps 4-8 (Evaluation of potential merges of the equalClassSplits) of
	 * the described Algorithm, Number[][] to be discretized is taken from FUSINTER
	 * paper Figure 7.
	 */
	@Test
	void testIntervalReduction() {
		final Number[][] values = new Number[][] { { 1.0, 1 }, { 2.0, 0 }, { 3.0, 1 }, { 4.0, 1 }, { 5.0, 1 },
				{ 6.0, 1 }, { 7.0, 1 }, { 8.0, 1 }, { 9.0, 1 }, { 10.0, 1 }, { 11.0, 1 }, { 12.0, 1 }, { 13.0, 1 },
				{ 14.0, 1 }, { 15.0, 0 }, { 16.0, 1 }, { 17.0, 0 }, { 18.0, 1 }, { 19.0, 0 }, { 20.0, 1 }, { 21.0, 0 },
				{ 22.0, 1 }, { 23.0, 0 }, { 24.0, 1 }, { 25.0, 0 }, { 26.0, 1 }, { 27.0, 0 }, { 28.0, 0 }, { 29.0, 0 },
				{ 30.0, 0 }, { 31.0, 0 }, { 32.0, 0 }, { 33.0, 0 }, { 34.0, 0 }, { 35.0, 0 }, { 36.0, 0 }, { 37.0, 0 },
				{ 38.0, 1 }, { 39.0, 0 }, { 40.0, 1 }, };
		final Serializable[] serializables = new Serializable[values.length];
		final Double[] doubles = new Double[values.length];
		for (int i = 0; i < values.length; i++) {
			serializables[i] = values[i][0];
			doubles[i] = values[i][1].doubleValue();
		}

		final FUSINTERDiscretizer fusinterDiscretizer = new FUSINTERDiscretizer();
		fusinterDiscretizer.fit(serializables, doubles);
		final List<DiscretizationTransition> list = new ArrayList<>(fusinterDiscretizer.getTransitions());
		assertEquals(4, list.size());
	}

	/**
	 * Tests Steps 4-8 (Evaluation of potential merges of the equalClassSplits) of
	 * the described Algorithm, Number[][] to be discretized is taken from FUSINTER
	 * paper Figure 5 and 6.
	 */
	@Test
	void testIntervalReductionBig() {
		final Number[][] values = new Number[][] { { 1.0, 1 }, { 2.0, 0 }, { 3.0, 1 }, { 3.0, 1 }, { 4.0, 1 },
				{ 4.0, 1 }, { 5.0, 1 }, { 5.0, 1 }, { 5.0, 1 }, { 6.0, 1 }, { 6.0, 1 }, { 6.0, 1 }, { 7.0, 1 },
				{ 7.0, 1 }, { 7.0, 1 }, { 8.0, 1 }, { 8.0, 1 }, { 9.0, 1 }, { 9.0, 1 }, { 9.0, 1 }, { 10.0, 1 },
				{ 10.0, 1 }, { 10.0, 1 }, { 11.0, 1 }, { 11.0, 1 }, { 11.0, 1 }, { 12.0, 1 }, { 12.0, 1 }, { 13.0, 1 },
				{ 13.0, 1 }, { 13.0, 0 }, { 14.0, 1 }, { 14.0, 1 }, { 14.0, 1 }, { 15.0, 0 }, { 15.0, 0 }, { 15.0, 0 },
				{ 16.0, 1 }, { 16.0, 1 }, { 16.0, 1 }, { 17.0, 0 }, { 17.0, 0 }, { 17.0, 0 }, { 18.0, 1 }, { 18.0, 1 },
				{ 18.0, 0 }, { 19.0, 0 }, { 19.0, 0 }, { 20.0, 1 }, { 20.0, 1 }, { 22.0, 1 }, { 22.0, 1 }, { 22.0, 1 },
				{ 23.0, 0 }, { 23.0, 0 }, { 23.0, 0 }, { 27.0, 0 }, { 27.0, 0 }, { 28.0, 0 }, { 28.0, 0 }, { 28.0, 0 },
				{ 29.0, 0 }, { 29.0, 0 }, { 29.0, 0 }, { 30.0, 0 }, { 30.0, 0 }, { 30.0, 0 }, { 31.0, 0 }, { 31.0, 0 },
				{ 31.0, 0 }, { 33.0, 0 }, { 34.0, 0 }, { 34.0, 0 }, { 34.0, 0 }, { 35.0, 0 }, { 35.0, 0 }, { 35.0, 0 },
				{ 36.0, 0 }, { 36.0, 0 }, { 36.0, 0 }, { 37.0, 0 }, { 37.0, 0 }, { 37.0, 1 }, { 38.0, 1 }, { 38.0, 1 },
				{ 38.0, 0 }, { 39.0, 0 }, { 39.0, 0 }, { 40.0, 1 }, { 40.0, 1 }, };
		final Serializable[] serializables = new Serializable[values.length];
		final Double[] doubles = new Double[values.length];
		for (int i = 0; i < values.length; i++) {
			serializables[i] = values[i][0];
			doubles[i] = values[i][1].doubleValue();
		}

		final FUSINTERDiscretizer fusinterDiscretizer = new FUSINTERDiscretizer();
		fusinterDiscretizer.fit(serializables, doubles);

		final List<DiscretizationTransition> list = new ArrayList<>(fusinterDiscretizer.getTransitions());

		final NumericDiscretizationOrigin discOrigin = ((NumericDiscretizationOrigin) list.get(0)
				.getDiscretizationOrigin());
		assertEquals(1D, discOrigin.getMinValue());
		assertEquals(14.5, discOrigin.getMaxValue());
		assertEquals(0D, list.get(0).getDiscretizedValue().doubleValue());
		assertTrue(discOrigin.isFirst());
		assertFalse(discOrigin.isLast());

		final NumericDiscretizationOrigin discOrigin1 = ((NumericDiscretizationOrigin) list.get(1)
				.getDiscretizationOrigin());
		assertEquals(14.5, discOrigin1.getMinValue());
		assertEquals(19.5, discOrigin1.getMaxValue());
		assertEquals(1D, list.get(1).getDiscretizedValue().doubleValue());
		assertFalse(discOrigin1.isFirst());
		assertFalse(discOrigin1.isLast());

		final NumericDiscretizationOrigin discOrigin2 = ((NumericDiscretizationOrigin) list.get(2)
				.getDiscretizationOrigin());
		assertEquals(19.5, discOrigin2.getMinValue());
		assertEquals(22.5, discOrigin2.getMaxValue());
		assertEquals(2D, list.get(2).getDiscretizedValue().doubleValue());
		assertFalse(discOrigin2.isFirst());
		assertFalse(discOrigin2.isLast());

		final NumericDiscretizationOrigin discOrigin3 = ((NumericDiscretizationOrigin) list.get(3)
				.getDiscretizationOrigin());
		assertEquals(22.5, discOrigin3.getMinValue());
		assertEquals(36.5, discOrigin3.getMaxValue());
		assertEquals(3D, list.get(3).getDiscretizedValue().doubleValue());
		assertFalse(discOrigin3.isFirst());
		assertFalse(discOrigin3.isLast());

		final NumericDiscretizationOrigin discOrigin4 = ((NumericDiscretizationOrigin) list.get(4)
				.getDiscretizationOrigin());
		assertEquals(36.5, discOrigin4.getMinValue());
		assertEquals(40D, discOrigin4.getMaxValue());
		assertEquals(4D, list.get(4).getDiscretizedValue().doubleValue());
		assertFalse(discOrigin4.isFirst());
		assertTrue(discOrigin4.isLast());

	}

	/**
	 * Tests Steps 4-8 (Evaluation of potential merges of the equalClassSplits) of
	 * the described Algorithm, Number[][] to be discretized is taken from FUSINTER
	 * paper Figure 7.
	 */
	@Test
	void testEqualDistribution() {
		final Number[][] values = new Number[][] { { 1.0, 1 }, { 1.0, 0 }, { 2.0, 1 }, { 2.0, 0 }, { 3.0, 1 },
				{ 3.0, 0 }, { 4.0, 1 }, { 4.0, 0 } };
		final Serializable[] serializables = new Serializable[values.length];
		final Double[] doubles = new Double[values.length];
		for (int i = 0; i < values.length; i++) {
			serializables[i] = values[i][0];
			doubles[i] = values[i][1].doubleValue();
		}

		final FUSINTERDiscretizer fusinterDiscretizer = new FUSINTERDiscretizer();
		fusinterDiscretizer.fit(serializables, doubles);
		final List<DiscretizationTransition> list = new ArrayList<>(fusinterDiscretizer.getTransitions());
		assertEquals(1, list.size());
	}

	/**
	 * Tests the performance with a larger dataset. Benchmark Runtime (Laptop) is
	 * 0.286 s.
	 */
	@Test
	void testFUSINTERPerformance() {
		final Number[][] saltValues = new Number[][] { { 1.0, 1 }, { 1.0, 0 }, { 2.0, 1 }, { 2.0, 0 }, { 3.0, 1 },
				{ 3.0, 0 }, { 4.0, 1 }, { 4.0, 0 } };
		final int dataMultiplicator = 50000; // blow up the dataset
		final Serializable[] serializables = new Serializable[saltValues.length * dataMultiplicator];
		final Double[] doubles = new Double[saltValues.length * dataMultiplicator];
		for (int i = 0; i < saltValues.length * dataMultiplicator; i++) {
			serializables[i] = saltValues[i % saltValues.length][0];
			doubles[i] = saltValues[i % saltValues.length][1].doubleValue();
		}

		final FUSINTERDiscretizer fusinterDiscretizer = new FUSINTERDiscretizer();
		fusinterDiscretizer.fit(serializables, doubles);
		final List<DiscretizationTransition> list = new ArrayList<>(fusinterDiscretizer.getTransitions());
		assertEquals(1, list.size());
	}

}