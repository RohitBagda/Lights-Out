import comp124graphics.GraphicsGroup;
import comp124graphics.GraphicsObject;
import comp124graphics.GraphicsText;

/**
 * Created by Rohit Bagda on 11/25/2017.
 */
public class Board extends GraphicsGroup{

    private double width;
    private double height;

    private double bulbLength;
    private int n;
    private Bulb board[][];

    private double BULB_GAP;


    public Board(double x, double y, double boardLength, int dimension){
        super(x,y);

        this.width=boardLength;
        this.height=boardLength;
        this.n = dimension;
        board = new Bulb[n][n];
        BULB_GAP = 10;
        bulbLength = (width - ((n+1)*BULB_GAP))/n;
//        BULB_GAP = width/(100*n);
        createBoard();
    }
    public void createBoard(){
        int idCounter=0;
        double xPos;
        double yPos=0;
        for(int i=0;i<n;i++){
            xPos = 0;
            for(int j=0;j<n;j++){
                Bulb bulb = new Bulb(xPos, yPos, bulbLength, bulbLength, idCounter);
                board[i][j]=bulb;

                GraphicsText label = new GraphicsText((""+(i*n + j)), (float)(xPos + bulbLength/2 - 5), (float)(yPos + bulbLength/2));
                //label.setPosition(xPos+bulbLength/2 - label.getWidth()/2, (float)(yPos + bulbLength/2) + label.getHeight()/4);
                add(bulb);
                //add(label);
                xPos+=BULB_GAP+bulbLength;
                idCounter++;
            }
            yPos+=BULB_GAP+bulbLength;
        }
    }

    public void toggleBulb(double x, double y){
        GraphicsObject obj = getElementAt(x,y);
        if(obj instanceof Bulb ){
            Bulb thisBulb = (Bulb)obj;
            thisBulb.toggle();
            int i = thisBulb.getId() / n;
            int j = thisBulb.getId() % n;
            System.out.println("Bulb toggled: " + thisBulb.getId());
            System.out.println(board[i][j].toString());

            //toggle left neighbor
            if (j != 0){
                board[i][j-1].toggle();
                System.out.println("Left neighbour " + board[i][j-1].getId());
                System.out.println("Left neighbour status: " +board[i][j-1].onOff());
            }

            //toggle right neighbor
            if(j!=(n-1)){
                board[i][j+1].toggle();
                System.out.println("Right neighbour: " + board[i][j+1].getId());
                System.out.println("Right neighbour status: " +board[i][j+1].onOff());
            }

            //toggle top neighbor
            if(i!=0){
                board[i-1][j].toggle();
                System.out.println("Top neighbour: " + board[i-1][j].getId());
                System.out.println("Top neighbour status: " +board[i-1][j].onOff());
            }

            //toggle bottom neighbor
            if(i!=(n-1)){
                board[i+1][j].toggle();
                System.out.println("Bottom neighbour: " + board[i+1][j].getId());
                System.out.println("Bottom neighbour status: " +board[i+1][j].onOff());
            }

//        } else if(obj instanceof GraphicsText){
//            Bulb thisBulb = (Bulb)getElementAt(((GraphicsText) obj).getX(), ((GraphicsText) obj).getY());
//            thisBulb.toggle();
//            System.out.println("Bulb toggled: " + thisBulb.getId());

        } else {
            System.out.println("No bulb toggled");
        }


    }

    public Bulb getBulbAt(int i, int j){
        return board[i][j];
    }

}
