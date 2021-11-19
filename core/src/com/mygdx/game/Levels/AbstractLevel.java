package com.mygdx.game.Levels;

import aurelienribon.tweenengine.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Enumerators.LEVELS;
import com.mygdx.game.FunctionalityClasses.MyAssetManager;
import com.mygdx.game.MainCharacter;
import com.mygdx.game.Particles.ParticleManager;
import com.mygdx.game.SanatizerBullet;
import com.mygdx.game.Tiles.TileData;
import com.mygdx.game.Tiles.TileEditor;
import com.mygdx.game.Viruses.AbstractEnemy;
import com.mygdx.game.utils.Point;
import com.mygdx.game.TweenCustom.SpriteAccessor;

import java.util.*;

/*
Skeltal implementaion for all levels implemnts screen and Level all Actual levels must extend this class

Must implement remaining methods of screen

When a level implements abstract level the level mustr call the super contrcuto to work properly
tjis conatins the view ports assets and camera as well as all of screens interface
Impe,emting level is very important for path finding as it alows the level to be turned into a connection graph



 */



public abstract class AbstractLevel implements Level, Screen, Telegraph {

    private ArrayList<Sprite> spriteList = new ArrayList<>();

    private static PirateJoes pirateJoes;

    BitmapFont font = new BitmapFont();

    private float timeElapsed = 0f;
    public final static String tileDir;
    private static int levelCount = 0;

    private Animation<TextureRegion> container;

    private Sprite spriteContainer = new Sprite();

    protected TileEditor baseLayer,secondLayer;

    MyAssetManager assetManager;

    private OrthographicCamera camera;
    private TileEditor wallsMaker;

    private Array<AbstractEnemy> groupOfViruses;
    private Array<AbstractEnemy> killedViruses;

    private List<TileData> weakPoints = new LinkedList<>();

    public final MainCharacter character;

    public static FillViewport viewport;

    public static final int cameraWidth;
    public static final int cameraHeight;
    public static final int worldUnits;

    private static MessageDispatcher aiDispatcher;
    private static MessageDispatcher messageToLevel;

    private LinkedList<SanatizerBullet> bullets;
    private LinkedList<SanatizerBullet> removedBullets;
    private LinkedList<MainCharacter.LandMine> mines;

    private TweenManager tweenManager = new TweenManager();
    protected Array<Point<Integer, Integer>> levelEdges = new Array<>();
    TextureAtlas containerAtlas;

    static {
        cameraHeight = 17;
        cameraWidth = 31;
        worldUnits = 16;
        //location of txt maps
        tileDir = "android/assets/tileMaps/";
    }

    public AbstractLevel(PirateJoes pirateJoes) {


        this.pirateJoes = pirateJoes;
        camera = new OrthographicCamera(cameraWidth * worldUnits,cameraHeight * worldUnits);
        camera.translate((31 * 16)/2, (17*16)/2);
        viewport = new FillViewport(camera.viewportWidth,camera.viewportHeight, camera);
        aiDispatcher = new MessageDispatcher();



        // all assets need to be loaded after loadAssets

        loadAssets();
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());

        containerAtlas = assetManager.manager.get(assetManager.container);
        wallsMaker = new TileEditor("wallMap.txt", assetManager.manager.get(assetManager.bruh), true);

        container = new Animation<TextureRegion>(1/24f, containerAtlas.getRegions());

        spriteContainer.setRegion(container.getKeyFrame(200f));
        spriteContainer.setBounds(0,0, spriteContainer.getRegionWidth(), spriteContainer.getRegionHeight());
        //spriteContainer.setPosition(viewport.getWorldWidth()/2 - spriteContainer.getWidth() / 2, viewport.getWorldHeight()/2 - spriteContainer.getHeight() /2);

        spriteContainer.setPosition(-10,0);



        groupOfViruses = new Array<>();
        killedViruses = new Array<>();

        character = new MainCharacter(this);
        character.setPosition(32,32);

        character.addTileMap(getWalls().getTileMap());

        bullets = new LinkedList<>();

        //bullets = character.getBullets();
        removedBullets = new LinkedList<>();

        spriteContainer.setRegion(container.getKeyFrame(container.getAnimationDuration()));
        container.setPlayMode(Animation.PlayMode.REVERSED);


