package pipoetry;

import static org.junit.Assert.*;

import org.junit.Test;

public class PiGeneratorTest {
    
    private static final String PI_FIRST_FIFTY = "243f6a8885a308d313198a2e03707344a4093822299f31d008";
    
    /**
     * powerMod tests.
     */
    
    @Test
    public void basicPowerModTest() {
        // 5^7 mod 23 = 17
        assertEquals(17, PiGenerator.powerMod(5, 7, 23));
    }

    @Test
    public void testNegativeA() {
        // -1^2 mod 23 = -1 by postcondition.
        assertEquals(-1, PiGenerator.powerMod(-1, 2, 23));
    }
    
    @Test
    public void testNegativeB() {
        // 2^(-1) mod 23 = -1 by postcondition.
        assertEquals(-1, PiGenerator.powerMod(2, -1, 23));
    }
    
    @Test
    public void testNegativeM() {
        // 2^2 mod (-1) = -1 by postcondition.
        assertEquals(-1, PiGenerator.powerMod(2, 2, -1));
    }
    
    @Test
    public void testZeroA() {
        // 0^5 mod 42 = 0
        assertEquals(0, PiGenerator.powerMod(0, 5, 42));
    }
    
    @Test
    public void testZeroB() {
        // 5^0 mod 42 = 1
        assertEquals(1, PiGenerator.powerMod(5, 0, 42));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testZeroM() {
        int mod = PiGenerator.powerMod(1, 2, 0);
    }
    
    @Test
    public void testLargeA() {
        // 123456^2 mod 23 = 18
        assertEquals(18, PiGenerator.powerMod(123456, 2, 23));
    }
    
    @Test
    public void testLargeB() {
        // 2^42 mod 13 = 12
        assertEquals(12, PiGenerator.powerMod(2, 42, 13));
    }
    
    @Test
    public void testLargeM() {
        // 2^5 mod  42 423 131 = 32
        assertEquals(32, PiGenerator.powerMod(2, 5, 42423131));
    }
    
    @Test
    public void testLargeALargeM() {
        // 123 456^2 mod 42 423 131 = 11 479 907
        assertEquals(11479907, PiGenerator.powerMod(123456, 2, 42423131));
    }
    
    
    /**
     * computePiInHex tests.
     */
    
    @Test
    public void testNegativePrecision() {
        assertEquals(null, PiGenerator.computePiInHex(-1));
    }
    
    @Test
    public void testZeroPrecision() {
        int[] result = PiGenerator.computePiInHex(0);
        assertEquals(0, result.length);
    }
    
    @Test
    public void testOnePrecision() {
        int[] result = PiGenerator.computePiInHex(1);
        assertEquals(1, result.length);
        // 2 is the first digit in hex pi.
        assertEquals(Integer.parseInt(PI_FIRST_FIFTY.substring(0, 1)), result[0]);
    }
    
    @Test
    public void testTenPrecision() {
        int[] result = PiGenerator.computePiInHex(10);
        assertEquals(10, result.length);
                
        for (int i = 0; i < result.length; i++) {
            int expectedPiDigit = Integer.valueOf(String.valueOf(PI_FIRST_FIFTY.substring(i, i+1)), 16);
            assertEquals(expectedPiDigit, result[i]);
        }
    }
    
    @Test
    public void testOneHundredPrecision() {
        String hundredPiDigits = PI_FIRST_FIFTY + "2efa98ec4e6c89452821e638d01377be5466cf34e90c6cc0ac";
        
        int[] result = PiGenerator.computePiInHex(100);
        assertEquals(100, result.length);
        
        for (int i = 0; i < result.length; i++) {
            int expectedPiDigit = Integer.valueOf(String.valueOf(hundredPiDigits.substring(i, i+1)), 16);
            assertEquals(expectedPiDigit, result[i]);
        }
    }
}