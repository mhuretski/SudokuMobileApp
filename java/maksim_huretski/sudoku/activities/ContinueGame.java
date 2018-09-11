package maksim_huretski.sudoku.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import maksim_huretski.sudoku.R;
import maksim_huretski.sudoku.database.SudokuSaver;
import maksim_huretski.sudoku.containers.game.Game;

public class ContinueGame extends Game {

    public void onClickGameButton(View view) {
       finish();
    }

    @Override
    protected void setInitialSudoku() {
        getDataFromDB();
        setInitialStyle();
        getCurrentDifficulty();
    }

    @Override
    protected void setInitialStyle() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (isInitialSudoku[i][j]) {
                    cell = findViewById(CELLS[i][j]);
                    cell.setTypeface(null, Typeface.BOLD);
                    cell.setClickable(false);
                    cell.setFocusable(false);
                }
            }
        }
    }

    private void getDataFromDB() {
        SudokuSaver sudokuSaver = new SudokuSaver(this);
        SQLiteDatabase database = sudokuSaver.getReadableDatabase();
        Cursor cursor = database.query(SudokuSaver.TABLE_PROGRESS,
                null,
                null,
                null,
                null,
                null,
                SudokuSaver.KEY_ID);
        getData(sudokuSaver, database, cursor);
    }

    private void getData(SudokuSaver sudokuSaver, SQLiteDatabase database, Cursor cursor) {
        if (cursor.moveToFirst()) {
            int isInitialIndex = cursor.getColumnIndex(SudokuSaver.KEY_INITIAL);
            int sudokuIndex = cursor.getColumnIndex(SudokuSaver.KEY_SUDOKU);

            int i = 0, j = 0;
            do {
                sudoku[i][j] = cursor.getInt(sudokuIndex);
                isInitialSudoku[i][j] = cursor.getInt(isInitialIndex) == 1;
                if (++j == 9) {
                    i++;
                    j = 0;
                }
            } while (cursor.moveToNext() && i < 9);
        }
        cursor.close();
        database.close();
        sudokuSaver.close();
    }

    protected void showGameButton(){
        Button mainMenu = findViewById(R.id.gameButton);
        mainMenu.setText(R.string.mainMenu);
        mainMenu.setVisibility(View.VISIBLE);
    }

    private void getCurrentDifficulty(){
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.savedGame), Context.MODE_PRIVATE);
        difficulty = sharedPref.getInt(getString(R.string.savedProgressDifficulty), R.id.normal);
    }

}
