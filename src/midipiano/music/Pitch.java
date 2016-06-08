/**
 * Author: rcm
 * 6.005 Elements of Software Construction
 * (c) 2008-9, MIT and Robert Miller
 */
package midipiano.music;

/**
 * Pitch represents the frequency of a musical note.
 * Standard music notation represents pitches by letters: A, B, C, D, E, F, G.
 * Pitches can be sharp or flat, or whole octaves up or down from these primitive
 * generators.
 * For example: 
 * new Pitch('C') makes middle C.
 * new Pitch('C').transpose(1) makes C-sharp.
 * new Pitch('E').transpose(-1) makes E-flat.
 * new Pitch('C').transpose(OCTAVE) makes high C.
 * new Pitch('C').transpose(-OCTAVE) makes low C.
 */
public class Pitch {
    private final int value;
    // Rep invariant: true.
    // Abstraction function AF(value): 
    //   AF(0),...,AF(12) map to middle C, C-sharp, D, ..., A, A-sharp, B.
    //   AF(i+12n) maps to n octaves above middle AF(i)
    //   AF(i-12n) maps to n octaves below middle AF(i)
    
    private static final int[] scale = {
        /* A */ 9,
        /* B */ 11,
        /* C */ 0,
        /* D */ 2,
        /* E */ 4,
        /* F */ 5,
        /* G */ 7,
    };
    
    // middle C in the Pitch data type, used to
    // map pitches to Midi frequency numbers.
    private final static Pitch C = new Pitch('C');

    /**
     * initialize Pitch directly using our internal pitch scale, which 
     * corresponds to the difference in midi frequency relative to midi frequency 60. 
     * 
     * @param value
     */
    public Pitch(int value) {
        this.value = value;
    }

    /**
     * Make a Pitch.
     * @requires c in {'A',...,'G'}
     * @returns Pitch named c in the middle octave of the piano keyboard.
     * For example, new Pitch('C') constructs middle C 
     */
    public Pitch(char c) {
        try {
            value = scale[c-'A'];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException(c + " must be in the range A-G");
        }
    }
    
    /**
     * Number of pitches in an octave.
     */
    public static final int OCTAVE = 12;
    
    /**
     * @return pitch made by transposing this pitch by semitonesUp semitones.
     * For example, middle C transposed by 12 semitones is high C;
     * E transposed by -1 semitones is E flat.
     */
    public Pitch transpose(int semitonesUp) {
        return new Pitch(value + semitonesUp);
    }
    
    /**
     * @return number of semitones between this and that; i.e.,
     * n such that that.transpose(n).equals(this).
     */
    public int difference(Pitch that) {
        return this.value - that.value;        
    }
    
    /**
     * @return true iff this pitch is lower than that pitch
     */
    public boolean lessThan(Pitch that) {
        return this.difference(that) < 0;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) return false;
        Pitch that = (Pitch) obj;
        return this.value == that.value;            
    }
    
    @Override
    public int hashCode() {
        return value;
    }
    
    /**
     * get midi frequency of pitch. standard frequency scale for midi protocol
     * in which "middle c" is 48, and a pitch foo semitones above middle c is foo+48.
     * 
     * @return freq: midi frequency
     */
    public int toMidiFrequency () {
    	return difference(C) + 60;
    }
    
    /**
     * @return this pitch in abc music notation
     *   (see http://www.walshaw.plus.com/abc/examples/)
     */    
    @Override
    public String toString() {
        String suffix = "";
        int v = value;
        
        while (v < 0) {
            suffix += ",";
            v += 12;
        }
        
        while (v >= 12) {
            suffix += "'";
            v -= 12;
        }
        
        return valToString[v] + suffix;
    }

    private static final String[] valToString = {
        "C", "^C", "D", "^D", "E", "F", "^F", "G", "G^", "A", "^A", "B"
    };
}
