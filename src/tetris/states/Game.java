package tetris.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import tetris.BoardRenderer;
import tetris.Observable;
import tetris.Observer;
import tetris.Tetris;
import tetris.model.Board;
import tetris.model.Explosion;
import tetris.model.Score;

import java.awt.Font;

public class Game extends BasicGameState implements KeyListener, Observer {
    public static final float[] FRAMES_PER_DROP = new float[] {
            48, 43, 38, 33, 28, 23, 18, 13, 8, 6, // 0 - 9
            5, 5, 5, // 10 - 12
            4, 4, 4, // 13 - 15
            3, 3, 3, // 16 - 18
            2, 2,  // 19 - 20
            1 // 21+
    };
    public static final int ID = 1;
    public static final boolean DEBUG = true;

    private GameContainer gameContainer;
    private BoardRenderer boardRenderer;
    private Board board;
    private long counter = 0;
    private float currentFallRate = calculateFallRate(0);
    private long pausedPressTime = 0;
    private boolean gameover = false;
    private Tetris tetris;
    private Score score;

    private UnicodeFont font;
    private static final String SCORE = Tetris.resources.getString("score");
    private static final String PAUSED = Tetris.resources.getString("paused");
    private static final String GAMEOVER = Tetris.resources.getString("gameover");
    private static final String LEVEL = Tetris.resources.getString("level");

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        this.gameContainer = gameContainer;
        tetris = (Tetris)stateBasedGame;
        score = tetris.getScore();

        tetris.getScore().registerObserver(this);
        board = new Board(score);
        boardRenderer = new BoardRenderer(board, gameContainer);
        font = new UnicodeFont(new Font("Times New Roman", Font.PLAIN, 20));
        font.addAsciiGlyphs();

        font.getEffects().add(new ColorEffect(java.awt.Color.white));

        font.loadGlyphs();
        start();
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        boardRenderer.render(graphics);

        //TODO: move these to a proper renderer
        font.drawString(BoardRenderer.CENTER_BOARD_LEFT - 100, BoardRenderer.CENTER_BOARD_TOP, LEVEL + score.getCurrentLevel(), Color.white);
        font.drawString(BoardRenderer.CENTER_BOARD_RIGHT + 10, BoardRenderer.CENTER_BOARD_TOP, SCORE + score.getScore(), Color.white);

        if (gameover) {
            int textCenterWidth = font.getWidth(GAMEOVER) / 2;
            int textCenterHeight = font.getHeight(GAMEOVER) / 2;
            font.drawString(Tetris.CENTER_WIDTH - textCenterWidth, Tetris.CENTER_HEIGHT - textCenterHeight, GAMEOVER, Color.red);
        } else if (gameContainer.isPaused()) {
            int textCenterWidth = font.getWidth(PAUSED) / 2;
            int textCenterHeight = font.getHeight(PAUSED) / 2;
            graphics.setColor(Color.red);
            graphics.fillRect(Tetris.CENTER_WIDTH - (textCenterWidth * 2), Tetris.CENTER_HEIGHT - (textCenterHeight * 2), textCenterWidth * 4, textCenterHeight * 4);
            font.drawString(Tetris.CENTER_WIDTH - textCenterWidth, Tetris.CENTER_HEIGHT - textCenterHeight, PAUSED, Color.yellow);
        }
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException {
        if (gameover) {
            return;
        }

        if (!gameContainer.isPaused()) {
            if (board.isTopOut()) {
                gameover = true;
            }

            counter += delta;

            if (counter >= currentFallRate) {
                counter = 0;
                board.update();
            }

            board.explosionUpdate();
        }
    }

    public long getTime() {
        return gameContainer.getTime();
    }

    private void start() {
        gameover = false;

        board.start();
    }

    @Override
    public void mousePressed(int button, int x, int y) {
        board.getExplosions().add(new Explosion(100, x, y));
    }

    @Override
    public void keyPressed(int i, char c) {
        Input input = gameContainer.getInput();

        if (gameover) {
            if (input.isKeyDown(Input.KEY_Y)) {
                start();
            } else if (input.isKeyDown(Input.KEY_N)) {

            }

            return;
        }

        if (input.isKeyDown(Input.KEY_P)) {
            if (getTime() - pausedPressTime > 250) {
                pausedPressTime = getTime();
                gameContainer.setPaused(!gameContainer.isPaused());
            }
        } else if (!gameContainer.isPaused()) {
            if (input.isKeyDown(Input.KEY_LEFT)) {
                board.moveLeft();
            } else if (input.isKeyDown(Input.KEY_RIGHT)) {
                board.moveRight();
            } else if (input.isKeyDown(Input.KEY_UP)) {
                board.rotateLeft();
            } else if (input.isKeyDown(Input.KEY_DOWN)) {
                board.rotateRight();
            } else if (input.isKeyDown(Input.KEY_SPACE)) {
                board.dropDown();
            } else if (input.isKeyDown(Input.KEY_D)) {
                board.oneLineDown();
            } else if (DEBUG && input.isKeyDown(Input.KEY_EQUALS)) {
                score.setLevel(score.getCurrentLevel() + 1);
            }
        }

        if (input.isKeyDown(Input.KEY_G)) {
            boardRenderer.setShowingGrid(!boardRenderer.isShowingGrid());
        }
    }

    @Override
    public void update(Observable observable) {
        int index = score.getCurrentLevel();
        if (index >= FRAMES_PER_DROP.length) {
            index = FRAMES_PER_DROP.length - 1;
        }
        currentFallRate = calculateFallRate(index);
    }

    private static float calculateFallRate(int index) {
        return (FRAMES_PER_DROP[index] / Tetris.FPS) * 1000;
    }
}