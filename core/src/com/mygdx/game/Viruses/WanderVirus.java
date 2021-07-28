package com.mygdx.game.Viruses;

import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.*;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Enumerators.Types;
import com.mygdx.game.Enumerators.plane;
import com.mygdx.game.FunctionalityClasses.DebugDrawer;
import com.mygdx.game.FunctionalityClasses.EntityLocation;
import com.mygdx.game.FunctionalityClasses.RayCast;
import com.mygdx.game.Levels.AbstractLevel;
import com.mygdx.game.Levels.Level;
import com.mygdx.game.SanatizerBullet;
import com.mygdx.game.Tiles.TileCollision;
import com.mygdx.game.Tiles.TileData;
import com.mygdx.game.utils.*;

import java.util.*;

public class WanderVirus extends AbstractEnemy{

    private final Sprite spr;
    private final Wander<Vector2> wander;
    private SteeringBehavior<Vector2> behavior;
    private final Flee<Vector2> flee;
    private final Seek<Vector2> seek;

    private boolean sortCalled = false;

    private Array<ReflectionPoint> validSecondShots = new Array<>();

    private Array<Vector2> rayo = new Array<Vector2>();

    private Array<Vector2> arraayOfReducedRays = new Array<>();
    private ArrayList<ReflectionPoint> reflectionPoints = new ArrayList<>();


    private Vector2 centerPos = new Vector2();

    private final Polygon polygon = new Polygon();


    private final Array<Point<Integer, Integer>> edges;

    private Array<Vector2> rays = new Array<>();

    private boolean justShot = false;
    private Color COLOR;

    private Vector2 rayCast = new Vector2();

    private final float MAX_FIRE_RATE = 0.1f;

    private final Seek<Vector2> seekPoint;
    private final EntityLocation target;
    private boolean seeking = false;
    private boolean canShoot = false, dead = false;
    private float fireRate = 0.1f;

    private Queue<SanatizerBullet> testBullets = new LinkedList<>();
    private SanatizerBullet a = null;
    private ArrayList<SanatizerBullet> removedBullets = new ArrayList<>();

    private float[] points;

    private float timeElapsed = 0, timeElapsed2 = 0;

    private Vector2 shotLine = new Vector2();

    private final Rectangle rectangle;
    private float bulletSpeed;

    public final ArrayList<ArrayList<ArrayList<TileData>>> tileDataMap;

    private final TileCollision tileCollider;
    private final BoundingPoint local;
    private final Turret nozzle;

    private final Types type;
    private final Level currentLevel;

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


        velocity = new Vector2();
        this.tileDataMap = builder.tileDataMap;

        this.tileCollider = new TileCollision.Builder().tileMap(tileDataMap.get(0), tileDataMap.get(1)).calcCorners(rectangle).charecter(this).build();

        this.nozzle = new Turret(1/29f, 10);

        this.currentLevel = builder.currentLevel;

        this.edges = builder.edges;

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

        private Array<Point<Integer, Integer>> edges = new Array<>();

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

        public Builder hyper(Array<Point<Integer, Integer>> edges) {

            this.type = Types.HYPER;
            this.bulletSpeed = 3.2f;
            this.COLOR = Color.TEAL;
            this.edges = edges;

            return this;
        }

