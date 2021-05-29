package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Particles.BulletSplash;
import com.mygdx.game.Particles.ParticleManager;
import com.mygdx.game.Tiles.TileCollision;
import com.mygdx.game.Tiles.TileData;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

public class SanatizerBullet extends AbstractBullet  {

    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    private TileCollision bulletColider;

    private boolean collided = false;
    private final float x,y,speed;
    public boolean remove, outOfBounds, playerHit = false, isLethal = false;
    private final boolean canCollide, testBullet;
    private Rectangle rectangle;
    private final Sprite sprite;
    private final Vector2 velocity;
    private final int maxBounces;
    private final int mapIndex;

    private float timeElapsed, distanceTraveled = 27f;

    private Animation<TextureRegion> splash;
    private TextureAtlas atlas;
    private boolean collsionAnimation = false;
    private Deque<BulletSplash> wallCollsions;
    private Sprite test;

    private Vector2 ogPos = new Vector2();
    private Vector2 quad = new Vector2();

    private SanatizerBullet(Builder builder) {

        super(builder.tileDataMap);
        this.wallCollsions = new LinkedList<>();
        this.mapIndex = builder.mapIndex;
        this.sprite = builder.sprite;
        this.test = new Sprite(new Texture(Gdx.files.internal("test_tile.png")));
        test.setPosition(200,200);
        this.x = builder.x;
        this.y = builder.y;
        ogPos.x = builder.x;
        ogPos.y = builder.y;
        this.maxBounces = builder.maxBounces;
        this.speed = builder.speed;
        this.velocity = builder.velocity;
        this.remove = false;
        //this.tileDataMap = builder.tileDataMap;
        this.rectangle = builder.rectangle;
        this.canCollide = builder.canCollide;
        this.testBullet = builder.testBullet;
        this.atlas = builder.atlas;
        this.splash = builder.bulletExplosionAnimation;
        this.quad = builder.quad;

        setPosition(x ,y);
        this.bulletColider = new TileCollision.Builder().tileMap(tileDataMap.get(0), tileDataMap.get(1)).calcCorners(rectangle).charecter(this).build();

        sprite.scale(-0.2f);

    }



    // x and y are the players x and y

    public static class Builder {


        private final float x,y, speed;
        private final Vector2 velocity;
        private final Rectangle rectangle;
        private boolean canCollide;
        private Sprite sprite;
        private TileCollision bulletColider;
        private ArrayList<ArrayList<ArrayList<com.mygdx.game.Tiles.TileData>>> tileDataMap;
        private int maxBounces;
        private int mapIndex;
        private boolean testBullet;
        private TextureAtlas atlas;
        private Animation<TextureRegion> bulletExplosionAnimation;
        private Vector2 quad = new Vector2();


        public Builder(float x, float y, Vector2 velocity, float speed, Texture texture) {


            this.x = x;
            this.y = y;
            this.speed = speed;
            this.velocity = velocity.scl(speed);
            this.sprite = new Sprite(texture);
            this.rectangle = new Rectangle(x, y, sprite.getWidth(), sprite.getHeight());
            this.mapIndex = -1;




        }


        public Builder initCollision(ArrayList<ArrayList<ArrayList<TileData>>> a) {

            this.canCollide = true;
            this.tileDataMap = a;
            return this;

        }

        public Builder maxBounces(int a) {
            this.maxBounces = a;
            return this;
        }

        public Builder whichMap(int index) {
            this.mapIndex = index;
            return this;
        }

        public Builder testBullet(boolean a) {

            testBullet = a;
            return this;

        }

        public Builder addQuad(Vector2 quad) {

            this.quad = quad;
            return this;

        }

        public Builder explosionAnimation(TextureAtlas atlas) {
            this.atlas = atlas;
            this.bulletExplosionAnimation = new Animation<TextureRegion>(1/60f, atlas.getRegions());
            return this;
        }

        public SanatizerBullet build() {
            return new SanatizerBullet(this);
        }
    }


