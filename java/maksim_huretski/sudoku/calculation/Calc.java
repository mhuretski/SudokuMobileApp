package maksim_huretski.sudoku.calculation;

public interface Calc {

    int[][] getSudoku();

    void init(int[][] sudoku, int[][] blockIDs);

    boolean calculateSudoku();

}
