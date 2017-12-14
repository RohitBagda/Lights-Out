import comp124graphics.CanvasWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by Katya Kelly, Chukwubueze Hosea Ogeleka, and Rohit Bagda on 11/25/2017.
 */
public class LightsOut extends CanvasWindow implements MouseListener, MouseMotionListener, ActionListener, KeyListener{


    private Board gameBoard;
    private Timer timer;
    private JTextField textField;

    private int n;
    private double boardLength;

    private int solution[];
    private int canvasWidth;
    private int pauseTime;
    private int solutionVectCounter;
    private int mainBulbVectCounter;

    private final double EDGE_GAP = 10;
    private final double CEILING_GAP = 100;
    private final int DIMENSION_LIMIT=100;

    private boolean pauseTimerRunning;
    private boolean showingSolution;
    private int solutionIndicator;

    private JButton userChoiceButton;


    private LightsOut(int canvasWidth, int n){
        super("Lights Out!", canvasWidth, canvasWidth+100);
        Color backGroundColor = new Color(29,111,140);
        super.setBackground(backGroundColor);

        this.n=n;
        boardLength=canvasWidth;
        this.canvasWidth=canvasWidth;
        int buttonWidth = 200;
        int buttonHeight = 40;
        int buttonGap = 5;
        int topGap = 20;
        pauseTimerRunning = false;
        showingSolution=false;
        solutionIndicator=0;

        addAllButtons(buttonWidth, buttonHeight, topGap, (canvasWidth/2)-(buttonWidth/2)-buttonWidth - buttonGap, buttonGap);
        addTextField(buttonWidth, buttonHeight, topGap);
        addMouseListener(this);
        addKeyListener(this);
        buildGame(n, true);
    }

    private void buildGame(int dimensionEntered, boolean nChanged){

        int currentN=n;
        this.n=dimensionEntered;
        int[][] A = createMatrix();
        if(nChanged){
            GaussianElimination elimination = new GaussianElimination(A);
            solution = elimination.findSolution(A);
        } else if(n>=DIMENSION_LIMIT){
            this.n=currentN;
        }
        drawBoard();
        solutionVectCounter=0;
        mainBulbVectCounter=0;
        calculatePauseTime();
        setupJavaTimer();
    }

    private void drawBoard(){
        gameBoard = new Board(EDGE_GAP, CEILING_GAP, boardLength, n);
        add(gameBoard);
        showingSolution=false;
    }