        public Builder stupid() {
            this.type = Types.STUPID;

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

        centerPos = new Vector2(getX() + getWidth()/2, getY() + getHeight()/2);


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

        //rayCast = RayCast.castRay(new Vector2(getX() + getWidth()/2, getY() + getHeight()/2), shotLine, fluVirusTileColliderMap.get(0));

        if (type != Types.HYPER) {

            if (RayCast.castRay(new Vector2(getX() + getWidth() / 2, getY() + getHeight() / 2), shotLine, target.getCenterPosition(), fluVirusTileColliderMap.get(0))) {

                canShoot = true;

            } else {
                canShoot = false;
            }

        }






        // if shot
        if (isTagged())
            setDeath();





            // add random time elapsed
            if (nozzle.isFinished(delta)) {

                if (type != Types.HYPER)


                    getVirusBullets().add(new SanatizerBullet.Builder(nozzle.tip().x, nozzle.tip().y, SteeringUtils.angleToVector((float)Math.toRadians(nozzle.nozzle.getRotation() - 90)), bulletSpeed, assetManager.manager.get(assetManager.bulletSprite))
                            .initCollision(tileDataMap).maxBounces(1)
                            .explosionAnimation(assetManager.manager.get(assetManager.splashBullet)).build());

                else {

                    getVirusBullets().add(new SanatizerBullet.Builder(nozzle.tip().x, nozzle.tip().y, nozzle.returnPoint, bulletSpeed, assetManager.manager.get(assetManager.bulletSprite))
                            .initCollision(tileDataMap).maxBounces(1)
                            .explosionAnimation(assetManager.manager.get(assetManager.splashBullet)).build());

                }

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

            case ROCKET:
            case SMART:

               // behavior = wander;

                // if it can see the player and the players close it flees
                if (SteeringUtils.inRadius(getPosition(), target.getPosition(), 55f) && canShoot) {

                    behavior.setEnabled(true);
                    behavior = flee;


                } else if (SteeringUtils.inRadius(getPosition(), target.getPosition(), 70f) && canShoot) {
                    behavior.setEnabled(false);
                }


                else if (canShoot){
                    behavior.setEnabled(true);
                    behavior = seek;
                } else {
                    behavior.setEnabled(true);
                    behavior = wander;
                }

                steeringOutput.linear.scl(delta);
                steeringOutput.linear.scl(1.1f);
                this.velocity.x = steeringOutput.linear.x;
                this.velocity.y = steeringOutput.linear.y;

                break;

            case HYPER:

                castAllRays();

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

    private Array<ReflectionPoint> reflectionRays = new Array<>();


    private void castAllRays() {

        if (!reflectionPoints.isEmpty()) {

            for (ReflectionPoint point : reflectionPoints) {

                // start point
                reflectionRays.add(new ReflectionPoint(point, RayCast.castRayNoObsatcle(point.postion, point.getStart(),fluVirusTileColliderMap.get(0)),
                        // endPoint
                        RayCast.castRayNoObsatcle(point.postion, point.getEnd(), fluVirusTileColliderMap.get(0))));
                //reflectionRaysStart.add(new ReflectionPoint(point, RayCast.castRayNoObsatcle(point.postion, target.getCenterPosition(),fluVirusTileColliderMap.get(0))));
            }

        }


        rays.add(RayCast.castRay(centerPos, RayCast.castDirection(centerPos, new Vector2(16, 256)), fluVirusTileColliderMap.get(0)));
        rays.add(RayCast.castRay(centerPos, RayCast.castDirection(centerPos, new Vector2(16, 16)), fluVirusTileColliderMap.get(0)));
        rays.add(RayCast.castRay(centerPos, RayCast.castDirection(centerPos, new Vector2(480, 256)), fluVirusTileColliderMap.get(0)));
        rays.add(RayCast.castRay(centerPos, RayCast.castDirection(centerPos, new Vector2(480, 16)), fluVirusTileColliderMap.get(0)));


        for (Point<Integer, Integer> point : edges) {

            //rays.add(new Vector2(point.getIndexi(), point.getIndexj()));


            rays.add((RayCast.castRay(centerPos, RayCast.castDirection(centerPos, new Vector2(point.getIndexi(), point.getIndexj())), fluVirusTileColliderMap.get(0))));
            rays.add((RayCast.castRay(centerPos, RayCast.castDirection(centerPos, new Vector2(point.getIndexi(), point.getIndexj()).rotateRad(0.0001f)), fluVirusTileColliderMap.get(0))));
            rays.add((RayCast.castRay(centerPos, RayCast.castDirection(centerPos, new Vector2(point.getIndexi(), point.getIndexj()).rotateRad(-0.0001f)), fluVirusTileColliderMap.get(0))));

        }

        if (!sortCalled) {

            // sorts rays by angle
            rays = GraphMaker.sortRays(rays, centerPos);

            // reduces dupelicate rays
            arraayOfReducedRays = GraphMaker.listToArray(GraphMaker.reducePoints(rays));

            // linesHorizontal = GraphMaker.makeLines(arraayOfReducedRays, false);

            // seperates lines by direction
            linesVertical = GraphMaker.makeLines(arraayOfReducedRays, false);
            linesHorizontal = GraphMaker.makeLines(arraayOfReducedRays, true);

            /*
            for (LinkedList<Vector2> vec : linesHorizontal) {

                reflectionPointHorz.add(new ReflectionPoint(vec.getFirst(), vec.getLast(), getPosition(), true));

            }

             */

            for (LinkedList<Vector2> vec : linesVertical) {

                reflectionPoints.add(new ReflectionPoint(vec.getFirst(), vec.getLast(), centerPos, plane.VERT));

            }

            for (LinkedList<Vector2> vec : linesHorizontal) {

                reflectionPoints.add(new ReflectionPoint(vec.getFirst(), vec.getLast(), centerPos, plane.HORZ));

            }

            //for (ReflectionPoint point : reflectionPoints)


            //for (ReflectionPoint reflectionPoint : reflectionPoints) {



            //}


            // sets up second angle


            sortCalled = true;

            rayo = rays;


            Array<Float> arrayOfFloats = new Array<Float>();

            for (Vector2 floater : arraayOfReducedRays) {

                arrayOfFloats.add(floater.x);
                arrayOfFloats.add(floater.y);

            }

            points = new float[arrayOfFloats.size];

            for (int i = 0; i < points.length; i += 1) {

                points[i] = arrayOfFloats.get(i);

            }



        }



        GraphMaker.returnEdgesInReflection(reflectionPoints.get(4), edges, testEdge, fluVirusTileColliderMap.get(0));
        reflectionPoints.get(4).updateShots(target.getCenterPosition(), fluVirusTileColliderMap.get(0));


        polygon.setVertices(points);

        // 360 rays

        //for (int i = 0; i < 360; i+=6) {
           // rays.add(RayCast.castRay(new Vector2(getX() + getWidth()/2, getY() + getHeight()/2), new Vector2((float)Math.sin(i), (float)Math.cos(i)), fluVirusTileColliderMap.get(0)));
        //}

        //validReflectShots = GraphMaker.betweenrays(reflectionRays , target.getCenterPosition(), fluVirusTileColliderMap.get(0));

        //if (validReflectShots.size() > 2)





        validReflectShots = GraphMaker.betweenrays(reflectionRays , target.getCenterPosition(), fluVirusTileColliderMap.get(0));


    }

    //DELETE
    Array<Vector2> testEdge = new Array<>();

    private LinkedList<ReflectionPoint> validReflectShots = new LinkedList<>();

    @Override
    public void draw(Batch batch, float parentAlpha) {

        spr.draw(batch);

        nozzle.draw(batch);

        if (COLOR != null)
            spr.setColor(COLOR);

        //System.out.println(rayCast);

        // drawRays();


        //System.out.println(reflectionPoints.size() + "Wander");

    }

    private LinkedList<LinkedList<Vector2>> linesHorizontal = new LinkedList<>(), linesVertical = new LinkedList<>();

    private void drawRays() {


        for (ReflectionPoint rayPoint : validReflectShots) {

            DebugDrawer.DrawDebugLine(rayPoint.finalPoint, target.getCenterPosition(), 8, Color.PURPLE, AbstractLevel.getViewport().getCamera().combined);

        }

        /*
        for (ReflectionPoint rayPoint : reflectionRays) {

            //DebugDrawer.DrawDebugCircle(ray, 5f, 4, Color.RED, AbstractLevel.getViewport().getCamera().combined);
            //DebugDrawer.DrawDebugLine(rayPoint.testPoint, rayPoint.getStart(), 8, Color.OLIVE, AbstractLevel.getViewport().getCamera().combined);



            DebugDrawer.DrawDebugLine(rayPoint.getLimitStart(), rayPoint.getStart(), 8, Color.CHARTREUSE, AbstractLevel.getViewport().getCamera().combined);

            DebugDrawer.DrawDebugLine(rayPoint.getLimitEnd(), rayPoint.getEnd(), 8, Color.ORANGE, AbstractLevel.getViewport().getCamera().combined);
            //DebugDrawer.DrawDebugCircle(ray, 5f, 4, Color.RED, AbstractLevel.getViewport().getCamera().combined);

        }



         */




        /*
        for (Vector2 rayo : rays) {

            DebugDrawer.DrawDebugCircle(rayo, 5f, 4, Color.RED, AbstractLevel.getViewport().getCamera().combined);

        }


         */


        if (linesHorizontal.size() > 1) {

            for (LinkedList<Vector2> list : linesHorizontal) {

                Vector2 start = list.get(0);
                Vector2 end = list.get(list.size() - 1);

                //for (Vector2 point : list) {

                    // Vector2 end = point;

                    DebugDrawer.DrawDebugLine(start, end, 12, Color.RED, AbstractLevel.getViewport().getCamera().combined);


                //}



            }

        }
        if (linesVertical.size() > 1) {

            for (LinkedList<Vector2> list : linesVertical) {

                Vector2 start = list.get(0);
                Vector2 end = list.get(list.size() - 1);

                //for (Vector2 point : list) {

                // Vector2 end = point;

                DebugDrawer.DrawDebugLine(start, end, 12, Color.GREEN, AbstractLevel.getViewport().getCamera().combined);


                //}



            }

        }

        rays.clear();
        reflectionRays.clear();

        //DebugDrawer.DrawDebugPolygon(polygon, 7, Color.PURPLE, AbstractLevel.getViewport().getCamera().combined);

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

        private final float rotationRate = 4f;



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

        private boolean rotating = true;
        private int randomDegree = 90;

        public void rotate() {



            Vector2 noz = SteeringUtils.angleToVector((float)Math.toRadians(nozzle.getRotation() - 90));
            //double randomDegree = Math.toDegrees(vectorToAngle(noz));

           if (rotating) {

                randomDegree = (int)(Math.random() * 360) - 180;
                rotating = false;

           }

            if (Math.abs(Math.toDegrees(vectorToAngle(noz)) - randomDegree) < 3.0){
                //System.out.println("true");
                rotating = true;
            }

            if (Math.toDegrees(vectorToAngle(noz)) > randomDegree) {
                nozzle.rotate(-2);
            } else if (Math.toDegrees(vectorToAngle(noz)) < randomDegree) {
                nozzle.rotate(+2);
            }
           // System.out.println(Math.toDegrees(vectorToAngle(noz)) - randomDegree);
        }


        //DELETE TESTING


        private boolean rotateToTarget(Vector2 start) {

            //System.out.println(Math.abs(Math.toDegrees(vectorToAngle(tip)) - Math.toDegrees(vectorToAngle(shotLine))));

            if (Math.toDegrees(SteeringUtils.calcAngleBetweenVectors(start, tip)) > 88
                    && Math.toDegrees(SteeringUtils.calcAngleBetweenVectors(start, tip)) < 92) {


                    return true;

            }

            else if (Math.toDegrees(SteeringUtils.calcAngleBetweenVectors(start, tip)) > 90) {
                nozzle.rotate(-rotationRate);
            } else {
                nozzle.rotate(+rotationRate);
            }

            return false;

        }

        private void timedShot(float delta) {

           // if ( /* missFireTimer > SteeringUtils.rangeOfTimes(0f, 10f) */ !chance) {

                missFireTimer = 0;


                //if (canShoot) {

                chance = false;

                // if it can shoot and it isnt current in a timeout then calculate next chance


                timeElapsedNoz += delta;
                nozzle.setRegion(nozzleAnimation.getKeyFrame(timeElapsedNoz, true));


                // }
           // }

        }

        private void fire() {



        }

        private Vector2 returnPoint = new Vector2();

        boolean HyperRandomShotFlag = true;

        public void act(float delta) {

            float xOffset = WanderVirus.this.getX() + nozzle.getWidth() / 2;

            nozzle.setPosition(xOffset, WanderVirus.this.getY());
            //start.set(nozzle.getX(), nozzle.getY() + WanderVirus.this.getHeight()/2, 0);
            //endPoint.set(start, new Vector3(tip.x, tip.y, 0));

            // mutates tip
            tip = SteeringUtils.angleToVector(tip, (float) Math.toRadians(nozzle.getRotation()));

            //nozzle.rotate(rotationRate);

            //nozzle.setRotation(shotLine.angleDeg());
            //tip.setAngleDeg(shotLine.angleDeg());

            if (chance)
                missFireTimer += delta;

            // if it can shoot then rotate nozzle towards player
            // if its a stupid type it never aims for the player

            //modifys tip using complex bounce algorithim



            if (type == Types.HYPER  && validReflectShots.size() > 0) {

                if (HyperRandomShotFlag) {

                    HyperRandomShotFlag = false;

                    int size = validReflectShots.size();

                    int random = (int)(Math.random() * size);

                    ReflectionPoint reflectPoint = validReflectShots.get(random);

                    returnPoint = new Vector2(reflectPoint.finalPoint.x - WanderVirus.this.centerPos.x, reflectPoint.finalPoint.y - WanderVirus.this.centerPos.y);


                }



                canShoot = rotateToTarget(returnPoint);

                canShoot = false;

                if (canShoot) {


                         timedShot(delta);

                         canShoot = false;




                }



            } else if (type == Types.HYPER && validReflectShots.isEmpty()) {

                nozzle.setRegion(nozzleAnimation.getKeyFrame(0f));
                timeElapsedNoz = 0;
                missFireTimer = 0;

                rotate();

            }

            if (canShoot && type != Types.HYPER) {

                if (type != Types.STUPID) {

                    rotateToTarget(shotLine);

                }

                // the range of time between each fire
                timedShot(delta);

            // when it doesnt see the player call rotate and rotate in random directions

            } else if (type != Types.HYPER) {

                nozzle.setRegion(nozzleAnimation.getKeyFrame(0f));
                timeElapsedNoz = 0;
                missFireTimer = 0;

                rotate();

            }
        }

        public void draw(Batch batch) {

            nozzle.draw(batch);

            if (COLOR != null)
            nozzle.setColor(COLOR);

        }

        public Vector2 tip() {

            Vector2 tipEnd = WanderVirus.this.centerPos;








            return tipEnd;

        }

        public boolean isFinished(float delta) {

            if (nozzleAnimation.isAnimationFinished(timeElapsedNoz)) {

               chance = true;
               timeElapsed2 += delta;
               timeElapsedNoz = 0;
               HyperRandomShotFlag = true;


                return true;
            }

            justShot = false;

            return false;
        }
    }
}
