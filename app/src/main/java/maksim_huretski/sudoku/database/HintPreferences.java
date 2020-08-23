package maksim_huretski.sudoku.database;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.widget.SwitchCompat;
import maksim_huretski.sudoku.R;
import maksim_huretski.sudoku.containers.game.Game;
import maksim_huretski.sudoku.containers.menu.HintSettingsDialog;

public class HintPreferences {

    private final Game game;
    private SharedPreferences sharedPref;
    private boolean hintRows;
    private boolean hintColumns;
    private boolean hintBlocks;
    private boolean hintPosValues;
    private boolean hintCalc;
    private final SwitchCompat switcherRows;
    private final SwitchCompat switcherColumns;
    private final SwitchCompat switcherBlocks;
    private final SwitchCompat switcherPossibleValues;
    private final SwitchCompat switcherCalculations;

    public HintPreferences(Game game, HintSettingsDialog dialog) {
        this.game = game;
        switcherRows = dialog.findViewById(R.id.switcherRows);
        switcherColumns = dialog.findViewById(R.id.switcherColumns);
        switcherBlocks = dialog.findViewById(R.id.switcherBlocks);
        switcherPossibleValues = dialog.findViewById(R.id.switcherPossibleValues);
        switcherCalculations = dialog.findViewById(R.id.switcherCalculations);
    }

    public void getHintPreferences() {
        sharedPref = game.getSharedPreferences(game.getString(R.string.hintSettingsStorage), Context.MODE_PRIVATE);
        hintRows = sharedPref.getBoolean(game.getString(R.string.hintRows), true);
        hintColumns = sharedPref.getBoolean(game.getString(R.string.hintColumns), true);
        hintBlocks = sharedPref.getBoolean(game.getString(R.string.hintBlocks), true);
        hintPosValues = sharedPref.getBoolean(game.getString(R.string.hintPosValues), false);
        hintCalc = sharedPref.getBoolean(game.getString(R.string.hintCalc), false);
    }

    public void setSwitcherValues() {
        switcherRows.setChecked(hintRows);
        switcherColumns.setChecked(hintColumns);
        switcherBlocks.setChecked(hintBlocks);
        switcherPossibleValues.setChecked(hintPosValues);
        switcherCalculations.setChecked(hintCalc);
    }

    public void setHintPreferences() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(game.getString(R.string.hintRows), switcherRows.isChecked());
        editor.putBoolean(game.getString(R.string.hintColumns), switcherColumns.isChecked());
        editor.putBoolean(game.getString(R.string.hintBlocks), switcherBlocks.isChecked());
        editor.putBoolean(game.getString(R.string.hintPosValues), switcherPossibleValues.isChecked());
        editor.putBoolean(game.getString(R.string.hintCalc), switcherCalculations.isChecked());
        editor.apply();
    }

    public boolean isHintRows() {
        return hintRows;
    }

    public boolean isHintColumns() {
        return hintColumns;
    }

    public boolean isHintBlocks() {
        return hintBlocks;
    }

    public boolean isHintPosValues() {
        return hintPosValues;
    }

    public boolean isHintCalc() {
        return hintCalc;
    }

}
