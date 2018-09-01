package maksim_huretski.sudoku.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import maksim_huretski.sudoku.database.SudokuSaver;
import maksim_huretski.sudoku.parts.Game;

public class ContinueGame extends Game {

    @Override
    protected void setInitialSudoku() {
        getDataFromDB();
        setInitialStyle();
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
                    isInitialSudoku[i][j] = true;
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

}
