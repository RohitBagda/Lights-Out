import comp124graphics.Rectangle;

import java.awt.*;

/**
 * The Bulb class represents each Bulb on canvas.
 * Created by Katya Kelly, Chukwubueze Hosea Ogeleka, and Rohit Bagda on 11/25/2017.
 */
public class Bulb extends Rectangle {

    private boolean isOn;
    private boolean wasClicked;
    private int id;

    private final Color onColor = Color.getHSBColor(174, 62, 68);
    private final Color offColor = Color.DARK_GRAY;

    /**
     * Creates a Bulb of a specific size and color in the form of a rectangle at a specific position.
     * @param x
     * @param y
     * @param width
     * @param height
     * @param id
     */
    public Bulb(double x, double y, double width, double height, int id){
        super(x,y, width, height);
        this.id = id;

        this.setFilled(true);
        this.setFillColor(onColor);

        isOn = true;
    }

    /**
     * Toggle color of a specific bulb on the canvas.
     */
    public void toggle(){
        if(isOn) {
            isOn = false;
            this.setFillColor(offColor);
        } else {
            isOn = true;
            this.setFillColor(onColor);
        }
    }

    /**Getters**/
    public int getId() {
        return id;
    }

    protected boolean getIsOn(){
        return isOn;
    }

    protected boolean getWasClicked() { return wasClicked; }

    /**Setters**/
    protected void toggleWasClicked() { wasClicked = !wasClicked; }
}
