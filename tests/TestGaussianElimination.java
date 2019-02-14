import org.junit.Test;
import static org.junit.Assert.*;

public class TestGaussianElimination {

    @Test
    public void testMakeAugmentedColumn(){
        GaussianElimination gaussianElimination = new GaussianElimination();
        double expectedColumnOfSize1[] = {1};
        double expectedColumnOfSize5[] = {1, 1, 1, 1, 1};
        double expectedColumnOfSize10[] = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        double expectedColumnOfSize15[] = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

        assertArrayEquals(expectedColumnOfSize1, gaussianElimination.makeAugmentedColumn(1), 0.000001);
        assertArrayEquals(expectedColumnOfSize5, gaussianElimination.makeAugmentedColumn(5), 0.000001);
        assertArrayEquals(expectedColumnOfSize10, gaussianElimination.makeAugmentedColumn(10), 0.000001);
        assertArrayEquals(expectedColumnOfSize15, gaussianElimination.makeAugmentedColumn(15), 0.000001);

    }

}
