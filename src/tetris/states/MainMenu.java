package tetris.states;

import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import tetris.Tetris;

/**
 * Created by thomas on 3/30/14.
 */
public class MainMenu extends BaseMenu implements KeyListener {
    public static final int ID = 0;

    private static final int PLAY = 0;
    private static final int CONTINUE = 1;
    private static final int HIGHSCORES = 2;
    private int selectedItem = PLAY;

    private static final String PLAY_STRING = Tetris.resources.getString("play");
    private static final String CONTINUE_STRING = Tetris.resources.getString("continue");
    private static final String HIGHSCORES_STRING = Tetris.resources.getString("highscores");

    private StateBasedGame game;

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        game = stateBasedGame;
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics g) throws SlickException {
        menuFont.drawString(Tetris.CENTER_WIDTH - titleTextCenterWidth, 50, Tetris.TITLE, Color.yellow);

        menuFont.drawString(70, 100, PLAY_STRING, selectedItem == PLAY ? Color.red : Color.white);
        menuFont.drawString(70, 130, CONTINUE_STRING, selectedItem == CONTINUE ? Color.red : Color.white);
        menuFont.drawString(70, 160, HIGHSCORES_STRING, selectedItem == HIGHSCORES ? Color.red : Color.white);
        menuFont.drawString(Tetris.CENTER_WIDTH - legal1TextCenterWidth, 500, LEGAL1, Color.white);
        menuFont.drawString(Tetris.CENTER_WIDTH - legal2TextCenterWidth, 520, LEGAL2, Color.white);
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {

    }

    public void keyReleased(int key, char c) {
        switch(key) {
            case Input.KEY_DOWN:
                if (selectedItem == PLAY) {
                    selectedItem = CONTINUE;
                } else if (selectedItem == CONTINUE) {
                    selectedItem = HIGHSCORES;
                }
                break;
            case Input.KEY_UP:
                if (selectedItem == CONTINUE) {
                    selectedItem = PLAY;
                } else if (selectedItem == HIGHSCORES) {
                    selectedItem = CONTINUE;
                }
                break;
            case Input.KEY_ENTER:
                if (selectedItem == PLAY) {
                    game.enterState(Game.ID, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
                } else if (selectedItem == CONTINUE) {
                    game.enterState(ContinueMenu.ID);
                }
                break;
        }
    }
}
