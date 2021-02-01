package com.mygdx.game.Particles;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class ParticleManager {

    public static final List<BulletSplash> splashBullets = new LinkedList<>();

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
