package com.mygdx.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

public interface Entity {

    public Rectangle getBoundingBox();

    public void drawDebugBox();

    public Vector2 getVelocity();

    boolean collisionLogic();


    void bottomCollision(int x, int y);
    void topCollision(int x, int y);
    void rightCollision(int x, int y);
    void leftCollision(int x, int y);


}
