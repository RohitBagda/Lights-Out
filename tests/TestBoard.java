import org.junit.Test;
import static org.junit.Assert.*;

public class TestBoard {
    Board board;

    @Test
    public void testBulbIds() {
        board = new Board(1, 2, 3, 4);

        assertEquals(0, board.getBulbAt(0,0).getId());
        assertEquals(1, board.getBulbAt(0,1).getId());
        assertEquals(2, board.getBulbAt(0,2).getId());
        assertEquals(3, board.getBulbAt(0,3).getId());

        assertEquals(4, board.getBulbAt(1,0).getId());
        assertEquals(5, board.getBulbAt(1,1).getId());
        assertEquals(6, board.getBulbAt(1,2).getId());
        assertEquals(7, board.getBulbAt(1,3).getId());

        assertEquals(8, board.getBulbAt(2,0).getId());
        assertEquals(9, board.getBulbAt(2,1).getId());
        assertEquals(10, board.getBulbAt(2,2).getId());
        assertEquals(11, board.getBulbAt(2,3).getId());

        assertEquals(12, board.getBulbAt(3,0).getId());
        assertEquals(13, board.getBulbAt(3,1).getId());
        assertEquals(14, board.getBulbAt(3,2).getId());
        assertEquals(15, board.getBulbAt(3,3).getId());
    }

    @Test
    public void testToggleBulb() {
        board = new Board(1, 2, 3, 4);

        Bulb bulb = board.getBulbAt(0,0);
        board.toggleBulb(bulb);

        boolean expectedState1[][] = {
                {false, false, true, true},
                {false, true, true, true},
                {true, true, true, true},
                {true, true, true, true}

        };

        for(int i=0; i<4; i++) {
            for(int j=0; j<4; j++) {
                assertEquals(expectedState1[i][j], board.getBulbAt(i,j).getIsOn());
            }
        }

        bulb = board.getBulbAt(1, 1);
        board.toggleBulb(bulb);

        boolean expectedState2[][] = {
                {false, true, true, true},
                {true, false, false, true},
                {true, false, true, true},
                {true, true, true, true}

        };

        for(int i=0; i<4; i++) {
            for(int j=0; j<4; j++) {
                assertEquals(expectedState2[i][j], board.getBulbAt(i,j).getIsOn());
            }
        }
    }

    @Test
    public void testUndoAllMoves() {
        board = new Board(1, 2, 3, 4);

    }
}
