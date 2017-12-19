import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * Created by Katya Kelly, Chukwubueze Hosea Ogeleka, Rohit Bagda on 12/14/2017.
 * Runs the game Lights Out!
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

        //Create an object of the LightsOut class.
        LightsOut lightsOut = new LightsOut(canvasWidth,ceilingGap);
    }
}
