package maksim_huretski.sudoku.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import maksim_huretski.sudoku.R;
import maksim_huretski.sudoku.animation.MenuAnimation;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    MenuAnimation menuAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        setContentView(R.layout.activity_main);
        menuAnimation = new MenuAnimation(this);
        menuAnimation.hide(R.id.menuDifficulty, true, this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.newGame:
                intent = new Intent(this, NewGame.class);
                startActivity(intent);
                break;
            case R.id.sudokuSolver:
                intent = new Intent(this, Calculator.class);
                startActivity(intent);
                break;
            case R.id.difficulty:
                menuAnimation.hide(R.id.mainMenuButtons, false, this);
                menuAnimation.show(R.id.menuDifficulty,this);
                break;
            case R.id.insane:
            case R.id.hard:
            case R.id.normal:
            case R.id.easy:
                menuAnimation.hide(R.id.menuDifficulty, false, this);
                menuAnimation.show(R.id.mainMenuButtons,this);
                break;
            default:
                break;
        }
    }

}
