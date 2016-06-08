/**
 * 6.005 Elements of Software Construction
 * (c) 2007-8-9, MIT and Rob Miller
 */
package midipiano.midi;

/**
 * Instrument represents a musical instrument.
 * 
 * These instruments are the 128 standard General 
 * MIDI Level 1 instruments.  See
 * http://www.midi.org/about-midi/gm/gm1sound.shtml.
 */
public enum Instrument {
    // Order is important in this enumeration,
    // because an instrument's position must 
    // corresponds to its MIDI program number.
    PIANO,
    BRIGHT_PIANO,
    ELECTRIC_GRAND,
    HONKY_TONK_PIANO,
    ELECTRIC_PIANO_1,
    ELECTRIC_PIANO_2,
    HARPSICHORD,
    CLAVINET,

    CELESTA,
    GLOCKENSPIEL,
    MUSIC_BOX,
    VIBRAPHONE,
    MARIMBA,
    XYLOPHONE,
    TUBULAR_BELL,
    DULCIMER,

    HAMMOND_ORGAN,
    PERC_ORGAN,
    ROCK_ORGAN,
    CHURCH_ORGAN,
    REED_ORGAN,
    ACCORDION,
    HARMONICA,
    TANGO_ACCORDION,

    NYLON_STR_GUITAR,
    STEEL_STRING_GUITAR,
    JAZZ_ELECTRIC_GTR,
    CLEAN_GUITAR,
    MUTED_GUITAR,
    OVERDRIVE_GUITAR,
    DISTORTION_GUITAR,
    GUITAR_HARMONICS,

    ACOUSTIC_BASS,
    FINGERED_BASS,
    PICKED_BASS,
    FRETLESS_BASS,
    SLAP_BASS_1,
    SLAP_BASS_2,
    SYN_BASS_1,
    SYN_BASS_2,

    VIOLIN,
    VIOLA,
    CELLO,
    CONTRABASS,
    TREMOLO_STRINGS,
    PIZZICATO_STRINGS,
    ORCHESTRAL_HARP,
    TIMPANI,

    ENSEMBLE_STRINGS,
    SLOW_STRINGS,
    SYNTH_STRINGS_1,
    SYNTH_STRINGS_2,
    CHOIR_AAHS,
    VOICE_OOHS,
    SYN_CHOIR,
    ORCHESTRA_HIT,

    TRUMPET,
    TROMBONE,
    TUBA,
    MUTED_TRUMPET,
    FRENCH_HORN,
    BRASS_ENSEMBLE,
    SYN_BRASS_1,
    SYN_BRASS_2,

    SOPRANO_SAX,
    ALTO_SAX,
    TENOR_SAX,
    BARITONE_SAX,
    OBOE,
    ENGLISH_HORN,
    BASSOON,
    CLARINET,

    PICCOLO,
    FLUTE,
    RECORDER,
    PAN_FLUTE,
    BOTTLE_BLOW,
    SHAKUHACHI,
    WHISTLE,
    OCARINA,

    SYN_SQUARE_WAVE,
    SYN_SAW_WAVE,
    SYN_CALLIOPE,
    SYN_CHIFF,
    SYN_CHARANG,
    SYN_VOICE,
    SYN_FIFTHS_SAW,
    SYN_BRASS_AND_LEAD,

    FANTASIA,
    WARM_PAD,
    POLYSYNTH,
    SPACE_VOX,
    BOWED_GLASS,
    METAL_PAD,
    HALO_PAD,
    SWEEP_PAD,

    ICE_RAIN,
    SOUNDTRACK,
    CRYSTAL,
    ATMOSPHERE,
    BRIGHTNESS,
    GOBLINS,
    ECHO_DROPS,
    SCI_FI,

    SITAR,
    BANJO,
    SHAMISEN,
    KOTO,
    KALIMBA,
    BAG_PIPE,
    FIDDLE,
    SHANAI,

    TINKLE_BELL,
    AGOGO,
    STEEL_DRUMS,
    WOODBLOCK,
    TAIKO_DRUM,
    MELODIC_TOM,
    SYN_DRUM,
    REVERSE_CYMBAL,

    GUITAR_FRET_NOISE,
    BREATH_NOISE,
    SEASHORE,
    BIRD,
    TELEPHONE,
    HELICOPTER,
    APPLAUSE,
    GUNSHOT;
    
    /**
     * @return the next instrument in the standard ordering (or the first
     * in the ordering if this is the last)
     */
    public Instrument next () {
    	for (Instrument i: Instrument.values()) {
    		if (i.ordinal() == (ordinal() + 1) % 128)
    			return i;
    	}
		return this;    	
    }
}
