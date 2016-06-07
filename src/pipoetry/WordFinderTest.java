package pipoetry;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class WordFinderTest {
    @Test
    public void basicGetSubstringsTest() {
        String haystack = "abcde";
        String[] needles = {"ab", "abc", "de", "fg"};

        Map<String, Integer> expectedOutput = new HashMap<String, Integer>();
        expectedOutput.put("ab", 0);
        expectedOutput.put("abc", 0);
        expectedOutput.put("de", 3);

        assertEquals(expectedOutput, WordFinder.getSubstrings(haystack,
                                                              needles));
    }
    
    @Test
    public void moreSubstringsTest() {
        String haystack = "qwertyhjklvim";
        String[] needles = {"wer", "ab", "ty", "hjkl", "vim"};
        
        Map<String, Integer> expectedOutput = new HashMap<String, Integer>();
        expectedOutput.put("wer", 1);
        expectedOutput.put("ty", 4);
        expectedOutput.put("hjkl", 6);
        expectedOutput.put("vim", 10);
        
        assertEquals(expectedOutput, WordFinder.getSubstrings(haystack,
                needles));
    }
    
    @Test
    public void nothigFoundInHaystack() {
        String haystack = "abcde";
        String[] needles = {"gh", "ejus", "plt", "eyw"};
        
        Map<String, Integer> expectedOutput = new HashMap<String,Integer>();
        
        assertEquals(expectedOutput, WordFinder.getSubstrings(haystack,
                needles));
    }    
}