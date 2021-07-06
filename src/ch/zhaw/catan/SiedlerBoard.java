package ch.zhaw.catan;

import ch.zhaw.catan.Config.Land;
import ch.zhaw.hexboard.HexBoard;

import java.awt.Point;
import java.util.Map;

/**
 * This class displays the board as a whole and checks the desired coordination of the structures to be set.
 * @author Gruppe2 IT20ta_WIN
 * @version 2020-12-04
 */

public class SiedlerBoard extends HexBoard<Land, String, String, String> {

    private SiedlerBoardTextView siedlerBoardTextView;


    /**
     * Constructs the whole board.
     */
    public SiedlerBoard() {
        siedlerBoardTextView = new SiedlerBoardTextView(this);
    }

    /**
     * This method behaves like a dice.
     * @return - A number between 2 and 12
     */
    public int rollDice() {
        int maxNr = 6;
        int minNr = 1;
        return (int) (((Math.random() * maxNr) + 1) + ((Math.random() * maxNr) + minNr));
    }


    // getters and setters beyond this point only

    public SiedlerBoardTextView getSiedlerBoardTextView() {
        return siedlerBoardTextView;
    }

    public void setSiedlerBoardTextView(SiedlerBoardTextView siedlerBoardTextView) {
        this.siedlerBoardTextView = siedlerBoardTextView;
    }
}
