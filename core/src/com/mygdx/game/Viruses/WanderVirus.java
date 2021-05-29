package com.mygdx.game.Viruses;

import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.steer.behaviors.*;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.mygdx.game.Enumerators.Tile;
import com.mygdx.game.Enumerators.Types;
import com.mygdx.game.FunctionalityClasses.Entity;
import com.mygdx.game.FunctionalityClasses.EntityLocation;
import com.mygdx.game.Levels.Level;
import com.mygdx.game.SanatizerBullet;
import com.mygdx.game.Tiles.TileCollision;
import com.mygdx.game.Tiles.TileData;
import com.mygdx.game.utils.SteeringUtils;
import org.w3c.dom.css.Rect;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class WanderVirus extends AbstractEnemy{

    private final Sprite spr;
    private final Wander<Vector2> wander;
    private final Seek<Vector2> seek;

    private final Seek<Vector2> seekPoint;
    private final EntityLocation target;
    private boolean seeking = false;
    private boolean canShoot = false, dead = false;

    private Queue<SanatizerBullet> testBullets = new LinkedList<>();
    private SanatizerBullet a = null;
    private ArrayList<SanatizerBullet> removedBullets = new ArrayList<>();

    private float timeElapsed = 0, timeElapsed2 = 0;

    private Vector2 shotLine = new Vector2();

    private final Rectangle rectangle;
    private float bulletSpeed;

    public final ArrayList<ArrayList<ArrayList<TileData>>> tileDataMap;

    private final TileCollision tileCollider;
    private final BoundingPoint local;
    private final Turret nozzle;

    private final Types type;

    private WanderVirus(Builder builder) {
        super(builder.target, builder.currentLevel);
        this.spr = new Sprite(assetManager.manager.get(assetManager.fluVirus));
        setBounds(getX(), getY(), spr.getWidth(), spr.getHeight());
        //this.seek = new Arrive<Vector2>(this,(Location)builder.target).setTimeToTarget(1f).setArrivalTolerance(2f).setDecelerationRadius(80f).setEnabled(true);

        this.target = builder.target;
        this.local = new BoundingPoint(30f);
        this.type = builder.type;
        this.bulletSpeed = builder.bulletSpeed;

        rectangle = new Rectangle(getX(), getY(), getWidth(), getHeight());
        maxLinearAcceleration = 40f;
        maxSpeed = 80f;
        setMaxAngularAcceleration(500f);
        setMaxAngularSpeed(100f);

        this.wander = new Wander<>(this).setWanderRate(MathUtils.PI * 8).setEnabled(true).setFaceEnabled(false).setWanderRadius(10f).setWanderOffset(0);
        this.seekPoint = new Seek<Vector2>(this, local).setEnabled(true);
        this.seek = new Seek<>(this, target).setEnabled(true);

        blendedSteering.add(new BlendedSteering.BehaviorAndWeight<>(wander, 0.8f));
        // blendedSteering.add(new BlendedSteering.BehaviorAndWeight<>(wander, 2f));

        System.out.println(wander.getInternalTargetPosition());
        velocity = new Vector2();
        this.tileDataMap = builder.tileDataMap;

        this.tileCollider = new TileCollision.Builder().tileMap(tileDataMap.get(0), tileDataMap.get(1)).calcCorners(rectangle).charecter(this).build();

        this.nozzle = new Turret();
    }

    public static class Builder {

        private final Level currentLevel;
        private final EntityLocation target;
        private final ArrayList<ArrayList<ArrayList<TileData>>> tileDataMap;
        private float bulletSpeed;

        private Types type;

        public Builder(EntityLocation target, Level currentLevel, ArrayList<ArrayList<ArrayList<TileData>>> tileDataMap) {

            this.tileDataMap = tileDataMap;
            this.currentLevel = currentLevel;
            this.target = target;
            this.type = Types.STILL;
            this.bulletSpeed = 2.1f;

        }

        public WanderVirus build() {

            return new WanderVirus(this);

        }

        public Builder wander() {
            this.type = Types.WANDER;
            return this;
        }

        public Builder fast() {
            this.type = Types.ROCKET;
            this.bulletSpeed = 3.2f;

            return this;
        }

    }

    @Override
    public void act(float delta) {
        //update(delta);
        GdxAI.getTimepiece().update(delta);
        //update(delta);
        // seekPoint.calculateSteering(steeringOutput);


       // if (new Vector2(getX() - local.position.x, getY() - local.position.y).len() > local.radius) {

            //seeking = true;
            //seekPoint.calculateSteering(steeringOutput);

       // } else if (!seeking) {

        //}

        shotLine.x = target.getX() - getX();
        shotLine.y = target.getY() - getY();

        timeElapsed += delta;
        timeElapsed2 += delta;

        if (timeElapsed > 1f) {

            testBullets.add( new SanatizerBullet.Builder(nozzle.tip().x, nozzle.tip().y, new Vector2(shotLine.x, shotLine.y), 16f ,assetManager.manager.get(assetManager.bulletSprite))
                    .initCollision(tileDataMap).maxBounces(0)
                    .build());

            timeElapsed = 0;

        }


        // if shot
        if (isTagged())
            setDeath();

        if (testBullets.peek() != null) {

            a = testBullets.element();

            if (a.remove) {


                canShoot = false;

            }

            else if (new Vector2(a.getX() - target.getX(), a.getY() - target.getY()).len() < 15f) {



                canShoot = true;

                a.playerHit = true;

            }



        }



        if (canShoot) {

            // add random time elapsed
            if (timeElapsed2 > 0.8) {

                getVirusBullets().add(new SanatizerBullet.Builder(nozzle.tip().x, nozzle.tip().y, new Vector2(shotLine.x, shotLine.y), bulletSpeed, assetManager.manager.get(assetManager.bulletSprite))
                        .initCollision(tileDataMap).maxBounces(1)
                        .explosionAnimation(assetManager.manager.get(assetManager.splashBullet)).build());

                timeElapsed2 = 0;
            }

        }

        wander.calculateSteering(steeringOutput);

        if (a != null) {


        if (a.remove || a.playerHit) {

            removedBullets.add(a);

        }}






        seeking = getX() - local.position.x < 1f && getY() - local.position.y < 1f ? false : true;


        switch (type) {

            case STILL:
                break;
            case WANDER:
                steeringOutput.linear.scl(delta);
                steeringOutput.linear.scl(1.1f);
                // System.out.println(steeringOutput.linear);
                this.velocity.x = steeringOutput.linear.x;
                this.velocity.y = steeringOutput.linear.y;
                break;

        }




        //velocity.scl(0.8f);



        //moveBy(this.velocity.x, this.velocity.y);
        rectangle.x = getX();
        rectangle.y = getY();

        setBoundingBox(rectangle);

        collisionLogic();
        //local.updateLocation((int)getX(), (int)getY());
       // moveBy(0.5f, 2f);

        nozzle.act(delta);

    }

    public class BoundingPoint implements Location<Vector2> {

        private final Vector2 position;
        private final float radius;

        public BoundingPoint(float radius) {

            System.out.println(WanderVirus.this.getX() + " " + WanderVirus.this.getY());

            position = new Vector2(200,200);
            this.radius = radius;
        }

        @Override
        public Vector2 getPosition() {
            return position;
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

        public void updateLocation(int x, int y) {
            this.position.x = x;
            this.position.y = y;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        spr.draw(batch);

        for (SanatizerBullet a : testBullets) {

            //a.draw(batch,1);
            a.act(1f);



        }

        testBullets.removeAll(removedBullets);

        nozzle.draw(batch);



    }

    @Override
    protected void positionChanged() {
        spr.setPosition(getX(), getY());
    }

    @Override
    public boolean isDead() {
        return dead;
    }

    @Override
    public void setDetonationToInstant() {

    }

    @Override
    public void setDeath() {
        dead = true;
    }



    @Override
    public boolean handleMessage(Telegram telegram) {
        return false;
    }

    @Override
    public boolean collisionLogic() {

        if (tileCollider.tileHorizontal(velocity)) {

            moveBy(velocity.x, 0);
        }
        if (tileCollider.tileVertical(velocity)) {

            moveBy(0,velocity.y);
        }
        return false;



    }


    @Override
    public void bottomCollision(int x, int y) {
        // getBoundingBox().setPosition(x, tileDataMap.get(0).get(y).get(x).getBottomEdge() - getWidth());
        // rectangle.setPosition(rectangle.getX(), tileDataMap.get(0).get(y).get(x).getBottomEdge() - rectangle.getHeight());

        //setPosition(rectangle.getX(), tileDataMap.get(0).get(y).get(x).getBottomEdge() - rectangle.getHeight());
    }

    @Override
    public void topCollision(int x, int y) {
        //rectangle.setPosition(rectangle.getX(), tileDataMap.get(0).get(y).get(x).getTopEdge());
        //setPosition(rectangle.getX(), tileDataMap.get(0).get(y).get(x).getTopEdge());
    }

    @Override
    public void rightCollision(int x, int y) {
        //setPosition(tileDataMap.get(0).get(y).get(x).getRightEdge(), rectangle.getY());
        //velocity.x = 0;
    }

    @Override
    public void leftCollision(int x, int y) {
        //setPosition(tileDataMap.get(0).get(y).get(x).getLeftEdge() - rectangle.getWidth(), rectangle.getY());
    }

    private class Turret {

        Sprite nozzle = new Sprite(assetManager.manager.get(assetManager.nozzle));
        Vector2 ray = new Vector2();

        Vector2 direction = new Vector2();
        private Vector2 tip = new Vector2(1,1);
        private Ray endPoint = new Ray();
        private Vector3 start;

        float centerx, centery;

        public Turret() {
            start = new Vector3();


          //  nozzle.setPosition(centerx, centery);
            //WanderVirus.this.spr.setRegion(nozzle.getTexture());

            nozzle.setOrigin(0,8);


            tip.setLength(nozzle.getWidth());



        }

        public void rotate(Vector2 angleToMoveTo) {



        }

        public void act(float delta) {

            float xOffset = WanderVirus.this.getX() + nozzle.getWidth()/2;

            nozzle.setPosition(xOffset, WanderVirus.this.getY());
            start.set(nozzle.getX(), nozzle.getY() + WanderVirus.this.getHeight()/2, 0);
            endPoint.set(start, new Vector3(shotLine.x, shotLine.y, 0));

            nozzle.setRotation(shotLine.angleDeg());
            tip.setAngleDeg(shotLine.angleDeg());

            tip.setLength(16f);





        }

        public void draw(Batch batch) {

            nozzle.draw(batch);


        }

        public Vector2 tip() {

            Vector3 newVec = endPoint.getEndPoint(start, 16f);


           return new Vector2(newVec.x, newVec.y);

        }

    }

}
