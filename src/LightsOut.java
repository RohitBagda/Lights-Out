import comp124graphics.CanvasWindow;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;

/**
 * Created by Katya Kelly, Chukwubueze Hosea Ogeleka, and Rohit Bagda on 11/25/2017.
 * The LightsOut class uses the comp124 graphics Library to create a CanvasWindow and build the Lights Out Puzzle
 * using other helper classes.
 */
public class LightsOut extends CanvasWindow implements MouseListener, MouseMotionListener, ActionListener, KeyListener{


    private Board gameBoard;
    private Timer timer;
    private JFormattedTextField textField;
    private Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    private JButton userChoiceButton;
    private JButton pause;
    private JButton play;

    private int n;
    private double boardLength;
    private double ceilingGap;
    private int solution[];
    private int pauseTime;
    private int solutionVectorCounter;
    private int mainBulbVectorCounter;
    private int solutionIndicator;
    private int canvasWidth;
    private int buttonWidth;
    private int buttonHeight;
    private int buttonGap;
    private int leftGap;
    private int topGap;
    private int currentSliderValue;
    private boolean pauseTimerRunning;
    private boolean showingSolution;

    private final int SCREEN_WIDTH=(int)(SCREEN_SIZE.getWidth());
    private final double EDGE_GAP = SCREEN_WIDTH/(SCREEN_WIDTH/10);
    private final int DIMENSION_LIMIT=100;
    private final int DEFAULT_DIMENSION=5;
    private final int FONT_SIZE=SCREEN_WIDTH/128;
    private final int CHARACTER_LIMIT = 2;


    public LightsOut(int canvasWidth, int ceilingGap){
        super("Lights Out!", canvasWidth, canvasWidth+ceilingGap);
        Color backGroundColor = new Color(29,111,140);
        super.setBackground(backGroundColor);

        this.n=DEFAULT_DIMENSION;
        this.canvasWidth=canvasWidth;
        this.ceilingGap =ceilingGap;

        calculateNecessaryComponents();
        addAllComponents();
        buildGame(n, true);
    }

    private void calculateNecessaryComponents(){
        boardLength=canvasWidth;
        buttonWidth = (int)(SCREEN_WIDTH/12.8);
        buttonHeight = SCREEN_WIDTH/72;
        buttonGap = SCREEN_WIDTH/384;
        topGap = SCREEN_WIDTH/256;
        pauseTimerRunning = false;
        showingSolution=false;
        solutionIndicator=0;
        leftGap=(canvasWidth/2)-(buttonWidth/2)-buttonWidth - buttonGap;
    }

    private void addAllComponents(){
        addAllButtons();
        addTextField();
        addAnimationSpeedSlider();
        addMouseListener(this);
        addKeyListener(this);
    }

    private NumberFormatter createNumberFormatForTextField(){
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(false);
        SpecialNumberFormatter numberFormatter = new SpecialNumberFormatter(numberFormat, CHARACTER_LIMIT);
        numberFormatter.setValueClass(Integer.class);
        numberFormatter.setMinimum(2);
        numberFormatter.setMaximum(DIMENSION_LIMIT);
        numberFormatter.setAllowsInvalid(false);
        numberFormatter.setCommitsOnValidEdit(true);
        return numberFormatter;
    }

