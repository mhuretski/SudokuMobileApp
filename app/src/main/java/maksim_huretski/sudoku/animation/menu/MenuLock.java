package maksim_huretski.sudoku.animation.menu;

public class MenuLock {
    private static boolean isUnlocked = true;

    public static void lock() {
        isUnlocked = false;
    }

    public static void unlock() {
        isUnlocked = true;
    }

    public static boolean isUnlocked() {
        return isUnlocked;
    }
}
