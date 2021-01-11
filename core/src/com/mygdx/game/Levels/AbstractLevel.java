package com.mygdx.game.Levels;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.FunctionalityClasses.MyAssetManager;
import com.mygdx.game.Tiles.TileEditor;

/*
Skeltal implementaion for all levels implemnts screen and Level all Actual levels must extend this class

Must implement remaining methods of screen




 */

public abstract class AbstractLevel implements Level, Screen {

    private PirateJoes pirateJoes;


    public final static String tileDir;

    MyAssetManager assetManager;

    private OrthographicCamera camera;
    private TileEditor wallsMaker;

    private static FillViewport viewport;

    public static final int cameraWidth;
    public static final int cameraHeight;
    public static final int worldUnits;

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




    }

    @Override
    public Viewport getViewport() {
        return viewport;
    }

    public final PirateJoes getPirateJoe() {
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

        wallsMaker = new TileEditor("wallMap.txt", assetManager.manager.get(assetManager.collisionMap));

    }

    public TileEditor getWalls() {
        return wallsMaker;
    }

    public MyAssetManager getAssetManager() {
        return assetManager;
    }

}
