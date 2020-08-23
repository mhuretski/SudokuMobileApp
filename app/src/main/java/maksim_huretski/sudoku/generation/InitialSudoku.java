package maksim_huretski.sudoku.generation;

public class InitialSudoku {

    private int[][] sudoku;
    private boolean isSolved = false;

    public int[][] generateInitialSudoku(int difficulty) {
        Generator generator = new Generator();
        Solver sudokuSolver = new Solver();

        do {
            sudoku = randomValues(generator);
            solvedSudoku(sudokuSolver);
        } while (!isSolved);
        sudoku = sudokuSolver.getSudoku();
        hideSudoku(difficulty);
        return sudoku;
    }

    private int[][] randomValues(Generator generator) {
        return generator.getSudoku();
    }

    private void solvedSudoku(Solver sudokuSolver) {
        sudokuSolver.init(sudoku, null);
        isSolved = sudokuSolver.calculateSudoku();
    }

    private void hideSudoku(int difficultyLevel){
        Difficulty difficulty = new Difficulty();
        difficulty.setDifficulty(difficultyLevel);
        boolean[][] shownCells = difficulty.shownCells();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (shownCells[i][j]) sudoku[i][j] = 0;
            }
        }
    }

}
