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
        writer = new Writer();
        composition = new Composition(4, 3, 4);
        // a note on the first beat of the last measure
        Note note = new Note(1, 1, 31);
        Measure measure = new Measure(3, 4);
        measure.addNote(note);
        composition.addMeasure(measure);

        // a note on the first beat of the first measure
        note = new Note( 1, 1, 60);
        composition.getMeasure(1).addNote(note);

        // a note on the last beat of the last measure
        note = new Note(3, 1, 70);
        composition.getMeasure(5).addNote(note);

        // a note on the last beat of the second measure
        note = new Note(3, 1, 88);
        composition.getMeasure(2).addNote(note);
    }

    @Test
    public void testWriteFile() {
        assertEquals(5, composition.getNumMeasures());
        // call
        writer.writeFile(composition, "./data/saveFileTest.mid");
        // read back
        Composition c = Reader.readFile(new File("./data/saveFileTest.mid"));
        assertEquals(5, c.getNumMeasures());
        assertEquals(3, c.getBeatNum());
        assertEquals(4, c.getBeatType());
        assertEquals(31, c.getMeasure(5).getNote(1, 31).getPitch());
        assertEquals(1, c.getMeasure(5).getNote(1, 31).getValue());
        assertEquals(60, c.getMeasure(1).getNote(1, 60).getPitch());
        assertEquals(70, c.getMeasure(5).getNote(3, 70).getPitch());
        assertEquals(88, c.getMeasure(2).getNote(3, 88).getPitch());
        //System.out.println(c.getContents());

        c = new Composition(2, 4, 4);

        c.getMeasure(2).addNewNote(4,1,20);
        writer.writeFile(c, "./data/saveFileTest.mid");
        c = Reader.readFile(new File("./data/saveFileTest.mid"));
        assertEquals(2, c.getNumMeasures());
    }
}
