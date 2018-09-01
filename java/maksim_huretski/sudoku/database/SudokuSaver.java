package maksim_huretski.sudoku.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SudokuSaver extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "sudokuDb";
    public static final String TABLE_PROGRESS = "progress";

    public static final String KEY_ID = "_id";
    public static final String KEY_INITIAL = "initial";
    public static final String KEY_SUDOKU = "sudoku";

    public SudokuSaver(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_PROGRESS
                + "(" + KEY_ID + " integer primary key,"
                + KEY_INITIAL + " integer,"
                + KEY_SUDOKU + " integer" + ")");
        init(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_PROGRESS);
        onCreate(db);
    }

    private void init(SQLiteDatabase db) {
        ContentValues contentValues;
        int ID = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++, ID++) {
                contentValues = new ContentValues();
                contentValues.put(SudokuSaver.KEY_ID, ID);
                contentValues.put(SudokuSaver.KEY_INITIAL, 0);
                contentValues.put(SudokuSaver.KEY_SUDOKU, 0);
                db.insert(SudokuSaver.TABLE_PROGRESS, null, contentValues);
            }
        }
    }

}
