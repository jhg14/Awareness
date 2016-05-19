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

    private String b3
            = "______\n_wwww_\n_wAAw_\n_wAwww\n_wwwBw\n___www\n";

    private String b4
            = "______\n_wwww_\n_wAAw_\n_wADww\n_wwwBw\n___www\n";

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
    public void overlappingRoomEntryGivesCorrectResult () {
        board = new Board(6, 6);
        board.applyInstruction(new Instruction(Command.ADD, new int[]{1, 1, 4, 4}));
        board.applyInstruction(new Instruction(Command.REMOVE, new int[]{3, 3, 2, 2}));
        board.applyInstruction(new Instruction(Command.ADD, new int[]{3, 3, 3, 3}));

        assertThat(board.toString(), is(b3));
    }

    @Test
    public void applicationOfDoorGivesCorrectStringResultAndValues () {
        board = new Board(6, 6);
        board.applyInstruction(new Instruction(Command.ADD, new int[]{1, 1, 4, 4}));
        board.applyInstruction(new Instruction(Command.REMOVE, new int[]{3, 3, 2, 2}));
        board.applyInstruction(new Instruction(Command.ADD, new int[]{3, 3, 3, 3}));
        board.applyInstruction(new Instruction(Command.DOOR, new int[]{3, 3}));

        assertThat(board.toString(), is(b4));
        assertThat(board.getNumberOfRooms(), is(2));
        assertThat(board.getNumberOfRoomsInEachGroup().get(0), is(1));
        assertThat(board.getNumberOfRoomsInEachGroup().get(1), is(1));

    }

}
