package maksim_huretski.sudoku.parts;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import maksim_huretski.sudoku.R;
import maksim_huretski.sudoku.calculation.Calc;
import maksim_huretski.sudoku.calculation.BlockIDs;
import maksim_huretski.sudoku.validation.Checker;
import maksim_huretski.sudoku.validation.InputValidator;

import java.util.Set;

public abstract class Screen extends AppCompatActivity {

    protected TextView cell;
    protected View possibleValues;
    protected int[][] sudoku = new int[9][9];
    protected int[][] blockIDs;
    protected boolean isSolved = false;
    protected boolean isClickable = true;
    protected boolean isCorrectSudoku = true;
    protected boolean highlighted = false;
    protected boolean isFirstTime = true;
    protected final int[][] CELLS = new int[][]{
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

    protected abstract void onClickCell(View view);

    protected abstract void findSolution(Calc calc, InputValidator iv);

    protected abstract void onClickValue(View view);

    protected void getUserValues() {
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

    protected void highlightIncorrectBlocks(InputValidator iv) {
        highlighted = true;
        ((TextView) findViewById(R.id.messageAtTop)).setText(R.string.invalidInput);
        if (iv.getIncorrectRows().size() != 0)
            highLightIncorrectRow(iv);
        if (iv.getIncorrectColumns().size() != 0)
            highLightIncorrectColumn(iv);
        if (iv.getIncorrectBlocks().size() != 0)
            highLightIncorrectBlock(iv);
    }

    private void highLightIncorrectRow(InputValidator iv) {
        Set<Integer> incorrectRows = iv.getIncorrectRows();
        for (int row : incorrectRows) {
            for (int cell : CELLS[row]) {
                highlightedStyle(cell);
            }
        }
    }

    private void highLightIncorrectColumn(InputValidator iv) {
        Set<Integer> incorrectColumns = iv.getIncorrectColumns();
        for (int column : incorrectColumns) {
            for (int cell[] : CELLS) {
                highlightedStyle(cell[column]);
            }
        }
    }

    private void highLightIncorrectBlock(InputValidator iv) {
        Set<Integer> incorrectBlocks = iv.getIncorrectBlocks();
        for (int block : incorrectBlocks) {
            for (int i = blockIDs[block][0];
                 i < blockIDs[block][1]; i++) {
                for (int j = blockIDs[block][2];
                     j < blockIDs[block][3]; j++) {
                    highlightedStyle(CELLS[i][j]);
                }
            }
        }
    }

    protected void getDefaultBorderState() {
        ((TextView) findViewById(R.id.messageAtTop)).setText(R.string.vDefault);
        for (int[] cellRow : CELLS) {
            for (int cell : cellRow) {
                normalStyle(cell);
            }
        }
    }

    protected void isDone() {
        Checker checker = new Checker();
        checker.checkSudoku(sudoku, blockIDs);
        if (checker.isDone()) {
            Button calc = findViewById(R.id.calculate);
            calc.setText(R.string.reset);
            isSolved = true;
            isClickable = false;
            ((TextView) findViewById(R.id.messageAtTop)).setText(R.string.congratulations);
        }
    }

    protected void showSolution() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (sudoku[i][j] != 0) {
                    cell = findViewById(CELLS[i][j]);
                    cell.setText(String.valueOf(sudoku[i][j]));
                }
            }
        }
    }

    protected void resetSudoku() {
        isSolved = false;
        isClickable = true;
        highlighted = false;
        TextView messages = findViewById(R.id.messageAtTop);
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

    protected void highlightedStyle(int id) {
        this.cell = findViewById(id);
        if (cell.getTag().equals(getResources().getString(R.string.light)))
            cell.setBackgroundResource(R.drawable.highlighted_light);
        else
            cell.setBackgroundResource(R.drawable.highlighted_dark);
    }

    protected void normalStyle(int id) {
        TextView cell = findViewById(id);
        if (cell.getTag().equals(getResources().getString(R.string.light)))
            cell.setBackgroundResource(R.drawable.light_block);
        else
            cell.setBackgroundResource(R.drawable.dark_block);
    }

    protected void setBlockIDs() {
        BlockIDs blockIDs = new BlockIDs();
        blockIDs.init();
        this.blockIDs = blockIDs.getBlockIDs();
    }

}
