package pipoetry;

public class BaseTranslator {
    /**
     * Converts an array where the ith digit corresponds to (1 / baseA)^(i + 1)
     * digits[i], return an array output of size precisionB where the ith digit
     * corresponds to (1 / baseB)^(i + 1) * output[i].
     * 
     * Stated in another way, digits is the fractional part of a number
     * expressed in baseA with the most significant digit first. The output is
     * the same number expressed in baseB with the most significant digit first.
     * 
     * To implement, logically, you're repeatedly multiplying the number by
     * baseB and chopping off the most significant digit at each iteration:
     * 
     * for (i < precisionB) {
     *   1. Keep a carry, initialize to 0.
     *   2. From RIGHT to LEFT
     *   	a. x = multiply the ith digit by baseB and add the carry
     *      b. the new ith digit is x % baseA
     *      c. carry = x / baseA
     *   3. output[i] = carry
     * 
     * If digits[i] < 0 or digits[i] >= baseA for any i, return null
     * If baseA < 2, baseB < 2, or precisionB < 1, return null
     * 
     * @param digits The input array to translate. This array is not mutated.
     * @param baseA The base that the input array is expressed in.
     * @param baseB The base to translate into.
     * @param precisionB The number of digits of precision the output should
     *                   have.
     * @return An array of size precisionB expressing digits in baseB.
     */
    public static int[] convertBase(int[] digits, int baseA,
                                    int baseB, int precisionB) {
        if (baseA < 2 || baseB < 2 || precisionB < 1) 
            return null;
        
        int[] converted = new int[precisionB];
        // Make a copy to avoid mutating the original input.
        int[] copy = new int[digits.length]; 
        System.arraycopy(digits, 0, copy, 0, digits.length);
        
        for (int i = 0; i < precisionB; i++) {
            int carry = 0;
            for (int j = copy.length - i - 1; j >= 0; j--) {
                if (copy[j] < 0 || copy[j] >= baseA) 
                    return null;
                
                int x = copy[j] * baseB + carry;
                copy[j] = x % baseA;
                carry = x / baseA;
            }
            converted[i] = carry;
        }
        return converted;
    }
}
