package com.mygdx.game.Viruses;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.steer.behaviors.*;
import com.badlogic.gdx.ai.steer.proximities.RadiusProximity;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.mygdx.game.FunctionalityClasses.EntityLocation;
import com.mygdx.game.Levels.AbstractLevel;
import com.mygdx.game.Levels.Level;
import com.mygdx.game.SanatizerBullet;
import com.mygdx.game.Tiles.HeuristicTile;
import com.mygdx.game.Tiles.LineOfSight;
import com.mygdx.game.Tiles.TileData;
import com.mygdx.game.Tiles.TilePath;
import com.mygdx.game.utils.GraphMaker;
import com.mygdx.game.utils.Messages;
import com.mygdx.game.utils.Pair;

import java.util.ArrayList;

/*

this enemey will attempt to hunt down the player and if hes within a certain radius will explode.

 */

public class FluVirus extends AbstractEnemy  {

    private final Texture texture;
    private final Sprite sprite;
    private final Vector2 detectionLine;
    private final EntityLocation target;
    private final Level level;

    private TextureAtlas splashAtlas;
    private Animation<TextureRegion> splash;

    //private final TileCollision fluVirusTileCollider;

    private final Vector2 getDetectionLine;
    private static final HeuristicTile her = new HeuristicTile();

    private FollowPath<Vector2,LinePath.LinePathParam> followPath;
    private Seek<Vector2> seek;
    private final IndexedAStarPathFinder<TileData> chase;
    private final TilePath resultPath;
    private boolean drawPath = true;
    private LineOfSight lineOfSight;

    private final float radiusDetection = 40f;
    private boolean fuse;
    private boolean dead;
    private float fuseTime = 0f;


    private Animation<TextureRegion> slimeMoves;
    private Animation<TextureRegion> slimeBlows;

    private boolean canColide;

    public float timeElapsed = 0;


    private FluVirus(Builder builder) {

        super(builder.target, builder.level);
        this.resultPath = new TilePath();
        this.seek = new Seek<>(this, builder.target);
        this.detectionLine = builder.detectionLine;
        this.target = builder.target;
        this.level = builder.level;


        this.canColide = builder.canCollide;
        texture = assetManager.manager.get(assetManager.fluVirus);

        slimeMoves = new Animation<TextureRegion>(1/12f, assetManager.manager.get(assetManager.slime).getRegions());
        slimeBlows = new Animation<TextureRegion>(1/12f, assetManager.manager.get(assetManager.slimeBlows).getRegions());

        this.sprite = new Sprite(texture);
        this.position = new Vector2(getX(), getY());


        this.splashAtlas = assetManager.manager.get(assetManager.splashBullet);

        maxSpeed = 400f;
        maxLinearAcceleration = 60f;
        this.getDetectionLine = new Vector2();

        chase = new IndexedAStarPathFinder<TileData>(getLevel());

        setBounds(getX(), getY(), sprite.getWidth(), sprite.getHeight());
        setBoundingBox(getX(), getY(), getWidth(), getHeight());

        velocity = new Vector2();

        this.followPath = new FollowPath(this, defaultPath, 1f).setArrivalTolerance(0.01f).setDecelerationRadius(100f);
        followPath.setArriveEnabled(true);

        lineOfSight = new LineOfSight(new Vector2(20,21), target.getPosition(), fluVirusTileColliderMap.get(0));

        lineOfSight.setPoints(new Vector2(20,21), target.getPosition());

    }

    public static class Builder {

        private final EntityLocation target;
        private final Vector2 detectionLine;
        private final Level level;

        private ArrayList<ArrayList<ArrayList<TileData>>> fluVirusTileColliderMap;
        private boolean canCollide;

        public Builder(EntityLocation target, Level level) {

            this.target = target;
            this.level = level;
            detectionLine = new Vector2();

        }

        public Builder collisionInit(ArrayList<ArrayList<ArrayList<TileData>>> a) {

            fluVirusTileColliderMap = a;
            canCollide = true;
            return this;
        }

        public Builder detectionCircle(Circle a) {

            return this;
        }

        public FluVirus build() {

            return new FluVirus(this);

        }



    }


    @Override
    public boolean initRayCollision() {

       // RaycastCollisionDetector<Vector2> collisionDetector = obstacleBehavior.getRaycastCollisionDetector();

       // obstacleBehavior.setRaycastCollisionDetector(new RayCollisionDetection<Vector2>(fluVirusTileColliderMap.get(0), chase));


        return false;

    }



    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.draw(batch);

