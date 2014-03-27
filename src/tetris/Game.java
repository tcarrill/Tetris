package tetris;

import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.font.effects.ColorEffect;
import tetris.model.Board;
import tetris.model.Explosion;
import tetris.model.Score;

import java.awt.Font;
import java.util.Locale;
import java.util.ResourceBundle;

public class Game extends BasicGame implements KeyListener, Observer {
    public static final float[] FRAMES_PER_DROP = new float[] {
            48, 43, 38, 33, 28, 23, 18, 13, 8, 6, // 0 - 9
            5, 5, 5, // 10 - 12
            4, 4, 4, // 13 - 15
            3, 3, 3, // 16 - 18
            2, 2,  // 19 - 20
            1 // 21+
    };
    public static final boolean DEBUG = true;
    public static final float FPS = 60;
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final int CENTER_WIDTH = WIDTH / 2;
    public static final int CENTER_HEIGHT = HEIGHT / 2;
    public static final boolean FULL_SCREEN = false;
    public static final boolean SHOW_FPS = false;

    private GameContainer gameContainer;
    private BoardRenderer boardRenderer;
    private Board board;
    private long counter = 0;
    private float currentFallRate = (FRAMES_PER_DROP[0] / FPS) * 1000;
    private long pausedPressTime = 0;
    private boolean gameover = false;
    private Score score;
    private UnicodeFont font;
    public static final ResourceBundle resources = ResourceBundle.getBundle("tetris.resources.Strings", Locale.getDefault());
    private static final String TITLE = resources.getString("title");
    private static final String SCORE = resources.getString("score");
    private static final String PAUSED = resources.getString("paused");
    private static final String GAMEOVER = resources.getString("gameover");
    private static final String LEVEL = resources.getString("level");

    public Game() {
        super(TITLE);
    }

    public static void main(String[] argv) {
        AppGameContainer app;
        try {
            app = new AppGameContainer(new Game());
            app.setDisplayMode(WIDTH, HEIGHT, FULL_SCREEN);
            app.setTargetFrameRate((int)FPS);
            app.setShowFPS(SHOW_FPS);
            app.start();
        } catch (SlickException ex) {
            ex.printStackTrace();
        }
    }

    public long getTime() {
        return gameContainer.getTime();
    }

    @Override
    public void init(GameContainer gameContainer) throws SlickException {
        this.gameContainer = gameContainer;
        score = new Score();
        score.registerObserver(this);
        board = new Board(score);
        boardRenderer = new BoardRenderer(board, gameContainer);
        font = new UnicodeFont(new Font("Times New Roman", Font.PLAIN, 20));
        font.addAsciiGlyphs();

        font.getEffects().add(new ColorEffect(java.awt.Color.white));

        font.loadGlyphs();
        start();
    }

    private void start() {
        gameover = false;
        board.start();
    }

    @Override
    public void update(GameContainer gameContainer, int delta) throws SlickException {
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

    @Override
    public void render(GameContainer gameContainer, Graphics g) throws SlickException {
        boardRenderer.render(g);

        //TODO: move these to a proper renderer
        font.drawString(BoardRenderer.CENTER_BOARD_LEFT - 100, BoardRenderer.CENTER_BOARD_TOP, LEVEL + score.getCurrentLevel(), Color.white);
        font.drawString(BoardRenderer.CENTER_BOARD_RIGHT + 10, BoardRenderer.CENTER_BOARD_TOP, SCORE + score.getScore(), Color.white);

        if (gameover) {
            int textCenterWidth = font.getWidth(GAMEOVER) / 2;
            int textCenterHeight = font.getHeight(GAMEOVER) / 2;
            font.drawString(CENTER_WIDTH - textCenterWidth, CENTER_HEIGHT - textCenterHeight, GAMEOVER, Color.red);
        } else if (gameContainer.isPaused()) {
            int textCenterWidth = font.getWidth(PAUSED) / 2;
            int textCenterHeight = font.getHeight(PAUSED) / 2;
            g.setColor(Color.red);
            g.fillRect(CENTER_WIDTH - (textCenterWidth * 2), CENTER_HEIGHT - (textCenterHeight * 2), textCenterWidth * 4, textCenterHeight * 4);
            font.drawString(CENTER_WIDTH - textCenterWidth, CENTER_HEIGHT - textCenterHeight, PAUSED, Color.yellow);
        }
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
        currentFallRate = (FRAMES_PER_DROP[index] / FPS) * 1000;
    }
}