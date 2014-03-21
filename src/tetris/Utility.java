package tetris;

/**
 * Created by thomas on 3/20/14.
 */
public class Utility {
    public static double randomDouble(double min, double max) {
        return min + Math.random() * ((max - min) + 1);
    }

    public static int randomInt(int min, int max) {
        return min + (int)Math.random() * ((max - min) + 1);
    }

    public static float randomFloat(float min, float max) {
        return min + (float)Math.random() * ((max - min) + 1);
    }
}
