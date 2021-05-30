package com.mygdx.game.Viruses;

import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.*;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.math.collision.Ray;
import com.mygdx.game.Enumerators.Collisions;
import com.mygdx.game.Enumerators.Tile;
import com.mygdx.game.Enumerators.Types;
import com.mygdx.game.FunctionalityClasses.Entity;
import com.mygdx.game.FunctionalityClasses.EntityLocation;
import com.mygdx.game.Levels.Level;
import com.mygdx.game.MainCharacter;
import com.mygdx.game.SanatizerBullet;
import com.mygdx.game.Tiles.TileCollision;
import com.mygdx.game.Tiles.TileData;
import com.mygdx.game.utils.SteeringUtils;
import org.graalvm.compiler.lir.alloc.SaveCalleeSaveRegisters;
import org.w3c.dom.css.Rect;

import javax.swing.plaf.BorderUIResource;
import java.awt.geom.Line2D;
import java.util.*;

public class WanderVirus extends AbstractEnemy{

    private final Sprite spr;
    private final Wander<Vector2> wander;
    private SteeringBehavior<Vector2> behavior;
    private final Flee<Vector2> flee;
    private final Seek<Vector2> seek;

    private boolean justShot = false;
    private Color COLOR;

    private final float MAX_FIRE_RATE = 0.1f;

    private final Seek<Vector2> seekPoint;
    private final EntityLocation target;
    private boolean seeking = false;
    private boolean canShoot = false, dead = false;
    private float fireRate = 0.1f;

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
        this.flee = new Flee<>(this, target);
        this.seekPoint = new Seek<Vector2>(this, local).setEnabled(true);
        this.seek = new Seek<>(this, target).setEnabled(true);

        blendedSteering.add(new BlendedSteering.BehaviorAndWeight<>(wander, 0.8f));
        // blendedSteering.add(new BlendedSteering.BehaviorAndWeight<>(wander, 2f));

        System.out.println(wander.getInternalTargetPosition());
        velocity = new Vector2();
        this.tileDataMap = builder.tileDataMap;

        this.tileCollider = new TileCollision.Builder().tileMap(tileDataMap.get(0), tileDataMap.get(1)).calcCorners(rectangle).charecter(this).build();

        this.nozzle = new Turret(1/29f, 10);


        behavior = wander;

