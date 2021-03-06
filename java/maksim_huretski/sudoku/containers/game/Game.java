package maksim_huretski.sudoku.containers.game;

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
import maksim_huretski.sudoku.animation.board.AnimGame;
import maksim_huretski.sudoku.animation.board.AnimHints;
import maksim_huretski.sudoku.calculation.Calc;
import maksim_huretski.sudoku.calculation.HintHelper;
import maksim_huretski.sudoku.calculation.hint_types.PossibleValuesMinimizer;
import maksim_huretski.sudoku.calculation.validation.Checker;
import maksim_huretski.sudoku.calculation.validation.InputValidator;
import maksim_huretski.sudoku.containers.menu.HintSettingsDialog;
import maksim_huretski.sudoku.database.SudokuSaver;

public abstract class Game extends Screen {

    protected int difficulty;
    private final HintHelper hintHelper = new HintHelper();
    private final AnimGame animGame = new AnimGame();
    protected final boolean[][] isInitialSudoku = new boolean[9][9];
    private boolean hintRows;
    private boolean hintColumns;
    private boolean hintBlocks;
    private boolean hintPosValues;
    private boolean hintCalc;
    private final int[] possibleValuesNumbers = new int[]
            {R.id.vZero, R.id.v0, R.id.v1, R.id.v2, R.id.v3, R.id.v4, R.id.v5, R.id.v6, R.id.v7, R.id.v8};
    private TextView possibleValue;

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
        setHintMenu();
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
                    if (hintPosValues) setPossibleValuesNumbers(view);
                    highlightedStyle(view.getId());
                } else {
                    highlighted = false;
                    normalStyle(cell.getId());
                    possibleValues.setVisibility(View.INVISIBLE);
                }
            } else {
                possibleValues.setVisibility(View.VISIBLE);
                if (hintPosValues) setPossibleValuesNumbers(view);
                highlightedStyle(view.getId());
                highlighted = true;
            }
        }
    }

    private void setPossibleValuesNumbers(View view) {
        int position = view.getId();
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
        if (sudoku[row][column] == 0) {
            setDefaultPossibleValuesVisibility();
            int[][][][] sudokuPossibleValues = new PossibleValuesMinimizer()
                    .init(sudoku)
                    .minimize();
            for (int k = 0; k < 9; k++) {
                if (sudokuPossibleValues[row][column][1][k] == 0) {
                    possibleValue = findViewById(possibleValuesNumbers[k + 1]);
                    possibleValue.setVisibility(View.INVISIBLE);
                }
            }
        } else {
            for (int k = 1; k < 10; k++) {
                possibleValue = findViewById(possibleValuesNumbers[k]);
                possibleValue.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void setDefaultPossibleValuesVisibility() {
        for (int k = 1; k < 10; k++) {
            possibleValue = findViewById(possibleValuesNumbers[k]);
            possibleValue.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClickValue(View view) {
        normalStyle(cell.getId());
        TextView value = findViewById(view.getId());
        String cellText = value.getText().toString();
        if (!cellText.equals(zero)) {
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
        isCorrectSudoku = iv.isCorrectInput();
        boolean isFilled = new Checker().isFilledSudoku(sudoku);
        if (isCorrectSudoku) {
            if (isFilled || hintCalc) findSolution(hintHelper, iv);
        } else highlightIncorrectValues(iv, isFilled);
    }

    private void highlightIncorrectValues(InputValidator iv, boolean isFilled) {
        boolean noHighlightedHints = !hintRows && !hintColumns && !hintBlocks;
        if ((isFilled || hintCalc) && noHighlightedHints)
            invalidInputMessage();
        else {
            boolean isHighlighted = false;
            if (iv.getIncorrectRows().size() != 0 && hintRows) {
                highLightIncorrectRow(iv);
                isHighlighted = true;
            }
            if (iv.getIncorrectColumns().size() != 0 && hintColumns) {
                highLightIncorrectColumn(iv);
                isHighlighted = true;
            }
            if (iv.getIncorrectBlocks().size() != 0 && hintBlocks) {
                highLightIncorrectBlock(iv);
                isHighlighted = true;
            }
            if (isHighlighted) {
                highlighted = true;
                invalidInputMessage();
            }
        }
    }

    @Override
    protected void findSolution(Calc calc, InputValidator iv) {
        calc.calculateSudoku();
        iv.setSudoku(calc.getSudoku());
        if (!iv.isCorrectInput()) {
            isSolved = false;
            isCorrectSudoku = false;
            highlighted = true;
            invalidSudokuMessage();
        } else {
            Checker checker = new Checker();
            checker.checkSudoku(sudoku, blockIDs);
            if (checker.isDone()) {
                congratulationsMessage();
                showGameButton();
                isSolved = true;
                isClickable = false;
                animGame.setHintMenuUnClickable(this);
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

    protected void showAnimatedValues(int[][] sudoku) {
        animGame.setCellsShown(this, CELLS, sudoku);
    }

    private void setHintMenu() {
        final HintSettingsDialog hints = new HintSettingsDialog(this);
        new AnimHints(this).setHintsMenu(hints);
    }

    public void setHintRows(boolean hintRows) {
        this.hintRows = hintRows;
    }

    public void setHintColumns(boolean hintColumns) {
        this.hintColumns = hintColumns;
    }

    public void setHintBlocks(boolean hintBlocks) {
        this.hintBlocks = hintBlocks;
    }

    public void setHintPosValues(boolean hintPosValues) {
        this.hintPosValues = hintPosValues;
    }

    public void setHintCalc(boolean hintCalc) {
        this.hintCalc = hintCalc;
    }

}
