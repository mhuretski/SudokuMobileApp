package maksim_huretski.sudoku.animation.board;

import android.app.Activity;
import maksim_huretski.sudoku.R;

public class AnimContinue extends AnimNewGame{

    @Override
    void setSecondData(final Activity game, final int[][] CELLS, int[][] sudoku, final int a, final int b) {
        sudokuCell = game.findViewById(CELLS[a][b]);
        if (sudoku[a][b] != 0)
            sudokuCell.setText(sudokuValues[a][b]);
        if (sudokuCell.getTag().equals(game.getResources().getString(R.string.dark)))
            sudokuCell.setBackgroundResource(R.drawable.dark_block);
        else
            sudokuCell.setBackgroundResource(R.drawable.light_block);
    }

}
