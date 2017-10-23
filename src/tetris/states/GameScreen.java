package tetris.states;

import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import tetris.BoardRenderer;
import tetris.Observable;
import tetris.Observer;
import tetris.Tetris;
import tetris.model.Board;
import tetris.model.Score;

public class GameScreen extends BaseState implements KeyListener, Observer {
    public static final float[] FRAMES_PER_DROP = new float[] {
            48, 43, 38, 33, 28, 23, 18, 13, 8, 6, // 0 - 9
            5, 5, 5, // 10 - 12
            4, 4, 4, // 13 - 15
            3, 3, 3, // 16 - 18
            2, 2,  // 19 - 20
            1 // 21+
    };
    public static final int ID = 1;
    public static final boolean DEBUG = false;
    private Color backgroundRed = new Color(255, 0, 0, 200);
    private GameContainer gameContainer;
    private BoardRenderer boardRenderer;
    private Board board;
    private long counter = 0;
    private float currentFallRate = calculateFallRate(0);
    private long pausedPressTime = 0;
    private boolean gameover = false;
    private Score score;
    private Tetris tetris;
    private static final String SCORE = Tetris.resources.getString("score");
    private static final String LINES = Tetris.resources.getString("lines");
    private static final String PAUSED = Tetris.resources.getString("paused");
    private static final String GAMEOVER = Tetris.resources.getString("gameover");
    private static final String CONTINUE = Tetris.resources.getString("gameover_continue");
    private static final String LEVEL = Tetris.resources.getString("level");
    private int gameoverTextCenterHeight;
    private int gameoverTextCenterWidth;
    private int continueTextCenterWidth;

    private int pausedTextCenterHeight;
    private int pausedTextCenterWidth;

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        this.gameContainer = gameContainer;
        tetris = (Tetris) stateBasedGame;
        score = tetris.getScore();

        tetris.getScore().registerObserver(this);
        board = new Board(score);
        boardRenderer = new BoardRenderer(board, gameContainer);

        gameoverTextCenterWidth = menuFont.getWidth(GAMEOVER) / 2;
        gameoverTextCenterHeight = menuFont.getHeight(GAMEOVER) / 2;

        continueTextCenterWidth = menuFont.getWidth(CONTINUE) / 2;

        pausedTextCenterWidth = menuFont.getWidth(PAUSED) / 2;
        pausedTextCenterHeight = menuFont.getHeight(PAUSED) / 2;

        //start();
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        boardRenderer.render(graphics);

        //TODO: move these to a proper renderer
        menuFont.drawString(BoardRenderer.CENTER_BOARD_LEFT - 125, BoardRenderer.CENTER_BOARD_TOP, LEVEL + " " + score.getCurrentLevel(), Color.white);
        menuFont.drawString(BoardRenderer.CENTER_BOARD_RIGHT + 50, BoardRenderer.CENTER_BOARD_TOP, SCORE + " " + score.getScore(), Color.white);
        menuFont.drawString(BoardRenderer.CENTER_BOARD_RIGHT + 50, BoardRenderer.CENTER_BOARD_TOP + 20, LINES + " " + score.getNumLinesRemoved(), Color.white);

        if (gameover) {
            graphics.setColor(backgroundRed);
            graphics.fillRect(0, Tetris.CENTER_HEIGHT - (gameoverTextCenterHeight * 2), Tetris.WIDTH, gameoverTextCenterHeight * 8);
            menuFont.drawString(Tetris.CENTER_WIDTH - gameoverTextCenterWidth, Tetris.CENTER_HEIGHT - gameoverTextCenterHeight, GAMEOVER, Color.yellow);
            menuFont.drawString(Tetris.CENTER_WIDTH - continueTextCenterWidth, Tetris.CENTER_HEIGHT - gameoverTextCenterHeight + 30, CONTINUE, Color.yellow);
        } else if (gameContainer.isPaused()) {
            graphics.setColor(backgroundRed);
            graphics.fillRect(0, Tetris.CENTER_HEIGHT - (pausedTextCenterHeight * 2), Tetris.WIDTH, pausedTextCenterHeight * 4);
            menuFont.drawString(Tetris.CENTER_WIDTH - pausedTextCenterWidth, Tetris.CENTER_HEIGHT - pausedTextCenterHeight, PAUSED, Color.yellow);
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

            board.particleUpdate();
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
    public void enter(GameContainer gameContainer, StateBasedGame game) {
        start();
    }

    @Override
    public void leave(GameContainer gameContainer, StateBasedGame game) {
        gameover = false;
        board.reset();
    }

    @Override
    public void keyPressed(int i, char c) {
        Input input = gameContainer.getInput();

        if (gameover) {
            if (input.isKeyDown(Input.KEY_Y)) {
                start();
            } else if (input.isKeyDown(Input.KEY_N)) {
                tetris.enterState(MainMenu.ID, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
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
        } else if (input.isKeyDown(Input.KEY_S)) {
            boardRenderer.setShowingShadow(!boardRenderer.isShowingShadow());
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