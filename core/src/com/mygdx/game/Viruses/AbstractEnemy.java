package com.mygdx.game.Viruses;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.behaviors.PrioritySteering;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.FunctionalityClasses.*;
import com.mygdx.game.Levels.Level;
import com.mygdx.game.Levels.Level1;
import com.mygdx.game.SanatizerBullet;
import com.mygdx.game.Tiles.TileData;
import com.mygdx.game.utils.SteeringUtils;
import sun.awt.image.ImageWatched;

import java.util.*;

public abstract class AbstractEnemy extends Actor implements EntitySteerable, Telegraph {

    private SpriteBatch sprite;
    private boolean tagged;
    private Rectangle boundingBox;
    public MyAssetManager assetManager = new MyAssetManager();
    Entity target;
    Vector2 velocity;
    private LinkedList<SanatizerBullet> virusBullets;
    final List<SanatizerBullet> removeBullets;
    final ArrayList<ArrayList<ArrayList<TileData>>> fluVirusTileColliderMap;
    final Circle detectionCircle;
    final PrioritySteering<Vector2> prioritySteering;

    public final LinePath<Vector2> defaultPath;
    Array<Vector2> wayPoints = new Array<>(2);

    private Level level;

    public AbstractEnemy(EntityLocation target, Level currentLevel) {

        this.target = target;
        this.detectionCircle = new Circle();
        this.boundingBox = new Rectangle();
        this.level = currentLevel;
        this.wayPoints.add(new Vector2(20, 20));
        this.wayPoints.add(new Vector2(target.getX(), target.getY()));
        //System.out.println(wayPoints.get(0) + " " + wayPoints.get(1));
        this.defaultPath = new LinePath<>(wayPoints, true);
        this.virusBullets = new LinkedList<>();
        this.fluVirusTileColliderMap = currentLevel.getCollisionMap();
        assetManager.loadEnemys();
        assetManager.loadCharecter();
        assetManager.manager.finishLoading();
        prioritySteering = new PrioritySteering<>(this);
        this.removeBullets = new LinkedList<>();

    }




    // must be supered by extended classes to set a boundingBox;
    @Override
    public void setBoundingBox(float x, float y, float width, float height) {

        boundingBox.x = x;
        boundingBox.y = y;
        boundingBox.width = width;
        boundingBox.height = height;

    }

    public void setBoundingBox(Rectangle rect) {

        boundingBox.x = rect.getX();
        boundingBox.y = rect.getY();
        boundingBox.width = rect.getWidth();
        boundingBox.height = rect.getHeight();

    }


    Vector2 position;
    float orientation;
    Vector2 linearVelocity;
    float angularVelocity;
    float maxSpeed;
    float maxLinearAcceleration;
    float maxAngularSpeed;
    float maxAngularAccel;

    boolean independentFacing;

    private float linearSpeedThreshold;

    static final SteeringAcceleration<Vector2> steeringOutput =
            new SteeringAcceleration<Vector2>(new Vector2());

    @Override
    public final void update(float delta) {
        if (boundingBox != null) {

            setBoundingBox(getX(), getY(), getWidth(), getHeight());

        }



        prioritySteering.calculateSteering(steeringOutput);
        applySteering(steeringOutput, delta);





    }

    public final void applySteering(SteeringAcceleration<Vector2> steer, float delta) {
        boolean anyAccerlaeration = false;


        steeringOutput.linear.scl(delta);
        velocity.x = steeringOutput.linear.x;
        velocity.y = steeringOutput.linear.y;
        moveBy(velocity.x, velocity.y);

        position.mulAdd(steer.linear, delta);

        anyAccerlaeration = true;

        // if there is a steeringOutput velocity then apply
        if (!steeringOutput.linear.isZero()) {

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
        return linearSpeedThreshold;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float value) {
        this.linearSpeedThreshold = value;
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
        this.maxAngularSpeed = maxAngularSpeed;
    }

    @Override
    public float getMaxAngularAcceleration() {
        return maxAngularAccel;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
        this.maxAngularAccel = maxAngularAcceleration;
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
        DebugDrawer.DrawDebugRectangle(getX(), getY(), getWidth(), getHeight(), Color.PURPLE, Level1.getViewport().getCamera().combined);
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





    public Circle getDetectionCircle() {
        return detectionCircle;
    }

    public LinkedList<SanatizerBullet> isHit(LinkedList<SanatizerBullet> bullets) {

        LinkedList<SanatizerBullet> remove = new LinkedList<>()
;
        for (SanatizerBullet s : bullets) {

            if (s.isLethal && Intersector.overlaps(getBoundingBox(), s.getBoundingBox())) {

                tagged = true;
                remove.add(s);
                s.remove = true;

            }

        }

        return remove;

    }

    public LinkedList<SanatizerBullet> getVirusBullets() {

        return virusBullets;

    }

    public void clearBullets() {

        virusBullets.clear();

    }


    public abstract boolean isDead();
    public abstract void setDetonationToInstant();
    public abstract void setDeath();
    public abstract boolean handleMessage(Telegram telegram);

}
