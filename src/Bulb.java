import comp124graphics.*;
import comp124graphics.Rectangle;

import java.awt.*;

/**
 * Created by Rohit Bagda on 11/25/2017.
 */
public class Bulb extends Rectangle {

    private boolean isOn;
    private Color onColor, offColor, isSolutionColor;
    private int id;
    private double width, height;
    private double XOnCanvas, YOnCanvas;

    public Bulb(double x, double y, double width, double height, int id){
        super(x,y, width, height);
        this.id = id;

        offColor = Color.DARK_GRAY;
        onColor = Color.getHSBColor(174, 62, 68);

        isSolutionColor = new Color(29,111,169);

        this.XOnCanvas=x;
        this.YOnCanvas=y;
        this.setFilled(true);
        this.setFillColor(onColor);

        isOn = true;


    }
    public void toggle(){
        if(isOn) {
            isOn = false;
            this.setFillColor(offColor);
        } else {
            isOn = true;
            this.setFillColor(onColor);
        }
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    public Color getOnColor() {
        return onColor;
    }

    public void setOnColor(Color onColor) {
        this.onColor = onColor;
    }

    public Color getOffColor() {
        return offColor;
    }

    public void setOffColor(Color offColor) {
        this.offColor = offColor;
    }

    public Color getIsSolutionColor(){
        return isSolutionColor;
    }

    public void setIsSolutionColor(Color isSolutionColor){
        this.isSolutionColor = isSolutionColor;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    @Override
    public String toString(){
        String onOff;
        if (isOn){
            onOff = "On";
        } else{
            onOff = "Off";
        }

        return "Current Bulb toggled: " + getId() + "; Bulb status: " + onOff;
    }

    public String onOff(){
        String onOff;
        if (isOn){
            onOff = "On";
        } else{
            onOff = "Off";
        }

        return onOff;
    }
}
