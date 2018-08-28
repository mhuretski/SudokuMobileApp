package maksim_huretski.sudoku.calculation.hint_types;

public class Irreducible {

    private int[][][][] sudoku;
    private int[][] blockIDs;
    private final int[] tempNumbers = new int[9];
    private boolean goFurther;

    public int[][][][] getSudoku() {
        return sudoku;
    }

    public boolean isGoFurther() {
        return goFurther;
    }

    public void reduce(int[][][][] sudoku, int[][] blockIDs, boolean goFurther) {
        this.sudoku = sudoku;
        this.blockIDs = blockIDs;
        this.goFurther = goFurther;
        for (int i = 0; i < 9; i++) {
            excludeValuesInRowInBlock(i);
        }
        for (int i = 0; i < 9; i++) {
            excludeValuesInColumnInBlock(i);
        }
    }

    private void excludeValuesInRowInBlock(int row) {
        int startBlock = 0;
        int endBlock = 9;
        if (row < 3) endBlock = 3;
        else if (row < 6) {
            startBlock = 3;
            endBlock = 6;
        } else startBlock = 6;

        for (int block = startBlock; block < endBlock; block++) {
            initializeTempNumbers();
            fillTempWithPossibleValuesInRowInBlock
                    (blockIDs[block][2], blockIDs[block][3], row);
            excludePossibleValuesInRowBySkippingValuesInRowThatOutOfBlock
                    (blockIDs[block][2], blockIDs[block][3], row);
            deleteOtherValuesFromBlockUsingUniqueValuesInRow(blockIDs[block][0],
                    blockIDs[block][1],
                    blockIDs[block][2],
                    blockIDs[block][3],
                    row);
        }
    }

    private void fillTempWithPossibleValuesInRowInBlock
            (int startColumn, int endColumn, int row) {
        for (int i = startColumn; i < endColumn; i++) {
            if (sudoku[row][i][0][0] == 0) {
                for (int n = 0; n < 9; n++) {
                    if (sudoku[row][i][1][n] != 0) {
                        tempNumbers[n] = sudoku[row][i][1][n];
                    }
                }
            }
        }
    }

    private void initializeTempNumbers() {
        for (int i = 0; i < tempNumbers.length; i++) {
            tempNumbers[i] = 0;
        }
    }

    private void excludePossibleValuesInRowBySkippingValuesInRowThatOutOfBlock
            (int startColumn, int endColumn, int row) {
        for (int i = 0; i < sudoku[row].length; i++) {
            if (!(i >= startColumn && i < endColumn)) {
                if (sudoku[row][i][0][0] == 0) {
                    for (int n = 0; n < 9; n++) {
                        if (sudoku[row][i][1][n] != 0 && tempNumbers[n] != 0) {
                            tempNumbers[n] = 0;
                        }
                    }
                }
            }
        }
    }

    private void deleteOtherValuesFromBlockUsingUniqueValuesInRow
            (int startRow, int endRow, int startColumn, int endColumn, int row) {
        for (int j = startRow; j < endRow; j++) {
            if (j != row) {
                for (int i = startColumn; i < endColumn; i++) {
                    if (sudoku[j][i][0][0] == 0) {
                        for (int n = 0; n < 9; n++) {
                            if (sudoku[j][i][1][n] != 0 && tempNumbers[n] != 0) {
                                sudoku[j][i][1][n] = 0;
                                goFurther = true;
                            }
                        }
                    }
                }
            }
        }
    }

    private void excludeValuesInColumnInBlock(int column) {
        int[] blocks = new int[3];
        if (column < 3) {
            blocks[1] = 3;
            blocks[2] = 6;
        } else if (column < 6) {
            blocks[0] = 1;
            blocks[1] = 4;
            blocks[2] = 7;
        } else {
            blocks[0] = 2;
            blocks[1] = 5;
            blocks[2] = 8;
        }
        for (int block : blocks) {
            initializeTempNumbers();
            fillTempWithPossibleValuesInColumnInBlock
                    (blockIDs[block][0], blockIDs[block][1], column);
            excludePossibleValuesInRowBySkippingValuesInColumnThatOutOfBlock
                    (blockIDs[block][0], blockIDs[block][1], column);
            deleteOtherValuesFromBlockUsingUniqueValuesInColumn(blockIDs[block][0],
                    blockIDs[block][1],
                    blockIDs[block][2],
                    blockIDs[block][3],
                    column);
        }
    }

    private void fillTempWithPossibleValuesInColumnInBlock
            (int startRow, int endRow, int column) {
        for (int i = startRow; i < endRow; i++) {
            if (sudoku[i][column][0][0] == 0) {
                for (int n = 0; n < 9; n++) {
                    if (sudoku[i][column][1][n] != 0) {
                        tempNumbers[n] = sudoku[i][column][1][n];
                    }
                }
            }
        }
    }

    private void excludePossibleValuesInRowBySkippingValuesInColumnThatOutOfBlock
            (int startRow, int endRow, int column) {
        for (int i = 0; i < sudoku.length; i++) {
            if (!(i >= startRow && i < endRow)) {
                if (sudoku[i][column][0][0] == 0) {
                    for (int n = 0; n < 9; n++) {
                        if (sudoku[i][column][1][n] != 0 && tempNumbers[n] != 0) {
                            tempNumbers[n] = 0;
                        }
                    }
                }
            }
        }
    }

    private void deleteOtherValuesFromBlockUsingUniqueValuesInColumn
            (int startRow, int endRow, int startColumn, int endColumn, int column) {
        for (int j = startColumn; j < endColumn; j++) {
            if (j != column) {
                for (int i = startRow; i < endRow; i++) {
                    if (sudoku[i][j][0][0] == 0) {
                        for (int n = 0; n < 9; n++) {
                            if (sudoku[i][j][1][n] != 0 && tempNumbers[n] != 0) {
                                sudoku[i][j][1][n] = 0;
                                goFurther = true;
                            }
                        }
                    }
                }
            }
        }
    }

}
