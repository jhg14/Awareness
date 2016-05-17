package tests;

import main.Command;
import main.Instruction;
import main.World;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by JHGWhite on 17/05/2016.
 */
public class WorldTests {

    private World world = new World(6, 6);

    @Test
    public void surroundingCharactersOfCentrePieceCorrectLengthAndContents () {
        List<Character> expectedCorner = new ArrayList<>();
        expectedCorner.addAll(Arrays.asList('_', '_', '_', '_', '_', '_', '_', '_'));

        List<Character> actual = world.getAdjacentTiles(1, 1);

        assertTrue(actual.equals(expectedCorner));

    }

    @Test
    public void surroundingCharactersOfEdgePieceCorrectLengthAndContents () {
        List<Character> expectedCorner = new ArrayList<>();
        expectedCorner.addAll(Arrays.asList('_', '_', '_'));

        List<Character> actual = world.getAdjacentTiles(0, 0);

        assertTrue(actual.equals(expectedCorner));

    }



}
