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
        tiles = new char[y][x];
        initBoard();
    }

    private void initBoard(){
        for (int j = 0; j < tiles.length; j++){
            Arrays.fill(tiles[j], '_');
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int j = 0; j < y; j++){
            for (int i = 0; i < x; i++){
                builder.append(tiles[j][i]);
            }
            builder.append('\n');
        }
        return builder.toString();
    }

}
