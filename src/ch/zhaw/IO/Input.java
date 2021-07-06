package ch.zhaw.IO;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

/**
 * This class reads the input commands
 *
 * @author Gruppe2 IT20ta_WIN
 * @version 2020-12-04
 */

public class Input {

    private TextIO textIO;
    private TextTerminal<?> textTerminal;

    /**
     * Constructs the reference of the terminal
     */
    public Input() {
        textIO = TextIoFactory.getTextIO();
        textTerminal = textIO.getTextTerminal();
    }

    /**
     * Shows a command list and returs the enumeration value
     *
     * @param commands enumeration type
     * @return a number of the enumeration value
     */
    public <T extends Enum<T>> T getEnumValue(Class<T> commands) {
        return textIO.newEnumInputReader(commands).read("Your next move please");
    }

    /**
     * Reads the next entered String value
     *
     * @return a String value
     */
    public String readNextString() {
        return textIO.newStringInputReader().read();
    }

    /**
     * Reads the next entered Int value
     *
     * @return a Int value
     */
    public int readNextInt(String message) {
        //Maybe we need this e.g. to match IDs to Resource and therefore access easier
        return textIO.newIntInputReader().read(message);
    }

    /**
     * Disposes the text io
     *
     */
    public void disposeTextIO(){
        textIO.dispose();
    }
}
