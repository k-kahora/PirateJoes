package com.mygdx.game;

import static java.lang.Math.abs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.Particles.BulletSplash;
import com.mygdx.game.FunctionalityClasses.EntityLocation;
import com.mygdx.game.FunctionalityClasses.MyAssetManager;
import com.mygdx.game.Levels.AbstractLevel;
import com.mygdx.game.Levels.PirateJoes;
import com.mygdx.game.Tiles.TileCollision;
import com.mygdx.game.Tiles.TileData;
import com.mygdx.game.utils.Messages;
import com.mygdx.game.utils.SteeringUtils;
import org.w3c.dom.css.Rect;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

public class MainCharacter extends Actor implements EntityLocation {

    private com.mygdx.game.FunctionalityClasses.MyAssetManager manager1 = new com.mygdx.game.FunctionalityClasses.MyAssetManager();

    private Rectangle rectangle;
    private static int debugBoxScale;

    private com.mygdx.game.Tiles.TileCollision collision;
    private ArrayList<ArrayList<ArrayList<com.mygdx.game.Tiles.TileData>>> collisionMap;

    private Animation<TextureRegion> leftWalk;
    private Animation<TextureRegion> rightIdle;
    private Animation<TextureRegion> leftIdle;
    private Animation<TextureRegion> rightWalk;

    private final Deque<BulletSplash> splashAnimations;

    Vector2 direction, velocity, position;


    float friction, maxSpeed, acceleration;

    boolean flagRight = false;
    boolean flagLeft = false;
    boolean spacePressed = false;

    boolean shooting = false;
    private float shootingTimer = 0, timeElapsedMines = 0;
    private int numTimesFired;

    public float xBeforeVector;
    public float yBeforeVector;

    private Vector2 centerPosition = new Vector2();

    Sprite sprite;

    TextureAtlas textureRight;
    TextureAtlas textureLeft;
    TextureAtlas textureidleRight;
    TextureAtlas textureidleLeft;

    float timeElapsed; // for animation

    com.mygdx.game.FunctionalityClasses.MyAssetManager assetManager;

    LinkedList<SanatizerBullet> bullets, removedBullets;
    LinkedList<LandMine> landMines;

    private int clickCount;

    public MainCharacter() {

        assetManager = new MyAssetManager();
        assetManager.loadCharecter();
        assetManager.manager.finishLoading();

        splashAnimations = new LinkedList<>();

        bullets = new LinkedList<>();
        numTimesFired = 0;
        removedBullets = new LinkedList<>();

        landMines = new LinkedList<>();

        maxSpeed = 2f;
        friction = 0.01f;
        acceleration = 0.5f;

        // how many bullets can be fired in succesion
        clickCount = 0;

        textureRight = assetManager.manager.get(assetManager.charecterAtlasRight);
        textureLeft = assetManager.manager.get(assetManager.charecterAtlasLeft);
        textureidleRight = assetManager.manager.get(assetManager.charecterIdleRight);
        textureidleLeft = assetManager.manager.get(assetManager.charecterIdleLeft);

        leftWalk = new Animation<TextureRegion>(1/12f, textureLeft.getRegions());
        rightIdle = new Animation<TextureRegion>(1/12f,textureidleRight.getRegions());
        leftIdle = new Animation<TextureRegion>(1/12f,textureidleLeft.getRegions());
        rightWalk = new Animation<TextureRegion>(1/12f,textureRight.getRegions());

        sprite = new Sprite(assetManager.manager.get(assetManager.storeManager));

        //sprite = new Sprite(new Texture(Gdx.files.internal("spriteSheets/pink.png")));

        // sets position of actor and of sprite
        //setPosition(Gdx.graphics.getWidth()/2 - sprite.getWidth()/2, Gdx.graphics.getHeight()/2 - sprite.getHeight()/2);
        //sprite.setPosition(Gdx.graphics.getWidth()/2 - sprite.getWidth()/2, Gdx.graphics.getHeight()/2 - sprite.getHeight()/2);
        //sprite.scale(0.01f);

        direction = new Vector2();
        velocity = new Vector2();
        position = new Vector2();

        debugBoxScale = 4;

        rectangle = new Rectangle(getX() + debugBoxScale,getY(), sprite.getWidth(), sprite.getHeight());
       setBounds(rectangle.getWidth(), getY(), sprite.getWidth(), sprite.getHeight());

        xBeforeVector = getX();
        yBeforeVector = getY();

        collisionMap = new ArrayList<ArrayList<ArrayList<com.mygdx.game.Tiles.TileData>>>();

    }

    public void addTileMap(ArrayList<ArrayList<TileData>> a) {

        this.collisionMap.add(a);

    }


    public Vector2 getDirection() {

        return direction;

    }

