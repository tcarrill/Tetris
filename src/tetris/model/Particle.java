package tetris.model;

import org.newdawn.slick.Color;

import java.util.Random;

/**
 * Created by thomas on 3/19/14.
 */
public class Particle {
    public static final int STATE_ALIVE = 0;	// particle is alive
    public static final int STATE_DEAD = 1;		// particle is dead

    public static final int DEFAULT_LIFETIME = 200;	// play with this
    public static final int MAX_DIMENSION = 5;	// the maximum width or height
    public static final int MIN_SPEED = 1;
    public static final int MAX_SPEED = 2;	// maximum speed (per update)

    private int state;			// particle is alive or dead
    private int width;		// width of the particle
    private int height;		// height of the particle
    private int x, y;			// horizontal and vertical position
    private double xv, yv;		// vertical and horizontal velocity
    private int age;			// current age of the particle
    private int lifetime;		// particle dies when it reaches this value
    private Color color;			// the color of the particle

    public Particle(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.state = Particle.STATE_ALIVE;
        this.width = 1;
        this.height = 1;
        this.lifetime = DEFAULT_LIFETIME;
        this.age = 0;
        Random random = new Random();
        this.xv = MIN_SPEED + Math.random() * (MAX_SPEED - MIN_SPEED);
        this.yv = MIN_SPEED + Math.random() * (MAX_SPEED - MIN_SPEED);
        // smoothing out the diagonal speed
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

    public int getX() {
        return x;
    }

    public int getY() {
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
