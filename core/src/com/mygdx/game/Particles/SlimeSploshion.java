package com.mygdx.game.Particles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MainCharacter;
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
    private final float killradius = 45f;
    float distance = 0f;
    float centerX  = 0f;
    float centerY = 0f;

    private static final float scaleFact = 1.1f;

    public SlimeSploshion(TextureAtlas atlas, float timeElapsed, float x, float y) {

        this.x = x;
        this.y = y;

        animation = new Animation<TextureRegion>(1/8f,atlas.getRegions());
        this.timeElapsed = timeElapsed;



        setBounds(getX(), getY(), animation.getKeyFrame(0).getRegionWidth() * scaleFact, animation.getKeyFrame(0).getRegionHeight() * scaleFact);
        setPosition(x - (getWidth()/2),y - getHeight()/2);



    }


    // this taakes a pos based on collision and adjust the particle accordingly
    //

    public void update(float delta, Array<AbstractEnemy> arrs, MainCharacter character) {
        start += delta;

        centerX = getX() + getWidth() / 2;
        centerY = getY() + getHeight() / 2;

        setRegion(animation.getKeyFrame(start));
        if (animation.isAnimationFinished(start)) {
            finished = true;
        }

        for (AbstractEnemy abs : arrs) {

            distance = (float)Math.sqrt(Math.pow((abs.getX() - centerX),2) + Math.pow((abs.getY() - centerY),2));


            if (distance < killradius) {

                abs.setTagged(true);
                abs.setDetonationToInstant();
            }

        }

        distance = (float)Math.sqrt(Math.pow((character.getCenterPosition().x - centerX),2) + Math.pow((character.getCenterPosition().y - centerY),2));

        System.out.println(distance);

        if (distance < killradius) {

            Gdx.app.exit();

        }



    }

    public void dispose() {


    }

    public boolean isDone() {

        return finished;

    }

}
