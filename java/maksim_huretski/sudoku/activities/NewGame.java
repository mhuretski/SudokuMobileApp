package maksim_huretski.sudoku.activities;

import android.graphics.Typeface;
import maksim_huretski.sudoku.R;
import maksim_huretski.sudoku.generation.InitialSudoku;
import maksim_huretski.sudoku.parts.Game;

public class NewGame extends Game {

    @Override
    protected void setInitialSudoku() {
        int difficulty = getIntent().getIntExtra(getString(R.string.difficultyLevel), R.id.normal);
        sudoku = new InitialSudoku().generateInitialSudoku(difficulty);
        setInitialStyle();
    }

    @Override
    protected void setInitialStyle(){
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (sudoku[i][j] != 0) {
                    cell = findViewById(CELLS[i][j]);
                    cell.setTypeface(null, Typeface.BOLD);
                    cell.setClickable(false);
                    cell.setFocusable(false);
                    isInitialSudoku[i][j] = true;
                }
            }
        }
    }

}
