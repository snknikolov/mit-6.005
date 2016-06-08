/**
 * 6.005 Elements of Software Construction
 * (c) 2007-8-9, MIT and Rob Miller
 */
package midipiano.midi;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sound.midi.*;

/**
 * Midi represents a MIDI synthesis device.
 */
public class Midi {
	// string for logging
	private StringBuilder stringBuilder;
	
	// time of previous note on or off, for logging purposes
	private long prevEventTime;
	
    private Synthesizer synthesizer;

    public final static Instrument DEFAULT_INSTRUMENT = Instrument.PIANO;
    
    // active MIDI channels, assigned to instruments
    private final Map<midipiano.midi.Instrument, MidiChannel> channels = 
            new HashMap<midipiano.midi.Instrument, MidiChannel>();
    
    // next available channel number (unassigned to an instrument yet)
    private int nextChannel = 0;
    
    //  volume -- a percentage?
    private static final int VELOCITY = 100; 

    /**
     * check that invariants are preserved
     */
    private void checkRep() {
        assert synthesizer != null;
        assert channels != null;
        assert nextChannel >= 0;
    }
    
    /**
     * Make a Midi.
     * @throws MidiUnavailableException if MIDI is not available
     */
    private Midi() throws MidiUnavailableException {
        synthesizer = MidiSystem.getSynthesizer();
        synthesizer.open();
        synthesizer.loadAllInstruments(synthesizer.getDefaultSoundbank());
        checkRep();
        stringBuilder = new StringBuilder();
        prevEventTime = -1;
    }
    
    private static Midi theMidi = null;
    
    public static Midi getInstance() throws MidiUnavailableException {
    	if(theMidi == null) {
    	    theMidi = new Midi();
    	}
    	return theMidi;
    }

    /**
     * Play a note on the Midi scale for a specified duration. Log to stringBuilder.
     * 
     * @param note: midi frequency value; 0<= note < 256.
     * @param duration: note duration in ms; >= 0.
     * @param instr: instrument in midi.Instrument enum.
     */
    public void play(int note, int duration, midipiano.midi.Instrument instr) {
        MidiChannel channel = getChannel(instr);
        synchronized (channel) {
            channel.noteOn(note, VELOCITY);
        }
        
        log(note,instr,true);
        
        wait(duration);

        synchronized (channel) {
            channel.noteOff(note);
        }
        
        log(note,instr,false);
    }

    /**
     * Start playing a note on the Midi scale using a specified instrument. Log to stringBuilder.
     * 
     * @param note: midi frequency value; 0<= note < 256.
     * @param instr: instrument in midi.Instrument enum.
     */
    public void beginNote(int note, midipiano.midi.Instrument instr) {
        MidiChannel channel = getChannel(instr);
        synchronized (channel) {
            channel.noteOn(note, VELOCITY);
        }
        
        log(note,instr,true);
    }
    /**
     * Call beginNote with the default instrument.
     * 
     * @param note: first argument to pass to beginNote(int note, midi.Instrument instr)
     */
    public void beginNote(int note) {
        beginNote(note, DEFAULT_INSTRUMENT);
    }
    
    /**
     * Stop playing a note on the Midi scale using a specified instrument. Log to stringBuilder.
     * 
     * @param note: midi frequency value; 0<= note < 256.
     * @param instr: instrument in midi.Instrument enum.
     */
    public void endNote(int note, midipiano.midi.Instrument instr) {
        MidiChannel channel = getChannel(instr);
        synchronized (channel) {
            channel.noteOff(note, VELOCITY);
        }
        
        log(note,instr,false);
    }

    /**
     * Call endNote with the default instrument.
     * 
     * @param note: first argument to pass to endNote(int note, midi.Instrument instr)
     */
    public void endNote(int note) {
        endNote (note, DEFAULT_INSTRUMENT);
    }

    /**
     * Wait for a duration in hundredths of a second.
     * @requires duration >= 0
     */
    public static void wait(int duration) {
        long now = System.currentTimeMillis();
        long end = now + 10*duration;
        while (now < end) {
            try {
                Thread.sleep((int) (end - now));
            } catch (InterruptedException e) {
            }
            now = System.currentTimeMillis();
        }
    }
    
