package maksim_huretski.sudoku.activities;

import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import maksim_huretski.sudoku.R;
import maksim_huretski.sudoku.animation.BoardAppearanceAnimation;
import maksim_huretski.sudoku.calculation.Calc;
import maksim_huretski.sudoku.calculation.HintHelper;
import maksim_huretski.sudoku.generation.InitialSudoku;
import maksim_huretski.sudoku.parts.Screen;
import maksim_huretski.sudoku.validation.Checker;
import maksim_huretski.sudoku.validation.InputValidator;

public class NewGame extends Screen {

    private boolean isFirstTime = true;
    private BoardAppearanceAnimation board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        setContentView(R.layout.activity_new_game);
        setBlockIDs();
        board = new BoardAppearanceAnimation();
        board.setCellsHidden(this, CELLS);
        setInitialSudoku();
        super.possibleValues = findViewById(R.id.possibleValuesMain);
        super.possibleValues.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isFirstTime) isFirstTime = board.setCellsShown(this, CELLS, sudoku);
    }

    @SuppressWarnings("unused") /*It's used. The value is set in styles.*/
    public void onClickCell(View view) {
        if (isClickable) {
            if (!isCorrectSudoku) getDefaultBorderState();
            if (cell != null) {
                if (!highlighted || cell.getId() != view.getId() || !isCorrectSudoku) {
                    isCorrectSudoku = true;
                    highlighted = true;
                    normalStyle(cell.getId());
                    possibleValues.setVisibility(View.VISIBLE);
                    highlightedStyle(view.getId());
                } else {
                    highlighted = false;
                    normalStyle(cell.getId());
                    possibleValues.setVisibility(View.GONE);
                }
            } else {
                possibleValues.setVisibility(View.VISIBLE);
                highlightedStyle(view.getId());
                highlighted = true;
            }
        }
    }

    @SuppressWarnings("unused") /*It's used. The value is set in styles.*/
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
        HintHelper hintHelper = new HintHelper();
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
        sudoku = new InitialSudoku().generateInitialSudoku();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cell = findViewById(CELLS[i][j]);
                cell.setClickable(false);
                cell.setFocusable(false);
            }
        }
    }

}