    private void addTextField(int buttonWidth, int buttonHeight, int buttonGap){
        textField = new JTextField("5");
        textField.setSize(buttonWidth,buttonHeight);
        textField.setLocation(canvasWidth-buttonWidth-2*buttonGap-textField.getWidth(), buttonHeight);
        textField.setEditable(true);
        textField.setFont(new Font(null, Font.BOLD, 20));
        add(textField);
        textField.grabFocus();
        textField.setCaretPosition(textField.getText().length());
        textField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
            }
            public void focusLost(FocusEvent e) {
                textField.requestFocus();
            }
        });
        textField.addKeyListener(new KeyAdapter() {
                    public void keyReleased( KeyEvent e ) {

                        if( e.getKeyCode() == KeyEvent.VK_ENTER ) {
                            userChoiceButton.doClick();
                        }
                    }
                }
        );
    }

    private void addAllButtons(int buttonWidth, int buttonHeight, int topGap, int leftGap, int buttonGap){
        addResetButton(buttonWidth, buttonHeight, leftGap, topGap);
        addShowSolutionButton(buttonWidth, buttonHeight, leftGap+buttonWidth+buttonGap, topGap);
        addPlayButton(buttonWidth, buttonHeight, leftGap+buttonWidth*2+buttonGap + buttonGap, topGap);
        addUserChoiceButton(buttonWidth, buttonHeight, canvasWidth-buttonWidth-buttonGap, buttonHeight );
    }

    private void addUserChoiceButton(int width, int height, int x, int y){
        userChoiceButton = new JButton("Enter Size");
        userChoiceButton.setLocation(x, y);
        userChoiceButton.setSize(width, height);
        userChoiceButton.setText("Enter Size");
        userChoiceButton.setFont(new Font(null, Font.BOLD, 20));
        userChoiceButton.addActionListener(this);
        userChoiceButton.addKeyListener(this);
        add(userChoiceButton);
    }

    private void addResetButton(int buttonWidth, int buttonHeight, int x, int y){
        JButton reset = new JButton("Reset");
        reset.setLocation(x, y);
        reset.setSize(buttonWidth,buttonHeight);
        reset.setText("Reset");
        reset.setFont(new Font(null, Font.BOLD, 20));
        add(reset);
        reset.addActionListener(this);
    }

    private void addPlayButton(int buttonWidth, int buttonHeight, int x, int y){
        JButton play = new JButton("Visualize");
        play.setLocation(x,y);
        play.setSize(buttonWidth,buttonHeight);
        play.setText("Visualize");
        play.setFont(new Font(null, Font.BOLD, 20));
        play.addActionListener(this);
        add(play);
    }


    public void addShowSolutionButton(int buttonWidth, int buttonHeight, int x, int y){
        JButton showSolutionButton = new JButton("Show Solution");
        showSolutionButton.setLocation(x,y);
        showSolutionButton.setSize(buttonWidth,buttonHeight);
        showSolutionButton.setText("Show Solution");
        showSolutionButton.addActionListener(this);
        showSolutionButton.setFont(new Font(null, Font.BOLD, 20));
        add(showSolutionButton);
    }



    private int[][] createMatrix() {
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

    private void calculatePauseTime(){

        pauseTime=0;
        if(n<10){
            pauseTime = 200;
        } else if(n<20){
            pauseTime = 100;
        } else if(n<50){
            pauseTime = 50;
        } else if(n<100){
            pauseTime = 25;
        }
    }


    private void toggleBulbAndNeighbors(int row, int column, long pauseTime){

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

    private void showSolution(int[] vector){
        for (int i=0; i<vector.length;i++){
            int row, col;
            row = i / n;
            col = i % n;
            if (vector[i] == 1){
                gameBoard.getBulbAt(row,col).setStroked(true);
                gameBoard.getBulbAt(row,col).setStrokeWidth((float)(0.7 * gameBoard.BULB_GAP));
                gameBoard.getBulbAt(row, col).setStrokeColor(Color.RED);
            }
        }
    }

    private void checkBoardClicked(double x, double y){
        gameBoard.toggleBulb(x,y);
    }

    private void undoShowSolution(int[] vector){
        for (int i=0; i<vector.length;i++){
            if(vector[i]!=0){
                int row = i / n;
                int col = i % n;
                gameBoard.getBulbAt(row, col).setStroked(false);
            }
        }
    }

    private void setupJavaTimer() {
        timer = new Timer(pauseTime, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (pauseTimerRunning && solution[solutionVectCounter]==1&&solutionVectCounter<solution.length) {
                    if(solutionIndicator%3 == 0){
                        visualizeMainBulbColor(solution);
                    } else if(solutionIndicator%3 == 1){
                        update(solution);
                    } else{
                        solutionVectCounter++;
                    }
                    solutionIndicator++;

                } else{
                    solutionVectCounter++;
                    mainBulbVectCounter++;
                    if(solutionVectCounter>=solution.length){
                        mainBulbVectCounter=0;
                        solutionVectCounter=0;
                        timer.stop();
                    }
                }

            }
        });
    }


    private void update(int vector[]){
        int row;
        int column;
        int i = solutionVectCounter;

        if(i<solution.length){
            if (vector[i] == 1){
                row = i/n;
                column = i % n;
                toggleBulbAndNeighbors(row,column, pauseTime);
            }
        }

        if(solutionVectCounter>=solution.length){
            pauseTimerRunning=false;
            timer.stop();
            solutionVectCounter=0;
            solutionIndicator=0;
        }
    }

    private void visualizeMainBulbColor(int vector[]){
        int row;
        int column;
        int l=vector.length;
        int i = mainBulbVectCounter;
        if(i<solution.length){
            if (vector[i] == 1){
                row = i/n;
                column = i % n;
                gameBoard.getBulbAt(row,column).setFillColor(Color.RED);
                if(!showingSolution) {
                    gameBoard.getBulbAt(row, column).setStroked(false);
                }
            }
        }
        mainBulbVectCounter++;
        if(mainBulbVectCounter>solution.length){
            mainBulbVectCounter=0;
            solutionIndicator=0;
        }
    }

    private void rebuildWithNewSize(){
        remove(gameBoard);
        int dimensionEntered=Integer.parseInt(textField.getText());

        boolean nChanged=false;
        if(dimensionEntered!=n && dimensionEntered<DIMENSION_LIMIT && dimensionEntered>1){
            nChanged=true;
        }
        calculatePauseTime();
        buildGame(dimensionEntered, nChanged);
    }


    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("Reset")) {
            remove(gameBoard);
            drawBoard();
            showingSolution=false;
            timer.stop();
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
                undoShowSolution(solution);
                showingSolution=false;
            }
        }
        if(cmd.equals("Enter Size")){
            timer.stop();
            solutionVectCounter=0;
            mainBulbVectCounter=0;
            solutionIndicator=0;
            pauseTimerRunning=false;
            rebuildWithNewSize();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        checkBoardClicked(x,y);
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {

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

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public static void main(String args[]){
        int defaultDimension=5;
        LightsOut lightsOut = new LightsOut(1600, defaultDimension);
    }

}
