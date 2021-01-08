package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.FunctionalityClasses.MyAssetManager;

public class Slime extends Actor {

    private Sprite sprite;

    private com.mygdx.game.FunctionalityClasses.MyAssetManager assetManager;

    private Animation<TextureRegion> animation;
    private float timeElapsed;
    private int index = 0;

    public Slime() {


        assetManager = new MyAssetManager();
        assetManager.load();

        assetManager.manager.finishLoading();













        animation = new Animation<TextureRegion>(1/12f,assetManager.manager.get(assetManager.slime).getRegions());
        sprite = new Sprite(animation.getKeyFrame(0));

        setBounds(getX(),getY(),sprite.getWidth(),sprite.getHeight());

    }

    @Override
    public void act(float delta) {
        timeElapsed += delta;
        // System.out.println(animation.getKeyFrame(timeElapsed, true));
        //moveBy(5f,1f);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {


        sprite.setRegion(animation.getKeyFrame(timeElapsed,true));
        sprite.draw(batch);



    }

    @Override
    protected void positionChanged() {
        sprite.setPosition(getX(),getY());
    }
}
