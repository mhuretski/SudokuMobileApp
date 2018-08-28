package maksim_huretski.sudoku.generation;

class Difficulty {

    private int amountOfCellsShown = 40;
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

    public int setDifficulty(int difficulty) {
        switch (difficulty) {
            case 1:
                amountOfCellsShown = 20;
                break;
            case 2:
                amountOfCellsShown = 30;
                break;
            case 3:
                amountOfCellsShown = 40;
                break;
            case 4:
                amountOfCellsShown = 50;
                break;
            case 5:
                amountOfCellsShown = 60;
                break;
        }
        return amountOfCellsShown;
    }

}
