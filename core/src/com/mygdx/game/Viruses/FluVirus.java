package com.mygdx.game.Viruses;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.mygdx.game.FunctionalityClasses.DebugDrawer;
import com.mygdx.game.FunctionalityClasses.Entity;
import com.mygdx.game.FunctionalityClasses.Level;
import com.mygdx.game.Level1;
import com.mygdx.game.SanatizerBullet;
import sun.awt.image.ImageWatched;

import javax.swing.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class FluVirus extends AbstractEnemy{

    private final Texture texture;
    private final Sprite sprite;
    private final Vector2 detectionLine;
    private final Entity target;
    private final Queue<SanatizerBullet> virusBullets;

    public float timeElapsed = 0;


    private FluVirus(Builder builder) {
        super();
        this.detectionLine = builder.detectionLine;
        this.target = builder.target;
        this.virusBullets = builder.virusBullets;
        texture = assetManager.manager.get(assetManager.fluVirus);
        this.sprite = new Sprite(texture);
        setBounds(getX(), getY(), sprite.getWidth(), sprite.getHeight());

    }

    public static class Builder {

        private final Entity target;
        private final Queue<SanatizerBullet> virusBullets;
        private final Vector2 detectionLine;

        public Builder(Entity target) {

            this.target = target;
            this.virusBullets = new LinkedList<>();
            detectionLine = new Vector2();

        }

        public FluVirus build() {

            return new FluVirus(this);

        }



    }



    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.draw(batch);
        for (SanatizerBullet shot : virusBullets
             ) {
            shot.draw(batch,0);
            shot.act(timeElapsed);
        }

    }

    @Override
    public void act(float delta) {

        timeElapsed += delta;

        System.out.println(delta);

        detectionLine.x = target.getX() - getX();
        detectionLine.y = target.getY() - getY();

        moveBy(0.2f,0.1f);

        // for debugging
        // drawDetectionLine();

        if (timeElapsed > 2f) {
            virusBullets.add(new SanatizerBullet.Builder(getX(), getY(), detectionLine, 2f,assetManager.manager.get(assetManager.bulletSprite)).build());
            timeElapsed = 0;
        }

    }

    private void drawDetectionLine() {

        DebugDrawer.DrawDebugLine(new Vector2(target.getX(), target.getY()),new Vector2(getX() + getWidth()/2, getY() + getWidth()/2),10,Color.PINK, Level1.getViewport().getCamera().combined);

    }

    @Override
    protected void positionChanged() {
        sprite.setPosition(getX(), getY());
    }

    @Override
    public boolean attack() {
        return false;
    }

    @Override
    public void updateDetection() {

    }
}
