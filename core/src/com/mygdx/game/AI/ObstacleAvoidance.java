package com.mygdx.game.AI;

import com.badlogic.gdx.ai.steer.behaviors.RaycastObstacleAvoidance;
import com.badlogic.gdx.ai.utils.Collision;
import com.badlogic.gdx.ai.utils.Ray;
import com.badlogic.gdx.ai.utils.RaycastCollisionDetector;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.FunctionalityClasses.DebugDrawer;
import com.mygdx.game.FunctionalityClasses.RayCast;
import com.mygdx.game.Levels.AbstractLevel;
import com.mygdx.game.Tiles.TileData;

import java.awt.*;
import java.util.ArrayList;

public class ObstacleAvoidance implements RaycastCollisionDetector<Vector2> {

    ArrayList<ArrayList<TileData>> world;

    public ObstacleAvoidance(ArrayList<ArrayList<TileData>> world) {

        this.world = world;

    }


    @Override
    public boolean collides(Ray<Vector2> ray) {

        return findCollision(null, ray);

    }

    @Override
    public boolean findCollision(Collision<Vector2> outputCollision, Ray<Vector2> inputRay) {

        Vector2 end  = RayCast.castRay(inputRay.start, RayCast.castDirection(inputRay.start, inputRay.end), world);

        DebugDrawer.DrawDebugLine(inputRay.start, end, 5, Color.PURPLE, AbstractLevel.getViewport().getCamera().combined);

        return true;
    }
}
