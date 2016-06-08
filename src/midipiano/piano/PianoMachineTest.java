package midipiano.piano;

import static org.junit.Assert.assertEquals;

import javax.sound.midi.MidiUnavailableException;

import midipiano.midi.Instrument;
import midipiano.midi.Midi;
import midipiano.music.Pitch;

import org.junit.Test;

public class PianoMachineTest {
	
	PianoMachine pm = new PianoMachine();
	
    @Test
    public void singleNoteTest() throws MidiUnavailableException {
        String expected0 = "on(61,PIANO) wait(100) off(61,PIANO)";
        
    	Midi midi = Midi.getInstance();

    	midi.clearHistory();
    	
        pm.beginNote(new Pitch(1));
		Midi.wait(100);
		pm.endNote(new Pitch(1));

        System.out.println(midi.history());
        assertEquals(expected0,midi.history());
    }
    
    @Test
    public void doubleNoteTest() throws MidiUnavailableException {
        String expected = "on(60,PIANO) wait(30) on(65,PIANO) wait(100) off(60,PIANO) wait(30) off(65,PIANO)";
        
        Midi midi = Midi.getInstance();
        midi.clearHistory();
        
        pm.beginNote(new Pitch(0));
        Midi.wait(30);
        
        pm.beginNote(new Pitch(5));
        Midi.wait(100);
        pm.endNote(new Pitch(0));
        
        Midi.wait(30);
        pm.endNote(new Pitch(5));
        
        System.out.println(midi.history());
        assertEquals(expected, midi.history());
    }
    
    @Test
    public void instrumentChange() {
        Instrument testInstrument = Instrument.PIANO;
        
        assertEquals(testInstrument, pm.getInstrumet());
        
        // Change instruments on both test and actual.
        pm.changeInstrument();
        testInstrument = testInstrument.next();
        
        // Check for consistency with both test and actual Instrument enum.
        assertEquals(testInstrument, pm.getInstrumet());
        assertEquals(Instrument.BRIGHT_PIANO, pm.getInstrumet());
        
    }
    
    @Test
    public void allInstrumentChange() {
        Instrument testInstrument = Instrument.PIANO;
        
        // Two iterations over all instruments (2 x 128 total instruments)
        int totalChanges = 256;
        
        for (int i = 0; i < totalChanges; i++) {
            assertEquals(testInstrument, pm.getInstrumet());

            testInstrument = testInstrument.next();
            pm.changeInstrument();
        }
    }
    
    @Test
    public void octaveShiftDown() {
        int defaultOctave = 0;
        int pmOctave = pm.getOctave();
        
        assertEquals(defaultOctave, pmOctave);
        
        pm.shiftDown();
        int expOctave = -1;
        assertEquals(expOctave, pm.getOctave());
        
        pm.shiftDown();
        expOctave = -2;
        assertEquals(expOctave, pm.getOctave());
        
        // Reset pm's octaves
        pm.shiftUp();
        pm.shiftUp();
    }
    
    @Test
    public void octaveShiftUp() {
        int defaultOctave = 0;
        int pmOctave = pm.getOctave();
        
        assertEquals(defaultOctave, pmOctave);
        
        pm.shiftUp();
        int expOctave = 1;
        assertEquals(expOctave, pm.getOctave());
        
        pm.shiftUp();
        expOctave = 2;
        assertEquals(expOctave, pm.getOctave());
        
        // Reset pm's octaves
        pm.shiftDown();
        pm.shiftDown();
    }
    
    @Test
    public void octaveMoreThanTwice() {
        int maxShiftUp = 2;
        // Try to shift 10 octaves up.
        for (int i = 0; i < 10; i++) {
            pm.shiftUp();
        }
        assertEquals(maxShiftUp, pm.getOctave());
        
        int maxShiftDown = -2;
        // Try to shift 8 octaves down (2 iterations to resent default state).
        for (int i = 0; i < 10; i++) {
            pm.shiftDown();
        }
        assertEquals(maxShiftDown, pm.getOctave());
        
        // Reset pm's octaves
        pm.shiftUp();
        pm.shiftUp();
    }
}