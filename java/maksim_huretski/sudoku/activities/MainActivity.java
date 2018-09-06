package maksim_huretski.sudoku.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import maksim_huretski.sudoku.R;
import maksim_huretski.sudoku.animation.menu.MenuActions;
import maksim_huretski.sudoku.database.SudokuSaver;

public class MainActivity extends MenuActions implements View.OnClickListener {

    private int difficultyLevel;
    private final int[][] difficulties = new int[][]{{R.id.insane, R.id.insaneStat},
            {R.id.hard, R.id.hardStat},
            {R.id.normal, R.id.normalStat},
            {R.id.easy, R.id.easyStat}};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        setContentView(R.layout.activity_main);
        setMenuAnimation();
        getDifficulty();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isSavedProgress();
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.newGame:
                intent = new Intent(this, NewGame.class);
                intent.putExtra(getString(R.string.difficultyLevel), difficultyLevel);
                startActivity(intent);
                break;
            case R.id.continueGame:
                intent = new Intent(this, ContinueGame.class);
                startActivity(intent);
                break;
            case R.id.sudokuSolver:
                intent = new Intent(this, Calculator.class);
                startActivity(intent);
                break;
            case R.id.difficulty:
                highlightCurrentDifficulty();
                hideMenu();
                showDifficulty();
                break;
            case R.id.insane:
            case R.id.hard:
            case R.id.normal:
            case R.id.easy:
                setDifficulty(view);
                isSavedProgress();
                hideDifficulty();
                showMenu();
                break;
            case R.id.statistics:
                getStatsFromDB();
                hideMenu();
                showStats();
                break;
            case R.id.back:
                showMenu();
                hideStats();
                break;
            default:
                break;
        }
    }

    private void setDifficulty(View view) {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        int level = view.getId();
        editor.putInt(getString(R.string.difficultyLevel), level);
        editor.apply();
        difficultyLevel = sharedPref.getInt(getString(R.string.difficultyLevel), R.id.normal);
    }

    private void getDifficulty() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        difficultyLevel = sharedPref.getInt(getString(R.string.difficultyLevel), R.id.normal);
    }

    private void isSavedProgress() {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.savedGame), Context.MODE_PRIVATE);
        boolean isSolved = sharedPref.getBoolean(getString(R.string.savedGame), true);
        int savedDifficulty = sharedPref.getInt(getString(R.string.savedProgressDifficulty), R.id.normal);
        if (!isSolved && savedDifficulty == difficultyLevel) showContinue();
        else hideContinue();
    }

    private void highlightCurrentDifficulty() {
        TextView difText;
        for (int difficulty[] : difficulties) {
            difText = findViewById(difficulty[0]);
            if (this.difficultyLevel == difficulty[0]) {
                difText.setTextColor(getResources().getColor(R.color.difficulty));
            } else
                difText.setTextColor(getResources().getColor(R.color.black));
        }
    }

    private void getStatsFromDB() {
        SudokuSaver sudokuSaver = new SudokuSaver(this);
        SQLiteDatabase database = sudokuSaver.getReadableDatabase();
        Cursor cursor = database.query(SudokuSaver.TABLE_STATS,
                null,
                null,
                null,
                null,
                null,
                null);
        getData(sudokuSaver, database, cursor);
    }

    private void getData(SudokuSaver sudokuSaver, SQLiteDatabase database, Cursor cursor) {
        TextView stats;
        if (cursor.moveToFirst()) {
            int id = cursor.getColumnIndex(SudokuSaver.KEY_ID);
            int timesSolved = cursor.getColumnIndex(SudokuSaver.KEY_SOLVED);
            do {
                for (int difficulty[] : difficulties) {
                    if (difficulty[0] == cursor.getInt(id)){
                        stats = findViewById(difficulty[1]);
                        stats.setText(String.valueOf(cursor.getInt(timesSolved)));
                        break;
                    }
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        sudokuSaver.close();
    }

}
