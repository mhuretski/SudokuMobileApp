package maksim_huretski.sudoku.animation.menu;

import android.app.Activity;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;

class MenuAnimation {

    private final int heightPixels;
    private final Handler handler = new Handler();
    private final int SLOW = 500;

    MenuAnimation(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        heightPixels = (int) (displayMetrics.heightPixels * .7);
    }

    public void hide(int view, boolean fast, Activity activity) {
        int speed;
        if (fast) speed = 0;
        else speed = SLOW;

        final View layout = activity.findViewById(view);
        layout.animate()
                .translationY(heightPixels)
                .alpha(0)
                .setDuration(speed)
                .start();
    }

    public void show(int view, Activity activity) {
        final View layout = activity.findViewById(view);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                layout.animate()
                        .translationY(0)
                        .alpha(1)
                        .setDuration(SLOW)
                        .start();
            }
        }, SLOW);
    }

}
