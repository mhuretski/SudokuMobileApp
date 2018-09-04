package maksim_huretski.sudoku.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import maksim_huretski.sudoku.R;
import maksim_huretski.sudoku.animation.MenuAnimation;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MenuAnimation menuAnimation;
    private int difficultyLevel;
    private View button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        setContentView(R.layout.activity_main);
        setMenuAnimation();
        getDifficulty();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isSavedProgress();
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.newGame:
                intent = new Intent(this, NewGame.class);
                intent.putExtra(getString(R.string.difficultyLevel), difficultyLevel);
                startActivity(intent);
                break;
            case R.id.continueGame:
                intent = new Intent(this, ContinueGame.class);
                startActivity(intent);
                break;
            case R.id.sudokuSolver:
                intent = new Intent(this, Calculator.class);
                startActivity(intent);
                break;
            case R.id.difficulty:
                showDifficulty();
                break;
            case R.id.insane:
            case R.id.hard:
            case R.id.normal:
            case R.id.easy:
                hideContinue();
                setDifficulty(view);
                showMenu();
                break;
            default:
                break;
        }
    }

    private void setDifficulty(View view) {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        int level = view.getId();
        editor.putInt(getString(R.string.difficultyLevel), level);
        editor.apply();
        difficultyLevel = sharedPref.getInt(getString(R.string.difficultyLevel), R.id.normal);
    }

    private void getDifficulty() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        difficultyLevel = sharedPref.getInt(getString(R.string.difficultyLevel), R.id.normal);
    }

    private void showDifficulty() {
        menuAnimation.hide(R.id.mainMenuButtons, false, this);
        menuAnimation.show(R.id.menuDifficulty, this);
    }

    private void showMenu() {
        menuAnimation.hide(R.id.menuDifficulty, false, this);
        menuAnimation.show(R.id.mainMenuButtons, this);
    }

    private void isSavedProgress() {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.savedGame), Context.MODE_PRIVATE);
        boolean isSolved = sharedPref.getBoolean(getString(R.string.savedGame), true);
        if (!isSolved) showContinue();
        else hideContinue();
    }

    private void showContinue() {
        button = findViewById(R.id.continueGame);
        button.setVisibility(View.VISIBLE);
        button = findViewById(R.id.tempoBlock);
        button.setVisibility(View.GONE);
    }

    private void hideContinue() {
        button = findViewById(R.id.continueGame);
        button.setVisibility(View.GONE);
        button = findViewById(R.id.tempoBlock);
        button.setVisibility(View.INVISIBLE);
    }

    private void setMenuAnimation() {
        menuAnimation = new MenuAnimation(this);
        menuAnimation.hide(R.id.menuDifficulty, true, this);
        menuAnimation.hide(R.id.mainMenuButtons, true, this);
        menuAnimation.show(R.id.mainMenuButtons, this);
    }

}
