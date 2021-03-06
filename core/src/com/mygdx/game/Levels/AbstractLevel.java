package com.mygdx.game.Levels;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.FunctionalityClasses.MyAssetManager;
import com.mygdx.game.Tiles.TileData;
import com.mygdx.game.Tiles.TileEditor;

/*
Skeltal implementaion for all levels implemnts screen and Level all Actual levels must extend this class

Must implement remaining methods of screen

When a level implements abstract level the level mustr call the super contrcuto to work properly
tjis conatins the view ports assets and camera as well as all of screens interface
Impe,emting level is very important for path finding as it alows the level to be turned into a connection graph



 */

public abstract class AbstractLevel implements Level, Screen, Telegraph {

    private static PirateJoes pirateJoes;


    public final static String tileDir;

    MyAssetManager assetManager;

    private OrthographicCamera camera;
    private TileEditor wallsMaker;

    private static FillViewport viewport;

    public static final int cameraWidth;
    public static final int cameraHeight;
    public static final int worldUnits;

    private static MessageDispatcher aiDispatcher;
    private static MessageDispatcher messageToLevel;

    static {
        cameraHeight = 17;
        cameraWidth = 31;
        worldUnits = 16;
        tileDir = "android/assets/tileMaps/";
    }

    public AbstractLevel(PirateJoes pirateJoes) {

        this.pirateJoes = pirateJoes;
        camera = new OrthographicCamera(cameraWidth * worldUnits,cameraHeight * worldUnits);
        camera.translate((31 * 16)/2, (17*16)/2);
        viewport = new FillViewport(camera.viewportWidth,camera.viewportHeight, camera);
        aiDispatcher = new MessageDispatcher();


    }

    public abstract void initMessages();

    // level must implemt these
    public abstract int getIndex(TileData node);
    public abstract int getNodeCount();



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
        assetManager.loadTileMap();
        assetManager.manager.finishLoading();

        wallsMaker = new TileEditor("wallMap.txt", assetManager.manager.get(assetManager.collisionMap), true);

    }

    public TileEditor getWalls() {
        return wallsMaker;
    }

    public MyAssetManager getAssetManager() {
        return assetManager;
    }

    public abstract boolean handleMessage(Telegram msg);


}
