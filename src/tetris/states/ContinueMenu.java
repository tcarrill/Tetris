package tetris.states;

import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;
import tetris.Tetris;

/**
 * Created by thomas on 3/30/14.
 */
public class ContinueMenu extends BaseMenu implements KeyListener {
    public static final int ID = 2;

    private static final int BACK = 1;
    private int selectedItem = BACK;
    private static final String BACK_STRING = Tetris.resources.getString("back");
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

        menuFont.drawString(70, 100, BACK_STRING, selectedItem == BACK ? Color.red : Color.white);

        menuFont.drawString(Tetris.CENTER_WIDTH - legal1TextCenterWidth, 500, LEGAL1, Color.white);
        menuFont.drawString(Tetris.CENTER_WIDTH - legal2TextCenterWidth, 520, LEGAL2, Color.white);
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {

    }

    public void keyReleased(int key, char c) {
        switch(key) {
            case Input.KEY_ENTER:
                if (selectedItem == BACK) {
                    game.enterState(MainMenu.ID);
                }
                break;
        }
    }
}
