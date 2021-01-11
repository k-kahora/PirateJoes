package com.mygdx.game;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.FunctionalityClasses.Entity;
import com.mygdx.game.Tiles.TileCollision;
import com.mygdx.game.Tiles.TileData;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class SanatizerBullet extends Actor implements Entity {

    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    private TileCollision bulletColider;
    private final ArrayList<ArrayList<ArrayList<com.mygdx.game.Tiles.TileData>>> tileDataMap;
    private final float x,y,speed;
    public boolean remove;
    private final boolean canCollide;
    private Rectangle rectangle;
    private final Sprite sprite;
    private final Vector2 velocity;

    private int numOfBounces;

    private SanatizerBullet(Builder builder) {

        this.sprite = builder.sprite;
        this.x = builder.x;
        this.y = builder.y;
        this.speed = builder.speed;
        this.velocity = builder.velocity;
        this.remove = false;
        this.tileDataMap = builder.tileDataMap;
        this.rectangle = builder.rectangle;
        this.canCollide = builder.canCollide;

        setPosition(x,y);

        if (canCollide) {
            this.bulletColider = new TileCollision.Builder().tileMap(tileDataMap.get(0), tileDataMap.get(1)).calcCorners(rectangle).charecter(this).build();
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

    // x and y are the players x and y

    public static class Builder {


        private final float x,y, speed;
        private final Vector2 velocity;
        private final Rectangle rectangle;
        private boolean canCollide;
        private Sprite sprite;
        private TileCollision bulletColider;
        private ArrayList<ArrayList<ArrayList<com.mygdx.game.Tiles.TileData>>> tileDataMap;


        public Builder(float x, float y, Vector2 velocity, float speed, Texture texture) {


            this.x = x;
            this.y = y;
            this.speed = speed;
            this.velocity = velocity.scl(speed);
            this.sprite = new Sprite(texture);
            this.rectangle = new Rectangle(x, y, sprite.getWidth(), sprite.getHeight());


        }


        public Builder initCollision(ArrayList<ArrayList<ArrayList<TileData>>> a) {

            this.canCollide = true;
            this.tileDataMap = a;
            return this;

        }

        public SanatizerBullet build() {
            return new SanatizerBullet(this);
        }



    }

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
    public Vector2 getPosition() {
        return null;
    }



    @Override
    public void bottomCollision(int x, int y) {
        velocity.y *= -1;
        rectangle.setPosition(rectangle.getX(), tileDataMap.get(0).get(y).get(x).getBottomEdge() - rectangle.getHeight());
        numOfBounces++;


    }



    @Override
    public void topCollision(int x, int y) {
        velocity.y *= -1;
        rectangle.setPosition(rectangle.getX(), tileDataMap.get(0).get(y).get(x).getTopEdge());
        numOfBounces++;
    }

    @Override
    public void rightCollision(int x, int y) {

        velocity.x *= -1;
        rectangle.setPosition(tileDataMap.get(0).get(y).get(x).getRightEdge(), getY());
        numOfBounces++;


    }

    @Override
    public void leftCollision(int x, int y) {

        velocity.x *= -1;
        rectangle.setPosition(tileDataMap.get(0).get(y).get(x).getLeftEdge() - rectangle.getWidth(), getY());
        numOfBounces++;
    }


    @Override
    public void act(float delta) {


        // update bounding box

        rectangle.x = getX();
        rectangle.y = getY();


        // only two bouces max
        if (numOfBounces > 2) {
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

}