    private void formatTextField(JFormattedTextField textField){
        textField.setDocument(new LengthRestrictedDocument(CHARACTER_LIMIT));
        textField.setSize(buttonWidth,buttonHeight);
        textField.setLocation(leftGap+buttonWidth+buttonGap, (int)(1.5*buttonHeight));
        textField.setEditable(true);
        textField.setFont(new Font(null, Font.BOLD, FONT_SIZE));
        textField.grabFocus();
        textField.setHorizontalAlignment(JFormattedTextField.CENTER);
        textField.setText(Integer.toString(DEFAULT_DIMENSION));
        textField.setCaretPosition(textField.getText().length());
        textField.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
    }
    private void addTextField(){
        NumberFormatter numberFormatter = createNumberFormatForTextField();

        textField = new JFormattedTextField(numberFormatter);
        formatTextField(textField);
        add(textField);
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

    private void addAllButtons(){
        int firstRowButtonHeight=topGap;
        int secondRowButtonHeight=(int)(1.5*buttonHeight);
        addResetButton(leftGap, firstRowButtonHeight);
        addShowSolutionButton(leftGap+buttonWidth+buttonGap, firstRowButtonHeight);
        addVisualizeButton(leftGap+buttonWidth*2+buttonGap + buttonGap, firstRowButtonHeight);
        addUserChoiceButton(leftGap, secondRowButtonHeight);
        addPauseButton(leftGap+buttonWidth*2+buttonGap + buttonGap, secondRowButtonHeight);
        addPlayButton(leftGap+(buttonWidth)*2+buttonWidth/2 + 2*buttonGap + buttonGap/2, secondRowButtonHeight);
    }

    private void addUserChoiceButton(int x, int y){
        userChoiceButton = new JButton("Enter Size");
        userChoiceButton.setLocation(x, y);
        userChoiceButton.setSize(buttonWidth, buttonHeight);
        userChoiceButton.setText("Enter Size");
        userChoiceButton.setFont(new Font(null, Font.BOLD, FONT_SIZE));
        userChoiceButton.addActionListener(this);
        userChoiceButton.addKeyListener(this);
        userChoiceButton.setPreferredSize(new Dimension(buttonWidth,buttonHeight));
        add(userChoiceButton);
    }

    private void addResetButton(int x, int y){
        JButton reset = new JButton("Reset");
        reset.setLocation(x, y);
        reset.setSize(buttonWidth,buttonHeight);
        reset.setText("Reset");
        reset.setFont(new Font(null, Font.BOLD, FONT_SIZE));
        reset.addActionListener(this);
        reset.setPreferredSize(new Dimension(buttonWidth,buttonHeight));
        add(reset);
    }

    private void addVisualizeButton(int x, int y){
        JButton visualize = new JButton("Visualize");
        visualize.setLocation(x,y);
        visualize.setSize(buttonWidth,buttonHeight);
        visualize.setText("Visualize");
        visualize.setFont(new Font(null, Font.BOLD, FONT_SIZE));
        visualize.addActionListener(this);
        visualize.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        add(visualize);
    }

    private void addPauseButton(int x, int y){
        pause = new JButton("Pause");
        pause.setLocation(x,y);
        pause.setSize(buttonWidth/2,buttonHeight);
        pause.setText("Pause");
        pause.setFont(new Font(null, Font.BOLD, FONT_SIZE));
        pause.addActionListener(this);
        pause.setEnabled(false);
        pause.setPreferredSize(new Dimension(buttonWidth/2, buttonHeight));
        add(pause);
    }

    private void addPlayButton(int x, int y){
        play = new JButton("Play");
        play.setLocation(x,y);
        play.setSize(buttonWidth/2-buttonGap/2,buttonHeight);
        play.setText("Play");
        play.setFont(new Font(null, Font.BOLD, FONT_SIZE));
        play.addActionListener(this);
        play.setEnabled(false);
        play.setPreferredSize(new Dimension(buttonWidth/2-buttonGap/2, buttonHeight));
        add(play);
    }


    private void addShowSolutionButton(int x, int y){
        JButton showSolutionButton = new JButton("Show Solution");
        showSolutionButton.setLocation(x,y);
        showSolutionButton.setSize(buttonWidth-buttonGap/2,buttonHeight);
        showSolutionButton.setText("Show Solution");
        showSolutionButton.addActionListener(this);
        showSolutionButton.setFont(new Font(null, Font.BOLD, FONT_SIZE));
        showSolutionButton.setPreferredSize(new Dimension(buttonWidth-buttonGap/2,buttonHeight));
        add(showSolutionButton);
    }

    private void addAnimationSpeedSlider(){
        JSlider animationSpeedSlider = new JSlider(JSlider.HORIZONTAL, 1, 100, 50);
        animationSpeedSlider.setSize(3*buttonWidth+2*buttonGap, buttonHeight);
        animationSpeedSlider.setVisible(true);
        animationSpeedSlider.setLocation(leftGap, 3*buttonHeight+2*buttonGap);
        animationSpeedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider slider = (JSlider)e.getSource();
                currentSliderValue = slider.getValue();
                calculatePauseTime();
                timer.setDelay(pauseTime);
            }
        });
        add(animationSpeedSlider);
    }

    private void buildGame(int dimensionEntered, boolean nChanged){
        int currentN=n;
        this.n=dimensionEntered;
        int[][] A = createMatrix();
        if(nChanged){
            GaussianElimination elimination = new GaussianElimination(A);
            solution = elimination.findSolution(A);
        } else if(n>=DIMENSION_LIMIT|| n<2){
            this.n=currentN;
        }
        drawBoard();
        pause.setEnabled(false);
        play.setEnabled(false);
        resetSolutionVectorCounter();
        resetMainBulbVectorCounter();
        calculatePauseTime();
        setupJavaTimer();
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

    private void drawBoard(){
        gameBoard = new Board(EDGE_GAP, ceilingGap, boardLength, n);
        add(gameBoard);
        showingSolution=false;
        pause.setEnabled(false);
    }

    private void calculatePauseTime(){
        pauseTime = 400 - 4*(currentSliderValue);
    }

    private void showSolution(int[] vector){
        for (int i=0; i<vector.length;i++){
            int row, col;
            row = i / n;
            col = i % n;
            if (vector[i] == 1){
                gameBoard.getBulbAt(row,col).setStroked(true);
                gameBoard.getBulbAt(row,col).setStrokeWidth((float)(0.7 * gameBoard.getBulbGap()));
                gameBoard.getBulbAt(row, col).setStrokeColor(Color.RED);
            }
        }
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
                if (pauseTimerRunning && solution[solutionVectorCounter]==1&& solutionVectorCounter <solution.length) {
                    if(solutionIndicator == 0){
                        visualizeMainBulbColor(solution);
                    } else if(solutionIndicator == 1){
                        update(solution);
                    } else{
                        solutionVectorCounter++;
                    }
                    solutionIndicator++;
                    if(solutionIndicator>=3){
                        resetSolutionIndicator();
                    }

                } else {
                    solutionVectorCounter++;
                    mainBulbVectorCounter++;
                }
                if(solutionVectorCounter >=solution.length){
                    resetSolutionVectorCounter();
                    resetMainBulbVectorCounter();
                    play.setEnabled(false);
                    pause.setEnabled(false);
                    pause.setText("Pause");
                    timer.stop();
                }
            }
        });
    }

    protected void update(int vector[]){
        int row;
        int column;
        int i = solutionVectorCounter;

        if(i<solution.length){
            if (vector[i] == 1){
                row = i/n;
                column = i % n;
                toggleBulbAndNeighbors(row,column, pauseTime);
            }
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

    private void visualizeMainBulbColor(int vector[]){
        int row;
        int column;
        int i = mainBulbVectorCounter;
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
        mainBulbVectorCounter++;
        if(mainBulbVectorCounter >solution.length){
            resetSolutionVectorCounter();
            resetMainBulbVectorCounter();
        }
    }

    private void resetSolutionVectorCounter(){
        solutionVectorCounter =0;
    }

    private void resetMainBulbVectorCounter(){
        mainBulbVectorCounter =0;
    }
    private void resetSolutionIndicator(){
        solutionIndicator=0;
    }

    private void checkBoardClicked(double x, double y){
        gameBoard.toggleBulb(x,y);
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("Reset")) {
            performReset();
        } else if(cmd.equals("Visualize")){
            performVisualize();
        } else if(cmd.equals("Show Solution")){
            performShowSolution();
        } else if(cmd.equals("Enter Size")){
            performNewSize();
        } else if (cmd.equals("Pause")){
            performPause();
        } else if(cmd.equals("Play")){
            performPlay();
        }
    }

    private void performReset(){
        remove(gameBoard);
        drawBoard();
        showingSolution=false;
        timer.stop();
        pause.setEnabled(false);
    }

    private void performVisualize(){
        remove(gameBoard);
        drawBoard();
        showSolution(solution);
        resetSolutionVectorCounter();
        resetMainBulbVectorCounter();
        resetSolutionIndicator();
        pauseTimerRunning=true;
        timer.start();
        pause.setEnabled(true);
    }

    private void performShowSolution(){
        if(!showingSolution){
            showSolution(solution);
            showingSolution=true;
        } else {
            undoShowSolution(solution);
            showingSolution=false;
        }
    }

    private void performNewSize(){
        timer.stop();
        resetSolutionVectorCounter();
        resetMainBulbVectorCounter();
        resetSolutionIndicator();
        pauseTimerRunning=false;
        rebuildWithNewSize();
        pause.setEnabled(false);
    }

    private void performPause(){
        if (pauseTimerRunning){
            timer.stop();
            pause.setEnabled(false);
            play.setEnabled(true);
            pauseTimerRunning = false;
        }
    }

    private void performPlay(){
        if(!pauseTimerRunning) {
            play.setEnabled(false);
            pause.setEnabled(true);
            timer.start();
            pauseTimerRunning = true;
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
}