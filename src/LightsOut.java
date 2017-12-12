import comp124graphics.CanvasWindow;
import comp124graphics.GraphicsGroup;
import comp124graphics.GraphicsObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by Rohit Bagda on 11/25/2017.
 */
public class LightsOut extends CanvasWindow implements MouseListener, MouseMotionListener, ActionListener {


    private Board gameBoard;
    private GaussianElimination elimination;
    private Color currentBulbColors[];
    private Timer timer;

    private int n;
    private double boardLength;

    private int solution[];
    private int canvasWidth;
    private int pauseTime;
    private int mainBulbToggleTime;
    private int solutionVectCounter;
    private int mainBulbVectCounter;

    private final double EDGE_GAP = 10;
    private final double CEILING_GAP = 75;

    private Button showSolutionButton;

    private boolean pauseTimerRunning;
    private boolean mainBulbPauseTimerRunning;
    private boolean showingSolution;
    private int solutionIndicator;


    public LightsOut(int canvasWidth, int canvasHeight, int n){
        super("Lights Out!", canvasWidth, canvasWidth+75);
        Color backGroundColor = new Color(29,111,140);

        super.setBackground(backGroundColor);
        this.n=n;
        boardLength=canvasWidth;
        this.canvasWidth=canvasWidth;

        drawBoard();
        int buttonWidth = 160;
        int buttonHeight = 40;
        int buttonGap = 5;
        int topGap = 20;
        pauseTimerRunning = false;
        mainBulbPauseTimerRunning=true;
        showingSolution=false;
        solutionIndicator=0;
        addAllButtons(buttonWidth, buttonHeight, topGap, (canvasWidth/2)-(buttonWidth/2)-buttonWidth - buttonGap, buttonGap);
        addMouseListener(this);


        int[][] A = createMatrix();
        elimination = new GaussianElimination(A);
        solution = elimination.findSolution(A);
        solutionVectCounter=0;
        mainBulbVectCounter=0;
        setupJavaTimer();
    }

    public void drawBoard(){
        gameBoard = new Board(EDGE_GAP, CEILING_GAP, boardLength, n);
        add(gameBoard);
        showingSolution=false;
    }

    public void addAllButtons(int buttonWidth, int buttonHeight, int topGap, int leftGap, int buttonGap){
        addResetButton(buttonWidth, buttonHeight, leftGap, topGap);
        addShowSolutionButton(buttonWidth, buttonHeight, leftGap+buttonWidth+buttonGap, topGap);
        addPlayButton(buttonWidth, buttonHeight, leftGap+buttonWidth*2+buttonGap + buttonGap, topGap);
    }

    public void addResetButton(int buttonWidth, int buttonHeight, int x, int y){
        Button reset = new Button("Reset");
        reset.setLocation(x, y);
        reset.setSize(buttonWidth,buttonHeight);
        reset.setText("Reset");
        add(reset);
        reset.addActionListener(this);
    }

    public void addPlayButton(int buttonWidth, int buttonHeight, int x, int y){
        Button play = new Button("Visualize");
        play.setLocation(x,y);
        play.setSize(buttonWidth,buttonHeight);
        play.setText("Visualize");
        add(play);
        play.addActionListener(this);
    }


    public void addShowSolutionButton(int buttonWidth, int buttonHeight, int x, int y){
        showSolutionButton = new Button("Show Solution");
        showSolutionButton.setLocation(x,y);
        showSolutionButton.setSize(buttonWidth,buttonHeight);
        showSolutionButton.setText("Show Solution");
        showSolutionButton.addActionListener(this);
        add(showSolutionButton);
    }



    public int[][] createMatrix() {
        int[][] matrix = new int[n*n][n*n];

        for (int i=0;i<n*n;i++) {
            for (int j=0;j<n*n;j++) {
                matrix[i][j] = 0;
            }
        }

        for (int i=0;i<n*n;i++) {
            int row = i/n;
            int col = i%n;
            int idLeft, idRight, idTop, idBottom;
            matrix[i][i] = 1;

            if (col!=0) {
                idLeft = row*n+col-1;
                matrix[i][idLeft] = 1;
            }

            if (col!=n-1) {
                idRight = row*n+col+1;
                matrix[i][idRight] = 1;
            }

            if (row!=0) {
                idTop = (row-1)*n+col;
                matrix[i][idTop] = 1;
            }

            if (row!=n-1) {
                idBottom = (row+1)*n+col;
                matrix[i][idBottom] = 1;
            }
        }

        return matrix;
    }

    private int calculatePauseTime(){

        pauseTime=0;
        if(n<10){
            pauseTime = 500;
        } else if(n<20){
            pauseTime = 200;
        } else if(n<50){
            pauseTime = 100;
        } else if(n<100){
            pauseTime = 50;
        }
        return pauseTime;
    }

//    public void carryOutSolution(int[] vector){
//        int row;
//        int column;
//        int l=vector.length;
//        long pauseTime=calculatePauseTime();
//        long mainBulbToggleTime=pauseTime/2;
//
//        for (int i=0;i<l;i++){
//
//            if (vector[i] == 1){
//                row = i/n;
//                column = i % n;
//
//                toggleNeighbors(row,column, mainBulbToggleTime, pauseTime);
//            }
//        }
//    }

