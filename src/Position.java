/**
 * The Position class is used to as a helper class to store the attributes of the pivot and non pivot positions of the
 * row echelon form.
 * Created by Katya Kelly, Chukwubueze Hosea Ogeleka, Rohit Bagda on 12/4/2017.
 */
public class Position {

    private int row;
    private int column;
    private boolean isPivot;

    /**
     * Creates a Position object and initializes its instance variables.
     * @param row
     * @param column
     * @param isPivot
     */
    public Position (int row, int column, boolean isPivot){
        this.row = row;
        this.column = column;
        this.isPivot = isPivot;
    }

    /**
     * Checks if the Position is a Pivot or not.
     * @return
     */
    public boolean isPivot() {
        return isPivot;
    }

    /**Getters**/
    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}