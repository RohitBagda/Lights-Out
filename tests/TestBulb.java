import org.junit.Test;
import static org.junit.Assert.*;

public class TestBulb {
    Bulb bulb;

    @Test
    public void testToggle(){
        bulb = new Bulb(0,0,10,10,0);
        boolean untoggled = bulb.getIsOn();
        bulb.toggle();
        boolean toggled = bulb.getIsOn();
        assertNotEquals(untoggled, toggled);
    }

}
