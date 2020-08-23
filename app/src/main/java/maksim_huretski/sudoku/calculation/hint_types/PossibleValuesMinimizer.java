package maksim_huretski.sudoku.calculation.hint_types;

public class PossibleValuesMinimizer {

    private boolean goFurther;
    private final int[][][][] sudoku = new int[9][9][2][];

    public PossibleValuesMinimizer init(int[][] sudoku) {
        addDataToSudoku();
        for (int i = 0; i < sudoku.length; i++) {
            for (int j = 0; j < sudoku[i].length; j++) {
                this.sudoku[i][j][0][0] = sudoku[i][j];
            }
        }
        return this;
    }

    public int[][][][] minimize() {
        do {
            goFurther = false;
            for (int i = 0; i < 9; i++) {
                minimizePossibleValuesInRow(i);
                minimizePossibleValuesInColumn(i);
                minimizePossibleValuesInBlock(i);
            }
        } while (goFurther);
        return sudoku;
    }

    private void minimizePossibleValuesInRow(int row) {
        for (int i = 0; i < sudoku[row].length; i++) {
            if (sudoku[row][i][0][0] == 0) {
                for (int j = 0; j < sudoku.length; j++) {
                    if (j != i) {
                        if (sudoku[row][j][0][0] != 0) {
                            for (int k = 0; k < sudoku[row][i][1].length; k++) {
                                if (sudoku[row][j][0][0] == sudoku[row][i][1][k]) {
                                    sudoku[row][i][1][k] = 0;
                                    goFurther = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    private void minimizePossibleValuesInColumn(int column) {
        for (int i = 0; i < sudoku.length; i++) {
            if (sudoku[i][column][0][0] == 0) {
                for (int j = 0; j < sudoku[i].length; j++) {
                    if (j != i) {
                        if (sudoku[j][column][0][0] != 0) {
                            for (int k = 0; k < sudoku[i][column][1].length; k++) {
                                if (sudoku[j][column][0][0] == sudoku[i][column][1][k]) {
                                    sudoku[i][column][1][k] = 0;
                                    goFurther = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void minimizePossibleValuesInBlock(int block) {
        switch (block) {
            case 0:
                getPossibleValuesInBlock(0, 3, 0, 3);
                break;
            case 1:
                getPossibleValuesInBlock(0, 3, 3, 6);
                break;
            case 2:
                getPossibleValuesInBlock(0, 3, 6, 9);
                break;
            case 3:
                getPossibleValuesInBlock(3, 6, 0, 3);
                break;
            case 4:
                getPossibleValuesInBlock(3, 6, 3, 6);
                break;
            case 5:
                getPossibleValuesInBlock(3, 6, 6, 9);
                break;
            case 6:
                getPossibleValuesInBlock(6, 9, 0, 3);
                break;
            case 7:
                getPossibleValuesInBlock(6, 9, 3, 6);
                break;
            case 8:
                getPossibleValuesInBlock(6, 9, 6, 9);
                break;
        }
    }

    private void getPossibleValuesInBlock(int startRow, int endRow, int startColumn, int endColumn) {
        for (int i = startRow; i < endRow; i++) {
            for (int j = startColumn; j < endColumn; j++) {
                if (sudoku[i][j][0][0] == 0) {
                    for (int k = startRow; k < endRow; k++) {
                        for (int l = startColumn; l < endColumn; l++) {
                            if (sudoku[k][l][0][0] != 0) {
                                for (int m = 0; m < sudoku[i][j][1].length; m++) {
                                    if (sudoku[k][l][0][0] == sudoku[i][j][1][m]) {
                                        sudoku[i][j][1][m] = 0;
                                        goFurther = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void addDataToSudoku() {
        for (int i = 0; i < sudoku.length; i++) {
            for (int j = 0; j < sudoku[i].length; j++) {
                sudoku[i][j][0] = new int[]{0};
                sudoku[i][j][1] = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
            }
        }
    }

}
