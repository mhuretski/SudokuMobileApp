package maksim_huretski.sudoku.animation.board;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import maksim_huretski.sudoku.R;
import maksim_huretski.sudoku.containers.game.Game;
import maksim_huretski.sudoku.containers.menu.HintSettingsDialog;

public class AnimHints {

    private final Game activity;
    private final float ROTATE_FROM = 0.0f;
    private final float ROTATE_TO = 180.0f;
    private final RotateAnimation rotateAnimation = new RotateAnimation(ROTATE_FROM, ROTATE_TO, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

    public AnimHints(Game activity) {
        this.activity = activity;
        rotateAnimation.setDuration(1000);
    }

    public void setHintsMenu(final HintSettingsDialog hints) {
        final ImageView imageViewThumb = activity.findViewById(R.id.hintMenuBtn);
        imageViewThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    imageViewThumb.startAnimation(rotateAnimation);
                    hints.onClickHints();
            }
        });
    }
}
