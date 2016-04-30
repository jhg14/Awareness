package main;

import java.util.Arrays;

/**
 * Created by JHGWhite on 26/04/2016.
 */
public class Board {

    private char[][] tiles;
    private int x;
    private int y;

    public Board(int x, int y){
        this.x = x;
        this.y = y;
        tiles = new char[x][y];
        initBoard();
    }

    //Account of out of board size accesses
    public Board applyInstruction (Instruction i) {
        switch (i.getCommand()) {
            case ADD:
                int[] args = i.getArguments();
                return add(args[0], args[1], args [2], args[3]);
            case REMOVE:
                break;
            case EXIT:
                break;
            case DOOR:
                break;
        }
        return null;
    }

    private void initBoard(){
        for (int j = 0; j < tiles.length; j++){
            Arrays.fill(tiles[j], '_');
        }
    }

    private Board add (int x, int y, int width, int height) {

        //if x/y == x/y or x+width/y+height?

        for (int i = x; i < x+width; i++) {
            for (int j = y; j < y+height; j++) {
                if (i == x || j == y || i == (x+width-1) || j == (y+height-1)){
                    tiles[i][j] = 'w';
                } else {
                    tiles[i][j] = 'A';
                }
            }
        }

        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int j = y-1; j >= 0; j--){
            for (int i = 0; i < x; i++){
                builder.append(tiles[i][j]);
            }
            builder.append('\n');
        }
        return builder.toString();
    }



}
