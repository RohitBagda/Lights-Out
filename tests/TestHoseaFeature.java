import javafx.scene.effect.Light;
import org.junit.Test;
import org.junit.Assert;


public class TestHoseaFeature {
    LightsOut lightsOut = new LightsOut(900, 900);

    @Test
    public void testThatGameInitiallyTogglesBulbAndNeighbours(){
        Assert.assertFalse(lightsOut.shouldToggleOnlyNeighbours);
    }

    @Test
    public void testSwitchGameMode(){
        boolean previousMode = lightsOut.shouldToggleOnlyNeighbours;
        lightsOut.switchMode();
        boolean currentMode = lightsOut.shouldToggleOnlyNeighbours;
        Assert.assertFalse(previousMode==currentMode);
    }

    @Test
    public void testPerfomToggleAtPosition() {
        int row = 1;
        int column = 1;
        //set mode to only toggle neighbours
        lightsOut.setShouldToggleOnlyNeighbours(true);
        lightsOut.toggleBulbAndNeighbors(row, column);
//By default all bulbs are on when the game initialised
//bulb at row 1, column 1 should still remain on.
        Assert.assertTrue(lightsOut.gameBoard.getBulbAt(row, column).getIsOn());
//now we switch the mode. Bulb at row 1 column 1should now be off after we toggle
        lightsOut.switchMode();
        lightsOut.toggleBulbAndNeighbors(row, column);
        Assert.assertFalse(lightsOut.gameBoard.getBulbAt(row, column).getIsOn());
    }




}
