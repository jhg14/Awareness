package tests;

import main.Board;
import main.Command;
import main.Instruction;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by JHGWhite on 26/04/2016.
 */
public class BoardTests {

    private Board board;

    // 5x7 Initial Board String for testing
    private String testInitialBoardString
            = "_____\n_____\n_____\n_____\n_____\n_____\n_____\n";

    private String b1
            = "______\n_wwww_\n_wAAw_\n_wAAw_\n_wwww_\n______\n";

    private String b2
            = "______\n_wwww_\n_w__w_\n_w____\n_ww___\n______\n";

    @Test
    public void blankBoardIsCorrectlyInitialised() {
        board = new Board(5, 7);
        assertThat(board.toString(), is(testInitialBoardString));
    }

    @Test
    public void applyingAddInstructionGivenInSpecReturnsAnswerGivenInSpec() {
        board = new Board(6, 6);
        board.applyInstruction(new Instruction(Command.ADD, new int[]{1, 1, 4, 4}));

        assertThat(board.toString(), is(b1));
    }

    @Test
    public void applyingRemoveInstructionGivenInSpecReturnsAnswerGivenInSpec() {
        board = new Board(6, 6);
        board.applyInstruction(new Instruction(Command.ADD, new int[]{1, 1, 4, 4}));
        board.applyInstruction(new Instruction(Command.REMOVE, new int[]{3, 3, 2, 2}));

        assertThat(board.toString(), is(b2));
    }

    @Test
    public void removalOfInvalidTbdsCorrectlyReducesToEnclosures () {
        board = new Board(8, 7);
        String withoutInvalidTbds
                = "________\n" +
                  "__wwww__\n" +
                  "__w%%w__\n" +
                  "__w%%w__\n" +
                  "__wwww__\n" +
                  "________\n" +
                  "________\n";

        board.applyInstruction(new Instruction(Command.ADD, new int[]{2, 1, 4, 4}));
        board.removeInvalidTbdTiles();

        assertThat(board.toString(), is(withoutInvalidTbds));

    }


}
