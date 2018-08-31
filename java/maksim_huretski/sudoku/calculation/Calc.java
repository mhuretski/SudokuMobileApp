package maksim_huretski.sudoku.calculation;

public abstract class Calc {

    public abstract int[][] getSudoku();

    @SuppressWarnings("unused")
    public abstract void init(int[][] sudoku, int[][] blockIDs);

    public abstract boolean calculateSudoku();

}
