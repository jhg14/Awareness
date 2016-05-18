package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by JHGWhite on 26/04/2016.
 */
public class Board {

    private static final int ALPHABET_SIZE = 26;

    private static final int ALPHABET_START_DEC = 65;
    private static final int ALPHABET_END_DEC = 90;

    private static final char PLACEHOLDER = '%';


    private char[] alphabet = new char[ALPHABET_SIZE];
    private boolean[] alphaBitmap = new boolean[ALPHABET_SIZE];

    private char[][] tiles;
    private boolean[][] doors;
    private int x;
    private int y;

//    private World world;

    public Board(int x, int y){
        this.x = x;
        this.y = y;
        tiles = new char[x][y];

        //world = new World(x,y);

        doors = new boolean[x][y];
        populateAlphabet();
        initBoard();
    }

    private void initBoard(){
        for (int j = 0; j < tiles.length; j++){
            Arrays.fill(tiles[j], '_');
        }
    }

    //Account for out of board size accesses
    public Board applyInstruction (Instruction i) {
        int[] args = i.getArguments();
        switch (i.getCommand()) {
            case ADD:
                return add(args[0], args[1], args [2], args[3]);
            case REMOVE:
                return remove(args[0], args[1], args [2], args[3]);
            case EXIT:
                break;
            case DOOR:
                return addDoor(args[0], args[1]);
        }
        return null;
    }

    private Board add (int x, int y, int width, int height) {
        for (int i = x; i < x+width; i++) {
            for (int j = y; j < y+height; j++) {
                if (i == x || j == y || i == (x+width-1) || j == (y+height-1)){
                    tiles[i][j] = 'w';
                    //world.setTile(i, j, 'w');
                } else {
                    tiles[i][j] = PLACEHOLDER;
                    //world.setTile(i, j, PLACEHOLDER);
                }
            }
        }
        //replaceEnclosure();
        updateSpaces();
        return this;
    }

    private Board remove(int x, int y, int width, int height) {
        for (int i = x; i < x+width; i++) {
            for (int j = y; j < y+height; j++) {
                tiles[i][j] = '_';
                //world.setTile(i, j, '_');
            }
        }
        //replaceEnclosure();
        updateSpaces();
        return this;
    }

    private Board addDoor(int x, int y) {

        doors[x][y] = true;
        return this;
    }

    private void populateAlphabet() {
        for (int letter = ALPHABET_START_DEC; letter <= ALPHABET_END_DEC; letter++) {
            alphabet[letter-ALPHABET_START_DEC] = (char) letter;
        }
    }

    private char getLetter(){
        for (int i = 0; i < alphaBitmap.length; i++) {
            if (!alphaBitmap[i]) {
                alphaBitmap[i] = true;
                return alphabet[i];
            }
        }
        return '#';
    }

    private void updateSpaces() {

        int changes = -1;

        while (changes != 0) {
            changes = 0;
            for (int i = 0; i < x; i++){
                for (int j = 0; j < y; j++) {
                    // NEED TO MAKE ADJACENT TILE FUNCTION ACCEPT THE ARRAY OF CHARS AS ARRAY OF CHARACTERS
                    if (getAdjacentTilesChar(i, j, tiles).contains('_') && (tiles[i][j] != '_') && (tiles[i][j] != 'w')){
                        tiles[i][j] = '_';
                        changes++;
                    }
                }
            }
        }
    }

    private void detectEnclosure() {
        ReplacementNode[][] nodes = new ReplacementNode[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if (tiles[i][j] == PLACEHOLDER){
                    nodes[i][j] = new ReplacementNode();
                }
            }
        }

        List<List<ReplacementNode>> groups = new ArrayList<>();

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                List<ReplacementNode> adjacent = getAdjacentTiles(i, j, nodes);

                int i_ = i;
                int j_ = j;
                adjacent.forEach((n) -> {
                    List<ReplacementNode> comb = n.mergeGroup(nodes[i_][j_].getGroup());
                    n.setGroup(comb);
                    nodes[i_][j_].setGroup(comb);
                });

                if (!groups.contains(nodes[i][j].getGroup())) {
                    groups.add(nodes[i][j].getGroup());
                }

            }
        }
        System.out.println("The number of different groups is: ");
        System.out.println(groups.size());

    }

    private void replaceEnclosure() {
        char letter = getLetter();
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if (tiles[i][j] == PLACEHOLDER) {
                    tiles[i][j] = letter;
                    //world.setTile(i, j, letter);
                }
            }
        }
    }

    private boolean isLetter(char c) {
        return (c >= ALPHABET_START_DEC) && (c >= ALPHABET_END_DEC) && (!(c == 'w'));
    }
    private boolean isEdge(int i, int j) {
        return (i == 0 || j == 0 || i == (x-1) || j == (y-1));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int j = 0; j < y; j++){
            for (int i = 0; i < x; i++){
                builder.append(tiles[i][j]);
            }
            builder.append('\n');
        }
        return builder.toString();
    }

    private <T> List<T> getAdjacentTiles(int x, int y, T[][] arr) {
        List<T> adj = new ArrayList<>();

        for(int i = x-1; i <= x+1; i++) {
            for (int j = y-1; j <= y+1; j++) {
                try {
                    if (!(x == i && y == j)) {
                        adj.add(arr[i][j]);
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
        }
        return adj;
    }

    private List<Character> getAdjacentTilesChar(int x, int y, char[][] arr) {
        List<Character> adj = new ArrayList<>();

        for(int i = x-1; i <= x+1; i++) {
            for (int j = y-1; j <= y+1; j++) {
                try {
                    if (!(x == i && y == j))
                        adj.add(arr[i][j]);
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
        }
        return adj;
    }

}