    @Override
    public void act(float delta) {

    //velocity = direction.scl(maxSpeed);

        timeElapsed += delta;

        yBeforeVector = getY();
        xBeforeVector = getX();
        position.x = getX();
        position.y = getY();

        centerPosition.x = getX() + getWidth() / 2;
        centerPosition.y = getY() + getHeight() / 2;

        keys(delta);

        // once you start clicking you can only fore five untile shoting Timer is 0
        // if the player shoot start a timer
        if (numTimesFired > 0) {
            shootingTimer += Gdx.graphics.getDeltaTime();
        }
        if (shootingTimer > 1.75f) {
            shootingTimer = 0;
            numTimesFired = 0;
        }

        if (!direction.equals(new Vector2())) {

            //velocity.x += 0.1f;

            velocity.interpolate(direction.scl(maxSpeed), acceleration, Interpolation.linear);

        } else {



            velocity.interpolate(new Vector2(0,0), friction, Interpolation.linear);

            // prevents infinitely small numbers

            if (abs(velocity.x) < 0.1) {
                velocity.x = 0;
            }

            if (abs(velocity.y) < 0.1) {
                velocity.y = 0;
            }

        }



        direction.setZero();



//        when velocity is 0 set the directions to false and set the sprie to idle.
//        this is for animation
        if (velocity.equals(new Vector2())) {
            if (flagRight) {
                sprite.setRegion(rightIdle.getKeyFrame(timeElapsed,true));

                //sprite.setRegion(animatorIdleRight.getFrame());
            }
            if (flagLeft) {



                sprite.setRegion(leftIdle.getKeyFrame(timeElapsed, true));

            }

        } else {

            if (flagRight) {

                sprite.setRegion(rightWalk.getKeyFrame(timeElapsed, true));

            }

            if (flagLeft) {


                sprite.setRegion(leftWalk.getKeyFrame(timeElapsed, true));
            }

        }

        collisionLogic();
        deltaMines(delta);

    }

    @Override
    protected void positionChanged() {
        sprite.setPosition(getX(),getY());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.draw(batch);

        drawMines(batch);

        /*
        for (int i = 0; i < bullets.size(); ++i) {
            bullets.get(i).draw(batch,0);

            if (bullets.get(i).collided()) {
                splashAnimations.addAll(bullets.get(i).getSplashAnimation());
                bullets.get(i).resetCollision();
            }

            if (i != 0) {
                bulletCollision(bullets.get(i - 1), bullets.get(i));
            }

            if (bullets.get(i).remove) {
                removedBullets.add(bullets.get(0));
            }
        }
        bullets.removeAll(removedBullets);

        for (BulletSplash splash : splashAnimations) {

            splash.draw(batch);

        }
        */


    }



    public void drawDebugBox() {

/*
        Level1.shapeRender.setProjectionMatrix(Level1.getViewport().getCamera().combined);
        Level1.shapeRender.setColor(Color.RED);

        Level1.shapeRender.begin(ShapeRenderer.ShapeType.Line);
        Level1.shapeRender.rect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
        for (SanatizerBullet bullets : bullets) {
            Rectangle bulletBox = bullets.getBoundingBox();
            Level1.shapeRender.rect(bulletBox.getX(), bulletBox.getY(), bulletBox.getWidth(), bulletBox.getHeight());
        }
        Level1.shapeRender.end();


 */
        rectangle.x = getX() + debugBoxScale;
        rectangle.y = getY() + debugBoxScale;
        rectangle.width = getWidth() - (2 * debugBoxScale);
        rectangle.height = getHeight() - (2 * debugBoxScale);


        //System.out.println(getWidth());



    }

    @Override
    public void setBoundingBox(float x, float y, float width, float height) {

    }

    @Override
    public Vector2 getVelocity() {
        return velocity;
    }

    @Override
    public boolean collisionLogic() {
        if (collision.tileHorizontal(velocity)) {

            moveBy(velocity.x, 0);
        }
        if (collision.tileVertical(velocity)) {

            moveBy(0,velocity.y);
        }
        return false;
    }

    // the reason this works with one collison map is because the poitioning of the map doesnt change.

    @Override
    public void bottomCollision(int x, int y) {
        rectangle.setPosition(rectangle.getX(), collisionMap.get(0).get(y).get(x).getBottomEdge() - rectangle.getHeight());
        velocity.y = 0;
    }

    @Override
    public void topCollision(int x, int y) {
        rectangle.setPosition(rectangle.getX(), collisionMap.get(0).get(y).get(x).getTopEdge());
        velocity.y = 0;
    }

    @Override
    public void rightCollision(int x, int y) {

        rectangle.setPosition(collisionMap.get(0).get(y).get(x).getRightEdge(), rectangle.getY());
        velocity.x = 0;

    }

    @Override
    public void leftCollision(int x, int y) {
        rectangle.setPosition(collisionMap.get(0).get(y).get(x).getLeftEdge() - rectangle.getWidth(), rectangle.getY());
        velocity.x = 0;
    }


