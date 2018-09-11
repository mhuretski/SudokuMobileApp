package maksim_huretski.sudoku.animation.board;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import maksim_huretski.sudoku.R;

public class AnimGame extends AnimCalc {

    public void setCellsShown(final Activity game, final int[][] CELLS, final int[][] sudoku, int timing) {
        super.delayBeforeStart = timing;
        setHintMenuUnClickable(game);
        animate(game, CELLS, sudoku);
        setHintMenuVisible(game);
    }

    @Override
    public void setCellsShown(final Activity game, final int[][] CELLS, final int[][] sudoku) {
        super.delayBeforeStart = 1000;
        animate(game, CELLS, sudoku);
        setHintMenuVisible(game);
    }

    @Override
    void setFirstData(final Activity game, final int[][] CELLS, final int a, final int b) {
        sudokuCell = game.findViewById(CELLS[a][b]);
        if (sudokuCell.getTag().equals(game.getResources().getString(R.string.light)))
            sudokuCell.setBackgroundResource(R.drawable.dark_block);
        else
            sudokuCell.setBackgroundResource(R.drawable.light_block);
    }

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

    public void setHintMenuUnClickable(Activity game){
        ImageView hintsMenu = game.findViewById(R.id.hintMenuBtn);
        hintsMenu.setFocusable(false);
        hintsMenu.setClickable(false);
        hintsMenu.setVisibility(View.VISIBLE);
    }

    private void setHintMenuVisible(final Activity game) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ImageView hintsMenu = game.findViewById(R.id.hintMenuBtn);
                hintsMenu.setFocusable(true);
                hintsMenu.setClickable(true);
                hintsMenu.setVisibility(View.VISIBLE);
            }
        }, delayUntilPreviousFinished + delay * 10 + delayBeforeStart);
    }

}
