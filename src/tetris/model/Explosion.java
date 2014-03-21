package tetris.model;

import org.newdawn.slick.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by thomas on 3/19/14.
 */
public class Explosion {
    private static final Random random = new Random();
    public static final int STATE_ALIVE = 0;
    public static final int STATE_DEAD = 1;

    private List<Particle> particles;
    private int state;

    public Explosion(int particleNum, int x, int y) {
        state = STATE_ALIVE;
        particles = new ArrayList<Particle>(particleNum);
        for (int i = 0; i < particleNum; i++) {
            particles.add(new Particle(x, y));
        }
    }

    public Explosion(int particleNum, int x, int y, Color color) {
        state = STATE_ALIVE;
        particles = new ArrayList<Particle>(particleNum);
        for (int i = 0; i < particleNum; i++) {
            particles.add(new Particle(x, y, color));
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
            particles.clear();
        }
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public boolean isAlive() {
        return state == STATE_ALIVE;
    }
}
