package maksim_huretski.sudoku.animation;

import android.app.Activity;
import maksim_huretski.sudoku.R;

public class BoardAnimationNewGame extends BoardAnimationCalc {

    @Override
    public boolean setCellsShown(final Activity game, final int[][] CELLS, final int[][] sudoku) {
        super.delayBeforeStart = 1100;
        sudokuToString(sudoku);
        firstAnimationFromLeftToRight(game, CELLS, sudoku);
        return false;
    }

    @Override
    void setFirstData(final Activity game, final int[][] CELLS, final int a, final int b) {
        sudokuCell = game.findViewById(CELLS[a][b]);
        sudokuCell.setText(R.string.vDefault);
        if (sudokuCell.getTag().equals(game.getResources().getString(R.string.light)))
            sudokuCell.setBackgroundResource(R.drawable.dark_block);
        else
            sudokuCell.setBackgroundResource(R.drawable.light_block);
    }

    @Override
    void setSecondData(final Activity game, final int[][] CELLS, int[][] sudoku, final int a, final int b) {
        sudokuCell = game.findViewById(CELLS[a][b]);
        if (sudoku[a][b] != 0) {
            sudokuCell.setText(sudokuValues[a][b]);
        }
        if (sudokuCell.getTag().equals(game.getResources().getString(R.string.dark)))
            sudokuCell.setBackgroundResource(R.drawable.dark_block);
        else
            sudokuCell.setBackgroundResource(R.drawable.light_block);
    }

}
