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
        if (drawable != null) drawable.setStroke(3, getResources().getColor(R.color.border));
        cell = findViewById(view.getId());
        possibleValues.setVisibility(View.VISIBLE);
        drawable = (GradientDrawable)view.getBackground();
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
    }
}
