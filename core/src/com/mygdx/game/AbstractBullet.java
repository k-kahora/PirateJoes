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

    public AbstractBullet(ArrayList<ArrayList<ArrayList<TileData>>> a) {

        velocity = new Vector2();
        tileDataMap = a;


    }




    public void addNumOfBounces() {
        numOfBounces++;
    }

    public int getNumOfBounces() {
        return numOfBounces;
    }

}
