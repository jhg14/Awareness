package main;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

/**
 * Created by JHGWhite on 26/04/2016.
 */

public class Awareness {

    public static void main(String[] args) {
        int worldx, worldy;
        worldx = Integer.valueOf(args[0]);
        worldy = Integer.valueOf(args[1]);
        //check for invalid input

        run(worldx, worldy, System.in, System.out);

    }

    public static void run(int x, int y, InputStream in, OutputStream out) {

        Scanner s = new Scanner(in);

        Board board = new Board(x, y);
        System.out.println(board);

        InstructionParser instructionParser = new InstructionParser();

        while (s.hasNext()){

            //Parse the input
            String input = s.nextLine();

            Instruction ins;
            try {
                ins = instructionParser.parse(input);
            } catch (InvalidInstructionException e) {
                System.out.println("Dodgy input - try again");
                continue;
            }

            board.applyInstruction(ins);

            System.out.println(board);
            int num = board.getNumberOfRooms();
            System.out.println("There " + (num == 1 ? "is " : "are ") + num
                                        + (num <= 1 ? "" : " different") + " room"
                                        + (num == 1 ? "." : "s."));
            System.out.println(board.getRoomsAndDoors());
        }

    }
}