    private void keys(float delta) {


        if (spacePressed)
            timeElapsedMines += delta;


        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            flagLeft = false;

            flagRight = true;



            direction.x = 1f;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {

            // thse flags make sure only one direction is happening at a time
            flagRight = false;

            flagLeft = true;

            direction.x = -1f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            direction.y = 1f;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            direction.y = -1f;
        }

        // if the timer started and there is more than five bullets dont call shoot
        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {

            numTimesFired++;
            if (shootingTimer != 0 && numTimesFired > 5) {
                return;
            }
           shoot();

        }

        // if list is empty add one to the list if not wait till a timer is reached before allowing a second
        // also only a max of two mines at a time
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {

            spacePressed = true;

            if (landMines.size() < 1)
                landMines.add(new LandMine(10f, assetManager.manager.get(assetManager.landMine)));

            if (timeElapsedMines > 1f && landMines.size() < 2) {
                landMines.add(new LandMine(10f, assetManager.manager.get(assetManager.landMine)));
                timeElapsedMines = 0;
                spacePressed = false;
            }

        }

    }

    public Rectangle getBoundingBox() {
        return rectangle;
    }

    public void addCollider(TileCollision collision) {
        this.collision = collision;
    }

//     called when the player cliks the right mouse
//     Converts a vectore between the charecter and the mouse and adds a bullet to teh Queue and the removes them if they collide.
//     The moment we start shotting the designated time we only can fire five bullets untile the timer runs out.
    private void shoot() {

        velocity.x = 0;
        velocity.y = 0;

        float rectangleMidX = (rectangle.getX() + rectangle.getWidth()/2);
        float rectangleMidY = (rectangle.getY() + rectangle.getHeight()/2);

        Vector2 bulletVellocity = new Vector2(com.mygdx.game.Levels.PirateJoes.mouseCordinates.x, com.mygdx.game.Levels.PirateJoes.mouseCordinates.y);

        // converts vector to appropiate angle
        mouseCordinatesRelativeToActor(bulletVellocity, rectangleMidX, rectangleMidY);


            bullets.add(new SanatizerBullet.Builder(rectangleMidX, rectangleMidY, bulletVellocity, 2.1f ,assetManager.manager.get(assetManager.bulletSprite))
                    .initCollision(collisionMap).maxBounces(10)
                    .explosionAnimation(assetManager.manager.get(assetManager.splashBullet)).build());


    }

    // mutates the vector
    private void mouseCordinatesRelativeToActor(Vector2 vector, float vectorx, float vectory) {

        vector.x = com.mygdx.game.Levels.PirateJoes.mouseCordinates.x - vectorx;
        vector.y = PirateJoes.mouseCordinates.y - vectory;

    }

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

    public LinkedList<SanatizerBullet> getBullets() {

        return bullets;

    }

    public Vector2 getCenterPosition() {

        return centerPosition;

    }

    public class LandMine {

        Vector2 position;
        float deathRadius;
        Sprite sprite;
        Rectangle boundingBox;
        float timeElasped = 1, yogurtBlowUpDelta = 0, scaleFactor = 1;
        public boolean blownUp = false;

        public final float killRadius = 40f;

        private Animation<TextureRegion> yogurtBlowUp;

        // implement animation
        public LandMine(float deathRadius, Texture atlas) {

            this.position = new Vector2(getX(), getY());

            sprite = new Sprite(atlas);
            boundingBox = new Rectangle(getX(), getY(), sprite.getWidth(), sprite.getHeight());
            this.deathRadius = deathRadius;

            yogurtBlowUp = new Animation<TextureRegion>(1 / 24f, assetManager.manager.get(assetManager.yogurtBlowUp).getRegions());

            sprite.setPosition(getX(), getY());

        }

        public void act(float delta) {

            float centerX = position.x + sprite.getRegionWidth()/2;
            float centerY = position.y + sprite.getRegionHeight()/2;



            sprite.setScale(scaleFactor);

            scaleFactor += 0.001;
            this.timeElasped += delta;

            if (timeElasped > 4f) {

                blownUp = true;

                sprite.setScale(8f);

                yogurtBlowUpDelta += delta;

                sprite.setRegion(yogurtBlowUp.getKeyFrame(yogurtBlowUpDelta));

                if (yogurtBlowUp.isAnimationFinished(yogurtBlowUpDelta))
                    landMines.remove(this);

                if (new Vector2((MainCharacter.this.getX() + MainCharacter.this.rectangle.getWidth()/2) - centerX, (MainCharacter.this.getY() + MainCharacter.this.rectangle.getHeight()/2) - centerY).len() < killRadius) {
                      Gdx.app.exit();
                }
            }
        }

        public Vector2 returnPosition() {

            return position;
        }


    }

    private void drawMines(Batch batch) {

        for (LandMine mine : landMines) {

            mine.sprite.draw(batch);

        }

    }

    public LinkedList<LandMine> getLandMines() {

        return landMines;

    }

    public void deltaMines(float delta) {

        for (LandMine mine : landMines) {

            mine.act(delta);

        }

    }

    public void reset() {

        collisionMap = null;

    }


}
