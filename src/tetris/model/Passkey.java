package tetris.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by thomas on 4/5/14.
 */
public class Passkey {
    private static final int PASSKEY_LENGTH = 10;

    private Integer[] passkey;
    private Map<String, Integer> passkeyToLevel;
    private StringBuilder sb;

    public Passkey()  {
        sb = new StringBuilder();

        passkeyToLevel = new HashMap<String, Integer>();
        passkeyToLevel.put("9085823000", 10);
        passkeyToLevel.put("4157802922", 16);
        passkeyToLevel.put("0073735963", 19);

        passkey = new Integer[PASSKEY_LENGTH];

        clear();
    }

    public Integer getDigit(int index) {
        return passkey[index];
    }

    public void increment(int index) {
        passkey[index] = passkey[index] == null ? 0 : passkey[index] + 1;
        if (passkey[index] == PASSKEY_LENGTH) {
            passkey[index] = 0;
        }
    }

    public void decrement(int index) {
        passkey[index] = passkey[index] == null ? 9 : passkey[index] - 1;
        if (passkey[index] == -1) {
            passkey[index] = 9;
        }
    }

    public boolean isValidPasskey() {
        return passkeyToLevel.get(getPasskeyString()) != null;
    }

    private String getPasskeyString() {
        for (Integer integer : passkey) {
            if (integer == null) {
                return null;
            }

            sb.append(integer);
        }

        return sb.toString();
    }

    public void clear() {
        sb.setLength(0);
        for (int i = 0; i < PASSKEY_LENGTH; i++) {
            passkey[i] = null;
        }
    }
}
