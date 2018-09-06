package maksim_huretski.sudoku.calculation.validation;

public class Checker {

    private final int SUDOKU_VALUE = 45;
    private boolean isValid = true;
    private boolean isFilled = true;

    public boolean isDone() {
        return isValid && isFilled;
    }

    public void checkSudoku(int[][] sudoku, int[][] blockIDs) {
        if (isDone()) checkRow(sudoku);
        if (isDone()) checkColumn(sudoku);
        if (isDone()) checkBlock(sudoku, blockIDs);
        if (isDone()) isFilled(sudoku);
    }

    private void checkRow(int[][] sudoku) {
        int tempData;
        label:
        for (int[] row : sudoku) {
            tempData = 0;
            for (int cell : row) {
                if (cell > 9 || cell < 0) {
                    isValid = false;
                    break label;
                }
                tempData += cell;
            }
            if (tempData != SUDOKU_VALUE) {
                isValid = false;
                break;
            }
        }
    }

    private void checkColumn(int[][] sudoku) {
        int tempData;
        label:
        for (int i = 0; i < sudoku[0].length; i++) {
            tempData = 0;
            for (int[] column : sudoku) {
                if (column[i] > 9 || column[i] < 0) {
                    isValid = false;
                    break label;
                }
                tempData += column[i];
            }
            if (tempData != SUDOKU_VALUE) {
                isValid = false;
                break;
            }
        }
    }

    private void checkBlock(int[][] sudoku, int[][] blockIDs) {
        int tempData;
        label:
        for (int block = 0; block < 9; block++) {
            tempData = 0;
            for (int i = blockIDs[block][0]; i < blockIDs[block][1]; i++) {
                for (int j = blockIDs[block][2]; j < blockIDs[block][3]; j++) {
                    if (sudoku[i][j] > 9 || sudoku[i][j] < 0) {
                        isValid = false;
                        break label;
                    }
                    tempData += sudoku[i][j];
                }
            }
            if (tempData != SUDOKU_VALUE) {
                isValid = false;
                break;
            }
        }
    }

    private void isFilled(int[][] sudoku) {
        label:
        for (int[] aSudoku : sudoku) {
            for (int anASudoku : aSudoku) {
                if (anASudoku == 0) {
                    isFilled = false;
                    break label;
                }
            }
        }
    }

}
