package maksim_huretski.sudoku.logic;

import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import maksim_huretski.sudoku.R;

import java.util.Set;

public abstract class SudokuWindow extends AppCompatActivity {

    private TextView cell;
    protected View possibleValues;
    private GradientDrawable drawable;
    private boolean isCorrectUserInput = true;
    private int[][] sudoku = new int[9][9];
    private int[][] blockIdentifications;
    private boolean isSolved = false;
    private boolean isClickable = true;
    private static final int[][] CELLS = new int[][]{
            {R.id.b00, R.id.b01, R.id.b02, R.id.b03, R.id.b04, R.id.b05, R.id.b06, R.id.b07, R.id.b08},
            {R.id.b10, R.id.b11, R.id.b12, R.id.b13, R.id.b14, R.id.b15, R.id.b16, R.id.b17, R.id.b18},
            {R.id.b20, R.id.b21, R.id.b22, R.id.b23, R.id.b24, R.id.b25, R.id.b26, R.id.b27, R.id.b28},
            {R.id.b30, R.id.b31, R.id.b32, R.id.b33, R.id.b34, R.id.b35, R.id.b36, R.id.b37, R.id.b38},
            {R.id.b40, R.id.b41, R.id.b42, R.id.b43, R.id.b44, R.id.b45, R.id.b46, R.id.b47, R.id.b48},
            {R.id.b50, R.id.b51, R.id.b52, R.id.b53, R.id.b54, R.id.b55, R.id.b56, R.id.b57, R.id.b58},
            {R.id.b60, R.id.b61, R.id.b62, R.id.b63, R.id.b64, R.id.b65, R.id.b66, R.id.b67, R.id.b68},
            {R.id.b70, R.id.b71, R.id.b72, R.id.b73, R.id.b74, R.id.b75, R.id.b76, R.id.b77, R.id.b78},
            {R.id.b80, R.id.b81, R.id.b82, R.id.b83, R.id.b84, R.id.b85, R.id.b86, R.id.b87, R.id.b88}
    };

    public void onClickCell(View view) {
        if (!isCorrectUserInput) getDefaultBorderState();
        if (drawable != null) normalStyle(cell.getId());
        possibleValues.setVisibility(View.VISIBLE);
        highlightedStyle(view.getId());
    }

    public void onClickValue(View view) {
        if (isClickable) {
            TextView value = findViewById(view.getId());
            String cellText = value.getText().toString();
            if (!cellText.equals("0")) {
                cell.setText(cellText);
                cell.setTypeface(cell.getTypeface(), Typeface.BOLD);
            } else {
                cell.setText(R.string.vDefault);
                cell.setTypeface(null, Typeface.NORMAL);
            }
            drawable.setStroke(3, getResources().getColor(R.color.border));
            possibleValues.setVisibility(View.GONE);
        }
    }

    public void onClickCalculate(View view) {
        if (!isSolved) {
            drawable.setStroke(3, getResources().getColor(R.color.border));
            possibleValues.setVisibility(View.GONE);
            getUserValues();
            SudokuCalculator sc = new SudokuCalculator(sudoku);
            UserInputValidation iv = new UserInputValidation(sc.getSudoku());
            isCorrectUserInput = iv.checkUserInput();
            if (isCorrectUserInput) findSolution(sc);
            else showIncorrectValues(iv);
        } else resetSudoku();
    }

    private void getUserValues() {
        TextView cell;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cell = findViewById(CELLS[i][j]);
                if (cell.getText().length() > 0)
                    sudoku[i][j] = Integer.parseInt(cell.getText().toString());
                else sudoku[i][j] = 0;
            }
        }
    }

    private void showIncorrectValues(UserInputValidation iv) {
        if (iv.getIncorrectRows().size() != 0)
            highLightIncorrectRow(iv);
        if (iv.getIncorrectColumns().size() != 0)
            highLightIncorrectColumn(iv);
        if (iv.getIncorrectBlocks().size() != 0)
            highLightIncorrectBlock(iv);
    }

    private void highLightIncorrectRow(UserInputValidation iv) {
        Set<Integer> incorrectRows = iv.getIncorrectRows();
        for (int row : incorrectRows) {
            for (int cell : CELLS[row]) {
                highlightedStyle(cell);
            }
        }
    }

    private void highLightIncorrectColumn(UserInputValidation iv) {
        Set<Integer> incorrectColumns = iv.getIncorrectColumns();
        for (int column : incorrectColumns) {
            for (int cell[] : CELLS) {
                highlightedStyle(cell[column]);
            }
        }
    }

    private void highLightIncorrectBlock(UserInputValidation iv) {
        Set<Integer> incorrectBlocks = iv.getIncorrectBlocks();
        if (blockIdentifications == null)
            blockIdentifications = iv.getBlockIdentifications();
        for (int block : incorrectBlocks) {
            for (int i = blockIdentifications[block][0];
                 i < blockIdentifications[block][1]; i++) {
                for (int j = blockIdentifications[block][2];
                     j < blockIdentifications[block][3]; j++) {
                    highlightedStyle(CELLS[i][j]);
                }
            }
        }
    }

    private void getDefaultBorderState() {
        for (int[] cellRow : CELLS) {
            for (int cell : cellRow) {
                normalStyle(cell);
            }
        }
    }

    private void findSolution(SudokuCalculator ex) {
        ex.calculateSudoku();
        sudoku = ex.getSudoku();
        isDone(ex);
        showSolution(CELLS);
    }

    private void showSolution(int[][] buttons) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (sudoku[i][j] != 0) {
                    cell = findViewById(buttons[i][j]);
                    cell.setText(String.valueOf(sudoku[i][j]));
                }
            }
        }
    }

    private void isDone(SudokuCalculator ex) {
        if (ex.isDone()) {
            Button calc = findViewById(R.id.calculate);
            calc.setText(R.string.reset);
            isSolved = true;
            isClickable = false;
            TextView messages = findViewById(R.id.messages);
            messages.setText(R.string.congratulations);
        }
    }

    private void resetSudoku() {
        isSolved = false;
        isClickable = true;
        TextView messages = findViewById(R.id.messages);
        messages.setText(R.string.vDefault);
        Button calc = findViewById(R.id.calculate);
        calc.setText(R.string.calculate);
        for (int[] cells : CELLS) {
            for (int cell : cells) {
                this.cell = findViewById(cell);
                this.cell.setTypeface(null, Typeface.NORMAL);
                this.cell.setText(R.string.vDefault);
            }
        }
    }

    private void highlightedStyle(int id) {
        this.cell = findViewById(id);
        drawable = (GradientDrawable) this.cell.getBackground();
        drawable.setStroke(10, getResources().getColor(R.color.highlighted));
    }

    private void normalStyle(int id) {
        this.cell = findViewById(id);
        drawable = (GradientDrawable) this.cell.getBackground();
        drawable.setStroke(3, getResources().getColor(R.color.border));
    }
}