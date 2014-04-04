package tetris.states;

import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;
import tetris.Tetris;

import java.util.HashMap;
import java.util.Map;

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
    private Tetris game;
    private Integer[] passkey;
    private Map<String, Integer> passkeys;
    private StringBuilder sb;

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
        if (sb == null) {
            sb = new StringBuilder();
        }

        if (passkeys == null) {
            passkeys = new HashMap<String, Integer>();
            passkeys.put("0073735963", 21);
        }

        passkey = new Integer[10];
        sb.setLength(0);
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics g) throws SlickException {
        menuFont.drawString(Tetris.CENTER_WIDTH - titleTextCenterWidth, 50, Tetris.TITLE, Color.yellow);

        menuFont.drawString(70, 100, BACK_STRING, selectedItem == BACK ? Color.red : Color.white);
        menuFont.drawString(Tetris.CENTER_WIDTH - passkeyTextCenterWidth, 120, PASSKEY_STRING, Color.white);

        int startX = 285;
        for (int i = 0; i < 10; i++) {
            menuFont.drawString(startX, 200, passkey[i] == null ? "_" : passkey[i].toString(), selectedItem == i ? Color.red : Color.white);
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
                    for (Integer integer : passkey) {
                        if (integer == null) {
                            break;
                        }

                        sb.append(integer);
                    }

                    if (sb.length() == 10) {
                        Integer level = passkeys.get(sb.toString());

                        if (level != null) {
                            System.out.println("PASS KEY ACTIVATED - LEVEL: " + level);
                            game.getScore().setLevel(level);
                        }
                    }

                    game.enterState(MainMenu.ID);
                }
                break;
            case Input.KEY_UP:
                if (selectedItem != BACK) {
                    passkey[selectedItem] = passkey[selectedItem] == null ? 0 : passkey[selectedItem] + 1;
                    if (passkey[selectedItem] > 9) {
                        passkey[selectedItem] = 0;
                    }
                }
                break;
            case Input.KEY_DOWN:
                if (selectedItem == BACK) {
                    selectedItem = 0;
                } else {
                    passkey[selectedItem] = passkey[selectedItem] == null ? 9 : passkey[selectedItem] - 1;
                    if (passkey[selectedItem] < 0) {
                        passkey[selectedItem] = 9;
                    }
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
