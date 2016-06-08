package midipiano.music;

import midipiano.midi.Instrument;

public class NoteEvent {
	
    public enum Kind { 
        start, stop
    }
    
	private final Pitch pitch; 
	private final long time;
	private final Instrument instr;
	private final Kind kind; 
	
	/**
	 * constructor for note event
	 * 
	 * @param pitch
	 * @param time
	 * @param instr
	 * @param kind
	 */
	public NoteEvent(Pitch pitch, long time, Instrument instr, Kind kind) {
		this.pitch = pitch;
		this.time = time;
		this.instr = instr;
		this.kind = kind;
	}

	/**
	 * 
	 * @return pitch: pitch object wrapping midi freq of note event.
	 */
	public Pitch getPitch() {
		return pitch;
	}

	/**
	 * 
	 * @return time: time in milliseconds when note event occurred.
	 */
	public long getTime() {
		return time;
	}

	/**
	 * 
	 * @return instr: instrument used for the midi note event
	 */
    public Instrument getInstr() {
        return instr;
    }

    /**
     * 
     * @return kind: was the event a note on or note off?
     */
    public Kind getKind() {
        return kind;
    } 
	
}