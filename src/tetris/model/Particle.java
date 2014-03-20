package tetris.model;

import org.newdawn.slick.Color;

/**
 * Created by thomas on 3/19/14.
 */
public class Particle {
    public static final int STATE_ALIVE = 0;
    public static final int STATE_DEAD = 1;

    public static final int DEFAULT_LIFETIME = 200;
    public static final int MIN_DIMENSION = 1;
    public static final int MAX_DIMENSION = 5;
    public static final int MIN_SPEED = -10;
    public static final int MAX_SPEED = 10;

    private int state;
    private int width;
    private int height;
    private double x, y;
    private double xv, yv;
    private int age;
    private int lifetime;
    private Color color;

    public Particle(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.state = Particle.STATE_ALIVE;
        this.width = (int)(MIN_DIMENSION + Math.random() * (MAX_DIMENSION - MIN_DIMENSION));
        this.height = (int)(MIN_DIMENSION + Math.random() * (MAX_DIMENSION - MIN_DIMENSION));
        this.lifetime = DEFAULT_LIFETIME;
        this.age = 0;
        this.xv = MIN_SPEED + Math.random() * (MAX_SPEED - MIN_SPEED);
        this.yv = MIN_SPEED + Math.random() * (MAX_SPEED - MIN_SPEED);

        if (xv * xv + yv * yv > MAX_SPEED * MAX_SPEED) {
            xv *= 0.7;
            yv *= 0.7;
        }
        this.color = color;
    }

    public void update() {
        if (state != STATE_DEAD) {
            x += xv;
            y += yv;

            int a = color.getAlpha();
            a -= 2;
            if (a <= 0) {
                state = STATE_DEAD;
            } else {
                color = new Color(color.getRed(), color.getGreen(), color.getBlue(), a);
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
