import comp124graphics.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 * Created by Rohit Bagda on 11/25/2017.
 */
public class LightsOut extends CanvasWindow implements MouseListener, MouseMotionListener {


    private Board gameBoard;
    private GaussianElimination elimination;
    private Color currentBulbColors[];
    private Timer timer;

    private int n;
    private double boardLength;
    private boolean gameRunning;
    private int solution[];


    private final double EDGE_GAP = 10;
    private final double CEILING_GAP = 75;



    public LightsOut(int canvasWidth, int canvasHeight, int n){
        super("Lights Out!", canvasWidth, canvasWidth+75);
        Color backGroundColor = new Color(29,111,140);

        super.setBackground(backGroundColor);
        this.n=n;
        boardLength=canvasWidth;
        gameRunning=false;

        drawBoard();
        addPlayButton();
        addResetButton();
        addShowSolution();
        addMouseListener(this);

        int[][] A = createMatrix();
        elimination = new GaussianElimination(A);
        solution = elimination.findSolution(A);
        carryOutSolution(solution);
    }

    public void drawBoard(){
        gameBoard = new Board(EDGE_GAP, CEILING_GAP, boardLength, n);
        add(gameBoard);
    }

    public void addResetButton(){
        Button button = new Button(0, 0, 40, 40, "reset");
        add(button);
    }

    public void addPlayButton(){
        Button button = new Button(80, 0, 40, 40, "play");
        button.setFillColor(Color.BLUE);
        add(button);
    }

    public void addShowSolution(){
        Button button = new Button(180, 0, 40, 40, "show solution");
        button.setFillColor(Color.CYAN);
        add(button);
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

        int pauseTime=0;
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

    public void carryOutSolution(int[] vector){
        int row;
        int column;
        int l=vector.length;
        int pauseTime=calculatePauseTime();
        int mainBulbToggleTime=pauseTime/2;

        for (int i=0;i<l;i++){

            if (vector[i] == 1){
                row = i/n;
                column = i % n;

                toggleNeighbors(row,column, mainBulbToggleTime, pauseTime);
            }
        }
    }

    private void toggleNeighbors(int row, int column, int mainBulbToggleTime, int pauseTime){

        gameBoard.getBulbAt(row,column).setFillColor(Color.RED);
        pause(mainBulbToggleTime);
        gameBoard.getBulbAt(row,column).setFillColor(Color.YELLOW);
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
                gameBoard.getBulbAt(row, col).setFillColor(color);
            }
        }
    }

    public void checkBoardClicked(double x, double y){
        gameBoard.toggleBulb(x,y);
    }

    public void dontShowSolution(int[] vector){
        for (int i=0; i<vector.length;i++){
            int row = i / n;
            int col = i % n;
            gameBoard.getBulbAt(row, col).setFillColor(currentBulbColors[i]);
        }
    }

    public void performButtonOperation(GraphicsObject obj){
        if (obj instanceof Button && ((Button)obj).getName().equals("reset")) {
            remove(gameBoard);
            drawBoard();
        }
        if (obj instanceof Button && ((Button)obj).getName().equals("play")){
            carryOutSolution(solution);
        }
        System.out.println("Bulb Toggled");
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        checkBoardClicked(x,y);
        GraphicsObject obj = getElementAt(x, y);
        performButtonOperation(obj);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        GraphicsObject obj = getElementAt(e.getX(),e.getY());
        if(obj instanceof Button && ((Button) obj).getName().equals("show solution")){
            showSolution(solution);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        GraphicsObject obj = getElementAt(e.getX(),e.getY());
        if(obj instanceof Button && ((Button) obj).getName().equals("show solution")){
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
        dimension=5;
        LightsOut lightsOut = new LightsOut(1500, 2000, dimension);
//        }

    }
}
