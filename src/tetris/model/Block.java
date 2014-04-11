package tetris.model;

import java.util.Random;

/**
 * Created by thomas on 1/16/14.
 */
public class Block {
    private static final int[][][] relativeCoordsTable = new int[][][]{
            {{  0, 0 }, {  0, 0 }, {  0, 0 }, {  0, 0 }},
            {{ -2, 0 }, { -1, 0 }, {  0, 0 }, {  1, 0 }}, // IBlock
            {{ -1, 0 }, {  0, 0 }, {  1, 0 }, {  1, 1 }}, // JBlock
            {{ -1, 0 }, {  0, 0 }, {  1, 0 }, { -1, 1 }}, // LBlock
            {{ -1, 0 }, {  0, 0 }, { -1, 1 }, {  0, 1 }}, // OBlock
            {{  0, 0 }, {  1, 0 }, { -1, 1 }, {  0, 1 }}, // SBlock
            {{ -1, 0 }, {  0, 0 }, {  1, 0 }, {  0, 1 }}, // TBlock
            {{ -1, 0 }, {  0, 0 }, {  0, 1 }, {  1, 1 }}  // ZBlock
    };
    private int coords[][] = new int[4][2];
    private Tetromino block;
    private Random random = new Random();
    private static final Tetromino[] TetrominoArray = Tetromino.values();

    public Block() {
        this(Tetromino.None);
    }

    public Block(Tetromino tetromino) {
        setBlock(tetromino);
    }

    public void setBlock(Tetromino block) {
        int ordinal = block.ordinal();
        for (int i = 0; i < coords.length; i++) {
            for (int j = 0; j < coords[i].length; j++) {
                coords[i][j] = relativeCoordsTable[ordinal][i][j];
            }
        }
        this.block = block;
    }

    public void setX(int index, int x) {
        coords[index][0] = x;
    }

    public void setY(int index, int y) {
        coords[index][1] = y;
    }

    public int x(int index) {
        return coords[index][0];
    }

    public int y(int index) {
        return coords[index][1];
    }

    public int[] getRelativeCoordsTable(int block, int index) {
        return relativeCoordsTable[block][index];
    }

    public Tetromino getBlock() {
        return block;
    }

    public void setRandomBlock() {
        int x = Math.abs(random.nextInt()) % 7 + 1;
        setBlock(TetrominoArray[x]);
        //setBlock(Tetromino.IBlock);
    }

    public Block rotateLeft() {
        if (block == Tetromino.OBlock)
            return this;

        Block result = new Block();
        result.block = block;

        for (int i = 0; i < 4; i++) {
            result.setX(i, y(i));
            result.setY(i, -x(i));
        }
        return result;
    }

    public Block rotateRight() {
        if (block == Tetromino.OBlock)
            return this;

        Block result = new Block();
        result.block = block;

        for (int i = 0; i < 4; i++) {
            result.setX(i, -y(i));
            result.setY(i, x(i));
        }
        return result;
    }

    @Override
    public String toString() {
        return block.name();
    }
}
