import comp124graphics.GraphicsGroup;
import comp124graphics.GraphicsObject;

/**
 * The Board class simulates the actual game board by maintaining a 2D array of Bulbs which is used to keep track of the
 * virtual bulbs and their status.
 * Created by Katya Kelly, Chukwubueze Hosea Ogeleka, and Rohit Bagda on 11/25/2017.
 */
public class Board extends GraphicsGroup{

    private double width;
    private int n;
    private Bulb board[][];

    private final double bulbGap =10;

    /**
     * Creates a new Game Board Object of a specific Dimension
     * @param x
     * @param y
     * @param boardLength
     * @param dimension
     */
    public Board(double x, double y, double boardLength, int dimension){
        super(x,y);

        this.width=boardLength;
        this.n = dimension;
        board = new Bulb[n][n];
        createBoard();
    }

    /**
     * Create 2D Array of Bulb objects and add them to the canvas.
     */
    private void createBoard(){
        double bulbLength = calculateLengthOfEachBulb();
        int idCounter=0;
        double xPos;
        double yPos=0;
        for(int i = 0; i < n; i++){
            xPos = 0;
            for(int j = 0; j < n; j++){
                Bulb bulb = new Bulb(xPos, yPos, bulbLength, bulbLength, idCounter);
                board[i][j]=bulb;
                add(bulb);
                xPos+= bulbGap +bulbLength;
                idCounter++;
            }
            yPos+= bulbGap +bulbLength;
        }
    }

    /**
     * Calculate the length of Bulb based on the current Dimension.
     * @return
     */
    private double calculateLengthOfEachBulb(){
        return (width - ((n+1)* bulbGap))/n;
    }

    /**
     * Update the status of a Bulb and its neighbors.
     * @param bulb - the bulb that was clicked, and whose neighbors should be toggled
     */
    public void toggleBulb(Bulb bulb){
        bulb.toggle();
        int i = bulb.getId() / n;
        int j = bulb.getId() % n;

        //toggle left neighbor
        if (j != 0){
            board[i][j-1].toggle();
        }

        //toggle right neighbor
        if(j!=(n-1)){
            board[i][j+1].toggle();
        }

        //toggle top neighbor
        if(i!=0){
            board[i-1][j].toggle();
        }

        //toggle bottom neighbor
        if(i!=(n-1)){
            board[i+1][j].toggle();
        }
    }

    /**Getters**/
    public Bulb getBulbAt(int i, int j){
        return board[i][j];
    }

    public double getBulbGap() {
        return bulbGap;
    }
}