    @Override
    public Rectangle getBoundingBox() {
        return rectangle;
    }

    @Override
    public void drawDebugBox() {

    }

    @Override
    public void setBoundingBox(float x, float y, float width, float height) {

    }

    // loops over every active sprite collsion animation sets the region and animates the collsion
    @Override
    public void draw(Batch batch, float parentAlpha) {

        sprite.draw(batch);

    }

    public Vector2 getVelocity() {
        return this.velocity;
    }

    @Override
    public boolean collisionLogic() {


        bulletColider.tileHorizontal(velocity);
        bulletColider.tileVertical(velocity);


        return true;
    }

    @Override
    public void bottomCollision(int x, int y) {
        velocity.y *= -1;
        //rectangle.setPosition(rectangle.getX(), tileDataMap.get(0).get(y).get(x).getBottomEdge() - rectangle.getHeight());
        if (atlas != null)
        ParticleManager.splashBullets.add(new BulletSplash(atlas,180,0, getX() + getWidth() / 2, getY()));
        addNumOfBounces();
    }



    @Override
    public void topCollision(int x, int y) {
        velocity.y *= -1;
        //rectangle.setPosition(rectangle.getX(), tileDataMap.get(0).get(y).get(x).getTopEdge());

        if (atlas != null)
        ParticleManager.splashBullets.add(new BulletSplash(atlas,0,0, getX() + getWidth() / 2, getY()));

        addNumOfBounces();
    }

    @Override
    public void rightCollision(int x, int y) {

        velocity.x *= -1;
        //rectangle.setPosition(tileDataMap.get(0).get(y).get(x).getRightEdge(), getY());
        if (atlas != null)
        ParticleManager.splashBullets.add(new BulletSplash(atlas,-90,0, getX() + getWidth() / 2, getY()));
        addNumOfBounces();


    }

    @Override
    public void leftCollision(int x, int y) {

        velocity.x *= -1;
        //rectangle.setPosition(tileDataMap.get(0).get(y).get(x).getLeftEdge() - rectangle.getWidth(), getY());
        if (atlas != null)
        ParticleManager.splashBullets.add(new BulletSplash(atlas,90,0, getX() + getWidth() / 2, getY()));
        addNumOfBounces();
    }

    @Override
    public Vector2 getPosition() {
        return null;
    }




    private boolean isOutOfBounds() {

        if (rectangle.x < 16 || rectangle.x > 16 * 30) {
            outOfBounds = true;
            return true;
        }
        if (rectangle.y < 16 || rectangle.y > 16 * 16) {
            outOfBounds = true;
            return true;
        }
        return false;
    }

    @Override
    public void act(float delta) {


        // update bounding box

        rectangle.x = getX();
        rectangle.y = getY();
        rectangle.width = sprite.getWidth();
        rectangle.height = sprite.getHeight();

        timeElapsed += delta;

        // after bullet travele 10 pixels its lethal

        //System.out.println(timeElapsed * velocity.len() + " the distance");

        // make sthe bullet useless until it travels a min Distance
        if (new Vector2(ogPos.x - rectangle.x, ogPos.y - rectangle.y).len() > distanceTraveled) {

            isLethal = true;

        }



        // only two bouces max
        if (getNumOfBounces() > maxBounces) {
            remove = true;
        }



        velocity.nor();

        velocity.scl(speed);



        if (canCollide)
            collisionLogic();


        moveBy(velocity.x, velocity.y);



    }

    @Override
    protected void positionChanged() {
        sprite.setPosition(getX(), getY());
    }


    // dispose is kinda like unload it unloads everything that uses that texture
    private void dispose() {
        sprite.getTexture().dispose();
    }


    public Vector2 getVeloicty() {
        return velocity;
    }

    public boolean getOutOfBounds() {
        return outOfBounds;
    }

    public Deque<BulletSplash> getSplashAnimation() {
        return wallCollsions;
    }

    public boolean collided() {
        return collided;
    }

    public void resetCollision() {
        collided = false;
    }


}
