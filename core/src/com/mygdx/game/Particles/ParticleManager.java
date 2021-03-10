package com.mygdx.game.Particles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class ParticleManager {

    public static final List<BulletSplash> splashBullets = new LinkedList<>();
    public static final List<SlimeSploshion> slimeBlowingUp = new LinkedList<>();

    public static void updateBulletSplashes (SpriteBatch batch, float delta) {


        for (BulletSplash splash : ParticleManager.splashBullets) {

            if (!splash.isDone()) {
                splash.update(delta);
                splash.draw(batch);
            }

        }
    }
    public static void updateSlimeSploshions (SpriteBatch batch, float delta) {


        for (SlimeSploshion splash : ParticleManager.slimeBlowingUp) {

            if (!splash.isDone()) {
                splash.update(delta);
                splash.draw(batch);
            }

        }
    }


    public static class BulletCollisions {

        final float animationRate = 1/60f;
        final Deque<BulletSplash> bulletParticles = new LinkedList<>();

        public BulletCollisions() {


        }

        void addParticle(BulletSplash a) {
            bulletParticles.add(a);
        }




    }

}
