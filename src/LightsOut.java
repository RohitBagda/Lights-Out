import comp124graphics.CanvasWindow;
import comp124graphics.GraphicsObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

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
    private boolean gameRunning;
    private int solution[];
    private int canvasWidth;
    private int pauseTime;

    private final double EDGE_GAP = 10;
    private final double CEILING_GAP = 75;

    private JButton showSolution;



    public LightsOut(int canvasWidth, int canvasHeight, int n){
        super("Lights Out!", canvasWidth, canvasWidth+75);
        Color backGroundColor = new Color(29,111,140);

        super.setBackground(backGroundColor);
        this.n=n;
        boardLength=canvasWidth;
        gameRunning=false;
        this.canvasWidth=canvasWidth;

        drawBoard();
        int buttonWidth = 100;
        int buttonHeight = 40;
        int buttonGap = 5;
        int topGap = 20;
        addAllButtons(buttonWidth, buttonHeight, topGap, (canvasWidth/2)-(buttonWidth/2)-buttonWidth, buttonGap);
        addMouseListener(this);


        int[][] A = createMatrix();
        elimination = new GaussianElimination(A);
        solution = elimination.findSolution(A);
        //carryOutSolution(solution);
    }

    public void drawBoard(){
        gameBoard = new Board(EDGE_GAP, CEILING_GAP, boardLength, n);
        add(gameBoard);
    }

    public void addAllButtons(int buttonWidth, int buttonHeight, int topGap, int leftGap, int buttonGap){
        addResetButton(buttonWidth, buttonHeight, leftGap, topGap);
        addShowSolutionButton(buttonWidth, buttonHeight, leftGap+buttonWidth+buttonGap, topGap);
        addPlayButton(buttonWidth, buttonHeight, leftGap+buttonWidth*2+buttonGap + buttonGap, topGap);
    }

    public void addResetButton(int buttonWidth, int buttonHeight, int x, int y){
        JButton reset = new JButton("Reset");
        reset.setLocation(x, y);
        reset.setSize(buttonWidth,buttonHeight);
        add(reset);
        reset.addActionListener(this);
    }

    public void addPlayButton(int buttonWidth, int buttonHeight, int x, int y){
        JButton play = new JButton("Visualize");
        play.setLocation(x,y);
        play.setSize(buttonWidth,buttonHeight);
        add(play);
        play.addActionListener(this);
    }


    public void addShowSolutionButton(int buttonWidth, int buttonHeight, int x, int y){
        showSolution = new JButton("Show Solution");
        showSolution.setLocation(x,y);
        showSolution.setSize(buttonWidth,buttonHeight);
        add(showSolution);
        showSolution.addActionListener(this);
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

    public void setupJavaTimer() {
        timer = new Timer(pauseTime, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                timer.stop();
            }
        });
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

    public void carryOutSolution(int[] vector){
        int row;
        int column;
        int l=vector.length;
        long pauseTime=calculatePauseTime();
        long mainBulbToggleTime=pauseTime/2;

        for (int i=0;i<l;i++){

            if (vector[i] == 1){
                row = i/n;
                column = i % n;

                toggleNeighbors(row,column, mainBulbToggleTime, pauseTime);
            }
        }
    }

    private void toggleNeighbors(int row, int column, long mainBulbToggleTime, long pauseTime){

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


    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("Reset")) {
            remove(gameBoard);
            drawBoard();
        }
        if(cmd.equals("Visualize")){
            carryOutSolution(solution);
        }
        if(cmd.equals("Show Solution")){
            showSolution(solution);
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
//        Object obj = getComponentAt(e.getX(),e.getY());
//        if(obj instanceof JButton && ((JButton) obj).getText().equals("Show Solution")){
//            showSolution(solution);
//        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
//        Object obj = getComponentAt(e.getX(),e.getY());
//        if(!(obj instanceof JButton && ((JButton) obj).getName().equals("show solution"))){
//            dontShowSolution(solution);
//        }
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
