package main;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by JHGWhite on 17/05/2016.
 */
public class World {

    private char[][] tiles;
    private int x;
    private int y;

    public World (int x, int y){
        tiles = new char[x][y];
        this.x = x;
        this.y = y;
        initWorld();
    }

    public List<Character> getAdjacentTiles(int x, int y) {
        List<Character> adj = new ArrayList<>();

        for(int i = x-1; i <= x+1; i++) {
            for (int j = y-1; j <= y+1; j++) {
                try {
                    if (!(x == i && y == j))
                    adj.add(tiles[i][j]);
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
        }
        return adj;
    }

    private void initWorld(){
        for (int j = 0; j < tiles.length; j++){
            Arrays.fill(tiles[j], '_');
        }
    }


    private boolean isEdge(int i, int j) {
        return (i == 0 || j == 0 || i == (x-1) || j == (y-1));
    }

}
