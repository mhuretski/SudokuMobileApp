package maksim_huretski.sudoku.animation;

import android.app.Activity;
import android.os.Handler;
import android.widget.TextView;
import maksim_huretski.sudoku.R;

public class BoardAnimationCalc {

    TextView sudokuCell;
    private final int DELAY = 50;
    private final int DELAY_BEFORE_START = 1000;
    private final int LENGTH = 9;
    private final Handler handler = new Handler();

    @SuppressWarnings("SameReturnValue")
    public boolean setCellsShown(final Activity game, final int[][] CELLS, final int[][] sudoku) {
        firstAnimationFromLeftToRight(game, CELLS);
        secondAnimationFromRightToLeft(game, CELLS, sudoku);
        return false;
    }

    void firstAnimationFromLeftToRight(final Activity game, final int[][] CELLS) {
        for (int i = 0; i < LENGTH; i++) {
            final int a = i;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < LENGTH; j++) {
                        final int b = j;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setFirstData(game, CELLS, a, b);
                            }
                        }, DELAY * j);
                    }
                }
            }, DELAY_BEFORE_START);
        }
    }

    void secondAnimationFromRightToLeft(final Activity game, final int[][] CELLS, final int[][] sudoku) {
        int delayBeforeFirstFinished = DELAY_BEFORE_START + (LENGTH - 1) * DELAY;
        for (int i = LENGTH - 1; i >= 0; i--) {
            final int a = i;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    for (int j = LENGTH - 1; j >= 0; j--) {
                        int partialDelay = LENGTH - j;
                        final int b = j;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setSecondData(game, CELLS, sudoku, a, b);
                            }
                        }, DELAY * partialDelay);
                    }
                }
            }, delayBeforeFirstFinished);
        }
    }

    private void setFirstData(final Activity game, final int[][] CELLS, final int a, final int b) {
        sudokuCell = game.findViewById(CELLS[a][b]);
        sudokuCell.setText(R.string.vDefault);
        if (sudokuCell.getTag().equals(game.getResources().getString(R.string.light)))
            sudokuCell.setBackgroundResource(R.drawable.dark_block);
        else
            sudokuCell.setBackgroundResource(R.drawable.light_block);
    }

    void setSecondData(final Activity game, final int[][] CELLS, int[][] sudoku, final int a, final int b) {
        sudokuCell = game.findViewById(CELLS[a][b]);
        sudokuCell.setClickable(true);
        sudokuCell.setFocusable(true);
        if (sudokuCell.getTag().equals(game.getResources().getString(R.string.dark)))
            sudokuCell.setBackgroundResource(R.drawable.dark_block);
        else
            sudokuCell.setBackgroundResource(R.drawable.light_block);
    }

}
