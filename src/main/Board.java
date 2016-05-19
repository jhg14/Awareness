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
    private List<Character> lettersInUse;
    private List<Integer> numberOfDoorsInEachGroup;

    private char[] alphabet = new char[ALPHABET_SIZE];
    private boolean[] alphaBitmap = new boolean[ALPHABET_SIZE];

    private char[][] tiles;
    private Boolean[][] doors;
    private int x;
    private int y;

    public Board(int x, int y){
        this.x = x;
        this.y = y;
        tiles = new char[x][y];
        doors = new Boolean[x][y];
        initDoors();

        populateAlphabet();
        initBoard();
        numberOfRooms = 0;
        lettersInUse = new ArrayList<>();
        numberOfDoorsInEachGroup = new ArrayList<>();
    }

    private void initBoard(){
        for (int j = 0; j < tiles.length; j++){
            Arrays.fill(tiles[j], '_');
        }
    }

    private void initDoors() {
        for (Boolean[] row: doors) {
            for (int j = 0; j < y; j++) {
                row[j] = false;
            }
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
        update();
        return this;
    }

    private Board remove(int x, int y, int width, int height) {
        for (int i = x; i < x+width; i++) {
            for (int j = y; j < y+height; j++) {
                tiles[i][j] = '_';
                if (doors[i][j]) doors[i][j] = false;
            }
        }
        update();
        return this;
    }

    private Board addDoor(int x, int y) {

        doors[x][y] = true;
        updateNumberOfDoors();
        return this;
    }

    private void update() {
        markAllButWallsAsTbd();
        removeInvalidTbdTiles();
        detectEnclosure();
        allocateLettersToGroups(groups);
        updateNumberOfDoors();
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
        lettersInUse.clear();
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
       2. Anything that is next to first '_', make '_'
           2.1. Until you reach first 'w'
       3. Repeat steps 2 & 3 until no change
     */
    public void removeInvalidTbdTiles() {

        //Replace edge tiles with '_'
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if (isEdge(i, j) && (tiles[i][j] != 'w')) tiles[i][j] = '_';
            }
        }

        //If first tile is connected to one of these newly created '_' tiles,
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

        int k = (x > y) ? x : y;
        do {
            for (int i = 0; i < x; i++) {
                for (int j = 0; j < y; j++) {
                    if (nodes[i][j] != null) {

                        List<Pair<Coord, ReplacementNode>> adjacent = getAdjacentTiles(i, j, nodes);

                        int i_ = i;
                        int j_ = j;
                        adjacent.forEach((n) -> {
                            if (n.second != null) {
                                nodes[i_][j_].mergeAndSetGroup(n.second);
                            }
                        });
                    }
                }
            }
            k--;
        } while (k > 0);

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
        System.out.println(groups);

        //Convert the list of list of replacement nodes into first list of list of indices,
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
            lettersInUse.add(letter);
        }
    }

    private void updateNumberOfDoors() {
        //For each group, check the adjacent nodes of each member of the group,
        //if it is next to first door, increment the door count, record where the door was, and continue until
        //all have been checked.

        numberOfDoorsInEachGroup.clear();
        for (List<ReplacementNode> group: groups) {
            List<Coord> checked = new ArrayList<>();
            int doorCount = 0;
            for (ReplacementNode member: group) {
                List<Pair<Coord, Boolean>> adjacentDoorTiles = getAdjacentTiles(member.getX(), member.getY(), doors);
                //if true and if not seen, add to checked, increment counter
                for (Pair<Coord, Boolean> adj: adjacentDoorTiles) {
                    if (adj.second && (!checked.contains(adj.first))) {
                        checked.add(adj.first);
                        doorCount++;
                    }
                }
            }

            numberOfDoorsInEachGroup.add(doorCount);
        }
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

    private <T> List<Pair<Coord, T>> getAdjacentTiles(int x, int y, T[][] arr) {
        List<Pair<Coord, T>> coordsAndTiles = new ArrayList<>();

        for(int i = x-1; i <= x+1; i++) {
            for (int j = y-1; j <= y+1; j++) {
                try {
                    if (!(x == i && y == j)) {
                        Pair<Coord, T> pair = new Pair<>((new Coord(i, j)), arr[i][j]);
                        coordsAndTiles.add(pair);
                    }
                } catch (ArrayIndexOutOfBoundsException e) {}
            }
        }
        return coordsAndTiles;
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

    public int getNumberOfRooms() {return numberOfRooms;}

    public String getGroupsAndDoors() {
        StringBuilder builder = new StringBuilder();
        for (List<ReplacementNode> group: groups) {
            int index = groups.indexOf(group);
            char letter = lettersInUse.get(index);
            int numberOfDoors = numberOfDoorsInEachGroup.get(index);
            builder.append("Room " + letter + " has " + numberOfDoors + " door" + (numberOfDoors == 1 ? "." : "s."));
        }

        return builder.toString();
    }
}
