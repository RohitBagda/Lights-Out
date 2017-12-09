import comp124graphics.Ellipse;

import java.awt.*;

/**
 * Created by Rohit Bagda on 11/26/2017.
 */
public class Button extends Ellipse {

    String name;

    public Button (double x, double y, double width, double height, String name){
       super(x, y, width, height);
       this.setFilled(true);
       this.setFillColor(Color.BLACK);
       this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
