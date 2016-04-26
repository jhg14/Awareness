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

        Board board = new Board(worldx, worldy);
        System.out.println(board);

        InstructionParser instructionParser = new InstructionParser();

        boolean exited = false;
        while (!exited){






        }

    }
}
