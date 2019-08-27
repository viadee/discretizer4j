package de.viadee.discretizers4j.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UniqueValueDiscretizerTest {

    @Test
    void testUniqueStringDiscretization() {
        UniqueValueDiscretizer uniqueValueDiscretizer = new UniqueValueDiscretizer();
        uniqueValueDiscretizer.fit(new String[]{"a", "b", "c", "d", "a", "e", "b"});
        Double[] discretizedValues = uniqueValueDiscretizer.apply(new String[]{"a", "b", "c", "d", "a", "e", "b"});
        assertEquals(7, discretizedValues.length);
        assertArrayEquals(new Double[]{0D, 1D, 2D, 3D, 0D, 4D, 1D}, discretizedValues);
    }

}