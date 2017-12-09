/**
 * Created by Rohit Bagda on 12/4/2017.
 */
public class Position {

    private int row, column;
    private boolean isPivot;

    public Position (int row, int column, boolean isPivot){
        this.row = row;
        this.column = column;
        this.isPivot = isPivot;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    @Override
    public String toString() {
        return "Position{" +
                "\nrow=" + row +
                "\n, column=" + column +
                "\n, isPivot=" + isPivot +
                '}';
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public boolean isPivot() {
        return isPivot;
    }

    public void setPivot(boolean pivot) {
        isPivot = pivot;
    }

}
