package tetris.states;

import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;
import tetris.Tetris;
import tetris.model.Passkey;

/**
 * Created by thomas on 3/30/14.
 */
public class ContinueMenu extends BaseState implements KeyListener {
    public static final int ID = 2;

    private static final int BACK = -1;
    private int selectedItem = BACK;

    private static final String BACK_STRING = Tetris.resources.getString("back");
    private static final String PASSKEY_STRING = Tetris.resources.getString("passkey");

    protected int passkeyTextCenterWidth;
    private Tetris game;
    private Passkey passkey;

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        game = (Tetris)stateBasedGame;
        passkeyTextCenterWidth = menuFont.getWidth(PASSKEY_STRING) / 2;
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        super.enter(container, game);
        if (passkey == null) {
            passkey = new Passkey();
        } else {
            passkey.clear();
        }
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics g) throws SlickException {
        menuFont.drawString(Tetris.CENTER_WIDTH - titleTextCenterWidth, 50, Tetris.TITLE, Color.yellow);

        menuFont.drawString(70, 100, BACK_STRING, selectedItem == BACK ? Color.red : Color.white);
        menuFont.drawString(Tetris.CENTER_WIDTH - passkeyTextCenterWidth, 120, PASSKEY_STRING, Color.white);

        int startX = 285;
        for (int i = 0; i < 10; i++) {
            menuFont.drawString(startX, 200, passkey.getDigit(i) == null ? "_" : passkey.getDigit(i).toString(), selectedItem == i ? Color.red : Color.white);
            startX += (i == 2 || i == 5) ? 40 : 20;
        }

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
                    game.getScore().setLevel(passkey.getLevel());
                    game.enterState(MainMenu.ID);
                }
                break;
            case Input.KEY_UP:
                if (selectedItem != BACK) {
                    passkey.increment(selectedItem);
                }
                break;
            case Input.KEY_DOWN:
                if (selectedItem == BACK) {
                    selectedItem = 0;
                } else {
                    passkey.decrement(selectedItem);
                }
                break;
            case Input.KEY_LEFT:
                if (selectedItem != BACK) {
                    selectedItem--;
                }
                break;
            case Input.KEY_RIGHT:
                if (selectedItem != BACK) {
                    selectedItem++;
                }

                if (selectedItem == 10) {
                    selectedItem = BACK;
                }
                break;
        }
    }
}
