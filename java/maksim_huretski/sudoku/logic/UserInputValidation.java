package maksim_huretski.sudoku.logic;

import java.util.HashSet;
import java.util.Set;

class UserInputValidation {
    private final int[][] sudoku;
    private final int[][] blockIdentifications = new int[9][4];
    private final Set<Integer> incorrectRows = new HashSet<>();
    private final Set<Integer> incorrectColumns = new HashSet<>();
    private final Set<Integer> incorrectBlocks = new HashSet<>();

    UserInputValidation(int[][] sudoku) {
        this.sudoku = sudoku;
        setBlockIdentifications();
    }

    public boolean checkUserInput() {
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

    private void setBlockIdentifications() {
        int[] ids = new int[]{0, 3, 0, 3};
        for (int i = 0; i < 9; i++, ids[2] += 3, ids[3] += 3) {
            if (i != 0 && i % 3 == 0) {
                ids[0] += 3;
                ids[1] += 3;
            }
            if (ids[2] > 6) {
                ids[2] = 0;
                ids[3] = 3;
            }
            System.arraycopy(ids, 0, blockIdentifications[i], 0, 4);
        }
    }

    private void checkBlocks(int block) {
        checkBlock(blockIdentifications[block][0],
                blockIdentifications[block][1],
                blockIdentifications[block][2],
                blockIdentifications[block][3],
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

    public int[][] getBlockIdentifications() {
        return blockIdentifications;
    }
}
