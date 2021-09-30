package com.mygdx.game.FunctionalityClasses;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;



public interface Entity {

    public Rectangle getBoundingBox();

    public void drawDebugBox();

    public void setBoundingBox(float x, float y, float width, float height);

    public Vector2 getVelocity();

    boolean collisionLogic();

    public float getX();

    public float getY();

    public Vector2 getPosition();


    void bottomCollision(int x, int y);
    void topCollision(int x, int y);
    void rightCollision(int x, int y);
    void leftCollision(int x, int y);



}
