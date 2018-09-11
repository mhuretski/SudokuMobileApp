package maksim_huretski.sudoku.animation.board;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import maksim_huretski.sudoku.R;

public class AnimCalc {

    TextView sudokuCell;
    int delayBeforeStart = 100;
    final int delay = 50;
    private final int LENGTH = 9;
    final int delayUntilPreviousFinished = (LENGTH - 1) * delay;
    final String[][] sudokuValues = new String[9][9];
    final Handler handler = new Handler();

    public void setCellsShown(final Activity game, final int[][] CELLS, final int[][] sudoku) {
        sudokuToString(sudoku);
        animate(game, CELLS, sudoku);
        congratulationsMessage(game);
    }

    void animate(final Activity game, final int[][] CELLS, final int[][] sudoku){
        sudokuToString(sudoku);
        firstAnimationFromLeftToRight(game, CELLS, sudoku);
    }

    private void firstAnimationFromLeftToRight(final Activity game, final int[][] CELLS, final int[][] sudoku) {
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
                        }, delay * j);
                    }
                    secondAnimationFromRightToLeft(game, CELLS, sudoku);
                }
            }, delayBeforeStart);
        }
    }

    private void secondAnimationFromRightToLeft(final Activity game, final int[][] CELLS, final int[][] sudoku) {
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
                        }, delay * partialDelay);
                    }
                }
            }, delayUntilPreviousFinished);
        }
    }

    void setFirstData(final Activity game, final int[][] CELLS, final int a, final int b) {
        sudokuCell = game.findViewById(CELLS[a][b]);
        if (sudokuCell.getTag().equals(game.getResources().getString(R.string.light)))
            sudokuCell.setBackgroundResource(R.drawable.dark_block);
        else
            sudokuCell.setBackgroundResource(R.drawable.light_block);
    }

    void setSecondData(final Activity game, final int[][] CELLS, int[][] sudoku, final int a, final int b) {
        sudokuCell = game.findViewById(CELLS[a][b]);
        sudokuCell.setText(sudokuValues[a][b]);
        if (sudokuCell.getTag().equals(game.getResources().getString(R.string.dark)))
            sudokuCell.setBackgroundResource(R.drawable.dark_block);
        else
            sudokuCell.setBackgroundResource(R.drawable.light_block);
    }

    private void congratulationsMessage(final Activity game) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TextView messageAtTop = game.findViewById(R.id.messageAtTop);
                messageAtTop.setText(R.string.congratulations);
                messageAtTop.setVisibility(View.VISIBLE);
                ((Button) game.findViewById(R.id.gameButton)).setText(R.string.reset);
            }
        }, delayUntilPreviousFinished + delay * 10 + delayBeforeStart);
    }

    private void sudokuToString(final int[][] sudoku) {
        for (int i = 0; i < sudoku.length; i++) {
            for (int j = 0; j < sudoku[i].length; j++) {
                sudokuValues[i][j] = String.valueOf(sudoku[i][j]);
            }
        }
    }

}
