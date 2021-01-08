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

import javax.swing.*;

public class FluVirus extends AbstractEnemy{

    private final Texture texture;
    private final Sprite sprite;
    private final Vector2 detectionLine;
    private final Entity target;



    private FluVirus(Builder builder) {
        super();
        this.detectionLine = builder.detectionLine;
        this.target = builder.target;
        texture = assetManager.manager.get(assetManager.fluVirus);
        this.sprite = new Sprite(texture);
        setBounds(getX(), getY(), sprite.getWidth(), sprite.getHeight());

    }

    public static class Builder {

        private final Entity target;

        private final Vector2 detectionLine;

        public Builder(Entity target) {


            this.target = target;
            detectionLine = new Vector2();

        }


        public FluVirus build() {

            return new FluVirus(this);

        }



    }



    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.draw(batch);
        System.out.println("Drawing Virus");
    }

    @Override
    public void act(float delta) {

        detectionLine.x = target.getX() - getX();
        detectionLine.y = target.getY() - getY();

        moveBy(0.2f,0.1f);

        drawDetectionLine();

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
