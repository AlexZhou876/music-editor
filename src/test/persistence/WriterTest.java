package persistence;
import model.Composition;
import model.Measure;
import model.Note;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import java.io.File;
import java.io.IOException;

public class WriterTest {
    Composition composition;
    Writer writer;

    @BeforeEach
    public void runBefore() {
        composition = new Composition(4, 4, 4);
        Note note = new Note(1, 1, 31);
        Measure measure = new Measure(4, 4);
        measure.addNote(note);
        composition.addMeasure(measure);
        writer = new Writer();
    }

    @Test
    public void testWriteFile() {
        assertEquals(5, composition.getNumMeasures());
        // call
        writer.writeFile(composition);
        // read back
        Composition c = Reader.readFile(new File("./data/saveFile.mid"));
        assertEquals(5, c.getNumMeasures());
        assertEquals(4, c.getBeatNum());
        assertEquals(4, c.getBeatType());
        assertEquals(31, c.getMeasure(5).getNote(1, 31).getPitch());
        assertEquals(1, c.getMeasure(5).getNote(1, 31).getValue());
    }

}
