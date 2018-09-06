package maksim_huretski.sudoku.parts.game;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import maksim_huretski.sudoku.R;
import maksim_huretski.sudoku.calculation.Calc;
import maksim_huretski.sudoku.calculation.HintHelper;
import maksim_huretski.sudoku.database.SudokuSaver;
import maksim_huretski.sudoku.calculation.validation.Checker;
import maksim_huretski.sudoku.calculation.validation.InputValidator;

@SuppressWarnings("unused")
public abstract class Game extends Screen {

    protected int difficulty;
    private final HintHelper hintHelper = new HintHelper();
    protected final boolean[][] isInitialSudoku = new boolean[9][9];
/* TODO uncomment when hints are refactored
    private final int[] possibleValuesNumbers = new int[]
            {R.id.vZero, R.id.v0, R.id.v1, R.id.v2, R.id.v3, R.id.v4, R.id.v5, R.id.v6, R.id.v7, R.id.v8};
    private int[][][][] sudokuPossibleValues = new int[9][9][][];
    private TextView possibleValue;*/

    protected abstract void setInitialSudoku();

    @SuppressWarnings("unused")
    protected abstract void setInitialStyle();

    protected abstract void showGameButton();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideGameButton();
        setInitialSudoku();
        showAnimatedValues(sudoku);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveProgress();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!isSolved) updateDataInDB();
    }

    @Override
    public void onClickCell(View view) {
        if (isClickable) {
            if (!isCorrectSudoku) getDefaultBorderState();
            if (cell != null) {
                if (!highlighted || cell.getId() != view.getId() || !isCorrectSudoku) {
                    isCorrectSudoku = true;
                    highlighted = true;
                    normalStyle(cell.getId());
                    possibleValues.setVisibility(View.VISIBLE);
/* TODO uncomment when hints are refactored
                    setPossibleValuesNumbers();*/
                    highlightedStyle(view.getId());
                } else {
                    highlighted = false;
                    normalStyle(cell.getId());
                    possibleValues.setVisibility(View.INVISIBLE);
                }
            } else {
                possibleValues.setVisibility(View.VISIBLE);
/* TODO uncomment when hints are refactored
                setPossibleValuesNumbers();*/
                highlightedStyle(view.getId());
                highlighted = true;
            }
        }
    }

/* TODO uncomment when hints are refactored
    private void setPossibleValuesNumbers() {
        for (int k = 0; k < 10; k++) {
                possibleValue = findViewById(possibleValuesNumbers[k]);
                possibleValue.setVisibility(View.VISIBLE);
        }
        hintHelper.init(sudoku, blockIDs);
        hintHelper.calculateSudoku();
        sudokuPossibleValues = hintHelper.getSudokuPossibleValues();
        int position = cell.getId();
        int row = 0;
        int column = 0;
        label:
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (CELLS[i][j] == position) {
                    row = i;
                    column = j;
                    break label;
                }
            }
        }
        for (int k = 0; k < 9; k++) {
            if (sudokuPossibleValues[row][column][1][k] % 2 != 0) {
                possibleValue = findViewById(possibleValuesNumbers[k + 1]);
                possibleValue.setVisibility(View.INVISIBLE);
            }
        }
    }*/

    @Override
    public void onClickValue(View view) {
        normalStyle(cell.getId());
        TextView value = findViewById(view.getId());
        String cellText = value.getText().toString();
        if (!cellText.equals("0")) {
            cell.setText(cellText);
        } else {
            cell.setText(R.string.vDefault);
        }
        highlighted = false;
        possibleValues.setVisibility(View.GONE);
        verify();
    }

    private void verify() {
        if (highlighted) {
            highlighted = false;
            normalStyle(cell.getId());
            possibleValues.setVisibility(View.GONE);
        }
        getUserValues();
        hintHelper.init(sudoku, blockIDs);
        InputValidator iv = new InputValidator();
        iv.init(blockIDs);
        iv.setSudoku(sudoku);
        isCorrectSudoku = iv.checkInput();
        if (isCorrectSudoku) findSolution(hintHelper, iv);
        else highlightIncorrectBlocks(iv);
    }

    @Override
    protected void findSolution(Calc calc, InputValidator iv) {
        calc.calculateSudoku();
        iv.setSudoku(calc.getSudoku());
        if (!iv.checkInput()) {
            isSolved = false;
            isCorrectSudoku = false;
            highlighted = true;
            ((TextView) findViewById(R.id.messageAtTop)).setText(R.string.invalidSudoku);
        } else {
            Checker checker = new Checker();
            checker.checkSudoku(sudoku, blockIDs);
            if (checker.isDone()) {
                ((TextView) findViewById(R.id.messageAtTop)).setText(R.string.congratulations);
                showGameButton();
                isSolved = true;
                isClickable = false;
                updateStatistics();
            }
        }
    }

    private void updateDataInDB() {
        SudokuSaver sudokuSaver = new SudokuSaver(this);
        SQLiteDatabase database = sudokuSaver.getWritableDatabase();
        ContentValues contentValues;
        int ID = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                contentValues = new ContentValues();
                int isInitial;
                if (isInitialSudoku[i][j]) isInitial = 1;
                else isInitial = 0;
                contentValues.put(SudokuSaver.KEY_INITIAL, isInitial);
                contentValues.put(SudokuSaver.KEY_SUDOKU, sudoku[i][j]);
                database.update(SudokuSaver.TABLE_PROGRESS, contentValues, "_id=" + ID, null);
                ID++;
            }
        }
        database.close();
        sudokuSaver.close();
    }

    private void saveProgress() {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.savedGame), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.savedGame), isSolved);
        editor.putInt(getString(R.string.savedProgressDifficulty), difficulty);
        editor.apply();
    }

    protected void hideGameButton() {
        Button newGame = findViewById(R.id.gameButton);
        newGame.setVisibility(View.GONE);
    }

    private void updateStatistics() {
        SudokuSaver statistics = new SudokuSaver(this);
        SQLiteDatabase database = statistics.getWritableDatabase();

        Cursor cursor = database.query(
                SudokuSaver.TABLE_STATS,
                new String[]{SudokuSaver.KEY_SOLVED},
                SudokuSaver.KEY_ID + " = ?",
                new String[]{String.valueOf(difficulty)},
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            int result = cursor.getInt(cursor.getColumnIndex(SudokuSaver.KEY_SOLVED)) + 1;
            ContentValues contentValues = new ContentValues();
            contentValues.put(SudokuSaver.KEY_SOLVED, result);
            database.update(SudokuSaver.TABLE_STATS, contentValues, "_id=" + difficulty, null);
        }
        cursor.close();
        database.close();
        statistics.close();
    }

}
