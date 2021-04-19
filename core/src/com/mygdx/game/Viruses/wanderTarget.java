package com.mygdx.game.Viruses;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.FunctionalityClasses.EntityLocation;
import com.mygdx.game.utils.SteeringUtils;

public class wanderTarget implements EntityLocation {

    private Vector2 position;

    public wanderTarget(float x, float y) {

        position = new Vector2(x,y);

    }

    @Override
    public float getOrientation() {
        return 0;
    }

    @Override
    public void setOrientation(float orientation) {

    }

    @Override
    public float vectorToAngle(Vector2 vector) {
        return SteeringUtils.vectorToAngle(vector);
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        return SteeringUtils.angleToVector(outVector, angle);
    }

    @Override
    public Location<Vector2> newLocation() {
        return null;
    }

    @Override
    public Rectangle getBoundingBox() {
        return null;
    }

    @Override
    public void drawDebugBox() {

    }

    @Override
    public void setBoundingBox(float x, float y, float width, float height) {

    }

    @Override
    public Vector2 getVelocity() {
        return null;
    }

    @Override
    public boolean collisionLogic() {
        return false;
    }

    @Override
    public float getX() {
        return position.x;
    }

    @Override
    public float getY() {
        return position.y;
    }

    @Override
    public Vector2 getPosition() {
        return position;
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
}