        COLOR = builder.COLOR;

    }

    public static class Builder {

        private final Level currentLevel;
        private final EntityLocation target;
        private final ArrayList<ArrayList<ArrayList<TileData>>> tileDataMap;
        private float bulletSpeed;
        private PrioritySteering<Vector2> behavior;
        private Color COLOR;

        private Types type;
        private Flee<Vector2> flee;

        public Builder(EntityLocation target, Level currentLevel, ArrayList<ArrayList<ArrayList<TileData>>> tileDataMap) {

            this.tileDataMap = tileDataMap;
            this.currentLevel = currentLevel;
            this.target = target;
            this.type = Types.STILL;
            this.bulletSpeed = 2.1f;
            this.COLOR = null;

        }

        public WanderVirus build() {

            return new WanderVirus(this);


        }

        public Builder wander() {
            this.type = Types.WANDER;
            this.COLOR = Color.CHARTREUSE;
            return this;
        }

        public Builder fast() {
            this.type = Types.ROCKET;
            this.bulletSpeed = 3.2f;
            this.COLOR = Color.TEAL;

            return this;
        }

        public Builder smart() {
            this.type = Types.SMART;
            this.COLOR = Color.LIME;

            return this;
        }

        public Builder invisible() {

            this.COLOR = Color.CLEAR;

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

            // i fthe bullets in a certain radius it can see the target
            else if (new Vector2(a.getX() - target.getX(), a.getY() - target.getY()).len() < 20f) {



                canShoot = true;

                a.playerHit = true;

            }



        }





            // add random time elapsed
            if (nozzle.isFinished(delta)) {


                getVirusBullets().add(new SanatizerBullet.Builder(nozzle.tip().x, nozzle.tip().y, SteeringUtils.angleToVector((float)Math.toRadians(nozzle.nozzle.getRotation() - 90)), bulletSpeed, assetManager.manager.get(assetManager.bulletSprite))
                        .initCollision(tileDataMap).maxBounces(1)
                        .explosionAnimation(assetManager.manager.get(assetManager.splashBullet)).build());



            }

        if (a != null) {


        if (a.remove || a.playerHit) {

            removedBullets.add(a);

        }}

        behavior.calculateSteering(steeringOutput);

        seeking = getX() - local.position.x < 1f && getY() - local.position.y < 1f ? false : true;

        switch (type) {

            case STILL:
                break;

            case WANDER:
                //behavior.calculateSteering(steeringOutput);
                behavior = wander;
                steeringOutput.linear.scl(delta);
                steeringOutput.linear.scl(1.1f);
                // System.out.println(steeringOutput.linear);
                this.velocity.x = steeringOutput.linear.x;
                this.velocity.y = steeringOutput.linear.y;

                break;
            // no break so it essentially gets the smart atributes;
            case SMART:

               // behavior = wander;

                // if it can see the player and the players close it flees
                if (SteeringUtils.inRadius(getPosition(), target.getPosition(), 50f) && canShoot) {


                    behavior = flee;


                } else if (canShoot){
                    behavior = seek;
                } else {
                    behavior = wander;
                }

                steeringOutput.linear.scl(delta);
                steeringOutput.linear.scl(1.1f);
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

        if (COLOR != null)
            spr.setColor(COLOR);




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

        private float fireRate;
        private final Animation<TextureRegion> nozzleAnimation;

        private int prevFrame = -1;

        private Sprite nozzle;

        private final float rotationRate = 2f;



        private Vector2 tip = new Vector2(1,0);
        private Ray endPoint = new Ray();
        private Vector3 start;
        private float timeElapsedNoz = 0f, missFireTimer = 0f;
        private boolean chance = false;
        private int missFire = 0;


        private float timeElasped = 0f;

        float centerx, centery;

        public Turret() {
            this(1/5f,-1);
        }

        public Turret(float fireRate) {
            this(fireRate, -1);
        }

        public Turret(float fireRate, int missFire) {
            this.missFire = missFire;
            start = new Vector3();
            this.fireRate = fireRate;
            nozzleAnimation = new Animation<TextureRegion>(fireRate, assetManager.manager.get(assetManager.nozzle).getRegions());
            nozzle = new Sprite(nozzleAnimation.getKeyFrame(0f));

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
            //start.set(nozzle.getX(), nozzle.getY() + WanderVirus.this.getHeight()/2, 0);
            //endPoint.set(start, new Vector3(tip.x, tip.y, 0));



            // mutates tip
            tip = SteeringUtils.angleToVector(tip, (float)Math.toRadians(nozzle.getRotation()));
            System.out.println(Math.toDegrees(SteeringUtils.calcAngleBetweenVectors(shotLine, tip)));

            if (Math.toDegrees(SteeringUtils.calcAngleBetweenVectors(shotLine, tip)) > 90 ) {
              nozzle.rotate(-rotationRate);
            } else {
                nozzle.rotate(+rotationRate);
            }

            //nozzle.rotate(rotationRate);

            //nozzle.setRotation(shotLine.angleDeg());
            //tip.setAngleDeg(shotLine.angleDeg());


            if (chance)
                missFireTimer += delta;

            if (canShoot) {


            if (missFireTimer > SteeringUtils.rangeOfTimes(0f, 10f) || !chance) {

                missFireTimer = 0;


                //if (canShoot) {

                    chance = false;

                    // if it can shoot and it isnt current in a timeout then calculate next chance


                    timeElapsedNoz += delta;
                    nozzle.setRegion(nozzleAnimation.getKeyFrame(timeElapsedNoz, true));


               // }




            }
            } else {


                nozzle.setRegion(nozzleAnimation.getKeyFrame(0f));
                timeElapsedNoz = 0;
                missFireTimer = 0;
            }





        }

        public void draw(Batch batch) {

            nozzle.draw(batch);

            if (COLOR != null)
            nozzle.setColor(COLOR);


        }

        public Vector2 tip() {

            Vector3 newVec = endPoint.getEndPoint(start, 16f);

            float centerX = getX() + getWidth()/2;
            float centery = getY() + getHeight()/2;

            Vector2 sillVec = SteeringUtils.angleToVector((float)Math.toRadians(nozzle.getRotation() - 90));

            sillVec.scl(16f);


           return new Vector2(sillVec.x + centerX, centery + sillVec.y);

        }



        public boolean isFinished(float delta) {



            if (nozzleAnimation.isAnimationFinished(timeElapsedNoz)) {

               chance = true;
               timeElapsed2 += delta;
               timeElapsedNoz = 0;


                return true;
            }

            justShot = false;


            return false;
        }


    }

}
