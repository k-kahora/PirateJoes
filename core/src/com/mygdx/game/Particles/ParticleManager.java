package com.mygdx.game.Particles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MainCharacter;
import com.mygdx.game.Viruses.AbstractEnemy;


import java.util.ArrayList;
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
    public static void updateSlimeSploshions (SpriteBatch batch, float delta, Array<AbstractEnemy> abstractEnemies, MainCharacter character) {


        for (SlimeSploshion splash : ParticleManager.slimeBlowingUp) {

            if (!splash.isDone()) {
                splash.update(delta, abstractEnemies, character);
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

    public static void clear() {

        splashBullets.clear();
        slimeBlowingUp.clear();

    }

}
