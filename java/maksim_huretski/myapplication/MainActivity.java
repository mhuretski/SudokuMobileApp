package maksim_huretski.myapplication;

import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        possibleValues = findViewById(R.id.possibleValues);
        possibleValues.setVisibility(View.GONE);
    }

    private TextView cell;
    private View possibleValues;
    private GradientDrawable drawable;
    private int[][] sudoku = new int[9][9];

    public void onClickCell(View view) {
        if (drawable != null)
            drawable.setStroke(3, getResources().getColor(R.color.border));
        cell = findViewById(view.getId());
        possibleValues.setVisibility(View.VISIBLE);
        drawable = (GradientDrawable) view.getBackground();
        drawable.setStroke(10, getResources().getColor(R.color.colorAccent));
    }

    public void onClickValue(View view) {
        TextView value = findViewById(view.getId());
        String cellText = String.valueOf(value.getText());
        if (!cellText.equals("0")) cell.setText(cellText);
        else cell.setText(R.string.vDefault);
        drawable.setStroke(3, getResources().getColor(R.color.border));
        possibleValues.setVisibility(View.GONE);
    }

    public void onClickCalculate(View view) {
        TextView cell;
        int[][] buttons = new int[][]{
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
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cell = findViewById(buttons[i][j]);
                if (cell.getText().length() > 0)
                    sudoku[i][j] = Integer.parseInt(cell.getText().toString());
                else sudoku[i][j] = 0;
            }
        }
        System.out.println(1);
        Executor ex = new Executor(sudoku);
        System.out.println(2);
        sudoku = ex.getSudoku();
        System.out.println(3);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(sudoku[i][j]);
            }
            System.out.println();
        }
        System.out.println(4);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cell = findViewById(buttons[i][j]);
                cell.setText(String.valueOf(sudoku[i][j]));
            }
        }
        System.out.println(5);
    }
}
