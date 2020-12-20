package persistence;

import model.Composition;
import model.Measure;
import model.Note;
import org.omg.CORBA.DynAnyPackage.Invalid;

import static persistence.Reader.*;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// writes a midi file based on program data.
public class Writer {
    private int ticksPerBeat = 24;
    private static int ARBITRARY_BUFFER = 10;

    public void writeFile(Composition composition, String path) throws InvalidMidiDataException, IOException {
        Sequence sequence = ToSequence.toSequence(composition);
        File file = new File(path);
        MidiSystem.write(sequence, 1, file);
    }



}
