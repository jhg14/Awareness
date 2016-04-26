package tests;

import main.Command;
import main.Instruction;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by JHGWhite on 26/04/2016.
 */
public class InstructionTests {

    @Test
    public void instructionEqualityFunctionWorkingInTrueCase() {
        Instruction ins1 = new Instruction(Command.ADD, new int[]{1 ,1, 4, 5});
        Instruction ins2 = new Instruction(Command.ADD, new int[]{1, 1, 4, 5});

        assertTrue(ins1.equals(ins2));
    }

    @Test
    public void instructionEqualityFunctionWorkingInFalseCase() {
        Instruction ins1 = new Instruction(Command.ADD, new int[]{1 ,1, 4, 5});
        Instruction ins2 = new Instruction(Command.DOOR, new int[]{1, 1});

        assertFalse(ins1.equals(ins2));
    }

}
