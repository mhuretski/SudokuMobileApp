package maksim_huretski.sudoku.animation;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Handler;
import android.widget.TextView;
import maksim_huretski.sudoku.R;

public class BoardAppearanceAnimation{

    private TextView sudokuCell;

    public void setCellsHidden(Activity game, int[][] CELLS) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sudokuCell = game.findViewById(CELLS[i][j]);
                sudokuCell.setBackgroundResource(R.drawable.nothing);
            }
        }
    }


    public boolean setCellsShown(final Activity game, final int[][] CELLS, final int[][] sudoku) {
        final int delay = 100;
        final int delayBeforeStart = 1500;
        final int length = 9;
        final int delayEnd = delayBeforeStart + 9 * delay * 2;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < length; i++) {
                    final int a = i;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            for (int j = 0; j < length; j++) {
                                final int b = j;
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        sudokuCell = game.findViewById(CELLS[a][b]);
                                        sudokuCell.setBackgroundResource(R.drawable.light_block);
                                        if (sudoku[a][b] != 0) {
                                            sudokuCell.setText(String.valueOf(sudoku[a][b]));
                                            sudokuCell.setTypeface(null, Typeface.BOLD);
                                            sudokuCell.setClickable(false);
                                            sudokuCell.setFocusable(false);
                                        }
                                    }
                                }, delay * j);
                            }
                        }
                    }, delay * i);
                }
            }
        }, delayBeforeStart);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        sudokuCell = game.findViewById(CELLS[i][j]);
                        if (sudoku[i][j] == 0) {
                            sudokuCell.setClickable(true);
                            sudokuCell.setFocusable(true);
                        }
                        if (sudokuCell.getTag().equals(game.getResources().getString(R.string.dark)))
                            sudokuCell.setBackgroundResource(R.drawable.dark_block);
                    }
                }
            }
        }, delayEnd);
        return false;
    }

}
