package midipiano.piano;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.sound.midi.MidiUnavailableException;

import midipiano.midi.Instrument;
import midipiano.midi.Midi;
import midipiano.music.NoteEvent;
import midipiano.music.Pitch;

public class PianoMachine {
	
	private Midi midi;
	private Instrument instrument;
	private int octave;
	private State state;
	
	/**
	 * A list that keeps track of the notes played during recording.
	 */
	private List<NoteEvent> recordingEvents;
	
	
    /**
     * Default pitch at 0 semitones. Used for comparison.
     */
    private static final Pitch defaultPitch = new Pitch(0);

    /**
     * Maximum octave shift allowed.
     */
    private static final int MAX_OCTAVE_SHIFT = 2;
    
	
	/**
	 * constructor for PianoMachine.
	 * 
	 * initialize midi device and any other state that we're storing.
	 */
    public PianoMachine() {
    	try {
            midi = Midi.getInstance();
            this.instrument = Instrument.PIANO;
            this.octave = 0;
            this.state = State.normal;
            
        } catch (MidiUnavailableException e1) {
            System.err.println("Could not initialize midi device");
            e1.printStackTrace();
            return;
        }
    }
    
    /**
     * Begin playing a note on the current instrument
     * 
     * If the piano machine is in recording state,
     * add the note as NoteEvent to recordingList.
     * 
     * If the piano machine is in playback state, don't do anything.
     * 
     * @param rawPitch The pitch (musical note's frequency) to be played.
     */
    public void beginNote(Pitch rawPitch) {
        if (this.state == State.playback) return;
        
        Pitch pitch = new Pitch(rawPitch.difference(defaultPitch))
                               .transpose(this.octave * Pitch.OCTAVE);
        
        midi.beginNote(pitch.toMidiFrequency(), this.instrument);
        
        if (this.state == State.recording) {
            NoteEvent event = new NoteEvent(pitch,
                                            System.currentTimeMillis(),
                                            this.instrument,
                                            NoteEvent.Kind.start);
            recordingEvents.add(event);
        }
    }
    
    /**
     * End a note that's currently being played.
     * 
     * If the piano machine is in recording state,
     * add the note as NoteEvent to recordingList.
     * 
     * If the piano machine is in playback state, don't do anything.
     * 
     * @param rawPitch The pitch (musical note's frequency) to be ended. 
     */
    public void endNote(Pitch rawPitch) {
        if (this.state == State.playback) return;
        
        Pitch pitch = new Pitch(rawPitch.difference(defaultPitch))
                               .transpose(this.octave * Pitch.OCTAVE);
        
        midi.endNote(pitch.toMidiFrequency(), this.instrument);
        
        if (this.state == State.recording) {
            NoteEvent event = new NoteEvent(pitch,
                                            System.currentTimeMillis(),
                                            this.instrument,
                                            NoteEvent.Kind.stop);
            recordingEvents.add(event);
        }
    }
        
    /**
     * Change the current instrument.
     */
    public void changeInstrument() {
       	this.instrument = instrument.next();
    }
    
    /**
     * Shift one octave (12 semitones) up.
     * The maximum up shift is by 2 octaves higher than the default. 
     */
    public void shiftUp() {
    	if (this.octave < MAX_OCTAVE_SHIFT) {
    	    this.octave++;
    	}
    }
    
    /**
     * Shift one octave (12 semitones) down.
     * The maximum down shift is by 2 octaves lower than the default.
     */
    public void shiftDown() {
    	if (this.octave > -MAX_OCTAVE_SHIFT) {
    	    this.octave--;
    	}
    }
    
    /**
     * Start/stop recording the notes played.
     * changes: the current state, unless it's in playback mode.
     * 
     * @return true if recording was started, false otherwise.
     */
    public boolean toggleRecording() {
        if (this.state == State.normal) {
            this.state = State.recording;
            this.recordingEvents = new LinkedList<NoteEvent>();
            
            return true;
        } else if (this.state == State.recording) {
            this.state = State.normal;
            
            return false;
        } else {
            // Currently in playback mode, recording not allowed.
            return false;
        }
    }
    
    
    /**
     * Play a recording. 
     * 
     * requires: existing recording and normal current state.
     * changes: the current state until the recording is played. 
     */
    protected void playback() {
        if (recordingEvents == null || recordingEvents.size() == 0 || state != State.normal) {
            return;
        }
        
        this.state = State.playback;
        
        try {
            playRecording();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.state = State.normal;
    }
    
    /**
     * Play a recorded playback.
     * 
     * @throws MidiUnavailableException
     */
    private void playRecording() throws MidiUnavailableException {
        Midi recordingMidi = Midi.getInstance();
        
        Iterator<NoteEvent> iter = recordingEvents.iterator();
        NoteEvent currentEvent = iter.next();
        
        while (iter.hasNext()) {
            Pitch currentPitch = currentEvent.getPitch();
            Instrument currentInstrument = currentEvent.getInstr();
            long eventTime = currentEvent.getTime();

            if (currentEvent.getKind() == NoteEvent.Kind.start) {
                recordingMidi.beginNote(currentPitch.toMidiFrequency(), currentInstrument);
            } else {
                recordingMidi.endNote(currentPitch.toMidiFrequency(), currentInstrument);
            }
            
            // Update the event to get its starting time.
            currentEvent = iter.next();
            long timeUntilNextEvent = (currentEvent.getTime() - eventTime) / 10;
            Midi.wait((int) timeUntilNextEvent);
        }
        
        // End the final note.
        recordingMidi.endNote(currentEvent.getPitch().toMidiFrequency(), 
                              currentEvent.getInstr());
    }
    
    /**
     * Possible type of states for the piano machine. 
     */
    private enum State { 
        recording, normal, playback
    }
    
    /**
     * Instrument getter for testing purposes.
     * 
     * @return Instrument the current instrument.
     */
    Instrument getInstrumet() {
        return this.instrument;
    }
    
    /**
     * Octave getter for testing purposes.
     * 
     * @return octave The current octave.
     */
    int getOctave() {
        return this.octave;
    }    
}