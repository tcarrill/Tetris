package tetris.model;

import org.newdawn.slick.Color;

import java.util.Random;

/**
 * Created by thomas on 3/19/14.
 */
public class Particle {
    private static final Random random = new Random();
    public static final int STATE_ALIVE = 0;
    public static final int STATE_DEAD = 1;

    public static final int MIN_AGE = 10;
    public static final int MAX_AGE = 20;
    public static final int MIN_DIMENSION = 1;
    public static final int MAX_DIMENSION = 3;
    public static final int MIN_SPEED = -1;
    public static final int MAX_SPEED = 1;

    private int state = STATE_ALIVE;
    private int width;
    private int height;
    private double xv;
    private double yv;
    private int age = 0;
    private int lifetime;

    private double x, y;
    private Color color;

    public Particle(int x, int y) {
        this(x, y, new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255), 255));
    }
    public Particle(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;

        width = random.nextInt(MAX_DIMENSION) + MIN_DIMENSION;
        height = random.nextInt(MAX_DIMENSION) + MIN_DIMENSION;
        lifetime = random.nextInt(MAX_AGE) + MIN_AGE;
        xv = MIN_SPEED + (MAX_SPEED - MIN_SPEED) * random.nextDouble();
        yv = MIN_SPEED + (MAX_SPEED - MIN_SPEED) * random.nextDouble();

        if (xv * xv + yv * yv > MAX_SPEED * MAX_SPEED) {
            xv *= 0.7;
            yv *= 0.7;
        }
    }

    public void update() {
        if (state != STATE_DEAD) {
            x += xv;
            y += yv;

            int alpha = color.getAlpha();
            alpha -= random.nextInt(10) + 1;

            if (alpha <= 0) {
                state = STATE_DEAD;
            } else {
                color = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
                age++;
            }

            if (age >= lifetime) {
                state = STATE_DEAD;
            }
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getHeight() { return height; }

    public int getWidth() { return width; }

    public Color getColor() {
        return color;
    }

    public boolean isAlive() {
        return state == STATE_ALIVE;
    }
}
