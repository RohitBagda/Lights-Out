
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * TestLightsOut class. This class contains some unit tests for some of the methods in LightsOut.java
 */
public class TestLightsOut {

    @Test
    public void testCreateMatrixForFourBulbs(){
        int sampleN = 2;
        LightsOut lightsout = new LightsOut(900, 900);
        lightsout.buildGame(sampleN, false);
        int[][] expectedResult = {
                {1, 1, 1, 0},
                {1, 1, 0, 1},
                {1, 0, 1, 1},
                {0, 1, 1, 1}
        };
        int[][] actualResult = lightsout.createMatrix();
        assertArrayEquals(expectedResult, actualResult);
    }

    @Test
    public void testCreateMatrixForNineBulbs() {
        int sampleN = 3;
        LightsOut lightsOut = new LightsOut(900, 900);
        lightsOut.buildGame(sampleN, false);
        int[][] expectedResult = {
                {1, 1, 0, 1, 0, 0, 0, 0, 0},
                {1, 1, 1, 0, 1, 0, 0, 0, 0},
                {0, 1, 1, 0, 0, 1, 0, 0, 0},
                {1, 0, 0, 1, 1, 0, 1, 0, 0},
                {0, 1, 0, 1, 1, 1, 0, 1, 0},
                {0, 0, 1, 0, 1, 1, 0, 0, 1},
                {0, 0, 0, 1, 0, 0, 1, 1, 0},
                {0, 0, 0, 0, 1, 0, 1, 1, 1},
                {0, 0, 0, 0, 0, 1, 0, 1, 1}
        };
        int[][] actualResult = lightsOut.createMatrix();
        assertArrayEquals(expectedResult, actualResult);
    }

    @Test
    public void testToggleBulbAndNeighboursForTwoByTwoBoard() {
        int sampleN = 2;
        LightsOut lightsout = new LightsOut(900, 900);
        lightsout.buildGame(sampleN, false);

        //check that top-left bulb (just like all other bulbs) is on when the game starts
        assertTrue(lightsout.gameBoard.getBulbAt(0, 0).getIsOn());

        //toggle top-left bulb
        lightsout.toggleBulbAndNeighbors(0,0);

        //check that the state of all the other bulbs changes (or not) accordingly
        assertFalse(lightsout.gameBoard.getBulbAt(0, 0).getIsOn());
        assertFalse(lightsout.gameBoard.getBulbAt(0, 1).getIsOn());
        assertFalse(lightsout.gameBoard.getBulbAt(1,0).getIsOn());
        assertTrue(lightsout.gameBoard.getBulbAt(1, 1).getIsOn());
    }

    @Test
    public void testToggleBulbAndNeighboursForFiveByFiveBoard(){
        int sampleN = 5;
        LightsOut lightsout = new LightsOut(900, 900);
        lightsout.buildGame(sampleN, false);

        //check that bottom-right bulb (just like all other bulbs) is on when the game starts
        assertTrue(lightsout.gameBoard.getBulbAt(4, 4).getIsOn());

        //toggle bottom-right bulb
        lightsout.toggleBulbAndNeighbors(4, 4);

        //check for the state of the bottom right bulb and its neighbours
        assertFalse(lightsout.gameBoard.getBulbAt(4, 4).getIsOn());
        assertFalse(lightsout.gameBoard.getBulbAt(3, 4).getIsOn());
        assertFalse(lightsout.gameBoard.getBulbAt(4, 3).getIsOn());
    }
}