package maksim_huretski.sudoku.activities;

import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import maksim_huretski.sudoku.R;
import maksim_huretski.sudoku.animation.BoardAnimationNewGame;
import maksim_huretski.sudoku.calculation.Calc;
import maksim_huretski.sudoku.calculation.HintHelper;
import maksim_huretski.sudoku.generation.InitialSudoku;
import maksim_huretski.sudoku.parts.Screen;
import maksim_huretski.sudoku.validation.Checker;
import maksim_huretski.sudoku.validation.InputValidator;

public class NewGame extends Screen {

    private final HintHelper hintHelper = new HintHelper();
    private int difficulty;
/* TODO uncomment when hints are refactored
    private final int[] possibleValuesNumbers = new int[]
            {R.id.vZero, R.id.v0, R.id.v1, R.id.v2, R.id.v3, R.id.v4, R.id.v5, R.id.v6, R.id.v7, R.id.v8};
    private int[][][][] sudokuPossibleValues = new int[9][9][][];
    private TextView possibleValue;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        setContentView(R.layout.activity_new_game);
        setBlockIDs();
        difficulty = getIntent().getIntExtra(getString(R.string.difficultyLevel), R.id.normal);
        setInitialSudoku();
        super.possibleValues = findViewById(R.id.possibleValuesMain);
        super.possibleValues.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFirstTime)
            isFirstTime = new BoardAnimationNewGame().setCellsShown(this, CELLS, sudoku);
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
            System.out.println(sudokuPossibleValues[row][column][1][k]);
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
                isSolved = true;
                isClickable = false;
            }
        }
    }

    private void setInitialSudoku() {
        sudoku = new InitialSudoku().generateInitialSudoku(difficulty);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (sudoku[i][j] != 0) {
                    cell = findViewById(CELLS[i][j]);
                    cell.setTypeface(null, Typeface.BOLD);
                    cell.setClickable(false);
                    cell.setFocusable(false);
                }
            }
        }
    }

}
