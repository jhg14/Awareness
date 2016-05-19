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
    private static final char DOOR = 'D';

    private int numberOfRooms;
    private List<List<ReplacementNode>> groups;


    private char[] alphabet = new char[ALPHABET_SIZE];
    private boolean[] alphaBitmap = new boolean[ALPHABET_SIZE];

    private char[][] tiles;
    private boolean[][] doors;
    private int x;
    private int y;

    public Board(int x, int y){
        this.x = x;
        this.y = y;
        tiles = new char[x][y];
        doors = new boolean[x][y];

        populateAlphabet();
        initBoard();
        numberOfRooms = 0;
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
                }
            }
        }
        markAllButWallsAsTbd();
        removeInvalidTbdTiles();
        detectEnclosure();
        allocateLettersToGroups(groups);
        return this;
    }

    private Board remove(int x, int y, int width, int height) {
        for (int i = x; i < x+width; i++) {
            for (int j = y; j < y+height; j++) {
                tiles[i][j] = '_';
            }
        }
        markAllButWallsAsTbd();
        removeInvalidTbdTiles();
        detectEnclosure();
        allocateLettersToGroups(groups);
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

    private void freeLetters() {
        for (int i = 0; i < alphaBitmap.length; i++) {
            alphaBitmap[i] = false;
        }
    }

    private void markAllButWallsAsTbd() {
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if (tiles[i][j] != 'w') {
                    tiles[i][j] = PLACEHOLDER;
                }
            }
        }
    }


    /* Every space has been marked as PLACEHOLDER
       1. Anything that is an edge, mark '_'
       2. Anything that is next to a '_', make '_'
           2.1. Until you reach a 'w'
       3. Repeat steps 2 & 3 until no change
     */
    public void removeInvalidTbdTiles() {

        //Replace edge tiles with '_'
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if (isEdge(i, j) && (tiles[i][j] != 'w')) tiles[i][j] = '_';
            }
        }

        //If a tile is connected to one of these newly created '_' tiles,
        //it must not be enclosed so replace those too.
        int change = -1;
        while (change != 0) {
            change = 0;
            for (int i = 0; i < x; i++) {
                for (int j = 0; j < y; j++) {
                    if (tiles[i][j] != 'w' && tiles[i][j] != '_') {
                        List<Character> adj = getAdjacentTilesChar(i, j, tiles);
                        boolean adjacentToEmpty = false;

                        for (Character c : adj) {
                            if (c.charValue() == '_') {
                                adjacentToEmpty = true;
                            }
                        }

                        if (adjacentToEmpty) {
                            tiles[i][j] = '_';
                            change++;
                        }
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
                    nodes[i][j] = new ReplacementNode(i, j);
                }
            }
        }

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if (nodes[i][j] != null) {

                    List<ReplacementNode> adjacent = getAdjacentTiles(i, j, nodes);

                    int i_ = i;
                    int j_ = j;
                    adjacent.forEach((n) -> {
                        if (n != null) {
                            nodes[i_][j_].mergeAndSetGroup(n);
                        }
                    });
                }
            }
        }

        List<List<ReplacementNode>> groups = new ArrayList<>();

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                ReplacementNode node = nodes[i][j];
                if (node != null) {
                    if (!groups.contains(nodes[i][j].getGroup())) {
                        groups.add(nodes[i][j].getGroup());
                    }
                }
            }
        }
        this.groups = groups;
        
        //Convert the list of list of replacement nodes into a list of list of indices,
        //and at each, store the letter assigned and the number of doors in the area.

        //Set the object's number of groups field to the correct value
        numberOfRooms = groups.size();
    }

    private void allocateLettersToGroups(List<List<ReplacementNode>> groups) {
        //Assign letters
        freeLetters();
        for (List<ReplacementNode> group: groups) {
            char letter = getLetter();
            group.forEach((n) -> {
                tiles[n.getX()][n.getY()] = letter;
            });
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
                if (doors[i][j]) {
                    builder.append(DOOR);
                } else {
                    builder.append(tiles[i][j]);
                }
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
                } catch (ArrayIndexOutOfBoundsException e) {}
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
