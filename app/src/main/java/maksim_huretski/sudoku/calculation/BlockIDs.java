package maksim_huretski.sudoku.calculation;

public class BlockIDs {

    private final int[][] blockIDs = new int[9][4];

    public void init() {
        int[] ids = new int[]{0, 3, 0, 3};
        for (int i = 0; i < 9; i++, ids[2] += 3, ids[3] += 3) {
            if (i != 0 && i % 3 == 0) {
                ids[0] += 3;
                ids[1] += 3;
            }
            if (ids[2] > 6) {
                ids[2] = 0;
                ids[3] = 3;
            }
            System.arraycopy(ids, 0, blockIDs[i], 0, 4);
        }
    }

    public int[][] getBlockIDs() {
        return blockIDs;
    }

}
