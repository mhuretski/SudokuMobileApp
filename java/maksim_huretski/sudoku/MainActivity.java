package maksim_huretski.sudoku;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import maksim_huretski.sudoku.logic.SudokuWindow;

public class MainActivity extends SudokuWindow {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        setContentView(R.layout.activity_main);
        debugFinalSudoku();
        super.possibleValues = findViewById(R.id.possibleValuesMain);
        super.possibleValues.setVisibility(View.GONE);
    }

}
