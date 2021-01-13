package com.mygdx.game.Tiles;

import com.badlogic.gdx.ai.utils.Collision;
import com.badlogic.gdx.ai.utils.Ray;
import com.badlogic.gdx.ai.utils.RaycastCollisionDetector;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Enumerators.Tile;

import java.util.ArrayList;

public class RayCollisionDetection implements RaycastCollisionDetector<Vector2> {

    private ArrayList<ArrayList<TileData>> map;

    public RayCollisionDetection(ArrayList<ArrayList<TileData>> a ) {
        map = a;
    }


    @Override
    public boolean collides(Ray<Vector2> ray) {
        findCollision(null, ray);

        return false;
    }

    @Override
    public boolean findCollision(Collision<Vector2> outputCollision, Ray<Vector2> inputRay) {

        int rayPointX = (int)(inputRay.end.x / 16);
        int rayPointY = (int)(inputRay.end.y / 16);

        System.out.println(map.get(rayPointY).get(rayPointX).getTile());
        if (!map.get(rayPointY).get(rayPointX).getTile().isCollideable().contains(Tile.INVISIBLE)) {

            return false;
        }

        return true;
    }
}
