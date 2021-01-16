package com.mygdx.game.Viruses;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.behaviors.PrioritySteering;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.FunctionalityClasses.*;
import com.mygdx.game.Levels.Level;
import com.mygdx.game.utils.SteeringUtils;

public abstract class AbstractEnemy extends Actor implements EntitySteerable, Telegraph {


    private SpriteBatch sprite;
    private boolean tagged;
    private Rectangle boundingBox;
    public  MyAssetManager assetManager = new MyAssetManager();
    Entity target;
    Vector2 velocity;
    private Level level;



    public AbstractEnemy(Entity target, Level currentLevel) {
        this.target = target;
        this.boundingBox = new Rectangle();
        this.level = currentLevel;
        assetManager.loadEnemys();
        assetManager.loadCharecter();
        assetManager.manager.finishLoading();


    }

    public abstract boolean handleMessage(Telegram telegram);

    public abstract boolean initRayCollision();

    // must be supered by extended classes to set a boundingBox;
    @Override
    public void setBoundingBox(float x, float y, float width, float height) {

        boundingBox.x = x;
        boundingBox.y = y;
        boundingBox.width = width;
        boundingBox.height = height;

    }


    Vector2 position;
    float orientation;
    Vector2 linearVelocity;
    float angularVelocity;
    float maxSpeed;
    float maxLinearAcceleration;
    boolean independentFacing;
    PrioritySteering<Vector2> steeringBehavior;

    static final SteeringAcceleration<Vector2> steeringOutput =
            new SteeringAcceleration<Vector2>(new Vector2());

    @Override
    public final void update(float delta) {
        if (boundingBox != null) {

            setBoundingBox(getX(), getY(), getWidth(), getHeight());

        }
        if (steeringBehavior != null) {
            steeringBehavior.calculateSteering(steeringOutput);
            applySteering(delta);
        }
    }

    @Override
    public final void applySteering(float delta) {
        boolean anyAccerlaeration = false;

        // if there is a steeringOutput velocity then apply
        if (!steeringOutput.linear.isZero()) {
            steeringOutput.linear.scl(delta);
            velocity.x = steeringOutput.linear.x;
            velocity.y = steeringOutput.linear.y;
            moveBy(velocity.x, velocity.y);
            anyAccerlaeration = true;
        }

        // makes sure the linear speed never exceed the maxSpeed
        /*
        if (anyAccerlaeration) {
            velocity = getLinearVelocity();
            float currentSpeedSquared = velocity.len2();
            if (currentSpeedSquared > Math.pow(maxSpeed, 2)) {
                setMaxLinearSpeed(maxSpeed / (float) Math.sqrt(currentSpeedSquared));
            }
        }

         */
    }

    public Level getLevel() {
        return level;
    }

    @Override
    public Vector2 getLinearVelocity() {
        return velocity;
    }

    @Override
    public float getAngularVelocity() {
        return 0;
    }

    @Override
    public float getBoundingRadius() {
        return 1.5f;
    }

    @Override
    public boolean isTagged() {
        return tagged;
    }

    @Override
    public void setTagged(boolean tagged) {
        this.tagged = tagged;
    }

    @Override
    public float getZeroLinearSpeedThreshold() {
        return 0;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float value) {

    }

    @Override
    public float getMaxLinearSpeed() {
        return maxSpeed;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        this.maxSpeed = maxLinearSpeed;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return this.maxLinearAcceleration;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {

        this.maxLinearAcceleration = maxLinearAcceleration;

    }

    @Override
    public float getMaxAngularSpeed() {
        return 0;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {

    }

    @Override
    public float getMaxAngularAcceleration() {
        return 0;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {

    }

    @Override
    public Vector2 getPosition() {
        return new Vector2(getX(), getY());
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

    // Entity interface

    @Override
    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    @Override
    public void drawDebugBox() {
        DebugDrawer.DrawDebugRectangle(getX(), getY(), getWidth(), getHeight(), Color.PURPLE, level.getViewport().getCamera().combined);
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
