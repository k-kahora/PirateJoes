package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.FunctionalityClasses.Entity;
import com.mygdx.game.Tiles.TileData;
import org.graalvm.compiler.core.common.type.ArithmeticOpTable;

import java.util.ArrayList;

public abstract class AbstractBullet extends Actor implements Entity {

    private final Vector2 velocity;
    private int numOfBounces;
    public final ArrayList<ArrayList<ArrayList<TileData>>> tileDataMap;
    final Rectangle defaultRectangle;

    public AbstractBullet(ArrayList<ArrayList<ArrayList<TileData>>> a, Vector2 direction) {

        velocity = direction.nor().scl(10f);
        tileDataMap = a;
        defaultRectangle = new Rectangle();



    }

    public AbstractBullet(ArrayList<ArrayList<ArrayList<TileData>>> a) {

        velocity = new Vector2();
        tileDataMap = a;
        defaultRectangle = new Rectangle();



    }



    @Override
    public void act(float delta) {

        defaultRectangle.x = getX();
        defaultRectangle.y = getY();

        moveBy(velocity.x, velocity.y);

    }

    public void addNumOfBounces() {
        numOfBounces++;
    }

    public int getNumOfBounces() {
        return numOfBounces;
    }

    @Override
    public void bottomCollision(int x, int y) {

    }

    @Override
    public void topCollision(int x, int y) {

    }

    @Override
    public void rightCollision(int x, int y) {

    }

    @Override
    public void leftCollision(int x, int y) {

    }

    @Override
    public Vector2 getVelocity() {
        return velocity;
    }
}
