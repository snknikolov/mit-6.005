package pipoetry;

import static org.junit.Assert.*;

import org.junit.Test;

public class BaseTranslatorTest {
    @Test
    public void basicBaseTranslatorTest() {
        // Expect that .01 in base-2 is .25 in base-10
        // (0 * 1/2^1 + 1 * 1/2^2 = .25)
        int[] input = {0, 1};
        int[] expectedOutput = {2, 5};

        assertArrayEquals(expectedOutput,
                          BaseTranslator.convertBase(input, 2, 10, 2));
    }

    @Test
    public void testBaseALessThanTwo() {
        int[] input = {0, 1};
        assertArrayEquals(null, BaseTranslator.convertBase(input, 1, 10, 2));
    }
    
    @Test
    public void testBaseBLessThanTwo() {
        int[] input = {0, 1};
        assertArrayEquals(null, BaseTranslator.convertBase(input, 2, 1, 2));
    }
    
    @Test
    public void testPrecisionLessThanOne() {
        int[] input = {0, 1};
        assertArrayEquals(null, BaseTranslator.convertBase(input, 2, 10, 0));
    }
    
    @Test
    public void testNegativeDigit() {
        int[] input = {0, -1};
        assertArrayEquals(null, BaseTranslator.convertBase(input, 2, 10, 2));
    }
    
    @Test
    public void testDigitGreaterThanBaseA() {
        int[] input = {0, 1, 27};
        assertArrayEquals(null, BaseTranslator.convertBase(input, 5, 10, 2));
    }
}