package generators.searching;

import static java.lang.Math.abs;

public class Queen
{
    private int column;
    private int currentRow;

    public Queen(int column)
    {
        this(column, 0);
    }
    public Queen(int column, int row)
    {
        this.column = column;
        this.currentRow = row;
    }
    public void move(int targetRow)
    {
        currentRow = targetRow;
    }
    public int getColumn()
    {
        return this.column;
    }
    public int getCurrentRow()
    {
        return this.currentRow;
    }
    public boolean attacks(Queen queen2)
    {
        if (this.getCurrentRow() == queen2.getCurrentRow())
        {
            return true;
        }
        else if (abs(this.getCurrentRow() - queen2.getCurrentRow()) == abs(this.getColumn() - queen2.getColumn()))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
