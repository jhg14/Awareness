package main;

import java.util.Scanner;

/**
 * Created by JHGWhite on 26/04/2016.
 */

public class Awareness {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int worldx, worldy;
        System.out.println("Please enter the width of your desired world: ");
        worldx = s.nextInt();
        System.out.println("Please enter the height of your desired world: ");
        worldy = s.nextInt();

        //Change to command line arguments

        Board board = new Board(worldx, worldy);
        System.out.println(board);

        InstructionParser instructionParser = new InstructionParser();

        while (s.hasNext()){

            //Parse the input
            String input = s.nextLine();
            System.out.println(input);
            //Instruction ins = instructionParser.parse(input);

            //System.out.println(ins);


            //Change the board

            //Print out the state of the board
        }

    }
}
