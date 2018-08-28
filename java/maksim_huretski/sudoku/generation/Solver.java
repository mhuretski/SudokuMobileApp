package maksim_huretski.sudoku.generation;


import maksim_huretski.sudoku.calculation.Calc;

import java.util.concurrent.TimeoutException;

public class Solver extends Calc {

    private final int LENGTH = 9;
    private int sudoku[][];
    private boolean[][] rows;
    private boolean[][] columns;
    private boolean[][] blocks;
    private long startTime;

    @Override
    public void init(int[][] sudoku, int[][] blockIDs) {
        this.sudoku = sudoku;
        rows = new boolean[LENGTH][LENGTH];
        columns = new boolean[LENGTH][LENGTH];
        blocks = new boolean[LENGTH][LENGTH];
        for (int i = 0; i < LENGTH; i++) {
            for (int j = 0; j < LENGTH; j++) {
                int value = sudoku[i][j];
                if (value != 0) {
                    setValue(i, j, value, true);
                }
            }
        }
    }

    @Override
    public boolean calculateSudoku() {
        startTime = System.nanoTime();
        try {
            return solve(0, 0);
        } catch (TimeoutException e) {
            return false;
        }
    }

    private void setValue(int i, int j, int value, boolean present) {
        rows[i][value - 1] = present;
        columns[j][value - 1] = present;
        blocks[getBlock(i, j)][value - 1] = present;
    }

    private int getBlock(int i, int j) {
        int block = 3;
        int blockColumn = j / block;
        int blockRow = i / block;
        return blockRow * block + blockColumn;
    }

    private boolean solve(int i, int j) throws TimeoutException {
        if (System.nanoTime() - startTime > 300000000)
            throw new TimeoutException();
        if (i == LENGTH) {
            i = 0;
            if (++j == LENGTH) {
                return true;
            }
        }
        if (sudoku[i][j] != 0) {
            return solve(i + 1, j);
        }
        for (int value = 1; value <= LENGTH; value++) {
            if (valueNotPresent(i, j, value)) {
                sudoku[i][j] = value;
                setValue(i, j, value, true);
                if (solve(i + 1, j)) {
                    return true;
                }
                setValue(i, j, value, false);
            }
        }

        sudoku[i][j] = 0;
        return false;
    }

    private boolean valueNotPresent(int i, int j, int possibleValue) {
        int position = possibleValue - 1;
        boolean isPresent = rows[i][position] || columns[j][position] || blocks[getBlock(i, j)][position];
        return !isPresent;
    }

    public int[][] getSudoku() {
        return sudoku;
    }

}
