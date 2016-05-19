package main;

/**
 * Created by JHGWhite on 19/05/2016.
 */

/*
 * Simple class to represent a co-ordinate in some 2D array
 */
public class Coord {

    final int x;
    final int y;

    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coord coord = (Coord) o;

        if (x != coord.x) return false;
        return y == coord.y;

    }
}
