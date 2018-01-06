import comp124graphics.CanvasWindow;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.Hashtable;

/**
 * Created by Katya Kelly, Chukwubueze Hosea Ogeleka, and Rohit Bagda on 11/25/2017.
 * The LightsOut class uses the comp124 graphics Library to create a CanvasWindow and builds the Lights Out Puzzle
 * using other helper classes.
 */
public class LightsOut extends CanvasWindow implements MouseListener, ActionListener, KeyListener {


    private Board gameBoard;
    private Timer timer;
    private JFormattedTextField textField;
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
    private int canvasHeight;
    private int buttonWidth;
    private int buttonHeight;
    private int buttonGap;
    private int leftGap;
    private int topGap;
    private int currentSliderValue;
    private boolean pauseTimerRunning;
    private boolean showingSolution;

    private final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    private final int SCREEN_WIDTH=(int)(SCREEN_SIZE.getWidth());
    private final double EDGE_GAP = SCREEN_WIDTH/(SCREEN_WIDTH/10);
    private final int DIMENSION_LIMIT=100;
    private final int DEFAULT_DIMENSION=5;
    private final int FONT_SIZE=SCREEN_WIDTH/128;
    private final String FONT_NAME="Tahoma";
    private final Font BUTTON_FONT = new Font(FONT_NAME, Font.PLAIN, FONT_SIZE);

    /**
     * Creates a Canvas for the Lights Out Puzzle and call methods to perform required operations.
     * @param canvasWidth
     * @param canvasHeight Gap between top edge of canvas and Bulbs.
     */
    public LightsOut(int canvasWidth, int canvasHeight){
        super("Lights Out!", canvasWidth, canvasHeight);

        this.n=DEFAULT_DIMENSION;
        this.canvasWidth=canvasWidth;
        this.canvasHeight =canvasHeight;

        setCanvasBackGround();
        calculateNecessaryComponents();
        addAllComponents();
        buildGame(n, true);
    }

    /**
     * Set Background Color of the Canvas.
     */
    private void setCanvasBackGround(){
        Color backGroundColor = new Color(29,111,140);
        super.setBackground(backGroundColor);
    }

    /**
     * Initialize instance variables with their corresponding values.
     */
    private void calculateNecessaryComponents(){
        ceilingGap=canvasHeight-canvasWidth;
        boardLength = canvasWidth;
        buttonWidth = (int)(SCREEN_WIDTH/12.8);
        buttonHeight = SCREEN_WIDTH/72;
        buttonGap = SCREEN_WIDTH/384;
        topGap = SCREEN_WIDTH/256;
        pauseTimerRunning = false;
        showingSolution = false;
        solutionIndicator = 0;
        leftGap = (canvasWidth/2)-(buttonWidth/2)-buttonWidth - buttonGap;
    }

    /**
     * Call Methods to add GUI components and Listeners.
     */
    private void addAllComponents(){
        addTextField();
        addAllButtons();
        addAnimationSpeedSlider();
        addMouseListener(this);
    }

    /**
     * Create a Special Number Formatter for Dimension Text Field Input.
     * @return a New Number Formatter which only excepts Integer inputs for Dimension.
     */
    private NumberFormatter createNumberFormatForTextField(){
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(false);
        final int characterLimit = 2;
        SpecialNumberFormatter numberFormatter = new SpecialNumberFormatter(numberFormat, characterLimit);
        numberFormatter.setValueClass(Integer.class);
        numberFormatter.setMinimum(2);
        numberFormatter.setMaximum(DIMENSION_LIMIT);
        numberFormatter.setAllowsInvalid(false);
        numberFormatter.setCommitsOnValidEdit(true);
        return numberFormatter;
    }

    /**
     * Set text field characteristics.
     * @param textField
     */
    private void formatTextField(JFormattedTextField textField){
        textField.setText(Integer.toString(DEFAULT_DIMENSION));
        textField.requestFocusInWindow();
        textField.setSize(buttonWidth,buttonHeight);
        textField.setLocation(leftGap+buttonWidth+buttonGap, (int)(1.5*buttonHeight));
        textField.setEditable(true);
        textField.setFont(BUTTON_FONT);
        textField.setHorizontalAlignment(JFormattedTextField.CENTER);
        textField.setCaretPosition(textField.getText().length());
        textField.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
    }

    /**
     * Add text field and its listeners to canvas.
     */
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

    /**
     * Call methods to create and add all Buttons to the Canvas.
     */
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

    /**
     * Create, format and, add "Enter Size" button.
     * @param x X Coordinate
     * @param y Y Coordinate
     */
    private void addUserChoiceButton(int x, int y){
        userChoiceButton = new JButton("Enter Size");
        userChoiceButton.setLocation(x, y);
        userChoiceButton.setSize(buttonWidth, buttonHeight);
        userChoiceButton.setText("Enter Size");
        userChoiceButton.setFont(BUTTON_FONT);
        userChoiceButton.addActionListener(this);
        userChoiceButton.addKeyListener(this);
        userChoiceButton.setPreferredSize(new Dimension(buttonWidth,buttonHeight));
        add(userChoiceButton);
    }

