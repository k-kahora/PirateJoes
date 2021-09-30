package com.mygdx.game.Particles;

import com.badlogic.gdx.graphics.g2d.*;
import org.w3c.dom.ls.LSProgressEvent;

public class BulletSplash extends Sprite {

    private float start = 0;
    private Animation<TextureRegion> animation;
    private float timeElapsed;
    private boolean finished = false;
    private final float x, y;

    public BulletSplash(TextureAtlas atlas, int deg, float timeElapsed, float x, float y) {

        this.x = x;
        this.y = y;

        animation = new Animation<TextureRegion>(1/120f,atlas.getRegions());
        this.timeElapsed = timeElapsed;


        setBounds(getX(), getY(), animation.getKeyFrame(0).getRegionWidth() / 2, animation.getKeyFrame(0).getRegionHeight() / 2);
        mutatePos(deg);

    }


    // this taakes a pos based on collision and adjust the particle accordingly
    //
    private void mutatePos(int pos) {

        rotate(pos);

        switch (pos) {

            case (0) :
                setPosition(x - animation.getKeyFrame(0).getRegionWidth() / 4,y - 15);
                break;
            case (90) :
                setPosition(x + 15, y - 20);
                break;
            case (-90) :
                setPosition(x - 15, y + 20);
                break;
            case (180) :
                setPosition(x + 30, y + 15);
                break;
            default:

                break;
        }

    }

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
