package ch.zhaw.catan;

import ch.zhaw.catan.Config.Land;
import ch.zhaw.hexboard.HexBoardTextView;

/**
 * This class displays the text within the board.
 * @author Gruppe2 IT20ta_WIN
 * @version 2020-12-04
 */


public class SiedlerBoardTextView extends HexBoardTextView<Land, String, String, String> {

  /**
   * This constructor displays the text within the board.
   * @param board - The board itself without description
   */
  public SiedlerBoardTextView(SiedlerBoard board) {
    super(board);
  }

}
