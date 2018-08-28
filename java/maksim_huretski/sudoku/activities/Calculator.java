package maksim_huretski.sudoku.activities;

import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import maksim_huretski.sudoku.R;
import maksim_huretski.sudoku.calculation.Calc;
import maksim_huretski.sudoku.generation.Solver;
import maksim_huretski.sudoku.parts.Screen;
import maksim_huretski.sudoku.validation.InputValidator;

public class Calculator extends Screen {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        setContentView(R.layout.activity_calculator);
        setBlockIDs();
        super.possibleValues = findViewById(R.id.possibleValuesMain);
        super.possibleValues.setVisibility(View.GONE);
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
            cell.setTypeface(null, Typeface.BOLD);
            cell.setText(cellText);
        } else {
            cell.setText(R.string.vDefault);
            cell.setTypeface(null, Typeface.NORMAL);
        }
        highlighted = false;
        possibleValues.setVisibility(View.GONE);
    }

    @SuppressWarnings("unused") /*It's used. The value is set in styles.*/
    public void onClickCalculate(View view) {
        if (!isSolved) {
            /*UI start*/
            if (highlighted) {
                highlighted = false;
                normalStyle(cell.getId());
                possibleValues.setVisibility(View.GONE);
            }
            /*UI end*/
            /*Logic start*/
            getUserValues();
            Solver solver = new Solver();
            solver.init(sudoku, blockIDs);
            InputValidator iv = new InputValidator();
            iv.init(blockIDs);
            iv.setSudoku(sudoku);
            isCorrectSudoku = iv.checkInput();
            /*Logic end*/
            if (isCorrectSudoku) findSolution(solver, iv);
                /*UI start*/
            else highlightIncorrectBlocks(iv);
        } else resetSudoku();
        /*UI end*/
    }

    @Override
    protected void findSolution(Calc calc, InputValidator iv) {
        /*Logic start*/
        boolean isValid = calc.calculateSudoku();
        iv.setSudoku(calc.getSudoku());
        if (iv.checkInput() && isValid) {
            sudoku = calc.getSudoku();
            isDone();
            /*Logic end*/
            /*UI start*/
            showSolution();
            /*UI end*/
        } else {
            /*UI start*/
            isSolved = false;
            isCorrectSudoku = false;
            highlighted = true;
            ((TextView) findViewById(R.id.messageAtTop)).setText(R.string.invalidSudoku);
            /*UI end*/
        }
    }

}
