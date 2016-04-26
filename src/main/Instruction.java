package main;

/**
 * Created by JHGWhite on 26/04/2016.
 */
public class Instruction {

    private int[] arguments;
    private Command cmd;

    public Instruction(Command cmd, int[] arguments) {
        this.cmd = cmd;
        this.arguments = arguments;
    }

}
