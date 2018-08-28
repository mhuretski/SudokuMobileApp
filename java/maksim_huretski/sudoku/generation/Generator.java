package maksim_huretski.sudoku.generation;

class Generator {
    private final int[][] sudoku = new int[9][9];
    private final int[] possibleValues = new int[9];

    int[][] getSudoku() {
        generate();
        return sudoku;
    }

    private void generate() {
        setPossibleValues();
        for (int i = 0; i < 9; i++) {
            int number = random();
            sudoku[0][i] = number;
            possibleValues[number - 1] = 0;
        }
        setPossibleValues();
        possibleValues[sudoku[0][0] - 1] = 0;
        for (int i = 3; i < 9; i++) {
            int number = random();
            sudoku[i][0] = number;
            possibleValues[number - 1] = 0;
        }
    }

    private void setPossibleValues() {
        for (int i = 1; i <= 9; i++) {
            possibleValues[i - 1] = i;
        }
    }

    private int random() {
        int min = 1;
        int max = 9;
        int range = (max - min) + 1;
        int result = (int) (Math.random() * range) + min;
        for (int i = 0; i < 9; i++) {
            if (possibleValues[i] == result) return result;
        }
        return random();
    }

}