        //Tween.to(spriteContainer, SpriteAccessor.POSITION_X, 0.5f).target(0).ease(Bounce.OUT).delay(1.0f).repeatYoyo(2, 0.5f).start(tweenManager);
        
    }

    public abstract void initMessages();

    // level must implemt these
    public abstract int getNodeCount();
    public abstract void setRender();
    public abstract boolean handleMessage(Telegram msg);

    public static MessageDispatcher getMessageDispatcherAI() {
        return aiDispatcher;
    }

    public static MessageDispatcher getMessageToLevel() { return messageToLevel; }

    public static Viewport getViewport() {
        return viewport;
    }

    public static final PirateJoes getPirateJoe() {
        return pirateJoes;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
    }

    @Override
    public final void loadAssets() {

        assetManager = new MyAssetManager();
        assetManager.load();
        assetManager.manager.finishLoading();

    }

    public TileEditor getWalls() {
        return wallsMaker;
    }

    public MyAssetManager getAssetManager() {
        return assetManager;
    }

    private void clear() {

        character.clear();
        bullets.clear();

    }

    private boolean nextLevel(float delta) {

        return container.isAnimationFinished(delta);

    }

    private float timeElapsedCont = 0;

    boolean called = true, startGame = false, tweenCalled = true, startContainerLeave = false, fontTimeFlag = true;

    float timeElaspedContBack = 0;

    protected void gameStart(float delta) {

        character.draw(getPirateJoe().batch, 0);
        tweenManager.update(delta);





        if (startGame) {

            update(delta);

        } else {

            fontTimer += delta;

            if (fontTimer > 4 && fontTimeFlag) {


                startContainerLeave = true;

            }




            if (!tweenCalled) {

                

                tweenCalled = true;
                Tween.to(spriteContainer, SpriteAccessor.POSITION_X, 2.5f).target(0, -600).setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        startGame = true;
                    }
                }).start(tweenManager);

            }



            if (startContainerLeave) {

                timeElaspedContBack += delta;

                spriteContainer.setRegion(container.getKeyFrame(timeElaspedContBack, false));

                fontTimeFlag = false;

                if (container.isAnimationFinished(timeElaspedContBack)) {


                        tweenCalled = false;
                        startContainerLeave = false;
                        container.setPlayMode(Animation.PlayMode.NORMAL);


                }

            }






            for (AbstractEnemy a : groupOfViruses) {
                a.draw(getPirateJoe().batch, 0);

            }

            spriteContainer.draw(getPirateJoe().batch);

            if (fontTimeFlag)
                font.draw(getPirateJoe().batch, "" + LevelManager.currentLevel + "\n" + LevelManager.currentLevel.getLevelDescription(), 300, 200);


        }


    }

    public void update(float delta) {

        mines = character.getLandMines();



        for (MainCharacter.LandMine l : character.getLandMines())
            l.draw(getPirateJoe().batch,delta);

        if (!groupOfViruses.isEmpty() && !character.isDead())
            character.act(Gdx.graphics.getDeltaTime());

        for (int i = 0; i < bullets.size(); ++i) {

            if (i != 0) {

                for (SanatizerBullet bullet: bullets) {

                    // never refrences the same bullet
                    if (bullet != bullets.get(i))
                        bulletCollision(bullet, bullets.get(i));

                }

            }

            bullets.get(i).draw(getPirateJoe().batch,0);
            bullets.get(i).act(Gdx.graphics.getDeltaTime());

            if (bullets.get(i).collided()) {
                ParticleManager.splashBullets.addAll(bullets.get(i).getSplashAnimation());
                bullets.get(i).resetCollision();
            }

            if (bullets.get(i).isLethal && Intersector.overlaps(bullets.get(i).getBoundingBox(), character.getBoundingBox())) {

               character.death();

            }

            if (bullets.get(i).remove) {
                removedBullets.add(bullets.get(i));
                if (bullets.get(i).isPlayerBullet()) {
                    character.reduceTimesFired(bullets.get(i));
                }
            }

            for (MainCharacter.LandMine mine : character.getLandMines())

            if (Intersector.overlaps(mine.boundingBox, bullets.get(i).getBoundingBox()))
             {
                 mine.activate();
                 bullets.get(i).remove = true;
                 //removedBullets.add(bullets.get(i));
            }

        }

        for (AbstractEnemy a : groupOfViruses) {
            //removedBullets.addAll

            a.act(delta);
            a.draw(getPirateJoe().batch, 0);

            a.isHit(bullets);

            bullets.addAll(a.getVirusBullets());
            a.clearBullets();

            for (MainCharacter.LandMine l : mines) {
                yogurt(a, l);
            }

            if (a.isDead())
                killedViruses.add(a);

        }

        groupOfViruses.removeAll(killedViruses, false);

        bullets.removeAll(removedBullets);

        ParticleManager.updateBulletSplashes(getPirateJoe().batch, delta);
        ParticleManager.updateSlimeSploshions(getPirateJoe().batch, delta, groupOfViruses, character);

        if (enableAnimation) {

            timeElapsedCont += delta;
            spriteContainer.setRegion(container.getKeyFrame(timeElapsedCont, false));

        }


        if (groupOfViruses.isEmpty()) {

            clear();
            if (called) {
                spriteContainer.setRegion(container.getKeyFrame(0));
                called = false;
                Tween.to(spriteContainer, SpriteAccessor.POSITION_X, 1.0f).target(0, 0).setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        enableAnimation = true;
                    }
                }).start(tweenManager);



            }



            spriteContainer.draw(getPirateJoe().batch);

            if (container.isAnimationFinished(timeElapsedCont)) {

                    ParticleManager.clear();

                    LevelManager.incrementLeve();

            }

        }



    }

    float fontTimer = 0;

    boolean enableAnimation = false;



    public Array<AbstractEnemy> getFluViruses() {

        return groupOfViruses;

    }

    private void bulletCollision(SanatizerBullet previousBullet, SanatizerBullet bullet) {

        if (Intersector.overlaps(previousBullet.getBoundingBox(), bullet.getBoundingBox())) {
            previousBullet.remove = true;
            bullet.remove = true;
           // removedBullets.add(previousBullet);
           // removedBullets.add(bullet);
        }

    }

    public static void destroyWalls(LinkedList<MainCharacter.LandMine> mines1, List<TileData> weakPoints1) {

        for (TileData v : weakPoints1) {

            for (MainCharacter.LandMine l : mines1) {

                if (l.blownUp && new Vector2(l.returnPosition().x - v.getWeakPoint().x, l.returnPosition().y - v.getWeakPoint().y).len() < l.killRadius) {

                    v.stable = false;

                }

            }

        }

    }

    private void yogurt(AbstractEnemy a, MainCharacter.LandMine l) {

        if (l.blownUp && new Vector2(l.returnPosition().x - a.getX(), l.returnPosition().y - a.getY()).len() < l.killRadius) {

            //a.setTagged(true);
            a.setTagged(true);
            a.setDetonationToInstant();

        }

    }

    public void render(float delta) {


        timeElapsed += delta;

        // sets mouse cord relative to pixels
        PirateJoes.mouseCordinates.x = Gdx.input.getX();
        PirateJoes.mouseCordinates.y = Gdx.input.getY();

        getPirateJoe().batch.setProjectionMatrix(getViewport().getCamera().combined);

        // turns pixel cordinates to world cordinates
        getViewport().getCamera().unproject(PirateJoes.mouseCordinates);

        getPirateJoe().batch.begin();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        baseLayer.draw(getPirateJoe().batch);
        secondLayer.draw(getPirateJoe().batch);
        getWalls().draw(getPirateJoe().batch);

        gameStart(delta);
        //update(delta);

        getPirateJoe().batch.end();
        character.drawDebugBox();

        //viewport.getCamera().position.set(character.getX(),character.getY(),0);
        getViewport().getCamera().update();

    }

    @Override
    public Array<Connection<TileData>> getConnections(TileData fromNode) {

        return fromNode.getConnectionArray();

    }

    public static class LevelManager {

        static int makeNextLevelEnum = 0;

        public static LEVELS currentLevel = LEVELS.values()[makeNextLevelEnum];


        public static void incrementLeve() {

            makeNextLevelEnum++;
            currentLevel = LEVELS.values()[makeNextLevelEnum];

            getPirateJoe().setScreen(currentLevel.getCurrentLevel());


        }

    }

    public void addBullets(SanatizerBullet sanatizerBullet) {
        bullets.add(sanatizerBullet);
    }

}
