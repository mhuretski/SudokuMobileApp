package maksim_huretski.sudoku.generation;

import maksim_huretski.sudoku.R;

class Difficulty {

    private int amountOfCellsShown = 41;
    private final int[] positions = new int[9];
    private final int[] amountOfShownCellsInEachRow = new int[9];
    private final boolean[][] shown = new boolean[9][9];

    public boolean[][] shownCells() {
        setAmountOfShownCellsInRow();
        setPositions();
        return shown;
    }

    private void setPositions() {
        for (int i = 0; i < 9; i++) {
            setPossiblePositions();
            while (amountOfShownCellsInEachRow[i] != 0) {
                int position = random();
                shown[i][position] = true;
                positions[position] = 0;
                amountOfShownCellsInEachRow[i]--;
            }
        }
    }

    private void setPossiblePositions() {
        for (int i = 1; i <= 9; i++) {
            positions[i - 1] = i;
        }
    }

    private int random() {
        int min = 1;
        int max = 9;
        int range = (max - min) + 1;
        int result = (int) (Math.random() * range) + min;
        for (int i = 0; i < 9; i++) {
            if (positions[i] == result) return i;
        }
        return random();
    }

    private void setAmountOfShownCellsInRow() {
        int medium = amountOfCellsShown / 9;
        int min = medium - 2;
        int max = medium + 2;
        int range = (max - min) + 1;

        int checkResult;
        do {
            checkResult = 0;
            for (int i = 0; i < 9; i++) {
                int result = (int) (Math.random() * range) + min;
                amountOfShownCellsInEachRow[i] = result;
                checkResult += result;
            }
        } while (checkResult != amountOfCellsShown);
    }

    public void setDifficulty(int difficulty) {
        switch (difficulty) {
            case R.id.easy:
                amountOfCellsShown = 36;
                break;
            case R.id.normal:
                amountOfCellsShown = 45;
                break;
            case R.id.hard:
                amountOfCellsShown = 54;
                break;
            case R.id.insane:
                amountOfCellsShown = 64;
                break;
        }
    }

}
