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

// better way to test reader on its own would be to write a small midi file in the RunBefore.
public class ReaderTest {
    File file;
    Sequence sequence;
    Composition c;

    final int KEY_FROM_BOTTOM_A3 = 24;
    final int KEY_FROM_BOTTOM_C4 = 39;
    // model considers A0 to be note 0, not note 1, hence C4 is 39, not 40.

    @BeforeEach
    public void RunBefore() throws InvalidMidiDataException, IOException {
        file = new File("./data/saveFile.mid");
        sequence = MidiSystem.getSequence(file);
    }

    @Test
    public void testReadFileA3() {
        c = Reader.readFile(file);
        assertEquals(1, c.getNumMeasures());
        assertEquals(4, c.getBeatNum());

        assertEquals(KEY_FROM_BOTTOM_C4, c.getMeasure(1).getNote(1, KEY_FROM_BOTTOM_C4).getPitch());
    }

    @Test
    public void testReadingMuseScoreOutput() throws InvalidMidiDataException, IOException {
        file = new File("./data/ReadingMuseScoreOutputTest.mid");
        sequence = MidiSystem.getSequence(file);
        c = Reader.readFile(file);
        assertEquals(1, c.getNumMeasures());
        assertEquals(4, c.getBeatNum());
        assertEquals(4, c.getMeasure(1).getListOfNote().size());

    }

}
