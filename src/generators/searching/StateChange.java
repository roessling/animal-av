package generators.searching;

public class StateChange {
    public int energy;
    public int queenToChange;
    public int moveQueenToRow;
    public StateChange(int energy, int queenToChange, int moveQueenToRow)
    {
        this.energy = energy;
        this.queenToChange = queenToChange;
        this.moveQueenToRow = moveQueenToRow;
    }
}
