package tetris.model;

import org.newdawn.slick.Color;

/**
 * Created by thomas on 3/19/14.
 */
public class Explosion {
    public static final int STATE_ALIVE = 0;
    public static final int STATE_DEAD = 1;

    private Particle[] particles;
    private int state;

    public Explosion(int particleNum, int x, int y) {
        this(particleNum, x, y, Color.red);
    }

    public Explosion(int particleNum, int x, int y, Color color) {
        state = STATE_ALIVE;
        particles = new Particle[particleNum];
        for (int i = 0; i < particles.length; i++) {
            particles[i] = new Particle(x, y, color);
        }
    }

    public void update() {
        boolean allParticlesDead = true;
        if (isAlive()) {
            for (Particle particle : particles) {
                if (particle.isAlive()) {
                    allParticlesDead = false;
                    particle.update();
                }
            }
        }

        if (allParticlesDead) {
            state = STATE_DEAD;
        }
    }

    public Particle[] getParticles() {
        return particles;
    }

    public boolean isAlive() {
        return state == STATE_ALIVE;
    }
}
