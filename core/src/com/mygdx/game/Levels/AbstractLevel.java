package com.mygdx.game.Levels;

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
import com.badlogic.gdx.utils.Timer;
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

import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;

/*
Skeltal implementaion for all levels implemnts screen and Level all Actual levels must extend this class

Must implement remaining methods of screen

When a level implements abstract level the level mustr call the super contrcuto to work properly
tjis conatins the view ports assets and camera as well as all of screens interface
Impe,emting level is very important for path finding as it alows the level to be turned into a connection graph



 */



public abstract class AbstractLevel implements Level, Screen, Telegraph {

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
        containerAtlas = assetManager.manager.get(assetManager.container);
        wallsMaker = new TileEditor("wallMap.txt", assetManager.manager.get(assetManager.bruh), true);

        container = new Animation<TextureRegion>(1/12f, containerAtlas.getRegions());

        spriteContainer.setRegion(container.getKeyFrame(200f));
        spriteContainer.setBounds(0,0, spriteContainer.getRegionWidth(), spriteContainer.getRegionHeight());
        spriteContainer.setPosition(viewport.getWorldWidth()/2 - spriteContainer.getWidth() / 2, viewport.getWorldHeight()/2 - spriteContainer.getHeight() /2);

        groupOfViruses = new Array<>();
        killedViruses = new Array<>();

        character = new MainCharacter(this);
        character.setPosition(32,32);

        character.addTileMap(getWalls().getTileMap());

        bullets = new LinkedList<>();

        //bullets = character.getBullets();
        removedBullets = new LinkedList<>();
        
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

    public void update(float delta) {

        System.out.println(delta);



        mines = character.getLandMines();







        character.draw(getPirateJoe().batch, 0);


        for (MainCharacter.LandMine l : character.getLandMines())
            l.draw(getPirateJoe().batch,delta);

        if (!character.isDead())
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

            a.act(delta);
            a.draw(getPirateJoe().batch, 0);
            //removedBullets.addAll

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

        if (groupOfViruses.isEmpty()) {

            clear();
            spriteContainer.setRegion(container.getKeyFrame(timeElapsedCont, true));
            timeElapsedCont += delta;
            spriteContainer.draw(getPirateJoe().batch);

            if (container.isAnimationFinished(timeElapsedCont)) {
                font.draw(getPirateJoe().batch, " " + LevelManager.currentLevel + "\n" + LevelManager.currentLevel.getLevelDescription(), 300, 200);



                //ParticleManager.clear();

                //LevelManager.incrementLeve();
            }

        }



    }

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



        // turns pixle cordinates to world cordinates
        getViewport().getCamera().unproject(PirateJoes.mouseCordinates);

        getPirateJoe().batch.begin();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        baseLayer.draw(getPirateJoe().batch);
        secondLayer.draw(getPirateJoe().batch);
        getWalls().draw(getPirateJoe().batch);





        update(delta);


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

        public static LEVELS currentLevel = LEVELS.LEVEL5;



        public static void incrementLeve() {

            switch (currentLevel) {

                case LEVEL1:
                    TileData.Indexer.reset();
                    getPirateJoe().setScreen(new Level2(getPirateJoe()));
                    currentLevel = LEVELS.LEVEL2;
                    break;
                case LEVEL2:
                    //getPirateJoe().setScreen(new Level3(getPirateJoe()));
                    TileData.Indexer.reset();
                    getPirateJoe().setScreen(new Level3(getPirateJoe()));
                    currentLevel = LEVELS.LEVEL3;
                    break;
                case LEVEL3:
                    TileData.Indexer.reset();
                    getPirateJoe().setScreen(new Level4(getPirateJoe()));
                    currentLevel = LEVELS.LEVEL4;
                    break;
                case LEVEL4:
                    TileData.Indexer.reset();
                    getPirateJoe().setScreen(new Level5(getPirateJoe()));
                    currentLevel = LEVELS.LEVEL5;
                    break;
                case LEVEL5:
                    //getPirateJoe().setScreen(new Finish(getPirateJoe()));
                    currentLevel = LEVELS.FINISH;
                    break;

            }


        }

    }

    public void addBullets(SanatizerBullet sanatizerBullet) {
        bullets.add(sanatizerBullet);
    }



}