        //drawDebugBox();

    }

    @Override
    public void act(float delta) {

        //obstacleBehavior.setRaycastCollisionDetector(new RayCollisionDetection(fluVirusTileColliderMap.get(0), chase, obstacleBehavior.getRayConfiguration().updateRays()));

        update(delta);
        timeElapsed += delta;
        resultPath.clear();
        //velocity.mulAdd(steeringOutput.linear, delta);

        //moveBy(velocity.x, velocity.y);



//        when the target is in range begin detonatiion
        // when out of reange reset fuse
        if (new Vector2(target.getX() - getX(), target.getY() - getY()).len() < radiusDetection) {
            drawPath = false;
            fuse = true;


        } else {
            sprite.setRegion(slimeMoves.getKeyFrame(timeElapsed, true));

        }

        if (fuse) {

            fuseTime += delta;
            sprite.setRegion(slimeBlows.getKeyFrame(fuseTime, false));

            if (timeElapsed > 10f) {

                dead = true;
                fuseTime = 0;

            }

        }

        // for debugging
        // drawDetectionLine();
        int StartX = (int)(getX()) / 16;
        int StartY = (int)(getY()) / 16;

        int endX = (int)calcMiddleTarget().getIndexi();
        int endY = (int)calcMiddleTarget().getIndexj();

        Vector2 start = new Vector2(StartX, StartY);
        Vector2 end = new Vector2(endX, endY);



        TileData startNode = fluVirusTileColliderMap.get(0).get(StartY).get(StartX);
        TileData endNode = fluVirusTileColliderMap.get(0).get(endY).get(endX);

        chase.searchNodePath(startNode, endNode, her, resultPath);

        //System.out.println(fluVirusTileColliderMap.get(0).get(endY).get(endX));

        // resultPath is empty and gets mutated by this function
        // ;
        if (resultPath.getCount() > 2) {
            LinePath path = GraphMaker.parsePath(resultPath);
            followPath.setPath(path);
        }
           // update(delta);
        if (drawPath) {

            followPath.calculateSteering(steeringOutput);
            applySteering(delta);

        }

    }


    private Pair calcMiddleTarget() {

        Rectangle rect = target.getBoundingBox();

        int x = (int)(rect.getX() / 16);
        int y = (int)(rect.getY() / 16);

        return new Pair(x,y);

    }

    private void detonating(float timeElapsed) {

        if (timeElapsed > fuseTime) {
            timeElapsed = 0;
        }

    }

    @Override
    protected void positionChanged() {
        sprite.setPosition(getX(), getY());
    }

    @Override
    public void leftCollision(int x, int y) {

        velocity.x = 0;
        setPosition(fluVirusTileColliderMap.get(0).get(y).get(x).getLeftEdge() - getWidth(), getY());

    }
    // stupid bug I goot fix where I have to subtract 0.1f to get it out of the tile
    @Override
    public void bottomCollision(int x, int y) {
        velocity.y = 0;
        velocity.x = 1f;
        setPosition(getX(), fluVirusTileColliderMap.get(0).get(y).get(x).getBottomEdge() - getHeight() - 0.1f);

    }

    @Override
    public void topCollision(int x, int y) {
        velocity.y = 0;
        velocity.x = 1f;
        setPosition(getX(), fluVirusTileColliderMap.get(0).get(y).get(x).getTopEdge());

    }

    @Override
    public void rightCollision(int x,int y) {
        velocity.x = 0;
        setPosition(fluVirusTileColliderMap.get(0).get(y).get(x).getRightEdge(), getY());

    }

    @Override
    public boolean handleMessage(Telegram msg) {

        switch (msg.message) {
            case (Messages.CHASE):
                drawPath = true;
                return true;
            case (Messages.DETONATING) :
                fuse = true;
                return true;
            default:
                return false;
        }

    }

    /*
    @Override
    public void drawDebugBox() {
        for (Ray r : obstacleBehavior.getRayConfiguration().updateRays()) {
            Vector2 vector2 = (Vector2) r.end;
            DebugDrawer.DrawDebugLine(new Vector2(getX(), getY()), vector2, 2, Color.PINK, getLevel().getViewport().getCamera().combined);
        }

    }

     */



    @Override
    public boolean collisionLogic() {

        //fluVirusTileCollider.tileHorizontal(velocity);
        //fluVirusTileCollider.tileVertical(velocity);




        return true;
    }

    public boolean isDead () {

        return dead;

    }

}
