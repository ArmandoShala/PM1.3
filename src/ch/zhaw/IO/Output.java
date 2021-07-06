package ch.zhaw.IO;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

/**
 * This class prints texts, new line and closes the terminal
 * @author Gruppe2 IT20ta_WIN
 * @version 2020-12-04
 */
public class Output {

    private TextIO textIO;
    private TextTerminal<?> textTerminal;

    public Output() {
        textIO = TextIoFactory.getTextIO();
        textTerminal = textIO.getTextTerminal();
    }

    /**
     * Prints the input message
     * @param message input message
     */
    public void printText(String message){
        textTerminal.println(message);
    }

    public void printError(){
        printText("Not possible");
    }

    /**
     * Prints a new line with empty space
     */
    public void printNewLine(){
        printText("");
    }

    /**
     * Disposes the text io
     */
    public void disposeTextIO(){
        textIO.dispose();
    }

}