    private void toggleBulbAndNeighbors(int row, int column, long mainBulbToggleTime, long pauseTime){

        gameBoard.getBulbAt(row, column).toggle();

        //toggle left neighbor
        if (column != 0){
            gameBoard.getBulbAt(row, column-1).toggle();
        }

        //toggle right neighbor
        if(column!=(n-1)){
            gameBoard.getBulbAt(row, column+1).toggle();
        }

        //toggle top neighbor
        if(row!=0){
            gameBoard.getBulbAt(row-1, column).toggle();
        }

        //toggle Bottom neighbor
        if(row!=n-1){
            gameBoard.getBulbAt(row+1, column).toggle();
        }
        pause(pauseTime);
    }
    public void showSolution(int[] vector){
        currentBulbColors=new Color[vector.length];
        for (int i=0; i<vector.length;i++){
            int row, col;
            row = i / n;
            col = i % n;
            currentBulbColors[i]=gameBoard.getBulbAt(row,col).getFillColor();
            if (vector[i] == 1){
                Color color = gameBoard.getBulbAt(row, col).getIsSolutionColor();
                gameBoard.getBulbAt(row,col).setStroked(true);
                gameBoard.getBulbAt(row,col).setStrokeWidth(7);

                gameBoard.getBulbAt(row, col).setStrokeColor(Color.RED);
            }
        }
    }

    public void checkBoardClicked(double x, double y){
        gameBoard.toggleBulb(x,y);
    }

    public void dontShowSolution(int[] vector){
        for (int i=0; i<vector.length;i++){
            if(vector[i]!=0){
                int row = i / n;
                int col = i % n;
//                gameBoard.getBulbAt(row, col).setFillColor(currentBulbColors[i]);
                gameBoard.getBulbAt(row, col).setStroked(false);
            }
        }
    }

    public void setupJavaTimer() {
        timer = new Timer(pauseTime, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                System.out.println("Timer Updated: " + solutionIndicator);
                if (pauseTimerRunning) {
                    if(solutionIndicator%3 == 0){
                       // System.out.println(solutionIndicator%3);
                        visualizeMainBulbColor(solution);
//                        solutionIndicator++;
                    } else if(solutionIndicator%3 == 1){
                       // System.out.println(solutionIndicator%3);
                        update(solution);
                        //System.out.println(solutionVectCounter);
//                        solutionIndicator++;
                    } else{
                        //System.out.println(solutionIndicator % 3);
//                        solutionIndicator++;
                        solutionVectCounter++;
                    }
                    solutionIndicator++;

                }

            }
        });
//        mainBulbTimer = new Timer(mainBulbToggleTime, new ActionListener() {
//            public void actionPerformed(ActionEvent evt) {
//                if (mainBulbPauseTimerRunning=true) {
//                    visualizeMainBulbColor(solution);
//                    System.out.println(mainBulbVectCounter);
//                }
//            }
//        });
    }


    public void update(int vector[]){
        int row;
        int column;
        int l=vector.length;
        int i = solutionVectCounter;
        int pauseTime=calculatePauseTime();


        if(i<solution.length){
            if (vector[i] == 1){
                row = i/n;
                column = i % n;
                System.out.println("changing main color");
                gameBoard.getBulbAt(row,column).setFillColor(Color.RED);

                gameBoard.getBulbAt(row,column).setFillColor(Color.YELLOW);
                toggleBulbAndNeighbors(row,column, mainBulbToggleTime, pauseTime);

            }
        }

//        solutionVectCounter++;
        if(solutionVectCounter>=solution.length){
            pauseTimerRunning=false;
            timer.stop();
            solutionVectCounter=0;
            solutionIndicator=0;
        }
    }

    public void visualizeMainBulbColor(int vector[]){
        int row;
        int column;
        int l=vector.length;
        int i = mainBulbVectCounter;
        int pauseTime=calculatePauseTime();
        mainBulbToggleTime=pauseTime/2;

        if(i<solution.length){
            if (vector[i] == 1){
                row = i/n;
                column = i % n;
                System.out.println("changing main color");
                gameBoard.getBulbAt(row,column).setFillColor(Color.RED);
                gameBoard.getBulbAt(row, column).setStroked(false);
            }
        }
        mainBulbVectCounter++;
        if(mainBulbVectCounter>solution.length){
            mainBulbVectCounter=0;
            solutionIndicator=0;
        }
    }



    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("Reset")) {
            remove(gameBoard);
            drawBoard();
            showingSolution=false;
        }
        if(cmd.equals("Visualize")){
            remove(gameBoard);
            drawBoard();
            showSolution(solution);
            solutionVectCounter=0;
            mainBulbVectCounter=0;
            solutionIndicator=0;
            pauseTimerRunning=true;
            timer.start();
        }
        if(cmd.equals("Show Solution")){
            if(!showingSolution){
                showSolution(solution);
                showingSolution=true;
            } else if(showingSolution){
                dontShowSolution(solution);
                showingSolution=false;
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        checkBoardClicked(x,y);
//        GraphicsObject obj = getElementAt(x, y);
//        performButtonOperation(obj);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Object obj = getComponentAt(e.getX(),e.getY());
        if(obj instanceof Button && ((Button) obj).getText().equals("Show Solution")){
            showSolution(solution);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Object obj = getComponentAt(e.getX(),e.getY());
        if(!(obj instanceof Button && ((Button) obj).getName().equals("show solution"))){
            dontShowSolution(solution);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    public static void main(String args[]){
        int dimension;
//        Scanner scan = new Scanner(System.in);
//        System.out.println("Enter Dimension of Board");
//        dimension = scan.nextInt();
//        for(int i=31;i<50;i++){
        dimension=20;
        LightsOut lightsOut = new LightsOut(1500, 2000, dimension);
//        }

    }

}
