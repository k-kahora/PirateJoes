package com.mygdx.game.Viruses;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Group;

import javax.swing.*;

public class FluVirus extends AbstractEnemy{

    private final Texture texture;
    private final Sprite sprite;


    public FluVirus() {
        super();
        texture = assetManager.manager.get(assetManager.fluVirus);
        sprite = new Sprite(texture);
        setBounds(getX(), getY(), sprite.getWidth(), sprite.getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.draw(batch);
        System.out.println("Drawing Virus");
    }

    @Override
    protected void positionChanged() {
        sprite.setPosition(getX(), getY());
    }
}
