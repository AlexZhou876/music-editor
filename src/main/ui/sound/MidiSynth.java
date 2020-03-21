// From SimpleDrawingPlayer-Starter

package ui.sound;


import javax.sound.midi.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Illustrates general MIDI melody instruments and MIDI controllers.
 *
 * @author Brian Lichtenwalter
 * <p>
 * NOTE TO SOFTWARE CONSTRUCTION STUDENTS:
 * Reduced & modified for Software Construction course
 * @version @(#)MidiSynth.java	1.15 99/12/03
 */

public class MidiSynth {

    private Synthesizer synthesizer;
    private Instrument[] instruments;

    private ChannelData[] channels;

    // EFFECTS: return the channel at the given index
    public ChannelData getSpecialisedChannel(int index) {
        return channels[index];
    }


    // MODIFIES: this
    // EFFECTS:  prepares instruments, channels for playback
    public void open() {
        getSynthesizer();
        setupInstruments();
        setupChannels();
    }

    // EFFECTS: synthesizes sound given instrument, note, and velocity
    public void play(int instrument, int note, int velocity) {
        ChannelData channelData = getChannelData(instrument);
        MidiChannel midiChannel = channelData.getChannel();
        midiChannel.noteOn(note, velocity);
    }

    // EFFECTS: stops playback of the given instrument
    public void stop(int instrument, int note) {
        ChannelData channelData = getChannelData(instrument);
        MidiChannel midiChannel = channelData.getChannel();
        midiChannel.noteOff(note, 0);
    }

    // MODIFIES: this
    // EFFECTS:  sets up the channels for this MidiSynth
    private void setupChannels() {
        MidiChannel[] midiChannels = synthesizer.getChannels();
        channels = new ChannelData[midiChannels.length];
        for (int i = 0; i < channels.length; i++) {
            channels[i] = new ChannelData(midiChannels[i], i);
        }
    }

    // MODIFIES: this
    // EFFECTS:  populates this with a variety of instruments
    private void setupInstruments() {
        if (getSoundBank() != null) {
            instruments = synthesizer.getDefaultSoundbank().getInstruments();
            synthesizer.loadInstrument(instruments[0]);
        }
    }

    // EFFECTS: gets the soundbank from the synthesizer
    private Soundbank getSoundBank() {
        return synthesizer.getDefaultSoundbank();
    }

    private void getSynthesizer() {
        try {
            if (synthesizer == null) {
                if ((synthesizer = MidiSystem.getSynthesizer()) == null) {
                    System.out.println("getSynthesizer() failed!");
                    return;
                }
            }
            synthesizer.open();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    // EFFECTS: returns the channel associated with the given instrument, stored in HashMap
    private ChannelData getChannelData(int instrument) {
        Map<Integer, ChannelData> channelMap = new HashMap<Integer, ChannelData>();
        ChannelData channelData = channelMap.get(instrument);
        if (channelData == null) {
            channelData = getSpecialisedChannel(channelMap.size());
            MidiChannel midiChannel = channelData.getChannel();
            midiChannel.programChange(instrument);
            channelMap.put(instrument, channelData);
        }
        return channelData;
    }
}
