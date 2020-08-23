package maksim_huretski.sudoku.calculation;

public interface Calc {

    int[][] getSudoku();

    @SuppressWarnings("unused")
    void init(int[][] sudoku, int[][] blockIDs);

    boolean calculateSudoku();

}
