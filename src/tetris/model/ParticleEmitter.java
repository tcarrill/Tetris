package tetris.model;

import org.newdawn.slick.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thomas on 3/19/14.
 */
public class ParticleEmitter {
    public static final int STATE_ALIVE = 0;
    public static final int STATE_DEAD = 1;

    private List<Particle> particles;
    private int state;

    public ParticleEmitter(int particleNum, int x, int y, Color color) {
        this(particleNum, x, y, color, STATE_ALIVE);
    }

    public ParticleEmitter(int particleNum, int x, int y, Color color, int state) {
        this.state = state;
        particles = new ArrayList<Particle>(particleNum);
        for (int i = 0; i < particleNum; i++) {
            particles.add(new Particle(x, y, color));
        }
    }

    public void initialize(int x, int y, Color color) {
        //System.out.println("Reusing Emitter "+this.hashCode());
        state = STATE_ALIVE;
        for (Particle particle : particles) {
            particle.initialize(x, y, color);
        }
    }

    public void update() {
        if (isAlive()) {
            boolean allParticlesDead = true;

            for (Particle particle : particles) {
                if (particle.isAlive()) {
                    allParticlesDead = false;
                    particle.update();
                }
            }

            if (allParticlesDead) {
                state = STATE_DEAD;
            }
        }
    }

    public void killAll() {
        for (Particle particle : particles) {
            particle.kill();
        }

        state = STATE_DEAD;
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public boolean isAlive() {
        return state == STATE_ALIVE;
    }
}
