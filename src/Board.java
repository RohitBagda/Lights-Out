import comp124graphics.GraphicsGroup;
import comp124graphics.GraphicsObject;

/**
 * Created by Rohit Bagda on 11/25/2017.
 */
public class Board extends GraphicsGroup{

    private double width;
    private double height;

    private double bulbLength;
    private int n;
    private Bulb board[][];

    private final double bulbGap =10;


    public Board(double x, double y, double boardLength, int dimension){
        super(x,y);

        this.width=boardLength;
        this.height=boardLength;
        this.n = dimension;
        board = new Bulb[n][n];
        bulbLength = (width - ((n+1)* bulbGap))/n;
        createBoard();
    }
    private void createBoard(){
        int idCounter=0;
        double xPos;
        double yPos=0;
        for(int i=0;i<n;i++){
            xPos = 0;
            for(int j=0;j<n;j++){
                Bulb bulb = new Bulb(xPos, yPos, bulbLength, bulbLength, idCounter);
                board[i][j]=bulb;
                add(bulb);
                xPos+= bulbGap +bulbLength;
                idCounter++;
            }
            yPos+= bulbGap +bulbLength;
        }
    }

    public void toggleBulb(double x, double y){
        GraphicsObject obj = getElementAt(x,y);
        if(obj instanceof Bulb ){
            Bulb thisBulb = (Bulb)obj;
            thisBulb.toggle();
            int i = thisBulb.getId() / n;
            int j = thisBulb.getId() % n;
//            System.out.println("Bulb toggled: " + thisBulb.getId());
//            System.out.println(board[i][j].toString());

            //toggle left neighbor
            if (j != 0){
                board[i][j-1].toggle();
//                System.out.println("Left neighbour " + board[i][j-1].getId());
//                System.out.println("Left neighbour status: " +board[i][j-1].onOff());
            }

            //toggle right neighbor
            if(j!=(n-1)){
                board[i][j+1].toggle();
//                System.out.println("Right neighbour: " + board[i][j+1].getId());
//                System.out.println("Right neighbour status: " +board[i][j+1].onOff());
            }

            //toggle top neighbor
            if(i!=0){
                board[i-1][j].toggle();
//                System.out.println("Top neighbour: " + board[i-1][j].getId());
//                System.out.println("Top neighbour status: " +board[i-1][j].onOff());
            }

            //toggle bottom neighbor
            if(i!=(n-1)){
                board[i+1][j].toggle();
//                System.out.println("Bottom neighbour: " + board[i+1][j].getId());
//                System.out.println("Bottom neighbour status: " +board[i+1][j].onOff());
            }

        } else {
//            System.out.println("No bulb toggled");
        }
    }

    public Bulb getBulbAt(int i, int j){
        return board[i][j];
    }

    public double getBulbGap() {
        return bulbGap;
    }
}