    /**
     * Create, format and, add "Reset" button.
     * @param x X Coordinate
     * @param y Y Coordinate
     */
    private void addResetButton(int x, int y){
        JButton reset = new JButton("Reset");
        reset.setLocation(x, y);
        reset.setSize(buttonWidth,buttonHeight);
        reset.setText("Reset");
        reset.setFont(BUTTON_FONT);
        reset.addActionListener(this);
        reset.setPreferredSize(new Dimension(buttonWidth,buttonHeight));
        add(reset);
    }

    /**
     * Create, format and, add "Visualize" button.
     * @param x X Coordinate
     * @param y Y Coordinate
     */
    private void addVisualizeButton(int x, int y){
        JButton visualize = new JButton("Visualize");
        visualize.setLocation(x,y);
        visualize.setSize(buttonWidth,buttonHeight);
        visualize.setText("Visualize");
        visualize.setFont(BUTTON_FONT);
        visualize.addActionListener(this);
        visualize.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        add(visualize);
    }

    /**
     * Create, format and, add "Pause" button.
     * @param x X Coordinate
     * @param y Y Coordinate
     */
    private void addPauseButton(int x, int y){
        pause = new JButton("Pause");
        pause.setLocation(x,y);
        pause.setSize(buttonWidth/2,buttonHeight);
        pause.setText("Pause");
        pause.setFont(BUTTON_FONT);
        pause.addActionListener(this);
        pause.setEnabled(false);
        pause.setPreferredSize(new Dimension(buttonWidth/2, buttonHeight));
        add(pause);
    }

    /**
     * Create, format and, add "Play" button.
     * @param x X Coordinate
     * @param y Y Coordinate
     */
    private void addPlayButton(int x, int y){
        play = new JButton("Play");
        play.setLocation(x,y);
        play.setSize(buttonWidth/2-buttonGap/2,buttonHeight);
        play.setText("Play");
        play.setFont(BUTTON_FONT);
        play.addActionListener(this);
        play.setEnabled(false);
        play.setPreferredSize(new Dimension(buttonWidth/2-buttonGap/2, buttonHeight));
        add(play);
    }

    /**
     * Create, format and, add "Show Solution" button.
     * @param x X Coordinate
     * @param y Y Coordinate
     */
    private void addShowSolutionButton(int x, int y){
        JButton showSolutionButton = new JButton("Show Solution");
        showSolutionButton.setLocation(x,y);
        showSolutionButton.setSize(buttonWidth-buttonGap/2,buttonHeight);
        showSolutionButton.setText("Show Solution");
        showSolutionButton.addActionListener(this);
        showSolutionButton.setFont(BUTTON_FONT);
        showSolutionButton.setPreferredSize(new Dimension(buttonWidth-buttonGap/2,buttonHeight));
        add(showSolutionButton);
    }

