package maksim_huretski.sudoku.containers.menu;

import android.app.Dialog;
import android.support.annotation.NonNull;
import maksim_huretski.sudoku.R;
import maksim_huretski.sudoku.containers.game.Game;
import maksim_huretski.sudoku.database.HintPreferences;

public class HintSettingsDialog extends Dialog {

    private final Game game;
    private final HintPreferences hintPreferences;

    public HintSettingsDialog(@NonNull Game game) {
        super(game, R.style.NoBarsDialog);
        this.setContentView(R.layout.hint_settings);
        this.game = game;
        hintPreferences = new HintPreferences(game, this);
        getPreferences();
    }

    @Override
    public void show() {
        super.show();
        hintPreferences.getHintPreferences();
        hintPreferences.setSwitcherValues();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        hintPreferences.setHintPreferences();
        getPreferences();
        getDefaultPossibleValuesVisibility();
    }

    private void getPreferences() {
        hintPreferences.getHintPreferences();
        game.setHintRows(hintPreferences.isHintRows());
        game.setHintColumns(hintPreferences.isHintColumns());
        game.setHintBlocks(hintPreferences.isHintBlocks());
        game.setHintPosValues(hintPreferences.isHintPosValues());
        game.setHintCalc(hintPreferences.isHintCalc());
    }

    private void getDefaultPossibleValuesVisibility() {
        if (!hintPreferences.isHintPosValues())
            game.setDefaultPossibleValuesVisibility();
    }

    public void onClickHints() {
        this.show();
        game.getDefaultBorderState();
        game.hidePossibleValues();
    }

}
