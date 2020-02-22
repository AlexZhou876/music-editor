package persistence;

import model.Composition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import java.io.File;
import java.io.IOException;

public class ReaderTest {
    File file;
    Sequence sequence;
    final int KEY_FROM_BOTTOM_A3 = 24;

    @BeforeEach
    public void RunBefore() throws InvalidMidiDataException, IOException {
        file = new File("./data/midifile.mid");
        sequence = MidiSystem.getSequence(file);
    }

    @Test
    public void testReadFileA3() {
        Composition c = Reader.readFile(file);
        assertEquals(1, c.getNumMeasures());
        assertEquals(4, c.getNumBeats());

        assertEquals(KEY_FROM_BOTTOM_A3, c.getMeasure(1).getNote(1, KEY_FROM_BOTTOM_A3).getPitch());
    }

}
