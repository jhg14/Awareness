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
    private static final char EMPTY = '_';
    private static final char WALL = 'w';

    private int numberOfRooms;
    private List<List<ReplacementNode>> groups;
    private List<Character> lettersInUse;
    private List<Integer> numberOfDoorsInEachRoom;

    private char[] alphabet = new char[ALPHABET_SIZE];
    private boolean[] alphaBitmap = new boolean[ALPHABET_SIZE];

    private char[][] tiles;
    private Boolean[][] doors;
    private int x;
    private int y;

    /*
     * Constructor for the Board Class.
     */
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
        numberOfDoorsInEachRoom = new ArrayList<>();
    }

    /*
     * Initialises the 2D array of chars, tiles to empty space.
     */
    private void initBoard(){
        for (int j = 0; j < tiles.length; j++){
            Arrays.fill(tiles[j], EMPTY);
        }
    }

    /*
     * Intialises the boolean array, doors, that determines whether there
     * is a door at a given location - doors lie on top of walls
     */
    private void initDoors() {
        for (Boolean[] row: doors) {
            for (int j = 0; j < y; j++) {
                row[j] = false;
            }
        }
    }

    /*
     * Delegates to the respective method call, given an enum which
     * identifies what command the user has input.
     */
    public Board applyInstruction (Instruction i) {
        int[] args = i.getArguments();
        switch (i.getCommand()) {
            case ADD:
                return add(args[0], args[1], args [2], args[3]);
            case REMOVE:
                return remove(args[0], args[1], args [2], args[3]);
            case DOOR:
                return addDoor(args[0], args[1]);
        }
        return null;
    }

    /*
     * Adds a room at the given location, and of the given height and width.
     * The room is bounded by walls.
     */
    private Board add (int x, int y, int width, int height) {
        for (int i = x; i < x+width; i++) {
            for (int j = y; j < y+height; j++) {
                if (i == x || j == y || i == (x+width-1) || j == (y+height-1)){
                    tiles[i][j] = WALL;
                }
            }
        }
        update();
        return this;
    }

    /*
     * Removes the wall, and doors contained within the given area.
     */
    private Board remove(int x, int y, int width, int height) {
        for (int i = x; i < x+width; i++) {
            for (int j = y; j < y+height; j++) {
                tiles[i][j] = EMPTY;
                if (doors[i][j]) doors[i][j] = false;
            }
        }
        update();
        return this;
    }

    /*
     * Adds a door to the given location in the array,
     * as long as the location contains a wall.
     */
    private Board addDoor(int x, int y) {

        if (tiles[x][y] == WALL) {
            doors[x][y] = true;
            updateNumberOfDoors();
        }
        return this;
    }

    /*
     * Calls the relevant methods to update the board
     * after changes have been applied e.g. via an add.
     */
    private void update() {
        markAllButWallsAsTbd();
        removeInvalidTbdTiles();
        detectEnclosure();
        allocateLettersToGroups(groups);
        updateNumberOfDoors();
    }

    /*
     * Populates the array with characters that are valid room identifiers.
     */
    private void populateAlphabet() {
        for (int letter = ALPHABET_START_DEC; letter <= ALPHABET_END_DEC; letter++) {
            alphabet[letter-ALPHABET_START_DEC] = (char) letter;
        }
    }

    /*
     * Retrieves an unused letter to be used for identifying a room.
     */
    private char getLetter(){
        for (int i = 0; i < alphaBitmap.length; i++) {
            if (!alphaBitmap[i]) {
                alphaBitmap[i] = true;
                return alphabet[i];
            }
        }
        return '#';
    }

    /*
     * Frees all the used letters, for use after a change
     * of the board, so that the rooms can be reallocated letters.
     */
    private void freeLetters() {
        for (int i = 0; i < alphaBitmap.length; i++) {
            alphaBitmap[i] = false;
        }
        lettersInUse.clear();

        //D is an invalid room identifier.
        alphaBitmap[4] = true;
    }

    /*
     * After the board has been mutated, with addition
     * or removal of walls, this method will set all space
     * except for walls as a PLACEHOLDER.
     * After this, the rooms and empty space can be recognised,
     * and allocated letters accordingly.
     */
    private void markAllButWallsAsTbd() {
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if (tiles[i][j] != WALL) {
                    tiles[i][j] = PLACEHOLDER;
                }
            }
        }
    }


    /* Every space has been marked as PLACEHOLDER
     * 1. Anything that is an edge, mark EMPTY
     * 2. Anything that is next to EMPTY, make EMPTY
     *     2.1. Unless WALL
     * 3. Repeat steps 2 & 3 until no change
     */
    private void removeInvalidTbdTiles() {

        //Replace edge tiles with '_'
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if (isEdge(i, j) && (tiles[i][j] != WALL)) tiles[i][j] = EMPTY;
            }
        }

        //If first tile is connected to one of these newly created '_' tiles,
        //it must not be enclosed so replace those too.
        int change = -1;
        while (change != 0) {
            change = 0;
            for (int i = 0; i < x; i++) {
                for (int j = 0; j < y; j++) {
                    if (tiles[i][j] != WALL && tiles[i][j] != EMPTY) {
                        List<Character> adj = getAdjacentTilesChar(i, j, tiles);
                        boolean adjacentToEmpty = false;

                        for (Character c : adj) {
                            if (c.charValue() == EMPTY) {
                                adjacentToEmpty = true;
                            }
                        }

                        if (adjacentToEmpty) {
                            tiles[i][j] = EMPTY;
                            change++;
                        }
                    }
                }
            }
        }

    }

    /*
     * Detects distinct enclosed areas, groups them together,
     * and stores them in the field 'groups'.
     * From here, letters can be allocated, doors counted etc.
     */
    private void detectEnclosure() {

        //Make a new Replacement Node for each PLACEHOLDER
        ReplacementNode[][] nodes = new ReplacementNode[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if (tiles[i][j] == PLACEHOLDER){
                    nodes[i][j] = new ReplacementNode(i, j);
                }
            }
        }

        //Choose the larger dimension of the two - this is the number
        //of times we need to iterate.
        //For each of teh Replacement Nodes, merge their groups, giving
        //them information about each other, until each node in a given group
        //holds identical information to each other node in the same group.
        int k = (x > y) ? x : y;
        do {
            for (int i = 0; i < x; i++) {
                for (int j = 0; j < y; j++) {
                    if (nodes[i][j] == null) {
                        continue;
                    }

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

        //Set the object's number of groups field to the correct value
        numberOfRooms = groups.size();
    }

    /*
     * For each of the distinct groups, in 'groups',
     * assign a new letter, and add it to the array
     * 'lettersInUse' which corresponds respectively to
     * the letter used to denote a group in 'groups'.
     */
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

    /*
     * For each group, check the adjacent nodes of each member of the group,
     * if it is next to first door, increment the door count, record where the door was, and continue until
     * all have been checked.
    */
    private void updateNumberOfDoors() {

        numberOfDoorsInEachRoom.clear();
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

            numberOfDoorsInEachRoom.add(doorCount);
        }
    }

    /*
     * heuristic for edge tiles
     */
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

    /*
     * For a given co-ordinate, return a List of Pairs of the Coordinates
     * of its 8-way neighbours, and its value.
     */
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

    /*
     * For a given co-ordinate, return a List of Characters that are
     * its 8-way neighbours.
     * This had to be a separate function due to my design choice of using
     * primitive 'char' for the 2D array 'tiles'.
     */
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

    /*
     * Returns the number of distinct rooms in the world.
     */
    public int getNumberOfRooms() {return numberOfRooms;}

    /*
     * Returns the list of the number of doors in each room
     */
    public List<Integer> getNumberOfRoomsInEachGroup() {
        return numberOfDoorsInEachRoom;
    }

    /*
     * Returns a String consisting of the groups, their respective letter,
     * and the number of doors in that room.
     */
    public String getRoomsAndDoors() {
        StringBuilder builder = new StringBuilder();
        for (List<ReplacementNode> group: groups) {
            int index = groups.indexOf(group);
            char letter = lettersInUse.get(index);
            int numberOfDoors = numberOfDoorsInEachRoom.get(index);
            builder.append("Room " + letter + " has " + numberOfDoors + " door" + (numberOfDoors == 1 ? "." : "s."));
        }

        return builder.toString();
    }
}
