package tetris.states;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.BasicGameState;
import tetris.Tetris;

/**
 * Created by thomas on 4/1/14.
 */
public abstract class BaseState extends BasicGameState {
    protected int titleTextCenterWidth;
    protected int legal1TextCenterWidth;
    protected int legal2TextCenterWidth;
    protected final String LEGAL1 = Tetris.resources.getString("legal1");
    protected final String LEGAL2 = Tetris.resources.getString("legal2");
    protected UnicodeFont menuFont;

    public BaseState() {
        try {
            menuFont = new UnicodeFont("fonts/KarmaSuture.ttf", 20, false, false);
            menuFont.addNeheGlyphs();
            menuFont.getEffects().add(new ColorEffect(java.awt.Color.white));
            menuFont.loadGlyphs();

            titleTextCenterWidth = menuFont.getWidth(Tetris.TITLE) / 2;
            legal1TextCenterWidth = menuFont.getWidth(LEGAL1) / 2;
            legal2TextCenterWidth = menuFont.getWidth(LEGAL2) / 2;
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
}
