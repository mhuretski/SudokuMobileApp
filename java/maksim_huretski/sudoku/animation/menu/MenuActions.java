package maksim_huretski.sudoku.animation.menu;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import maksim_huretski.sudoku.R;

public abstract class MenuActions extends AppCompatActivity {

    protected MenuAnimation menuAnimation;
    protected View button;

    protected void showDifficulty() {
        menuAnimation.show(R.id.menuDifficulty, this);
    }

    protected void hideDifficulty() {
        menuAnimation.hide(R.id.menuDifficulty, false, this);
    }

    protected void showMenu() {
        menuAnimation.show(R.id.mainMenuButtons, this);
    }

    protected void hideMenu() {
        menuAnimation.hide(R.id.mainMenuButtons, false, this);
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
        menuAnimation.hide(R.id.menuDifficulty, true, this);
        menuAnimation.hide(R.id.mainMenuButtons, true, this);
        menuAnimation.show(R.id.mainMenuButtons, this);
    }

}
