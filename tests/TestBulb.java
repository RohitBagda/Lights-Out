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

    @Test
    public void testGetId(){
        Bulb bulb1 = new Bulb(0, 0,10,10,0); //Two bulbs Can currently have the same id.
        assertEquals(bulb1.getId(),0);
        bulb1 = new Bulb(0, 0,10,10,1);
        assertEquals(bulb1.getId(),1);
    }

}
