package pipoetry;

import static org.junit.Assert.*;

import org.junit.Test;

public class AlphabetGeneratorTest {
    @Test
    public void generateFrequencyAlphabetTest() {
        // Expect in the training data that Pr(a) = 2/5, Pr(b) = 2/5,
        // Pr(c) = 1/5.
        String[] trainingData = {"aa", "bbc"};
        // In the output for base 10, they should be in the same proportion.
        char[] expectedOutput = {'a', 'a', 'a', 'a',
                                 'b', 'b', 'b', 'b',
                                 'c', 'c'};
        assertArrayEquals(expectedOutput,
                AlphabetGenerator.generateFrequencyAlphabet(
                        10, trainingData));
    }
    
    @Test
    public void frequencyMixed() {
        // Expect in the training data that Pr(a) = 2/5, Pr(b) = 2/5,
        // Pr(c) = 1/5.
        String[] trainingData = {"ba", "bca"};
        
        // In the output for base 10, they should be in the same proportion.
        char[] expectedOutput = {'a', 'a', 'a', 'a',
                                 'b', 'b', 'b', 'b',
                                 'c', 'c'};
        assertArrayEquals(expectedOutput,
                AlphabetGenerator.generateFrequencyAlphabet(
                        10, trainingData));
    }
    
    @Test
    public void exampleTest() {
        // Expect in training data Pr(a) = 302/1000, Pr(b) = 500/1000.
        // Pr(c) = 198/1000
        
        String[] trainingData = new String[3];
        int[] distribution = {302, 500, 198};
        StringBuilder sb = new StringBuilder();

        char testChar = 'a';
        for (int i = 0; i < distribution.length; i++) {
            for (int j = 0; j < distribution[i]; j++) {
                sb.append(testChar);
            }
            trainingData[i] = sb.toString();
            sb.setLength(0);
            testChar++;
        }
       
       char[] expectedOutput = {'a', 'a', 'a', 'b',
                                'b', 'b', 'b', 'b',
                                'c', 'c' };
       
       assertArrayEquals(expectedOutput,
               AlphabetGenerator.generateFrequencyAlphabet(
                       10, trainingData));
    }
    
    @Test
    public void singleChar() {
        // Expect that Pr(a) = 10/10.
        String[] trainingData = {"aa", "a", "aaa", "aa", "aaaa"};
        
        // All should be a's.
        char[] expectedOutput = {'a', 'a', 'a', 'a',
                                 'a', 'a', 'a', 'a',
                                 'a', 'a'};
        
        assertArrayEquals(expectedOutput,
                AlphabetGenerator.generateFrequencyAlphabet(
                        10, trainingData));
    }
    
    @Test
    public void charOutOfRange() {
        // Expect in the training data that Pr(a) = 2/5, Pr(b) = 2/5,
        // Pr(c) = 1/5. "<" should be ignored.
        String[] trainingData = {"aa", "bb<c"};
        // In the output for base 10, they should be in the same proportion.
        char[] expectedOutput = {'a', 'a', 'a', 'a',
                                 'b', 'b', 'b', 'b',
                                 'c', 'c'};
        assertArrayEquals(expectedOutput,
                AlphabetGenerator.generateFrequencyAlphabet(
                        10, trainingData));
    }

    @Test
    public void zeroBase() {
        String[] trainingData = {"aa", "bbc"};

        assertEquals(0,
                    AlphabetGenerator.generateFrequencyAlphabet(
                            0, trainingData).length);
    }
    
    @Test
    public void negativeBase() {
        String[] trainingData = {"aa", "bbc"};
        
        assertEquals(null,
                    AlphabetGenerator.generateFrequencyAlphabet(
                            -1, trainingData));
    }
}