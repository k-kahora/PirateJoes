package com.mygdx.game.Viruses;

import com.badlogic.gdx.graphics.g2d.Batch;

public interface Enemy {

    void loadFiles();

    boolean attack();

    void updateDetection();

    boolean isDead();

    void act(float act);
    void draw(Batch batch, float alpha);

    void beenShot();

}
