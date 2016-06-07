package pipoetry;

import static org.junit.Assert.*;

import org.junit.Test;

public class DigitsToStringConverterTest {
    @Test
    public void basicNumberSerializerTest() {
        // Input is a 4 digit number, 0.123 represented in base 4
        int[] input = {0, 1, 2, 3};

        // Want to map 0 -> "d", 1 -> "c", 2 -> "b", 3 -> "a"
        char[] alphabet = {'d', 'c', 'b', 'a'};

        String expectedOutput = "dcba";
        assertEquals(expectedOutput,
                     DigitsToStringConverter.convertDigitsToString(
                             input, 4, alphabet));
    }

    @Test
    public void digitGreaterThanBase() {
        int[] input = {0, 1, 2, 3, 5};
        char[] alphabet = {'d', 'c', 'b', 'a'};
        
        assertEquals(null,
                    DigitsToStringConverter.convertDigitsToString(
                            input, 4, alphabet));
    }
    
    @Test
    public void digitLessThanZero() {
        int[] input = {0, 1, -1, 3};
        char[] alphabet = {'d', 'c', 'b', 'a'};
        
        assertEquals(null,
                    DigitsToStringConverter.convertDigitsToString(
                            input, 4, alphabet));
    }
    
    @Test
    public void alphabetLengthGreaterThanBase() {
        int[] input = {0, 1, 2, 3};
        char[] alphabet = {'d', 'c', 'b', 'a'};

        assertEquals(null,
                     DigitsToStringConverter.convertDigitsToString(
                             input, 5, alphabet));
    }
    
    @Test
    public void alphabetLengthLessThanBase() {
        int[] input = {0, 1, 2, 3};
        char[] alphabet = {'d', 'c', 'b', 'a'};

        assertEquals(null,
                     DigitsToStringConverter.convertDigitsToString(
                             input, 3, alphabet));
    }
}
