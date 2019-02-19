import java.util.ArrayList;

/**
 * The Gaussian Elimination reduces a Binary Matrix of dimension n, to row echelon form and then finds the solution to
 * the Lights Out Puzzle.
 * Created by Katya Kelly, Chukwubueze Hosea Ogeleka, and Rohit Bagda on 12/8/2017.
 */

public class GaussianElimination {

    /**
     * Constructor
     */
    public GaussianElimination(){
    }

    /**
     * Generate Augmented Column of size l comprising of all 1's representing the state of each Bulb being switched on.
     * @param l size of Augmented Column
     * @return a vector of size l comprising of all 1's
     */
    protected double[] makeAugmentedColumn(int l){
        double augmentedColumn[] = new double[l];

        for(int i = 0; i < l; i++){
            augmentedColumn[i]=1;
        }

        return augmentedColumn;
    }

    /**
     * Generate Augmented Matrix by combining the original matrix with augmented vector.
     * @param matrix original matrix
     * @return augmented matrix
     */
    private double[][] makeAugmentedMatrix(int matrix[][]) {

        int numRows = matrix.length;
        double b[] = makeAugmentedColumn(numRows);

        int l = b.length;
        double augmentedMatrix[][] = new double[l][l+1];

        for (int i = 0; i < l; i++) {
            for (int j = 0; j < l; j++) {
                augmentedMatrix[i][j] = (double)(matrix[i][j]);
            }
        }

        for (int i = 0; i < l; i++) {
            augmentedMatrix[i][l] = b[i];
        }

        return augmentedMatrix;
    }

    /**
     * Convert Binary Matrix to Row Echelon Form
     * @param matrix Original Matrix
     * @return Binary matrix in Row Echelon Form
     */
    private int[][] convertToRowEchelonForm(int matrix[][]){

        double augmentedMatrix[][] = makeAugmentedMatrix(matrix);

        int pivotRow;
        int n = augmentedMatrix.length;

        for (int i = 0; i < n - 1; i++) {
            pivotRow = i;

            for (int j = i + 1; j < n; j++) {
                if (Math.abs(augmentedMatrix[j][i]) > Math.abs(augmentedMatrix[pivotRow][i])) {
                    pivotRow = j;
                }
            }

            for (int k = i; k < n + 1; k++) {
                double t = augmentedMatrix[i][k];
                augmentedMatrix[i][k] = augmentedMatrix[pivotRow][k];
                augmentedMatrix[pivotRow][k] = t;
            }

            for (int j = i + 1; j < n; j++) {
                double temp = augmentedMatrix[j][i] / augmentedMatrix[i][i];
                for (int k = i; k < n + 1; k++) {
                    augmentedMatrix[j][k] = Math.abs((augmentedMatrix[j][k] - augmentedMatrix[i][k] * temp)) % 2;
                }
            }
        }

        int rowEchelonMatrix[][] = new int[n][n+1];
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n+1; j++){
               rowEchelonMatrix[i][j] = (int)augmentedMatrix[i][j];
            }
        }
        return rowEchelonMatrix;
    }

    /**
     * Carry Out Backward Substitution on Row Echelon Matrix to find solution to the Lights Out Puzzle.
     * @param matrix Binary Matrix
     * @return A solution vector indicating the Bulbs that need to be toggled to solve Lights Out.
     */
    public int[] findSolution(int[][] matrix){

        int[][] rowEchelonForm = convertToRowEchelonForm(matrix);
        int l = matrix.length;

        Position results[] = new Position[l];
        ArrayList<Integer> listPivotColumns= new ArrayList<>();
        int solution[] = new int[l];

        for (int i = 0; i < l; i++) {
            boolean pFound = false;
            for (int j = i; j < l; j++) {
                if (matrix[i][j] == 1) {
                    pFound = true;
                    results[i] = new Position(i, j, pFound);
                    break;
                }
            }
            if (!pFound) {
                results[i] = new Position(i, -1, pFound);
            }
        }

        for (Position pos : results) {
            if (pos.isPivot()) {
                int col = pos.getColumn();
                listPivotColumns.add(col);
            }
        }

        for (int i = 0; i < l; i++) {
            if (!listPivotColumns.contains(i)) {
                solution[i] = 0;
                for (int j = 0; j < i; j++) {
                    rowEchelonForm[j][i] = 0;
                }
            }
        }

        int p = results.length - 1;

        while (p >= 0) {
            if (results[p].isPivot()) {
                int row = results[p].getRow();
                int col = results[p].getColumn();
                double aug = rowEchelonForm[row][l];
                double sum = 0;
                for (int j = col + 1; j < l; j++) {
                    sum += rowEchelonForm[row][j];

                }
                int xi = (int) Math.abs((aug - sum) % 2);
                for (int i = 0; i < row; i++) {
                    rowEchelonForm[i][col] = rowEchelonForm[i][col] * xi;
                }
                solution[col] = xi;

            }
            p--;
        }
        return solution;
    }
}