    /**
     * Create, format and, add "Visualization Speed" Slider.
     */
    private void addAnimationSpeedSlider(){

        JLabel animationSpeedLabel = new JLabel("Visualization Speed");
        animationSpeedLabel.setFont(BUTTON_FONT);
        JSlider animationSpeedSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        Hashtable<Integer, JLabel> table = new Hashtable<Integer, JLabel>();
        table.put (50, animationSpeedLabel);
        animationSpeedSlider.setLabelTable(table);

        animationSpeedSlider.setPaintTrack(true);
        animationSpeedSlider.setPaintLabels(true);
        animationSpeedSlider.setPaintTicks(false);
        animationSpeedSlider.setBackground(super.getBackground());
        animationSpeedSlider.setSize(3*buttonWidth+2*buttonGap, buttonHeight);
        animationSpeedSlider.setVisible(true);
        animationSpeedSlider.setLocation(leftGap, 2*buttonHeight+ buttonHeight/2 +3*buttonGap);
        animationSpeedSlider.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int notches = e.getWheelRotation();
                if (notches<0) {
                    animationSpeedSlider.setValue(animationSpeedSlider.getValue() + 1);
                } else if(notches>0){
                    animationSpeedSlider.setValue(animationSpeedSlider.getValue() - 1);
                }
            }
        });
        animationSpeedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider slider = (JSlider)e.getSource();
                currentSliderValue = slider.getValue();
                calculatePauseTime();
                timer.setDelay(pauseTime);
            }
        });
        animationSpeedLabel.addKeyListener(new KeyAdapter() {
            public void keyReleased( KeyEvent e ) {
                if( e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_DOWN) {
                    animationSpeedSlider.setValue(animationSpeedSlider.getValue() - 1);
                } else if( e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_UP){
                    animationSpeedSlider.setValue(animationSpeedSlider.getValue() + 1);
                }
            }
        });
        add(animationSpeedSlider);
    }

    /**
     * Builds a new game each time a new dimension entered by the player.
     * @param dimensionEntered Size entered by user
     * @param nChanged Denotes whether the user enters a new dimension or not.
     */
    private void buildGame(int dimensionEntered, boolean nChanged){
        int currentN=n;
        this.n=dimensionEntered;
        int[][] A = createMatrix();
        if(nChanged){
            GaussianElimination elimination = new GaussianElimination();
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

    /**
     * Sets up initial matrix for Gaussian Elimination.
     * @return
     */
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

    /**
     * Draws a new Board on the canvas of size n.
     */
    private void drawBoard(){
        gameBoard = new Board(EDGE_GAP, ceilingGap, boardLength, n);
        add(gameBoard);
        showingSolution=false;
        pause.setEnabled(false);
    }

    /**
     * Calculate the Visualization pause time for the Java Timer.
     */
    private void calculatePauseTime(){
        pauseTime = 400 - 4*(currentSliderValue);
    }

    /**
     * Indicates the solution to Lights Out on the canvas
     * @param vector Solution to Lights out
     */
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

    /**
     * Stops Showing the solution on the canvas.
     * @param vector Solution to Lights out
     */
    private void undoShowSolution(int[] vector){
        for (int i=0; i<vector.length;i++){
            if(vector[i]!=0){
                int row = i / n;
                int col = i % n;
                gameBoard.getBulbAt(row, col).setStroked(false);
            }
        }
    }

    /**
     * A Java timer to help visualize the solution. The timer repeats after a particular pause time and performs three
     * operations alternately to show the current bulb being toggled, followed by it's neighbors and then proceed to the
     * next Bulb in the solution vector. When the solution is completely visualized, it resets related components it had
     * used to perform the visualization.
     * There are three counters used to carry out this process smoothly.
     *      1) solutionVectorCounter - It is used to keep track of the index position of the Solution Vector.
     *      2) mainBulbVectorCounter - It is used to keep track of the Bulbs which need to be highlighted red while
     *                                 the visualization is being carried out.
     *      3) solutionIndicator - It is used to denote which of the three operations is to be performed.
     */
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
                    pauseTimerRunning=false;
                }
            }
        });
    }

    /**
     * Updates status of the bulb toggled and its neighbors.
     * @param vector
     */
    private void update(int vector[]){
        int row;
        int column;
        int i = solutionVectorCounter;

        if(i<solution.length){
            if (vector[i] == 1){
                row = i/n;
                column = i % n;
                toggleBulbAndNeighbors(row,column);
            }
        }
    }

    /**
     * Toggles Bulb and its neighbors
     * @param row
     * @param column
     */
    private void toggleBulbAndNeighbors(int row, int column){

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
    }

    /**
     * Visualizes the Bulb being toggled by highlighting it to red.
     * @param vector
     */
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

    /**
     * Reset Solution Vector Counter back to 0.
     */
    private void resetSolutionVectorCounter(){
        solutionVectorCounter =0;
    }

    /**
     * Reset Main Bulb Vector Counter back to 0.
     */
    private void resetMainBulbVectorCounter(){
        mainBulbVectorCounter =0;
    }

    /**
     * Reset Solution Indicator back to 0.
     */
    private void resetSolutionIndicator(){
        solutionIndicator=0;
    }

    /**
     * Call a method in the Board class to toggle a Bulb and its neighbors on the game board.
     * @param x
     * @param y
     */
    private void performBulbToggleOperations(double x, double y){
        gameBoard.toggleBulb(x,y);
    }

    /**
     * Call respective methods based on user's selection of button.
     * @param e
     */
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

    /**
     * Reset the Canvas.
     */
    private void performReset(){
        remove(gameBoard);
        drawBoard();
        showingSolution=false;
        timer.stop();
        pause.setEnabled(false);
    }

    /**
     * Visualize Solution
     */
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

    /**
     * Show Solution
     */
    private void performShowSolution(){
        if(!showingSolution){
            showSolution(solution);
            showingSolution=true;
        } else {
            undoShowSolution(solution);
            showingSolution=false;
        }
    }

    /**
     * Accept New Size from User
     */
    private void performNewSize(){
        timer.stop();
        resetSolutionVectorCounter();
        resetMainBulbVectorCounter();
        resetSolutionIndicator();
        pauseTimerRunning=false;
        rebuildWithNewSize();
        pause.setEnabled(false);
    }

    /**
     * Pause the Visualization
     */
    private void performPause(){
        if (pauseTimerRunning){
            timer.stop();
            pause.setEnabled(false);
            play.setEnabled(true);
            pauseTimerRunning = false;
        }
    }

    /**
     * Resume Visualization
     */
    private void performPlay(){
        if(!pauseTimerRunning) {
            play.setEnabled(false);
            pause.setEnabled(true);
            timer.start();
            pauseTimerRunning = true;
        }
    }

    /**
     * Rebuild puzzle with a new dimension.
     */
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

    /**Mouse & Key Listeners**/
    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if(!pauseTimerRunning){
            performBulbToggleOperations(x,y);
        }
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
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}