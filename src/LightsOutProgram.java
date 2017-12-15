import java.awt.*;

/**
 * Created by Katya Kelly, Chukwubueze Hosea Ogeleka, Rohit Bagda on 12/14/2017.
 */
public class LightsOutProgram {
    public static void main(String args[]){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double screenWidth=screenSize.getWidth();
        int canvasWidth=(int)(screenWidth/2.4);
        int ceilingGap=(int)(screenWidth/(19.2*0.8));
        LightsOut lightsOut = new LightsOut(canvasWidth,ceilingGap);
    }
}
