package tetris.states;

import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;
import tetris.Tetris;

/**
 * Created by thomas on 3/30/14.
 */
public class ContinueMenu extends BaseMenu implements KeyListener {
    public static final int ID = 2;

    private static final int BACK = -1;
    private int selectedItem = BACK;
    private static final String BACK_STRING = Tetris.resources.getString("back");
    private static final String PASSKEY_STRING = Tetris.resources.getString("passkey");
    protected int passkeyTextCenterWidth;
    private StateBasedGame game;

    private static final String[][] KEYPAD = new String[][]  {
            {"7", "8", "9"},
            {"4", "5", "6"},
            {"1", "2", "3"},
            {"-2", "0", "-2"}
    };

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        game = stateBasedGame;
        passkeyTextCenterWidth = menuFont.getWidth(PASSKEY_STRING) / 2;
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics g) throws SlickException {
        menuFont.drawString(Tetris.CENTER_WIDTH - titleTextCenterWidth, 50, Tetris.TITLE, Color.yellow);

        menuFont.drawString(70, 100, BACK_STRING, selectedItem == BACK ? Color.red : Color.white);
        menuFont.drawString(Tetris.CENTER_WIDTH - passkeyTextCenterWidth, 120, PASSKEY_STRING, Color.white);

        int x = 344;
        int y = 200;
        for (String[] row : KEYPAD) {
            for (String key : row) {
                if (!key.equals("-2")) {
                    menuFont.drawString(x, y, key,  selectedItem == Integer.parseInt(key) ? Color.red : Color.white);
                }
                x += 50;
            }
            x = 344;
            y += 50;
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
                    game.enterState(MainMenu.ID);
                }
                break;
            case Input.KEY_UP:
                if (selectedItem == 7 || selectedItem == 8 || selectedItem == 9) {
                    selectedItem = BACK;
                } else if (selectedItem == 0) {
                    selectedItem = 2;
                } else if (selectedItem == BACK) {

                } else {
                    selectedItem += 3;
                }
                break;
            case Input.KEY_DOWN:
                if (selectedItem == BACK) {
                    selectedItem = 5;
                } else if (selectedItem == 1 || selectedItem == 2) {
                    selectedItem = 0;
                } else if (selectedItem == 0) {
                    selectedItem = 8;
                } else {
                    selectedItem -= 3;
                }
                break;
            case Input.KEY_LEFT:
                if (selectedItem == 8 || selectedItem == 5 || selectedItem == 2 ||
                    selectedItem == 9 || selectedItem == 6 || selectedItem == 3) {
                    selectedItem -= 1 ;
                }
                break;
            case Input.KEY_RIGHT:
                if (selectedItem == 7 || selectedItem == 4 || selectedItem == 1 ||
                        selectedItem == 8 || selectedItem == 5 || selectedItem == 2) {
                    selectedItem += 1 ;
                }
                break;

        }
    }
}
