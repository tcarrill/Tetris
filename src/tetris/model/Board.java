package tetris.model;

import tetris.BoardRenderer;

import java.util.List;

/**
 * Created by thomas on 1/16/14.
 */
public class Board {
    public static final int BOARD_WIDTH = 10;
    public static final int BOARD_HEIGHT = 20;

    private boolean isFallingFinished = false;
    private int curX = 0;
    private int curY = 0;
    private int hardDropY = 0;

    private Block curPiece;
    private Block nextPiece;
    private Tetromino[] board;
    private boolean topOut = false;
    private Score score;
    private ParticleSystem particleSystem;

    public Board(Score score) {
        //curPiece = new Block();
        nextPiece = new Block();
        this.score = score;
        board = new Tetromino[BOARD_WIDTH * BOARD_HEIGHT];
        particleSystem = new ParticleSystem();
        clearBoard();
    }

    public void update() {
        if (!topOut) {
            if (isFallingFinished) {
                isFallingFinished = false;
                spawnPiece();
            } else {
                oneLineDown();
            }
        }
    }

    public void reset() {
        nextPiece = null;
        isFallingFinished = false;
        score.reset();
        particleSystem.killAll();

        clearBoard();
    }

    public void particleUpdate() {
        particleSystem.update();
    }

    public Tetromino getBlock(int x, int y) {
        int index = (y * BOARD_WIDTH) + x;
        if (index < board.length) {
            return board[(y * BOARD_WIDTH) + x];
        } else {
            return null;
        }
    }

    public void setBlock(int x, int y, Tetromino tetromino) {
        board[(y * BOARD_WIDTH) + x] = tetromino;
    }

    public Block getCurrentBlock() {
        return curPiece;
    }

    public int getCurrentX() {
        return curX;
    }

    public int getCurrentY() {
        return curY;
    }

    public void start() {
        reset();
        spawnPiece();
    }

    public void dropDown() {
        int newY = curY;
        hardDropY = curY;
        while (newY > 0) {
            if (!tryMove(curPiece, curX, newY - 1)) {
                break;
            }
            newY--;
        }
        pieceDropped();
    }

    public void oneLineDown() {
        if (!tryMove(curPiece, curX, curY - 1)) {
            pieceDropped();
        }
    }

    private void clearBoard() {
        for (int i = 0; i < BOARD_HEIGHT * BOARD_WIDTH; i++) {
            board[i] = Tetromino.None;
        }
        topOut = false;
    }

    public void pieceDropped() {
        for (int i = 0; i < 4; i++) {
            int x = curX + curPiece.x(i);
            int y = curY - curPiece.y(i);
            setBlock(x, y, curPiece.getBlock());
        }

        removeFullLines();

        if (!isFallingFinished) {
            spawnPiece();
        }
    }

    private void spawnPiece() {
        if (nextPiece == null) {
            curPiece = new Block();
        } else {
            curPiece = nextPiece;
        }

        score.incTetromino(curPiece.getBlock());
        nextPiece = new Block();
        //nextPiece.setRandomBlock();

        curX = BOARD_WIDTH / 2;
        curY = BOARD_HEIGHT - 1;

        if (!tryMove(curPiece, curX, curY)) {
            curPiece.setBlock(Tetromino.None);
            topOut = true;
        }
    }

    public boolean moveLeft() {
        return tryMove(curPiece, curX - 1, curY);
    }

    public boolean moveRight() {
        return tryMove(curPiece, curX + 1, curY);
    }

    public boolean rotateLeft() {
        return tryMove(curPiece.rotateLeft(), curX, curY);
    }

    public boolean rotateRight() {
        return tryMove(curPiece.rotateRight(), curX, curY);
    }

    private boolean tryMove(Block newPiece, int newX, int newY) {
        for (int i = 0; i < 4; i++) {
            int x = newX + newPiece.x(i);
            int y = newY - newPiece.y(i);

            if (x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGHT) {
                return false;
            }

            if (getBlock(x, y) != Tetromino.None) {
                return false;
            }
        }

        curPiece = newPiece;
        curX = newX;
        curY = newY;

        return true;
    }

    private void removeFullLines() {
        int numFullLines = 0;

        for (int i = BOARD_HEIGHT - 1; i >= 0; i--) {
            boolean isLineFull = true;

            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (getBlock(j, i) == Tetromino.None) {
                    isLineFull = false;
                    break;
                }
            }

            if (isLineFull) {
                numFullLines++;

                for (int j = 0; j < BOARD_WIDTH; j++) {
                    Tetromino tetromino = getBlock(j, i);
                    //todo: BOARD_HEIGHT - i is shitty
                    particleSystem.addEmitter(50, j, BOARD_HEIGHT - i, BoardRenderer.colors[tetromino.ordinal()]);
                }

                for (int k = i; k < BOARD_HEIGHT - 1; k++) {
                    for (int j = 0; j < BOARD_WIDTH; j++) {
                        setBlock(j, k, getBlock(j, k + 1)); //Shift blocks down
                    }
                }
            }
        }

        if (numFullLines > 0) {
            score.setNumLinesRemoved(numFullLines, hardDropY);
            hardDropY = 0;
            isFallingFinished = true;
            curPiece.setBlock(Tetromino.None);
        }
    }

    public Block getNextPiece() {
        return nextPiece;
    }

    public boolean isTopOut() {
        return topOut;
    }

    public List<ParticleEmitter> getParticleEmitters() {
        return particleSystem.getParticleEmitters();
    }
}
