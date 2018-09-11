package maksim_huretski.sudoku.containers.menu;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import maksim_huretski.sudoku.R;
import maksim_huretski.sudoku.containers.game.Game;

public class HintSettingsDialog extends Dialog {

    private final Game game;

    public HintSettingsDialog(@NonNull Game game) {
        super(game);
        this.game = game;
        this.setContentView(R.layout.hint_settings);
        if (this.getWindow() != null)
            this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void onClickHints() {
        super.show();
        game.getDefaultBorderState();
        game.hidePossibleValues();
    }

}
