package tetris.states;

import org.newdawn.slick.*;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import tetris.Tetris;

/**
 * Created by thomas on 3/30/14.
 */
public class MainMenu extends BasicGameState implements KeyListener {
    public static final int ID = 0;
    private StateBasedGame game;
    private static final int PLAY = 0;
    private static final int HIGHSCORES = 1;
    private int selectedItem = PLAY;
    private UnicodeFont font;
    private static final String PLAY_STRING = Tetris.resources.getString("play");
    private static final String HIGHSCORES_STRING = Tetris.resources.getString("highscores");

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        game = stateBasedGame;
        font = new UnicodeFont("fonts/ArcadeClassic.ttf", 20, false, false);
        font.addAsciiGlyphs();
        font.getEffects().add(new ColorEffect(java.awt.Color.white));
        font.loadGlyphs();
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics g) throws SlickException {
        g.setColor(Color.white);
        g.drawString(Tetris.TITLE, Tetris.CENTER_WIDTH, 10);

        font.drawString(50, 100, PLAY_STRING, selectedItem == PLAY ? Color.red : Color.white);
        font.drawString(50, 120, HIGHSCORES_STRING, selectedItem == HIGHSCORES ? Color.red : Color.white);
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {

    }

    public void keyReleased(int key, char c) {
        switch(key) {
            case Input.KEY_DOWN:
                if (selectedItem == PLAY) {
                    selectedItem = HIGHSCORES;
                }
                break;
            case Input.KEY_UP:
                if (selectedItem == HIGHSCORES) {
                    selectedItem = PLAY;
                }
                break;
            case Input.KEY_ENTER:
                if (selectedItem == PLAY) {
                    game.enterState(Game.ID, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
                }
                break;
        }
    }
}
