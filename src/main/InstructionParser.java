package main;

import com.sun.javaws.exceptions.InvalidArgumentException;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by JHGWhite on 26/04/2016.
 */
public class InstructionParser {

    public Instruction parse(String input) throws InvalidInstructionException {
        //Pre: string is of format <x x x x COM> <x x DOOR>
        //Post: returns Instruction: arguments = {x, x, x, x}, cmd = Command.COM
        List<String> tokens = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(input);
        while (tokenizer.hasMoreElements()){
            tokens.add(tokenizer.nextToken());

        }
        /* Parse the command - it is always the last token */

        Command cmd;
        try {
            cmd = parseCommand(tokens.remove(tokens.size()-1));
        } catch (IllegalArgumentException e) {
            throw new InvalidInstructionException(e.getMessage());
        }

        /* Parse the arguments */
        int[] args = parseArgs(tokens);

        Instruction instruction = new Instruction(cmd, args);
        return instruction;
    }

    private Command parseCommand(String cmd) {
        return Command.valueOf(cmd);
    }

    private int[] parseArgs(List<String> args) {
        int[] intArgs = new int[args.size()];
        for (int i = 0; i < args.size(); i++) {
            intArgs[i] = Integer.parseInt(args.get(i));
        }
        return intArgs;
    }

}
