package maksim_huretski.sudoku.activities;

import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import maksim_huretski.sudoku.R;
import maksim_huretski.sudoku.animation.board.AnimGame;
import maksim_huretski.sudoku.generation.InitialSudoku;
import maksim_huretski.sudoku.parts.game.Game;

public class NewGame extends Game {

    @Override
    protected void setInitialSudoku() {
        difficulty = getIntent().getIntExtra(getString(R.string.difficultyLevel), R.id.normal);
        sudoku = new InitialSudoku().generateInitialSudoku(difficulty);
        setInitialStyle();
    }

    @Override
    protected void setInitialStyle() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cell = findViewById(CELLS[i][j]);
                if (sudoku[i][j] != 0) {
                    cell.setClickable(false);
                    cell.setFocusable(false);
                    cell.setTypeface(null, Typeface.BOLD);
                    isInitialSudoku[i][j] = true;
                }
            }
        }
    }

    private void setNextGameInitialStyle() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cell = findViewById(CELLS[i][j]);
                cell.setText(R.string.vDefault);
                if (sudoku[i][j] != 0) {
                    cell.setClickable(false);
                    cell.setFocusable(false);
                    cell.setTypeface(null, Typeface.BOLD);
                    isInitialSudoku[i][j] = true;
                } else {
                    cell.setClickable(true);
                    cell.setFocusable(true);
                    cell.setTypeface(null, Typeface.NORMAL);
                    isInitialSudoku[i][j] = false;
                }
            }
        }
    }

    public void onClickGameButton(View view) {
        hideGameButton();
        int[][] tempSudoku = new InitialSudoku().generateInitialSudoku(difficulty);
        setNextGameInitialStyle();
        new AnimGame().setCellsShown(this, CELLS, tempSudoku, 300);
        super.sudoku = tempSudoku;
        isSolved = false;
        isClickable = true;
        ((TextView) findViewById(R.id.messageAtTop)).setText(R.string.vDefault);
    }

    protected void showGameButton() {
        Button newGame = findViewById(R.id.gameButton);
        newGame.setText(R.string.newGame);
        newGame.setVisibility(View.VISIBLE);
    }

    protected void showAnimatedValues(int[][] sudoku) {
        new AnimGame().setCellsShown(this, CELLS, sudoku);
    }

}
