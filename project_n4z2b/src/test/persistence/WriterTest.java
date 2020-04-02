package persistence;
import model.Composition;
import model.Measure;
import model.Note;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

public class WriterTest {
    Composition composition;
    Writer writer;

    @BeforeEach
    public void runBefore() {
        writer = new Writer();
        composition = new Composition(5, 4, 4);
        // a note on the first beat of the last measure
        Measure measure = composition.getMeasure(5);
        new Note(measure, 17, 1, 31, null);

        // a note on the first beat of the first measure
        measure = composition.getMeasure(1);
        new Note(measure, 1, 1, 60,null);


        // a note on the last beat of the last measure
        measure = composition.getMeasure(5);
        new Note(measure,20, 1, 70, null);


        // a note on the last beat of the second measure
        measure = composition.getMeasure(2);
        new Note(measure,8, 1, 88,null);

        // a note on the first beat of the third measure
        measure = composition.getMeasure(3);
        new Note(measure,9, 1, 1,null);

        // a note on the second beat of the third measure
        measure = composition.getMeasure(3);
        new Note(measure,10, 1, 2,null);

    }

    @Test
    public void testWriteFile() {
        assertEquals(5, composition.getNumMeasures());
        // call
        writer.writeFile(composition, "./data/saveFileTest.mid");
        // read back
        Composition c = Reader.readFile(new File("./data/saveFileTest.mid"));
        assertEquals(5, c.getNumMeasures());
        assertEquals(4, c.getBeatNum());
        assertEquals(4, c.getBeatType());
        assertEquals(31, c.getMeasure(5).getNote(17, 31).getPitch());
        assertEquals(1, c.getMeasure(5).getNote(17, 31).getValue());
        assertEquals(60, c.getMeasure(1).getNote(1, 60).getPitch());
        assertEquals(70, c.getMeasure(5).getNote(20, 70).getPitch());
        assertEquals(88, c.getMeasure(2).getNote(8, 88).getPitch());
        assertEquals(1, c.getMeasure(3).getNote(9, 1).getPitch());
        assertEquals(2, c.getMeasure(3).getNote(10, 2).getPitch());
        //System.out.println(c.getContents());

        c = new Composition(2, 4, 4);

        c.getMeasure(2).addNewNote(8,1,20);
        writer.writeFile(c, "./data/saveFileTest.mid");
        c = Reader.readFile(new File("./data/saveFileTest.mid"));
        assertEquals(2, c.getNumMeasures());
    }
}
