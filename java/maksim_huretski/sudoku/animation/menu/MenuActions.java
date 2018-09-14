package maksim_huretski.sudoku.animation.menu;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import maksim_huretski.sudoku.R;

public abstract class MenuActions extends AppCompatActivity {

    private MenuAnimation menuAnimation;
    private View button;

    protected void startActivity(Intent intent, View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ActivityOptions options = ActivityOptions.makeScaleUpAnimation(
                    view,
                    0,
                    0,
                    view.getWidth(),
                    view.getHeight());
            startActivity(intent, options.toBundle());
        } else startActivity(intent);
    }

    protected void showDifficulty() {
        menuAnimation.show(R.id.menuDifficultyFrame, this);
    }

    protected void hideDifficulty() {
        menuAnimation.hide(R.id.menuDifficultyFrame, false, this);
    }

    protected void showMenu() {
        menuAnimation.show(R.id.mainMenuButtonsFrame, this);
    }

    protected void hideMenu() {
        menuAnimation.hide(R.id.mainMenuButtonsFrame, false, this);
    }

    protected void showStats() {
        menuAnimation.show(R.id.menuStatsFrame, this);
    }

    protected void hideStats() {
        menuAnimation.hide(R.id.menuStatsFrame, false, this);
    }

    protected void showContinue() {
        button = findViewById(R.id.continueGame);
        button.setVisibility(View.VISIBLE);
        button = findViewById(R.id.tempoBlock);
        button.setVisibility(View.GONE);
    }

    protected void hideContinue() {
        button = findViewById(R.id.continueGame);
        button.setVisibility(View.GONE);
        button = findViewById(R.id.tempoBlock);
        button.setVisibility(View.INVISIBLE);
    }

    protected void setMenuAnimation() {
        menuAnimation = new MenuAnimation(this);
        menuAnimation.hide(R.id.menuDifficultyFrame, true, this);
        menuAnimation.hide(R.id.mainMenuButtonsFrame, true, this);
        menuAnimation.hide(R.id.menuStatsFrame, true, this);
        menuAnimation.show(R.id.mainMenuButtonsFrame, this);
    }

}
