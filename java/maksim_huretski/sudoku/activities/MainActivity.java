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
    private boolean isMainMenu = true;
    private final int[][] difficulties = new int[][]{
            {R.id.insane, R.id.insaneStat, R.integer.insaneD},
            {R.id.hard, R.id.hardStat, R.integer.hardD},
            {R.id.normal, R.id.normalStat, R.integer.normalD},
            {R.id.easy, R.id.easyStat, R.integer.easyD}};

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
    public void onBackPressed() {
        if (isMainMenu) super.onBackPressed();
        else {
            hideStats();
            hideDifficulty();
            showMenu();
            isMainMenu = true;
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.newGame:
                if (difficultyLevel == 0)
                    setDifficulty(findViewById(R.id.normal));
                intent = new Intent(this, NewGame.class);
                intent.putExtra(getString(R.string.difficultyLevel), difficultyLevel);
                startActivity(intent, view);
                break;
            case R.id.continueGame:
                intent = new Intent(this, ContinueGame.class);
                startActivity(intent, view);
                break;
            case R.id.sudokuSolver:
                intent = new Intent(this, Calculator.class);
                startActivity(intent);
                break;
            case R.id.difficulty:
                highlightCurrentDifficulty();
                hideMenu();
                showDifficulty();
                isMainMenu = false;
                break;
            case R.id.insane:
            case R.id.hard:
            case R.id.normal:
            case R.id.easy:
                setDifficulty(view);
                isSavedProgress();
                hideDifficulty();
                showMenu();
                isMainMenu = true;
                break;
            case R.id.statistics:
                getStatsFromDB();
                hideMenu();
                showStats();
                isMainMenu = false;
                break;
            case R.id.back:
                showMenu();
                hideStats();
                isMainMenu = true;
                break;
            default:
                break;
        }
    }

    private void setDifficulty(View view) {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        int level = getDifId(view);
        editor.putInt(getString(R.string.difficultyLevel), level);
        editor.apply();
        difficultyLevel = sharedPref.getInt(getString(R.string.difficultyLevel), R.integer.normalD);
    }

    private int getDifId(View view) {
        int chosenBtn = view.getId();
        for (int[] difficulty : difficulties) {
            if (difficulty[0] == chosenBtn) {
                return difficulty[2];
            }
        }
        return R.integer.normalD;
    }

    private void getDifficulty() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        if (sharedPref.contains(getString(R.string.difficultyLevel))) {
            difficultyLevel = sharedPref.getInt(getString(R.string.difficultyLevel), R.integer.normalD);
            boolean isValid = false;
            for (int difficulty[] : difficulties) {
                if (difficultyLevel == difficulty[2]) {
                    isValid = true;
                    break;
                }
            }
            if (!isValid) difficultyLevel = 0;
        } else difficultyLevel = 0;
    }

    private void isSavedProgress() {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.savedGame), Context.MODE_PRIVATE);
        boolean isSolved = sharedPref.getBoolean(getString(R.string.savedGame), true);
        int savedDifficulty = sharedPref.getInt(getString(R.string.savedProgressDifficulty), R.integer.normalD);
        if (!isSolved && savedDifficulty == difficultyLevel) showContinue();
        else hideContinue();
    }

    private void highlightCurrentDifficulty() {
        TextView difText;
        for (int difficulty[] : difficulties) {
            difText = findViewById(difficulty[0]);
            if (this.difficultyLevel == difficulty[2]) {
                difText.setTextColor(getResources().getColor(R.color.white));
                difText.setBackgroundResource(R.drawable.chosen_dif_button);
            } else {
                difText.setTextColor(getResources().getColor(R.color.black));
                difText.setBackgroundResource(R.drawable.calculate_button);
            }
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
                    if (difficulty[2] == cursor.getInt(id)) {
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
