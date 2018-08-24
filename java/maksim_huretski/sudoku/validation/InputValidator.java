package maksim_huretski.sudoku.validation;

import java.util.HashSet;
import java.util.Set;

public class InputValidator {

    private int[][] sudoku;
    private int[][] blockIDs;
    private final Set<Integer> incorrectRows = new HashSet<>();
    private final Set<Integer> incorrectColumns = new HashSet<>();
    private final Set<Integer> incorrectBlocks = new HashSet<>();

    public void init(int[][] blockIDs) {
        this.blockIDs = blockIDs;
    }

    public void setSudoku(int[][] sudoku) {
        this.sudoku = sudoku;
    }

    public boolean checkInput() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                checkRow(i, j);
                checkColumn(i, j);
            }
            checkBlocks(i);
        }
        return okInput();
    }

    private void checkRow(int i, int j) {
        if (sudoku[i][j] != 0) {
            for (int k = 0; k < 9; k++) {
                if (j == k) continue;
                if (sudoku[i][k] == 0) continue;
                if (sudoku[i][j] == sudoku[i][k]) {
                    incorrectRows.add(i);
                }
            }
        }
    }

    private void checkColumn(int i, int j) {
        if (sudoku[j][i] != 0) {
            for (int k = 0; k < 9; k++) {
                if (j == k) continue;
                if (sudoku[k][i] == 0) continue;
                if (sudoku[j][i] == sudoku[k][i]) {
                    incorrectColumns.add(i);
                }
            }
        }
    }

    private void checkBlocks(int block) {
        checkBlock(blockIDs[block][0],
                blockIDs[block][1],
                blockIDs[block][2],
                blockIDs[block][3],
                block);
    }

    private void checkBlock(int startRow, int endRow, int startColumn, int endColumn, int block) {
        for (int i = startRow; i < endRow; i++) {
            for (int j = startColumn; j < endColumn; j++) {
                if (sudoku[i][j] == 0) continue;
                for (int k = startRow; k < endRow; k++) {
                    for (int l = startColumn; l < endColumn; l++) {
                        if (i != k || j != l) {
                            if (sudoku[i][j] == sudoku[k][l])
                                incorrectBlocks.add(block);
                        }
                    }
                }
            }
        }
    }

    private boolean okInput() {
        return (incorrectRows.size() +
                incorrectColumns.size() +
                incorrectBlocks.size()) == 0;
    }


    public Set<Integer> getIncorrectRows() {
        return incorrectRows;
    }

    public Set<Integer> getIncorrectColumns() {
        return incorrectColumns;
    }

    public Set<Integer> getIncorrectBlocks() {
        return incorrectBlocks;
    }

}
