package com.mygdx.game.Particles;

import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Viruses.AbstractEnemy;
import org.w3c.dom.Entity;
import org.w3c.dom.ls.LSProgressEvent;

import java.util.ArrayList;
import java.util.Vector;

public class SlimeSploshion extends Sprite {

    private float start = 0;
    private Animation<TextureRegion> animation;
    private float timeElapsed;
    private boolean finished = false;
    private final float x, y;
    private final float killradius = 80f;

    private static final float scaleFact = 1.1f;

    public SlimeSploshion(TextureAtlas atlas, float timeElapsed, float x, float y) {

        this.x = x;
        this.y = y;

        animation = new Animation<TextureRegion>(1/80f,atlas.getRegions());
        this.timeElapsed = timeElapsed;



        setBounds(getX(), getY(), animation.getKeyFrame(0).getRegionWidth() * scaleFact, animation.getKeyFrame(0).getRegionHeight() * scaleFact);
        setPosition(x - (getWidth()/2),y - getHeight()/2);



    }


    // this taakes a pos based on collision and adjust the particle accordingly
    //

    public void update(float delta, Array<AbstractEnemy> arrs) {
        start += delta;
        setRegion(animation.getKeyFrame(start));
        if (animation.isAnimationFinished(start)) {
            finished = true;
        }

        for (AbstractEnemy abs : arrs) {

            if (new Vector2(abs.getX() - getX(), abs.getY() - getY()).len() < killradius) {



                abs.setTagged(true);
                abs.setDetonationToInstant();
            }

        }
    }

    public void dispose() {


    }

    public boolean isDone() {

        return finished;

    }

}