    /**
     * Returns a trace of events on this object since the last call to clearHistory().
     * Events are formatted as follows:
     * "on(pitch,instr)" for every note-on event.
     * "off(pitch,instr)" for every note-off event.
     * "wait(time)" denotes the time separating every pair of note-on or note-off events.
     * In the parameters above, "pitch" is the midi frequency of the note event, "instr" is
     * the instrument name as it appears in the Instrument enum, and "time" is the 
     * separation time in hundredths of a second. These individual event logs are space-separated.
     * 
     * As an example, suppose we are on the default piano instrument. 
     * We clearHistory(), then start a note with midi frequency 60, then wait 30 ms, 
     * then start a note with midi frequency 65, then wait 100 ms, then release the first note, 
     * then wait 30 ms, then release the second note, and then call history().  
     * history() will return this string:
     * "on(60,PIANO) wait(3) on(65,PIANO) wait(10) off(60,PIANO) wait(3) off(65,PIANO)"
     * 
     * @return history: trace of events.
     */
    public String history() {
    	if(stringBuilder.length() == 0) {
    		return "";
    	} else {
        	// take the substring to strip off the trailing space.
    		return stringBuilder.substring(0, stringBuilder.length()-1).toString();
    	}
    }
    
    /**
     * Clear the event history which history() returns.
     */
    public void clearHistory() {
    	stringBuilder = new StringBuilder();
    	prevEventTime = -1;
    }
    
    /**
     * Helper method which adds a new note-on or note-off entry to the trace 
     * returned by history().  The note entry is preceded by a wait(time) entry 
     * when appropriate.
     * 
     * @param note: midi frequency of note event being logged
     * @param instr: instrument of note event being logged
     * @param isNoteOn: true for note on event, false for note off event
     */
    private void log(int note, midipiano.midi.Instrument instr, boolean isNoteOn) {
    	long time = (new Date()).getTime();
        if(prevEventTime > 0) {
        	long timeDif = Math.round((time-prevEventTime)/10.0);
        	stringBuilder.append("wait("+timeDif+") ");
        }
        prevEventTime = time;
        if(isNoteOn) {
        	stringBuilder.append("on(");
        } else {
        	stringBuilder.append("off(");
        }
        stringBuilder.append(note+","+instr+") ");
    }
    
    /**
     * Assign an instrument to a midi channel, if it doesn't have a channel already.
     * 
     * @param instr
     * @return channel: MidiChannel which instr is patched to
     * 
     * modifies this
     */
    private MidiChannel getChannel(midipiano.midi.Instrument instr) {
        synchronized (channels) {
            // check whether this instrument already has a channel
            MidiChannel channel = channels.get(instr);
            if (channel != null) return channel;
            
            channel = allocateChannel();
            patchInstrumentIntoChannel(channel, instr);            
            channels.put(instr, channel);
            checkRep();
            return channel;
        }        
    }

    /**
     * Return a channel from the midi synthesizer.
     * 
     * We iterate through the channels of the synthesizer circularly, thereby returning 
     * the least recently allocated channel.
     * 
     * @return channel
     */
    private MidiChannel allocateChannel() {
        MidiChannel[] channels = synthesizer.getChannels();
        if (nextChannel >= channels.length) throw new RuntimeException("tried to use too many instruments: limited to " + channels.length);
        MidiChannel channel = channels[nextChannel];
        // quick hack by DNJ to allow more instruments to be used
        nextChannel = (nextChannel + 1) % channels.length;
        return channel;
    }
    
    /**
     * Wraps midi interface method to assign an instrument to a channel
     * 
     * @param channel
     * @param instr
     */
    private void patchInstrumentIntoChannel(MidiChannel channel, 
                                            midipiano.midi.Instrument instr) {
        channel.programChange(0, instr.ordinal());        
    }

    /**
     * Discover all the instruments in your MIDI synthesizer and print
     * them to standard output, along with their bank and program codes.
     */
    public static void main(String[] args) throws MidiUnavailableException {
        Midi m = new Midi();
        for (javax.sound.midi.Instrument i : m.synthesizer.getLoadedInstruments()) {
            System.out.println(i.getName() + " " + i.getPatch().getBank() + " " + i.getPatch().getProgram());
        }
    }
}
