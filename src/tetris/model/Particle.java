package tetris.model;

import org.newdawn.slick.Color;

/**
 * Created by thomas on 3/19/14.
 */
public class Particle {
    public static final int STATE_ALIVE = 0;
    public static final int STATE_DEAD = 1;

    public static final int DEFAULT_LIFETIME = 500;
    public static final int MIN_DIMENSION = 1;
    public static final int MAX_DIMENSION = 5;
    public static final int MIN_SPEED = -1;
    public static final int MAX_SPEED = 1;

    private int state = STATE_ALIVE;
    private int width = (int)(MIN_DIMENSION + Math.random() * (MAX_DIMENSION - MIN_DIMENSION));
    private int height = (int)(MIN_DIMENSION + Math.random() * (MAX_DIMENSION - MIN_DIMENSION));
    private double xv = MIN_SPEED + Math.random() * (MAX_SPEED - MIN_SPEED);
    private double yv = MIN_SPEED + Math.random() * (MAX_SPEED - MIN_SPEED);
    private int age = 0;
    private int lifetime = DEFAULT_LIFETIME;

    private double x, y;
    private Color color;

    public Particle(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;

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
            alpha -= 2;

            if (alpha <= 0) {
                state = STATE_DEAD;
            } else  {
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
