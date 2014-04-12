package tetris.model;

import org.newdawn.slick.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thomas on 4/11/14.
 */
public class ParticleSystem {
    private List<ParticleEmitter> emitters;

    public ParticleSystem() {
        emitters = new ArrayList<ParticleEmitter>(10);
        for (int i = 0; i < 10; i++) {
            ParticleEmitter emitter = new ParticleEmitter(50, 0, 0, Color.black, ParticleEmitter.STATE_DEAD);
            emitters.add(emitter);
        }
    }

    public void addEmitter(int numParticles, int x, int y, Color color) {
        if (emitters == null) {
            emitters = new ArrayList<ParticleEmitter>();
            emitters.add(new ParticleEmitter(numParticles, x, y, color));
        } else {
            boolean hasUsableEmitter = false;
            for (ParticleEmitter emitter : emitters) {
                if (!emitter.isAlive()) {
                    emitter.initialize(x, y, color);
                    hasUsableEmitter = true;
                }
            }

            if (!hasUsableEmitter) {
                System.out.println("NO USABLE EMITTERS FOUND!!!");
                emitters.add(new ParticleEmitter(numParticles, x, y, color));
            }
        }
    }

    public List<ParticleEmitter> getParticleEmitters() {
        return emitters;
    }

    public void update() {
        if (emitters != null) {
            for(ParticleEmitter emitter : emitters) {
                emitter.update();
            }
        }
    }

    public void killAll() {
        if (emitters != null) {
            for(ParticleEmitter emitter : emitters) {
                emitter.killAll();
            }
        }
    }
}
