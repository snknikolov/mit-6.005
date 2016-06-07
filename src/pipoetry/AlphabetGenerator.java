package pipoetry;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class AlphabetGenerator {
    /**
     * Given a numeric base, return a char[] that maps every digit that is
     * representable in that base to a lower-case char.
     * 
     * This method will try to weight each character of the alphabet
     * proportional to their occurrence in words in a training set.
     * 
     * This method should do the following to generate an alphabet:
     *   1. Count the occurrence of each character a-z in trainingData.
     *   2. Compute the probability of each character a-z by taking
     *      (occurrence / total_num_characters).
     *   3. The output generated in step (2) is a PDF of the characters in the
     *      training set. Convert this PDF into a CDF for each character.
     *   4. Multiply the CDF value of each character by the base we are
     *      converting into.
     *   5. For each index 0 <= i < base,
     *      output[i] = (the first character whose CDF * base is > i)
     * 
     * A concrete example:
     * 	 0. Input = {"aaaaa..." (302 "a"s), "bbbbb..." (500 "b"s),
     *               "ccccc..." (198 "c"s)}, base = 93
     *   1. Count(a) = 302, Count(b) = 500, Count(c) = 193
     *   2. Pr(a) = 302 / 1000 = .302, Pr(b) = 500 / 1000 = .5,
     *      Pr(c) = 198 / 1000 = .198
     *   3. CDF(a) = .302, CDF(b) = .802, CDF(c) = 1
     *   4. CDF(a) * base = 28.086, CDF(b) * base = 74.586, CDF(c) * base = 93
     *   5. Output = {"a", "a", ... (28 As, indexes 0-27),
     *                "b", "b", ... (47 Bs, indexes 28-74),
     *                "c", "c", ... (18 Cs, indexes 75-92)}
     * 
     * The letters should occur in lexicographically ascending order in the
     * returned array.
     *   - {"a", "b", "c", "c", "d"} is a valid output.
     *   - {"b", "c", "c", "d", "a"} is not.
     *   
     * If base >= 0, the returned array should have length equal to the size of
     * the base.
     * 
     * If base < 0, return null.
     * 
     * If a String of trainingData has any characters outside the range a-z,
     * ignore those characters and continue.
     * 
     * @param base A numeric base to get an alphabet for.
     * @param trainingData The training data from which to generate frequency
     *                     counts. This array is not mutated.
     * @return A char[] that maps every digit of the base to a char that the
     *         digit should be translated into.
     */
    public static char[] generateFrequencyAlphabet(int base,
                                                   String[] trainingData) {
        if (base < 0) return null;
        
        Map<Character, Integer> frequencyMap = new HashMap<Character, Integer>();
        
        char[] trainingChars = Arrays.toString(trainingData).toCharArray();
        double totalNumValidChars = trainingChars.length;
        
        for (Character c : trainingChars) {
            Integer count = frequencyMap.get(c);
            
            if (count != null) {
                frequencyMap.put(c, count + 1);
            } else if (count == null && isInRange(c)) {
                frequencyMap.put(c, 1);
            } else {
                totalNumValidChars--;
            }
        }
                  
        char[] output = distributeChars(frequencyMap, totalNumValidChars, base);
        return output;
    }
    
    /**
     * Distribute characters in a char[] based on their occurrence frequency.
     * 
     * @param frequencyMap Mapping between char and number of the char's occurrences.
     * @param totalNumValidChars The total number of valid (in range a-z) characters.
     * @param base A numeric base to get an alphabet for.
     * 
     * @return Alphabetically sorted char[] based on the number of occurrences.
     */
    private static char[] distributeChars(Map<Character, Integer> frequencyMap,
                                          double totalNumValidChars, int base) {
        // Sort the character keys to get the output in alphabetical order.
        SortedSet<Character> sorted = new TreeSet<Character>(frequencyMap.keySet());
        double[] cdf = new double[sorted.size()];
        char[] output = new char[base];

        Iterator<Character> iter = sorted.iterator();
        
        // Get the first character, set the initial CDF value and max index.
        Character first = iter.next();
        double currentProb = (double) frequencyMap.get(first) / totalNumValidChars;
        cdf[0] = currentProb;
        int maxIndex = (int) (currentProb * base);
        int prevIndex = maxIndex;
        
        // Fill the first range in the output array with the first character.
        for (int i = 0; i < maxIndex; i++) {
            output[i] = first;
        }
        
        for (int i = 1; i < cdf.length; i++) {
            // Advance to next character, compute its CDF and max index.
            Character c = iter.next();
            double probCurrent = (double) frequencyMap.get(c) / totalNumValidChars;
            cdf[i] = cdf[i-1] + probCurrent;
            maxIndex = (int) (cdf[i] * base);

            // Fill the computed range with current character.
            for (int j = prevIndex; j < maxIndex; j++) {
                output[j] = c;
            }
            
            prevIndex = maxIndex;
        }
                
        return output;
    }
    
    /**
     * Check if a Character c is in range a <= c <= z
     * 
     * @param c Character to be checked for range.
     * @return true if c is in range, false otherwise.
     */
    private static boolean isInRange(Character c) {
        return c >= 'a' && c <= 'z';
    }    
}