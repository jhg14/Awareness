package tests;

import main.Board;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by JHGWhite on 26/04/2016.
 */
public class BoardTests {

    Board board;

    // 5x7 Initial Board String for testing
    private String testInitialBoardString
            = "_____\n_____\n_____\n_____\n_____\n_____\n_____\n";

    @Test
    public void blankBoardIsCorrectlyInitialised() {
        board = new Board(5, 7);
        assertThat(board.toString(), is(testInitialBoardString));
    }

}
