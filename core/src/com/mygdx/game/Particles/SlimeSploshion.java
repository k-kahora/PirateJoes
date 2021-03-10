package com.mygdx.game.Particles;

import com.badlogic.gdx.graphics.g2d.*;
import org.w3c.dom.ls.LSProgressEvent;

public class SlimeSploshion extends Sprite {

    private float start = 0;
    private Animation<TextureRegion> animation;
    private float timeElapsed;
    private boolean finished = false;
    private final float x, y;

    private static final float scaleFact = 1.2f;

    public SlimeSploshion(TextureAtlas atlas, float timeElapsed, float x, float y) {

        this.x = x;
        this.y = y;

        animation = new Animation<TextureRegion>(1/40f,atlas.getRegions());
        this.timeElapsed = timeElapsed;



        setBounds(getX(), getY(), animation.getKeyFrame(0).getRegionWidth() * scaleFact, animation.getKeyFrame(0).getRegionHeight() * scaleFact);
        setPosition(x - (getWidth()/2),y - getHeight()/2);



    }


    // this taakes a pos based on collision and adjust the particle accordingly
    //

    public void update(float delta) {
        start += delta;
        setRegion(animation.getKeyFrame(start));
        if (animation.isAnimationFinished(start)) {
            finished = true;
        }
    }

    public void dispose() {


    }

    public boolean isDone() {

        return finished;

    }

}
