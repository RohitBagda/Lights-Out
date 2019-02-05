import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Homework 3
 * Tests for Die class.
 *
 * Created by Julia Romare.
 */
public class TestBulb {
    Bulb bulb;

    @Before
    public void setup() {
        bulb = new Bulb(0,0,10,10,0);
    }

    @Test
    public void testConstructor(){
        Die d1 = new Die();
        Die d2 = new Die();
        Die d3 = new Die();

        d2.setIsSetAside(true);
        d3.setIsSetAside(false);

        assertEquals(false, d1.getIsSetAside());
        assertEquals(true, d2.getIsSetAside());
        assertEquals(false, d3.getIsSetAside());
    }

}
