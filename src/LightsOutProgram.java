import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * Runs the game Lights Out!
 *
 * Created by Katya Kelly, Chukwubueze Hosea Ogeleka, Rohit Bagda on 12/14/2017.
 * Acknowledgement:
 * We would like to express our gratitude to Professor Andrew Beveridge for his guidance, support and inspiration behind
 * this project.
 * This project uses comp124graphics Library created by Professor Bret Jackson at Macalester College to help build the
 * game components to visualize the puzzle.
 */

public class LightsOutProgram {
    /**
     * The method creates an object of LightsOut to run the puzzle.
     * @param args
     */
    public static void main(String args[]){

        //Get Screen Resolution and calculate canvas size.
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double screenWidth=screenSize.getWidth();
        int canvasWidth=(int)(screenWidth/2.4);
        int ceilingGap=(int)(screenWidth/(19.2*0.8));
        int canvasHeight=canvasWidth+ceilingGap;


        //Create an object of the LightsOut class.
        LightsOut lightsOut = new LightsOut(canvasWidth,canvasHeight);
    }
}
