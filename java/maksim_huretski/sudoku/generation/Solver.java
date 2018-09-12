package maksim_huretski.sudoku.generation;

import maksim_huretski.sudoku.calculation.Calc;

import java.util.concurrent.*;

public class Solver implements Calc {

    private final int LENGTH = 9;
    private final int sudoku[][] = new int[9][9];
    private boolean[][] rows;
    private boolean[][] columns;
    private boolean[][] blocks;

    public void init(int[][] sudoku, int[][] blockIDs) {
        for (int i = 0; i < 9; i++) {
            this.sudoku[i] = sudoku[i].clone();
        }
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

    public boolean calculateSudoku() {
        FutureTask<Boolean> calculation = new FutureTask<>(new Callable<Boolean>() {
            @Override
            public Boolean call() {
                return solve(0, 0);
            }
        });
        new Thread(calculation).start();
        try {
            return calculation.get(300, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
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

    private boolean solve(int i, int j) {
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
