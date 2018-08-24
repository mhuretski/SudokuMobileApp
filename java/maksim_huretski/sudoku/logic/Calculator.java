package maksim_huretski.sudoku.logic;

import java.util.Arrays;

public class Calculator {

    private final int SUDOKU_VALUE = 45;
    private final int[] tempNumbers = new int[9];
    private int tempData;
    private int tempRow;
    private int tempColumn;
    private int[][] blockIDs;
    private int[][] tempBlocks = new int[3][3];
    private final int[] tempValues = new int[2];
    private boolean goFurther = false;
    private boolean isOne = false;
    private final int[][][][] sudoku = new int[9][9][2][];
    private final int[][] calculatedSudoku = new int[9][9];


    public int[][] getSudoku() {
        return calculatedSudoku;
    }

    public void init(int[][] sudoku, int[][] blockIDs) {
        setBlockIDs(blockIDs);
        addDataToSudoku();
        addValuesToSudoku(sudoku);
    }

    public void calculateSudoku() {
        fillCells();
        setCalculatedSudoku();
    }

    private void setCalculatedSudoku() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                calculatedSudoku[i][j] = sudoku[i][j][0][0];
            }
        }
    }

    private void fillCells() {
        do {
            goFurther = false;

            checkBlock();
            checkRow();
            checkColumn();

            for (int i = 0; i < 9; i++) {
                minimizePossibleValuesInRow(i);
                minimizePossibleValuesInColumn(i);
                minimizePossibleValuesInBlock(i);
            }

            fillIfOnlyOnePossibleValueInRow();
            fillIfOnlyOnePossibleValueInColumn();
            fillIfOnlyOnePossibleValueInBlock();

            fillIfOnlyOnePossibleValue();

            findOpenPairsInColumns();
            findOpenPairsInRows();

            findExcludedValuesUsingColumn();
            findExcludedValuesUsingRow();

            findClosedPairs();
        } while (goFurther);
    }

    private void addDataToSudoku() {
        for (int i = 0; i < sudoku.length; i++) {
            for (int j = 0; j < sudoku[i].length; j++) {
                sudoku[i][j][0] = new int[]{0};
                sudoku[i][j][1] = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
            }
        }
    }

    private void addValuesToSudoku(int[][] initialSudoku) {
        for (int i = 0; i < sudoku.length; i++) {
            for (int j = 0; j < sudoku[i].length; j++) {
                if (initialSudoku[i][j] != 0) {
                    sudoku[i][j][0][0] = initialSudoku[i][j];
                    for (int k = 0; k < sudoku[i][j][1].length; k++) {
                        sudoku[i][j][1][k] = 0;
                    }
                }
            }
        }
    }

    private void checkRow() {
        for (int row = 0; row < 9; row++) {
            tempData = 0;
            tempRow = 0;
            tempColumn = 0;
            for (int column = 0; column < sudoku[row].length; column++) {
                if (sudoku[row][column][0][0] == 0) {
                    tempData++;
                    tempRow = row;
                    tempColumn = column;
                }
            }
            if (tempData == 1) {
                goFurther = true;
                fillCellRow(tempRow, tempColumn);
            }
        }
    }

    private void fillCellRow(int row, int column) {
        tempData = SUDOKU_VALUE;
        for (int i = 0; i < sudoku[row].length; i++) {
            tempData -= sudoku[row][i][0][0];
        }
        if (sudoku[row][column][0][0] == 0) {
            sudoku[row][column][0][0] = tempData;
            for (int k = 0; k < sudoku[row][column][1].length; k++)
                sudoku[row][column][1][k] = 0;
        }
    }


    private void checkColumn() {
        for (int column = 0; column < 9; column++) {
            tempData = 0;
            tempRow = 0;
            tempColumn = 0;
            for (int row = 0; row < sudoku.length; row++) {
                if (sudoku[row][column][0][0] == 0) {
                    tempData++;
                    tempRow = row;
                    tempColumn = column;
                }
            }
            if (tempData == 1) {
                goFurther = true;
                fillCellColumn(tempRow, tempColumn);
            }
        }
    }

    private void fillCellColumn(int row, int column) {
        tempData = SUDOKU_VALUE;
        for (int[][][] aSudoku : sudoku) {
            tempData -= aSudoku[column][0][0];
        }
        if (sudoku[row][column][0][0] == 0) {
            sudoku[row][column][0][0] = tempData;
            for (int k = 0; k < sudoku[row][column][1].length; k++)
                sudoku[row][column][1][k] = 0;
        }
    }

    private void checkBlock() {
        for (int block = 0; block < 9; block++) {
            blockInitializer(block);
            fillBlock(block);
        }
    }

    private void blockInitializer(int block) {
        switch (block) {
            case 0:
                tempBlocks = feelTheBlock(0, 3, 0, 3);
                break;
            case 1:
                tempBlocks = feelTheBlock(0, 3, 3, 6);
                break;
            case 2:
                tempBlocks = feelTheBlock(0, 3, 6, 9);
                break;
            case 3:
                tempBlocks = feelTheBlock(3, 6, 0, 3);
                break;
            case 4:
                tempBlocks = feelTheBlock(3, 6, 3, 6);
                break;
            case 5:
                tempBlocks = feelTheBlock(3, 6, 6, 9);
                break;
            case 6:
                tempBlocks = feelTheBlock(6, 9, 0, 3);
                break;
            case 7:
                tempBlocks = feelTheBlock(6, 9, 3, 6);
                break;
            case 8:
                tempBlocks = feelTheBlock(6, 9, 6, 9);
                break;
        }
    }

    private void fillBlock(int block) {
        tempData = 0;
        tempRow = 0;
        tempColumn = 0;
        int row = 0;
        int column = 0;
        if (block > 2 && block < 6) row = 1;
        else if (block > 5 && block < 9) row = 2;
        if (block == 1 || block == 4 || block == 7) column = 1;
        else if (block == 2 || block == 5 || block == 8) column = 2;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tempBlocks[i][j] == 0) {
                    tempData++;
                    tempRow = i;
                    tempColumn = j;
                }
            }
        }
        if (tempData == 1) {
            if (row != 0) {
                if (row == 1) tempRow += 3;
                if (row == 2) tempRow += 6;
            }
            if (column != 0) {
                if (column == 1) tempColumn += 3;
                if (column == 2) tempColumn += 6;
            }
            goFurther = true;
            fillCellBlock(tempRow, tempColumn);
        }
    }

    private int[][] feelTheBlock(int startRow, int endRow, int startColumn, int endColumn) {
        int k;
        int l = 0;
        for (int i = startRow; i < endRow; i++, l++) {
            k = 0;
            for (int j = startColumn; j < endColumn; j++, k++) {
                tempBlocks[l][k] = sudoku[i][j][0][0];
            }
        }
        return tempBlocks;
    }

    private void fillCellBlock(int row, int column) {
        tempData = SUDOKU_VALUE;
        for (int[] aTempBlock : tempBlocks) {
            for (int anATempBlock : aTempBlock) {
                tempData -= anATempBlock;
            }
        }
        if (sudoku[row][column][0][0] == 0)
            sudoku[row][column][0][0] = tempData;
        for (int k = 0; k < sudoku[row][column][1].length; k++)
            sudoku[row][column][1][k] = 0;
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

    private void minimizePossibleValuesInBlock(int row, int column) {
        int block = 10;
        if (row < 3 && column < 3) block = 0;
        else if (row < 3 && column < 6) block = 1;
        else if (row < 3 && column < 9) block = 2;
        else if (row < 6 && column < 3) block = 3;
        else if (row < 6 && column < 6) block = 4;
        else if (row < 6 && column < 9) block = 5;
        else if (row < 9 && column < 3) block = 6;
        else if (row < 9 && column < 6) block = 7;
        else if (row < 9 && column < 9) block = 8;
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

    private void fillIfOnlyOnePossibleValueInRow() {
        for (int row = 0; row < sudoku.length; row++) {
            for (int i = 0; i < sudoku[row].length; i++) {
                for (int j = 0; j < sudoku[row][i][1].length; j++) {
                    isOne = true;
                    if (sudoku[row][i][0][0] == 0) {
                        if (sudoku[row][i][1][j] != 0) {
                            for (int l = 0; l < sudoku[row].length; l++) {
                                for (int m = 0; m < sudoku[row][l][1].length; m++) {
                                    if (i != l) {
                                        if (sudoku[row][i][1][j] == sudoku[row][l][1][m]) {
                                            isOne = false;
                                            break;
                                        }
                                    }
                                }
                                if (!isOne) break;
                            }
                            if (isOne) {
                                rowIsOne(row, i, j);
                            }
                        }
                    }
                }
            }
        }
    }

    private void rowIsOne(int row, int i, int j) {
        goFurther = true;
        sudoku[row][i][0][0] = sudoku[row][i][1][j];
        minimizePossibleValuesInColumn(i);
        minimizePossibleValuesInRow(i);
        minimizePossibleValuesInBlock(row, i);
        for (int l = 0; l < sudoku[row][i][1].length; l++) {
            if (sudoku[row][i][1][l] != 0) {
                sudoku[row][i][1][l] = 0;
            }
        }
    }

    private void fillIfOnlyOnePossibleValueInColumn() {
        for (int column = 0; column < sudoku.length; column++) {
            for (int i = 0; i < sudoku.length; i++) {
                for (int j = 0; j < sudoku[i][column][1].length; j++) {
                    isOne = true;
                    if (sudoku[i][column][0][0] == 0) {
                        if (sudoku[i][column][1][j] != 0) {
                            for (int l = 0; l < sudoku.length; l++) {
                                for (int m = 0; m < sudoku[l][column][1].length; m++) {
                                    if (i != l) {
                                        if (sudoku[i][column][1][j] == sudoku[l][column][1][m]) {
                                            isOne = false;
                                            break;
                                        }
                                    }
                                }
                                if (!isOne) break;
                            }
                            if (isOne) {
                                columnIsOne(i, column, j);
                            }
                        }
                    }
                }
            }
        }
    }

    private void columnIsOne(int i, int column, int j) {
        goFurther = true;
        sudoku[i][column][0][0] = sudoku[i][column][1][j];
        minimizePossibleValuesInColumn(i);
        minimizePossibleValuesInRow(i);
        minimizePossibleValuesInBlock(i, column);
        for (int l = 0; l < sudoku[i][column][1].length; l++) {
            if (sudoku[i][column][1][l] != 0) {
                sudoku[i][column][1][l] = 0;
            }
        }
    }

    private void fillIfOnlyOnePossibleValueInBlock() {
        for (int block = 0; block < 9; block++) {
            switch (block) {
                case 0:
                    getOnePossibleValuesInBlock(0, 3, 0, 3, 0);
                    break;
                case 1:
                    getOnePossibleValuesInBlock(0, 3, 3, 6, 1);
                    break;
                case 2:
                    getOnePossibleValuesInBlock(0, 3, 6, 9, 2);
                    break;
                case 3:
                    getOnePossibleValuesInBlock(3, 6, 0, 3, 3);
                    break;
                case 4:
                    getOnePossibleValuesInBlock(3, 6, 3, 6, 4);
                    break;
                case 5:
                    getOnePossibleValuesInBlock(3, 6, 6, 9, 5);
                    break;
                case 6:
                    getOnePossibleValuesInBlock(6, 9, 0, 3, 6);
                    break;
                case 7:
                    getOnePossibleValuesInBlock(6, 9, 3, 6, 7);
                    break;
                default:
                    getOnePossibleValuesInBlock(6, 9, 6, 9, 8);
                    break;
            }
        }
    }

    private void getOnePossibleValuesInBlock(int startRow, int endRow, int startColumn, int endColumn, int block) {
        for (int i = startRow; i < endRow; i++) { /*row 1-3*/
            for (int j = startColumn; j < endColumn; j++) { /*column 1-3*/
                if (sudoku[i][j][0][0] == 0) { /*zero value in block*/
                    for (int n = 0; n < sudoku[i][j][1].length; n++) { /*for each possible value in block 1-9*/
                        isOne = false;
                        if (sudoku[i][j][1][n] != 0) { /*if possible value != 0*/
                            compareWithPossibleOtherValuesInBlock(startRow, endRow, startColumn, endColumn, i, j, n);
                        }
                        if (isOne) {
                            setOnePossibleValueToBlock(i, j, n, block);
                        }
                    }
                }
            }
        }
    }

    private void compareWithPossibleOtherValuesInBlock(int startRow, int endRow, int startColumn, int endColumn, int i, int j, int n) {
        isOne = true;
        label: /*looping through all possible values in block*/
        for (int k = startRow; k < endRow; k++) { /*row 1-3*/
            for (int l = startColumn; l < endColumn; l++) { /*column 1-3*/
                if (i != k || j != l) { /* skip cell that being compared*/
                    for (int o = 0; o < sudoku[k][l][1].length; o++) { /*looping through possible values*/
                        if (sudoku[k][l][1][o] != 0) { /*if possible value in another cell != 0*/
                            if (sudoku[k][l][1][o] == sudoku[i][j][1][n]) {
                                isOne = false; /* value isn't the only one*/
                                break label;
                            }
                        }
                    }
                }
            }
        }
    }

    private void setOnePossibleValueToBlock(int i, int j, int n, int block) {
        goFurther = true;
        sudoku[i][j][0][0] = sudoku[i][j][1][n];
        for (int l = 0; l < sudoku[i][j][1].length; l++) {
            if (sudoku[i][j][1][l] != 0) {
                sudoku[i][j][1][l] = 0;
            }
        }
        minimizePossibleValuesInRow(i);
        minimizePossibleValuesInColumn(j);
        minimizePossibleValuesInBlock(block);
    }

    private void fillIfOnlyOnePossibleValue() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                tempData = 0;
                int tempValue = 0;
                if (sudoku[i][j][0][0] == 0) {
                    for (int k = 0; k < 9; k++) {
                        if (sudoku[i][j][1][k] != 0) {
                            tempData++;
                            tempValue = sudoku[i][j][1][k];
                        }
                    }
                }
                if (tempData == 1) {
                    goFurther = true;
                    sudoku[i][j][0][0] = tempValue;
                    minimizePossibleValuesInColumn(i);
                    minimizePossibleValuesInRow(i);
                    minimizePossibleValuesInBlock(i, j);
                    for (int l = 0; l < sudoku[i][j][1].length; l++) {
                        if (sudoku[i][j][1][l] != 0) {
                            sudoku[i][j][1][l] = 0;
                        }
                    }
                }
            }
        }
    }

    private void setBlockIDs(int[][] blockIDs) {
        this.blockIDs = blockIDs;
    }

    private void findClosedPairs() {
        for (int block = 0; block < 9; block++) {
            checkBlocksForClosedPairsInRow(block);
            checkBlocksForClosedPairsInColumn(block);
        }
    }

    private void checkBlocksForClosedPairsInRow(int block) {
        boolean isStartColumn;
        boolean isEndColumn;
        switch (block) {
            case 0:
            case 3:
            case 6:
                isStartColumn = true;
                isEndColumn = false;
                break;
            case 1:
            case 4:
            case 7:
                isStartColumn = false;
                isEndColumn = false;
                break;
            default:
                isStartColumn = false;
                isEndColumn = true;
                break;
        }
        closedPairsBlockIdentifierInRow(
                blockIDs[block][0],
                blockIDs[block][1],
                blockIDs[block][2],
                blockIDs[block][3],
                isStartColumn, isEndColumn);
    }

    private void closedPairsBlockIdentifierInRow
            (int startRow, int endRow, int startColumn, int endColumn,
             boolean isStartColumn, boolean isEndColumn) {
        zerosInTempNumbers();
        for (int i = startRow; i < endRow; i++) {
            fillArrWithPossibleValuesInRowInBlock(i, startColumn, endColumn);
            excludeFromArrPossibleValuesInOtherRowsOfBlock(i, startRow, endRow, startColumn, endColumn);
            excludeOtherRowValuesFromClosedPairs(i, isStartColumn, isEndColumn);
            deleteOutOfClosedPairsInRow(i, startColumn, endColumn);
        }
    }

    private void zerosInTempNumbers() {
        for (int i = 0; i < 9; i++) {
            tempNumbers[i] = 0;
        }
    }

    private void fillArrWithPossibleValuesInRowInBlock
            (int row, int startColumn, int endColumn) {
        for (int j = startColumn; j < endColumn; j++) {
            if (sudoku[row][j][0][0] == 0) {
                for (int k = 0; k < 9; k++) {
                    if (sudoku[row][j][1][k] != 0) {
                        tempNumbers[k] = sudoku[row][j][1][k];
                    }
                }
            }
        }
    }

    private void excludeFromArrPossibleValuesInOtherRowsOfBlock
            (int i, int startRow, int endRow, int startColumn, int endColumn) {
        for (int k = startRow; k < endRow; k++) {
            if (i != k) {
                for (int l = startColumn; l < endColumn; l++) {
                    if (sudoku[k][l][0][0] == 0) {
                        for (int m = 0; m < 9; m++) {
                            if (sudoku[k][l][1][m] != 0) {
                                tempNumbers[m] = 0;
                            }
                        }
                    }
                }
            }
        }
    }

    private void excludeOtherRowValuesFromClosedPairs(int row, boolean isStartColumn, boolean isEndColumn) {
        int startColumn = 0;
        int endColumn = 9;
        boolean isMiddleColumn = false;
        if (isStartColumn) startColumn = 3;
        else if (isEndColumn) endColumn = 6;
        else isMiddleColumn = true;
        for (int k = startColumn; k < endColumn; k++) {
            if (isMiddleColumn && k == 3) k = 6;
            if (sudoku[row][k][0][0] == 0) {
                for (int m = 0; m < 9; m++) {
                    if (sudoku[row][k][1][m] != 0) {
                        tempNumbers[m] = 0;
                    }
                }
            }
        }
    }

    private void deleteOutOfClosedPairsInRow(int row, int startColumn, int endColumn) {
        tempData = 0;
        amountOfRemainingValuesInClosedPair();
        int amountOfMatchedValues = amountOfMatchedCellsInClosedPairsInRow(row, startColumn, endColumn);
        deleteValuesFromClosedPairsInRow(amountOfMatchedValues, row, startColumn, endColumn);
    }

    private int amountOfMatchedCellsInClosedPairsInRow(int row, int startColumn, int endColumn) {
        int amountOfMatchedValues = 0;
        for (int j = startColumn; j < endColumn; j++) {
            if (sudoku[row][j][0][0] == 0) {
                for (int k = 0; k < 9; k++) {
                    if (tempNumbers[k] != 0 && sudoku[row][j][1][k] != 0) {
                        amountOfMatchedValues++;
                        break;
                    }
                }
            }
        }
        return amountOfMatchedValues;
    }

    private void deleteValuesFromClosedPairsInRow(int amountOfMatchedValues, int row, int startColumn, int endColumn) {
        if (tempData == amountOfMatchedValues && tempData != 0) {
            switch (amountOfMatchedValues) {
                case 2:
                    deleteValuesFromClosedPairsInRowFromDouble(row, startColumn, endColumn);
                    break;
                case 3:
                    deleteValuesFromClosedPairsInRowFromTriple(row, startColumn, endColumn);
                    break;
            }
        }
    }

    private void deleteValuesFromClosedPairsInRowFromTriple(int row, int startColumn, int endColumn) {
        for (int j = startColumn; j < endColumn; j++) {
            if (sudoku[row][j][0][0] == 0) {
                for (int k = 0; k < 9; k++) {
                    if (tempNumbers[k] == 0 && sudoku[row][j][1][k] != 0) {
                        sudoku[row][j][1][k] = 0;
                        goFurther = true;
                    }
                }
            }
        }
    }

    private void deleteValuesFromClosedPairsInRowFromDouble(int row, int startColumn, int endColumn) {
        int[] columnsWithSuitableValues = new int[2];
        for (int j = startColumn; j < endColumn; j++) {
            if (sudoku[row][j][0][0] == 0) {
                for (int k = 0; k < 9; k++) {
                    if (tempNumbers[k] != 0 && sudoku[row][j][1][k] != 0) {
                        if (columnsWithSuitableValues[0] == 0)
                            columnsWithSuitableValues[0] = j;
                        else columnsWithSuitableValues[1] = j;
                        break;
                    }
                }
            }
        }
        for (int column : columnsWithSuitableValues) {
            if (sudoku[row][column][0][0] == 0) {
                for (int k = 0; k < 9; k++) {
                    if (tempNumbers[k] == 0 && sudoku[row][column][1][k] != 0) {
                        sudoku[row][column][1][k] = 0;
                        goFurther = true;
                    }
                }
            }
        }
    }

    private void checkBlocksForClosedPairsInColumn(int block) {
        boolean isStartRow;
        boolean isEndRow;
        if (block < 3) {
            isStartRow = true;
            isEndRow = false;
        } else if (block < 6) {
            isStartRow = false;
            isEndRow = false;
        } else {
            isStartRow = false;
            isEndRow = true;
        }
        closedPairsBlockIdentifierInColumn(
                blockIDs[block][0],
                blockIDs[block][1],
                blockIDs[block][2],
                blockIDs[block][3],
                isStartRow, isEndRow);
    }

    private void closedPairsBlockIdentifierInColumn
            (int startRow, int endRow, int startColumn, int endColumn,
             boolean isStartRow, boolean isEndRow) {
        zerosInTempNumbers();
        for (int i = startColumn; i < endColumn; i++) {
            fillArrWithPossibleValuesInColumnInBlock(i, startRow, endRow);
            excludeFromArrPossibleValuesInOtherColumnsOfBlock(i, startRow, endRow, startColumn, endColumn);
            excludeOtherColumnValuesFromClosedPairs(i, isStartRow, isEndRow);
            deleteOutOfClosedPairsInColumn(i, startRow, endRow);
        }
    }

    private void fillArrWithPossibleValuesInColumnInBlock
            (int column, int startRow, int endRow) {
        for (int j = startRow; j < endRow; j++) {
            if (sudoku[j][column][0][0] == 0) {
                for (int k = 0; k < 9; k++) {
                    if (sudoku[j][column][1][k] != 0) {
                        tempNumbers[k] = sudoku[j][column][1][k];
                    }
                }
            }
        }
    }

    private void excludeFromArrPossibleValuesInOtherColumnsOfBlock
            (int i, int startRow, int endRow, int startColumn, int endColumn) {
        for (int k = startColumn; k < endColumn; k++) {
            if (i != k) {
                for (int l = startRow; l < endRow; l++) {
                    if (sudoku[l][k][0][0] == 0) {
                        for (int m = 0; m < 9; m++) {
                            if (sudoku[l][k][1][m] != 0) {
                                tempNumbers[m] = 0;
                            }
                        }
                    }
                }
            }
        }
    }

    private void excludeOtherColumnValuesFromClosedPairs(int column, boolean isStartRow, boolean isEndRow) {
        int startRow = 0;
        int endRow = 9;
        boolean isMiddleRow = false;
        if (isStartRow) startRow = 3;
        else if (isEndRow) endRow = 6;
        else isMiddleRow = true;
        for (int k = startRow; k < endRow; k++) {
            if (isMiddleRow && k == 3) k = 6;
            if (sudoku[k][column][0][0] == 0) {
                for (int m = 0; m < 9; m++) {
                    if (sudoku[k][column][1][m] != 0) {
                        tempNumbers[m] = 0;
                    }
                }
            }
        }
    }

    private void deleteOutOfClosedPairsInColumn(int column, int startRow, int endRow) {
        tempData = 0;
        amountOfRemainingValuesInClosedPair();
        int amountOfMatchedValues = amountOfMatchedCellsInClosedPairsInColumn(column, startRow, endRow);
        deleteValuesFromClosedPairsInColumn(amountOfMatchedValues, column, startRow, endRow);
    }

    private void amountOfRemainingValuesInClosedPair() {
        for (int j = 0; j < 9; j++) {
            if (tempNumbers[j] != 0) tempData++;
        }
    }

    private int amountOfMatchedCellsInClosedPairsInColumn(int column, int startRow, int endRow) {
        int amountOfMatchedValues = 0;
        for (int j = startRow; j < endRow; j++) {
            if (sudoku[j][column][0][0] == 0) {
                for (int k = 0; k < 9; k++) {
                    if (tempNumbers[k] != 0 && sudoku[j][column][1][k] != 0) {
                        amountOfMatchedValues++;
                        break;
                    }
                }
            }
        }
        return amountOfMatchedValues;
    }

    private void deleteValuesFromClosedPairsInColumn(int amountOfMatchedValues, int column, int startRow, int endRow) {
        if (tempData == amountOfMatchedValues && tempData != 0) {
            switch (amountOfMatchedValues) {
                case 2:
                    deleteValuesFromClosedPairsInColumnFromDouble(column, startRow, endRow);
                    break;
                case 3:
                    deleteValuesFromClosedPairsInColumnFromTriple(column, startRow, endRow);
                    break;
            }
        }
    }

    private void deleteValuesFromClosedPairsInColumnFromTriple(int column, int startRow, int endRow) {
        for (int j = startRow; j < endRow; j++) {
            if (sudoku[j][column][0][0] == 0) {
                for (int k = 0; k < 9; k++) {
                    if (tempNumbers[k] == 0 && sudoku[j][column][1][k] != 0) {
                        sudoku[j][column][1][k] = 0;
                        goFurther = true;
                    }
                }
            }
        }
    }

    private void deleteValuesFromClosedPairsInColumnFromDouble(int column, int startRow, int endRow) {
        int[] rowsWithSuitableValues = new int[2];
        for (int j = startRow; j < endRow; j++) {
            if (sudoku[j][column][0][0] == 0) {
                for (int k = 0; k < 9; k++) {
                    if (tempNumbers[k] != 0 && sudoku[j][column][1][k] != 0) {
                        if (rowsWithSuitableValues[0] == 0)
                            rowsWithSuitableValues[0] = j;
                        else rowsWithSuitableValues[1] = j;
                        break;
                    }
                }
            }
        }
        for (int row : rowsWithSuitableValues) {
            if (sudoku[row][column][0][0] == 0) {
                for (int k = 0; k < 9; k++) {
                    if (tempNumbers[k] == 0 && sudoku[row][column][1][k] != 0) {
                        sudoku[row][column][1][k] = 0;
                        goFurther = true;
                    }
                }
            }
        }
    }


    private void findOpenPairsInRows() {
        for (int row = 0; row < 9; row++) {
            for (int column1 = 0; column1 < 9; column1++) {
                if (sudoku[row][column1][0][0] == 0) {
                    for (int column2 = 0; column2 < 9; column2++) {
                        if (column1 != column2) {
                            if (Arrays.equals(sudoku[row][column1][1], sudoku[row][column2][1])) {
                                checkAmountOfPossibleValuesInRowInOpenPairs(row, column1, column2);
                            }
                        }
                    }
                }
            }
        }
    }

    private void checkAmountOfPossibleValuesInRowInOpenPairs(int row, int column1, int column2) {
        tempData = 0;
        tempValues[0] = 0;
        tempValues[1] = 0;
        for (int k = 0; k < 9; k++) {
            if (sudoku[row][column1][1][k] != 0) {
                tempData++;
                if (tempData > 2) break;
                if (tempValues[0] == 0)
                    tempValues[0] = sudoku[row][column1][1][k];
                else tempValues[1] = sudoku[row][column1][1][k];
            }
        }
        if (tempData == 2) {
            deletePossibleValuesOutOfRowWithOpenPairsGottenFromRow(row, column1, column2);
            deletePossibleValuesOutOfBlockWithOpenPairsGottenFromRow(row, column1, column2);
        }
    }

    private void deletePossibleValuesOutOfBlockWithOpenPairsGottenFromRow(int row, int column1, int column2) {
        if (column1 < 3 && column2 < 3
                || column1 > 2 && column2 > 2 && column1 < 6 && column2 < 6
                || column1 > 5 && column2 > 5 && column1 < 9 && column2 < 9) {
            int block = 10;
            if (row < 3 && column1 < 3) block = 0;
            else if (row < 3 && column1 < 6) block = 1;
            else if (row < 3) block = 2;
            else if (row < 6 && column1 < 3) block = 3;
            else if (row < 6 && column1 < 6) block = 4;
            else if (row < 6) block = 5;
            else if (row < 9 && column1 < 3) block = 6;
            else if (row < 9 && column1 < 6) block = 7;
            else if (row < 9) block = 8;
            switch (block) {
                case 0:
                    deleteUnnecessaryValuesInBlockGottenFromRow(0, 3, 0, 3, row, column1, column2);
                    break;
                case 1:
                    deleteUnnecessaryValuesInBlockGottenFromRow(0, 3, 3, 6, row, column1, column2);
                    break;
                case 2:
                    deleteUnnecessaryValuesInBlockGottenFromRow(0, 3, 6, 9, row, column1, column2);
                    break;
                case 3:
                    deleteUnnecessaryValuesInBlockGottenFromRow(3, 6, 0, 3, row, column1, column2);
                    break;
                case 4:
                    deleteUnnecessaryValuesInBlockGottenFromRow(3, 6, 3, 6, row, column1, column2);
                    break;
                case 5:
                    deleteUnnecessaryValuesInBlockGottenFromRow(3, 6, 6, 9, row, column1, column2);
                    break;
                case 6:
                    deleteUnnecessaryValuesInBlockGottenFromRow(6, 9, 0, 3, row, column1, column2);
                    break;
                case 7:
                    deleteUnnecessaryValuesInBlockGottenFromRow(6, 9, 3, 6, row, column1, column2);
                    break;
                case 8:
                    deleteUnnecessaryValuesInBlockGottenFromRow(6, 9, 6, 9, row, column1, column2);
                    break;
            }
        }
    }

    private void deleteUnnecessaryValuesInBlockGottenFromRow(int startRow, int endRow, int startColumn, int endColumn, int row, int column1, int column2) {
        for (int value : tempValues) {
            for (int i = startRow; i < endRow; i++) {
                for (int j = startColumn; j < endColumn; j++) {
                    if ((i != row || j != column1) && (i != row || j != column2)) {
                        if (sudoku[i][j][0][0] == 0) {
                            deleteUnnecessaryValueInBlockGottenFromRow(i, j, value);
                        }
                    }
                }
            }
        }
    }

    private void deleteUnnecessaryValueInBlockGottenFromRow(int i, int j, int value) {
        for (int m = 0; m < sudoku[i][j][1].length; m++) {
            if (sudoku[i][j][1][m] == value) {
                sudoku[i][j][1][m] = 0;
                goFurther = true;
            }
        }
    }

    private void deletePossibleValuesOutOfRowWithOpenPairsGottenFromRow(int row, int column1, int column2) {
        for (int value : tempValues) {
            for (int k = 0; k < 9; k++) {
                if (k != column1 && k != column2) {
                    for (int m = 0; m < 9; m++) {
                        if (sudoku[row][k][1][m] == value) {
                            goFurther = true;
                            sudoku[row][k][1][m] = 0;
                        }
                    }
                }
            }
        }
    }

    private void findOpenPairsInColumns() {
        for (int column = 0; column < 9; column++) {
            for (int row1 = 0; row1 < 9; row1++) {
                if (sudoku[row1][column][0][0] == 0) {
                    for (int row2 = 0; row2 < 9; row2++) {
                        if (row1 != row2) {
                            if (Arrays.equals(sudoku[row1][column][1], sudoku[row2][column][1])) {
                                checkAmountOfPossibleValuesInColumnInOpenPairs(column, row1, row2);
                            }
                        }
                    }
                }
            }
        }
    }

    private void checkAmountOfPossibleValuesInColumnInOpenPairs(int column, int row1, int row2) {
        tempData = 0;
        tempValues[0] = 0;
        tempValues[1] = 0;
        for (int k = 0; k < 9; k++) {
            if (sudoku[row1][column][1][k] != 0) {
                tempData++;
                if (tempData > 2) break;
                if (tempValues[0] == 0)
                    tempValues[0] = sudoku[row1][column][1][k];
                else tempValues[1] = sudoku[row1][column][1][k];
            }
        }
        if (tempData == 2) {
            deletePossibleValuesOutOfColumnWithOpenPairs(column, row1, row2);
            deletePossibleValuesOutOfBlockWithOpenPairsGottenFromColumn(column, row1, row2);
        }
    }

    private void deletePossibleValuesOutOfBlockWithOpenPairsGottenFromColumn(int column, int row1, int row2) {
        if (row1 < 3 && row2 < 3
                || row1 > 2 && row2 > 2 && row1 < 6 && row2 < 6
                || row1 > 5 && row2 > 5 && row1 < 9 && row2 < 9) {
            int block;
            if (row1 < 3 && column < 3) block = 0;
            else if (row1 < 3 && column < 6) block = 1;
            else if (row1 < 3) block = 2;
            else if (row1 < 6 && column < 3) block = 3;
            else if (row1 < 6 && column < 6) block = 4;
            else if (row1 < 6) block = 5;
            else if (column < 3) block = 6;
            else if (column < 6) block = 7;
            else block = 8;
            switch (block) {
                case 0:
                    deleteUnnecessaryValuesInBlockGottenFromColumn(0, 3, 0, 3, column, row1, row2);
                    break;
                case 1:
                    deleteUnnecessaryValuesInBlockGottenFromColumn(0, 3, 3, 6, column, row1, row2);
                    break;
                case 2:
                    deleteUnnecessaryValuesInBlockGottenFromColumn(0, 3, 6, 9, column, row1, row2);
                    break;
                case 3:
                    deleteUnnecessaryValuesInBlockGottenFromColumn(3, 6, 0, 3, column, row1, row2);
                    break;
                case 4:
                    deleteUnnecessaryValuesInBlockGottenFromColumn(3, 6, 3, 6, column, row1, row2);
                    break;
                case 5:
                    deleteUnnecessaryValuesInBlockGottenFromColumn(3, 6, 6, 9, column, row1, row2);
                    break;
                case 6:
                    deleteUnnecessaryValuesInBlockGottenFromColumn(6, 9, 0, 3, column, row1, row2);
                    break;
                case 7:
                    deleteUnnecessaryValuesInBlockGottenFromColumn(6, 9, 3, 6, column, row1, row2);
                    break;
                default:
                    deleteUnnecessaryValuesInBlockGottenFromColumn(6, 9, 6, 9, column, row1, row2);
                    break;
            }
        }
    }

    private void deleteUnnecessaryValuesInBlockGottenFromColumn(int startRow, int endRow, int startColumn, int endColumn, int column, int row1, int row2) {
        for (int value : tempValues) {
            for (int j = startColumn; j < endColumn; j++) {
                for (int i = startRow; i < endRow; i++) {
                    if ((j != column || i != row1) && (j != column || i != row2)) {
                        if (sudoku[i][j][0][0] == 0) {
                            deleteUnnecessaryValueInBlockGottenFromColumn(i, j, value);
                        }
                    }
                }
            }
        }
    }

    private void deleteUnnecessaryValueInBlockGottenFromColumn(int i, int j, int value) {
        for (int m = 0; m < sudoku[i][j][1].length; m++) {
            if (sudoku[i][j][1][m] == value) {
                sudoku[i][j][1][m] = 0;
                goFurther = true;
            }
        }
    }

    private void deletePossibleValuesOutOfColumnWithOpenPairs(int column, int row1, int row2) {
        for (int value : tempValues) {
            for (int k = 0; k < 9; k++) {
                if (k != row1 && k != row2) {
                    for (int m = 0; m < 9; m++) {
                        if (sudoku[k][column][1][m] == value) {
                            goFurther = true;
                            sudoku[k][column][1][m] = 0;
                        }
                    }
                }
            }
        }
    }

    private void findExcludedValuesUsingColumn() {
        for (int block = 0; block < 9; block++) {
            switch (block) {
                case 0:
                    checkBlockForExistingValuesInBlockColumn(0, 3, 0, 3);
                    break;
                case 1:
                    checkBlockForExistingValuesInBlockColumn(0, 3, 3, 6);
                    break;
                case 2:
                    checkBlockForExistingValuesInBlockColumn(0, 3, 6, 9);
                    break;
                case 3:
                    checkBlockForExistingValuesInBlockColumn(3, 6, 0, 3);
                    break;
                case 4:
                    checkBlockForExistingValuesInBlockColumn(3, 6, 3, 6);
                    break;
                case 5:
                    checkBlockForExistingValuesInBlockColumn(3, 6, 6, 9);
                    break;
                case 6:
                    checkBlockForExistingValuesInBlockColumn(6, 9, 0, 3);
                    break;
                case 7:
                    checkBlockForExistingValuesInBlockColumn(6, 9, 3, 6);
                    break;
                default:
                    checkBlockForExistingValuesInBlockColumn(6, 9, 6, 9);
                    break;
            }
        }
    }

    private void checkBlockForExistingValuesInBlockColumn(int startRow, int endRow, int startColumn, int endColumn) {
        /*fill list of possible values in column of block*/
        for (int j = startColumn; j < endColumn; j++) {
            for (int i = 0; i < 9; i++) {
                tempNumbers[i] = 0;
            }
            for (int i = startRow; i < endRow; i++) {
                if (sudoku[i][j][0][0] == 0) {
                    for (int n = 0; n < 9; n++) {
                        if (sudoku[i][j][1][n] != 0) {
                            tempNumbers[n] = sudoku[i][j][1][n];
                        }
                    }
                }
            }
            /*exclude if there is such value in other columns of block*/
            for (int k = startColumn; k < endColumn; k++) {
                if (k != j) {
                    for (int i = startRow; i < endRow; i++) {
                        if (sudoku[i][k][0][0] == 0) {
                            for (int n = 0; n < 9; n++) {
                                if (sudoku[i][k][1][n] != 0) {
                                    tempNumbers[n] = 0;
                                }
                            }
                        }
                    }
                }
            }
            /*if list contains values after duplicates are excluded*/
            for (int i = 0; i < 9; i++) {
                if (tempNumbers[i] != 0) {
                    if (startRow < 3) {
                        getColumnWithoutBlock(j, true);
                    } else if (startRow < 6) {
                        getColumnWithoutBlock(j);
                    } else if (startRow < 9) {
                        getColumnWithoutBlock(j, false);
                    }
                }
            }
        }
    }

    private void getColumnWithoutBlock(int column, boolean isBeginning) {
        if (isBeginning) {
            for (int i = 3; i < 9; i++) {
                if (sudoku[i][column][0][0] == 0) {
                    for (int j = 0; j < 9; j++) {
                        if (tempNumbers[j] != 0 && sudoku[i][column][1][j] != 0) {
                            goFurther = true;
                            sudoku[i][column][1][j] = 0;
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < 6; i++) {
                if (sudoku[i][column][0][0] == 0) {
                    for (int j = 0; j < 9; j++) {
                        if (tempNumbers[j] != 0 && sudoku[i][column][1][j] != 0) {
                            goFurther = true;
                            sudoku[i][column][1][j] = 0;
                        }
                    }
                }
            }
        }
    }

    private void getColumnWithoutBlock(int column) {
        for (int i = 0; i < 9; i++) {
            if (i != 3 && i != 4 & i != 5) {
                if (sudoku[i][column][0][0] == 0) {
                    for (int j = 0; j < 9; j++) {
                        if (tempNumbers[j] != 0 && sudoku[i][column][1][j] != 0) {
                            goFurther = true;
                            sudoku[i][column][1][j] = 0;
                        }
                    }
                }
            }
        }
    }

    private void findExcludedValuesUsingRow() {
        for (int block = 0; block < 9; block++) {
            switch (block) {
                case 0:
                    checkBlockForExistingValuesInBlockRow(0, 3, 0, 3);
                    break;
                case 1:
                    checkBlockForExistingValuesInBlockRow(0, 3, 3, 6);
                    break;
                case 2:
                    checkBlockForExistingValuesInBlockRow(0, 3, 6, 9);
                    break;
                case 3:
                    checkBlockForExistingValuesInBlockRow(3, 6, 0, 3);
                    break;
                case 4:
                    checkBlockForExistingValuesInBlockRow(3, 6, 3, 6);
                    break;
                case 5:
                    checkBlockForExistingValuesInBlockRow(3, 6, 6, 9);
                    break;
                case 6:
                    checkBlockForExistingValuesInBlockRow(6, 9, 0, 3);
                    break;
                case 7:
                    checkBlockForExistingValuesInBlockRow(6, 9, 3, 6);
                    break;
                default:
                    checkBlockForExistingValuesInBlockRow(6, 9, 6, 9);
                    break;
            }
        }
    }

    private void checkBlockForExistingValuesInBlockRow(int startRow, int endRow, int startColumn, int endColumn) {
        /*fill list of possible values in row of block*/
        for (int j = startRow; j < endRow; j++) {
            for (int i = 0; i < 9; i++) {
                tempNumbers[i] = 0;
            }
            for (int i = startColumn; i < endColumn; i++) {
                if (sudoku[j][i][0][0] == 0) {
                    for (int n = 0; n < 9; n++) {
                        if (sudoku[j][i][1][n] != 0) {
                            tempNumbers[n] = sudoku[j][i][1][n];
                        }
                    }
                }
            }
            /*exclude if there is such value in other rows of block*/
            for (int k = startRow; k < endRow; k++) {
                if (k != j) {
                    for (int i = startColumn; i < endColumn; i++) {
                        if (sudoku[k][i][0][0] == 0) {
                            for (int n = 0; n < 9; n++) {
                                if (sudoku[k][i][1][n] != 0) {
                                    tempNumbers[n] = 0;
                                }
                            }
                        }
                    }
                }
            }
            /*if list contains values after duplicates are excluded*/
            for (int i = 0; i < 9; i++) {
                if (tempNumbers[i] != 0) {
                    if (startColumn < 3) {
                        getRowWithoutBlock(j, true);
                    } else if (startColumn < 6) {
                        getRowWithoutBlock(j);
                    } else if (startColumn < 9) {
                        getRowWithoutBlock(j, false);
                    }
                }
            }
        }
    }

    private void getRowWithoutBlock(int row, boolean isBeginning) {
        if (isBeginning) {
            for (int i = 3; i < 9; i++) {
                if (sudoku[row][i][0][0] == 0) {
                    for (int j = 0; j < 9; j++) {
                        if (tempNumbers[j] != 0 && sudoku[row][i][1][j] != 0) {
                            goFurther = true;
                            sudoku[row][i][1][j] = 0;
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < 6; i++) {
                if (sudoku[row][i][0][0] == 0) {
                    for (int j = 0; j < 9; j++) {
                        if (tempNumbers[j] != 0 && sudoku[row][i][1][j] != 0) {
                            goFurther = true;
                            sudoku[row][i][1][j] = 0;
                        }
                    }
                }
            }
        }
    }

    private void getRowWithoutBlock(int row) {
        for (int i = 0; i < 9; i++) {
            if (i != 3 && i != 4 & i != 5) {
                if (sudoku[row][i][0][0] == 0) {
                    for (int j = 0; j < 9; j++) {
                        if (tempNumbers[j] != 0 && sudoku[row][i][1][j] != 0) {
                            goFurther = true;
                            sudoku[row][i][1][j] = 0;
                        }
                    }
                }
            }
        }
    }
}
