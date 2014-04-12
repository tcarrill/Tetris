package tetris;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import tetris.model.Score;
import tetris.states.ContinueMenu;
import tetris.states.GameScreen;
import tetris.states.MainMenu;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by thomas on 3/30/14.
 */
public class Tetris extends StateBasedGame {
    public static final boolean FULL_SCREEN = false;
    public static final boolean SHOW_FPS = false;
    public static final float FPS = 60;
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final int CENTER_WIDTH = WIDTH / 2;
    public static final int CENTER_HEIGHT = HEIGHT / 2;
    public static final ResourceBundle resources = ResourceBundle.getBundle("tetris.resources.Strings", Locale.getDefault());
    public static final String TITLE = resources.getString("title");
    private Score score;

    public Tetris() {
        super(TITLE);
        score = new Score();
    }

    public static void main(String[] argv) {
        AppGameContainer app;
        try {
            app = new AppGameContainer(new Tetris());
            app.setDisplayMode(WIDTH, HEIGHT, FULL_SCREEN);
            app.setTargetFrameRate((int)FPS);
            app.setShowFPS(SHOW_FPS);
            app.start();
        } catch (SlickException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException {
        addState(new MainMenu());
        addState(new GameScreen());
        addState(new ContinueMenu());
    }

    public Score getScore() {
        return score;
    }
}
