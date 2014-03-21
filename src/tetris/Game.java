package tetris;

import org.newdawn.slick.*;
import tetris.model.Board;
import tetris.model.Explosion;
import tetris.model.Score;

public class Game extends BasicGame implements KeyListener, Observer {
    public static final byte FPS = 60;
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final int CENTER_WIDTH = WIDTH / 2;
    public static final int CENTER_HEIGHT = HEIGHT / 2;
    public static final boolean FULL_SCREEN = false;
    public static final boolean SHOW_FPS = false;

    //TODO: put these in a resource bundle
    public static final String TITLE = "Tetris";
    public static final String PAUSED = "P A U S E D";
    public static final String GAMEOVER = "G A M E O V E R";
    public static final String SCORE = "Score: ";
    public static final String NEXT = "Next";
    public static final String LEVEL = "Level: ";

    private GameContainer gameContainer;
    private BoardRenderer boardRenderer;
    private Board board;
    private long lastBoardUpdate = 0;
    private int currentBoardTick = 800;
    private long pausedPressTime = 0;
    private boolean gameover = false;
    private Score score;

    public Game() {
        super(TITLE);
    }

    public static void main(String[] argv) {
        AppGameContainer app;
        try {
            app = new AppGameContainer(new Game());
            app.setDisplayMode(WIDTH, HEIGHT, FULL_SCREEN);
            app.setTargetFrameRate(FPS);
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

            if ((getTime() - lastBoardUpdate) > currentBoardTick) {
                lastBoardUpdate = getTime();
                board.update();
            }

            board.explosionUpdate();
        }
    }

    @Override
    public void render(GameContainer gameContainer, Graphics g) throws SlickException {
        boardRenderer.render(g);

        //TODO: move these to a proper renderer
        gameContainer.getDefaultFont().drawString(BoardRenderer.CENTER_BOARD_LEFT - 100, BoardRenderer.CENTER_BOARD_TOP, LEVEL + score.getCurrentLevel());
        gameContainer.getDefaultFont().drawString(BoardRenderer.CENTER_BOARD_RIGHT + 10, BoardRenderer.CENTER_BOARD_TOP, SCORE + score.getScore());

        if (gameover) {
            int textCenterWidth = gameContainer.getDefaultFont().getWidth(GAMEOVER) / 2;
            int textCenterHeight = gameContainer.getDefaultFont().getHeight(GAMEOVER) / 2;
            gameContainer.getDefaultFont().drawString(CENTER_WIDTH - textCenterWidth, CENTER_HEIGHT - textCenterHeight, GAMEOVER);
        } else if (gameContainer.isPaused()) {
            int textCenterWidth = gameContainer.getDefaultFont().getWidth(PAUSED) / 2;
            int textCenterHeight = gameContainer.getDefaultFont().getHeight(PAUSED) / 2;
            g.setColor(Color.red);
            g.fillRect(CENTER_WIDTH - (textCenterWidth * 2), CENTER_HEIGHT - (textCenterHeight * 2), textCenterWidth * 4, textCenterHeight * 4);
            gameContainer.getDefaultFont().drawString(CENTER_WIDTH - textCenterWidth, CENTER_HEIGHT - textCenterHeight, PAUSED, Color.yellow);
        }
    }

    @Override
    public void mousePressed(int button, int x, int y) {
        System.out.println("Mouse Clicked " + button + " (" + x + ", " + y + ")");
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
            }
        }

        if (input.isKeyDown(Input.KEY_G)) {
            boardRenderer.setShowingGrid(!boardRenderer.isShowingGrid());
        }
    }

    @Override
    public void update(Observable observable) {
        currentBoardTick -= 84;
    }
}