package main;

import java.util.Arrays;

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

    public Command getCommand() {
        return cmd;
    }

    public int[] getArguments() {
        return arguments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Instruction that = (Instruction) o;

        if (!Arrays.equals(arguments, that.arguments)) return false;
        return cmd == that.cmd;

    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(arguments);
        result = 31 * result + (cmd != null ? cmd.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        String command = cmd.toString();
        String args = "";
        for (int i = 0; i < arguments.length; i++) {
            args+= arguments[i] + " ";
        }
        return args + command;
    }
}
