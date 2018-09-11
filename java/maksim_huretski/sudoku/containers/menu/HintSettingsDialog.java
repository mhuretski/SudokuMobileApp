package maksim_huretski.sudoku.containers.menu;

import android.app.Dialog;
import android.support.annotation.NonNull;
import maksim_huretski.sudoku.R;
import maksim_huretski.sudoku.containers.game.Game;

public class HintSettingsDialog extends Dialog {

    private final Game game;

    public HintSettingsDialog(@NonNull Game game) {
        super(game, R.style.NoBarsDialog);
        this.game = game;
        this.setContentView(R.layout.hint_settings);
    }

    public void onClickHints() {
        super.show();
        game.getDefaultBorderState();
        game.hidePossibleValues();
    }

}
