package maksim_huretski.sudoku.animation;

import android.app.Activity;
import android.graphics.Typeface;
import maksim_huretski.sudoku.R;

public class BoardAnimationNewGame extends BoardAnimationCalc{

    @Override
    public boolean setCellsShown(final Activity game, final int[][] CELLS, final int[][] sudoku) {
        firstAnimationFromLeftToRight(game, CELLS);
        secondAnimationFromRightToLeft(game, CELLS, sudoku);
        return false;
    }

    @Override
    void setSecondData(final Activity game, final int[][] CELLS, int[][] sudoku, final int a, final int b) {
        sudokuCell = game.findViewById(CELLS[a][b]);
        if (sudoku[a][b] == 0) {
            sudokuCell.setClickable(true);
            sudokuCell.setFocusable(true);
        } else {
            sudokuCell.setTypeface(null, Typeface.BOLD);
            sudokuCell.setText(String.valueOf(sudoku[a][b]));
        }
        if (sudokuCell.getTag().equals(game.getResources().getString(R.string.dark)))
            sudokuCell.setBackgroundResource(R.drawable.dark_block);
        else
            sudokuCell.setBackgroundResource(R.drawable.light_block);
    }

}
