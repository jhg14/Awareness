package tests;

import main.Command;
import main.Instruction;
import main.InstructionParser;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by JHGWhite on 26/04/2016.
 */
public class InstructionParserTests {

    InstructionParser ip = new InstructionParser();

    @Test
    public void parsesValidInputCorrectly () {
        String valid = "1 1 4 5 ADD";

        int[] correctArgs = {1, 1, 4, 5};
        Command correctCommand = Command.ADD;
        Instruction correctInstruction = new Instruction(correctCommand, correctArgs);

        Instruction ins = ip.parse(valid);

        assertThat(ins, is(correctInstruction));
    }

    @Test
    public void parsesInvalidInputByReturning() {

    }

}

// INSTRUCTION .EQUALS TEST