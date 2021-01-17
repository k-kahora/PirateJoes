package com.mygdx.game.Viruses;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.*;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.FunctionalityClasses.EntityLocation;
import com.mygdx.game.Levels.Level;
import com.mygdx.game.SanatizerBullet;
import com.mygdx.game.Tiles.HeuristicTile;
import com.mygdx.game.Tiles.TileCollision;
import com.mygdx.game.Tiles.TileData;
import com.mygdx.game.Tiles.TilePath;
import com.mygdx.game.utils.GraphMaker;
import com.mygdx.game.utils.Messages;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class FluVirus extends AbstractEnemy  {

    private final Texture texture;
    private final Sprite sprite;
    private final Vector2 detectionLine;
    private final EntityLocation target;
    private final Queue<SanatizerBullet> virusBullets;
    private final TileCollision fluVirusTileCollider;
    private final ArrayList<ArrayList<ArrayList<TileData>>> fluVirusTileColliderMap;
    //private final RaycastObstacleAvoidance<Vector2> obstacleBehavior;
    private final IndexedAStarPathFinder<TileData> chase;
    private final TilePath resultPath;
    private final Vector2 getDetectionLine;


    private boolean canColide;

    public float timeElapsed = 0;


    private FluVirus(Builder builder) {

        super(builder.target, builder.level);
        this.resultPath = new TilePath();
        this.detectionLine = builder.detectionLine;
        this.target = builder.target;
        this.virusBullets = builder.virusBullets;
        this.canColide = builder.canCollide;
        this.fluVirusTileColliderMap = builder.fluVirusTileColliderMap;
        texture = assetManager.manager.get(assetManager.fluVirus);
        this.sprite = new Sprite(texture);
        this.position = new Vector2(getX(), getY());
        steeringBehavior = new PrioritySteering<>(this);

       // obstacleBehavior = new RaycastObstacleAvoidance<>(this, new CentralRayWithWhiskersConfiguration<>(this, 40f,20f,0.5f));




        initRayCollision();

        // accel has to be greater than maxSpeed
        maxSpeed = 32f;
        maxLinearAcceleration = 128f;
        this.getDetectionLine = new Vector2();
//        obstacleBehavior.setDistanceFromBoundary(0f);


        chase = new IndexedAStarPathFinder<TileData>(getLevel());
        //steeringBehavior.add(obstacleBehavior);
        //steeringBehavior.add(chase);

        setBounds(getX(), getY(), sprite.getWidth(), sprite.getHeight());
        setBoundingBox(getX(), getY(), getWidth(), getHeight());

        velocity = new Vector2();


        if (canColide) {
            fluVirusTileCollider = new TileCollision.Builder<>().charecter(this).calcCorners(getBoundingBox())
            .tileMap(fluVirusTileColliderMap.get(0), fluVirusTileColliderMap.get(1)).build();
        } else {
            fluVirusTileCollider = null;
        }

        steeringBehavior.setEnabled(true);



    }




    public static class Builder {

        private final EntityLocation target;
        private final Queue<SanatizerBullet> virusBullets;
        private final Vector2 detectionLine;
        private final Level level;

        private ArrayList<ArrayList<ArrayList<TileData>>> fluVirusTileColliderMap;
        private boolean canCollide;

        public Builder(EntityLocation target, Level level) {

            this.target = target;
            this.virusBullets = new LinkedList<>();
            this.level = level;
            detectionLine = new Vector2();

        }

        public Builder collisionInit(ArrayList<ArrayList<ArrayList<TileData>>> a) {

            fluVirusTileColliderMap = a;
            canCollide = true;
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
        for (SanatizerBullet shot : virusBullets
             ) {
            shot.draw(batch,0);
            shot.act(timeElapsed);
        }
        drawDebugBox();

    }

    @Override
    public void act(float delta) {

        //obstacleBehavior.setRaycastCollisionDetector(new RayCollisionDetection(fluVirusTileColliderMap.get(0), chase, obstacleBehavior.getRayConfiguration().updateRays()));

        timeElapsed += delta;


        detectionLine.x = target.getX() - getX();
        detectionLine.y = target.getY() - getY();

/*
        update(delta);

        if (canColide)
            collisionLogic();

*?

 */
        //velocity.mulAdd(steeringOutput.linear, delta);

        //moveBy(velocity.x, velocity.y);

        // for debugging
        // drawDetectionLine();

        if (timeElapsed > 2f) {
            virusBullets.add(new SanatizerBullet.Builder(getX(), getY(), new Vector2(detectionLine.x, detectionLine.y), 2f,assetManager.manager.get(assetManager.bulletSprite)).build());
            timeElapsed = 0;
        }



        int StartX = (int)getX() / 16;
        int StartY = (int)getY() / 16;



        int endX = (int)target.getX() / 16;
        int endY = (int)target.getY() / 16;

        TileData startNode = fluVirusTileColliderMap.get(0).get(StartY).get(StartX);
        TileData endNode = fluVirusTileColliderMap.get(0).get(endY).get(endX);

        System.out.println(startNode + " " + endNode);

        // resultPath is empty and gets mutated by this function
        System.out.println(chase.searchNodePath(startNode, endNode, new HeuristicTile(), resultPath));


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

        fluVirusTileCollider.tileHorizontal(velocity);
        fluVirusTileCollider.tileVertical(velocity);




        return true;
    }

}
