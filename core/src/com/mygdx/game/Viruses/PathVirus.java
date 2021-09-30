package com.mygdx.game.Viruses;

import com.badlogic.gdx.ai.steer.utils.Path;
import com.badlogic.gdx.math.Vector2;

public class PathVirus implements Path<Vector2, Path.PathParam> {
    @Override
    public PathParam createParam() {
        return null;
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public float getLength() {
        return 0;
    }

    @Override
    public Vector2 getStartPoint() {
        return null;
    }

    @Override
    public Vector2 getEndPoint() {
        return null;
    }

    @Override
    public float calculateDistance(Vector2 position, PathParam param) {
        return 0;
    }

    @Override
    public void calculateTargetPosition(Vector2 out, PathParam param, float targetDistance) {

    }
}
