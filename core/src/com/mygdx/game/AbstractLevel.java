package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/*
Skeltal implementaion for all levels implemnts screen and Level all Actual levels must extend this class
 */

public class AbstractLevel implements Level, Screen {

    private PirateJoes pirateJoes;

    public final static String tileDir;

    MyAssetManager assetManager;

    private OrthographicCamera camera;
    public final TileEditor wallsMaker;

    private static FillViewport viewport;
    public final static ShapeRenderer shapeRender;

    public static final int cameraWidth;
    public static final int cameraHeight;
    public static final int worldUnits;

    static {
        cameraHeight = 17;
        cameraWidth = 31;
        worldUnits = 16;
        shapeRender = new ShapeRenderer();
        tileDir = "android/assets/tileMaps/";
    }

    public AbstractLevel(PirateJoes pirateJoes) {

        this.pirateJoes = pirateJoes;
        camera = new OrthographicCamera(cameraWidth * worldUnits,cameraHeight * worldUnits);
        camera.translate((31 * 16)/2, (17*16)/2);
        viewport = new FillViewport(camera.viewportWidth,camera.viewportHeight, camera);

        assetManager = new MyAssetManager();
        assetManager.load();
        assetManager.manager.finishLoading();

        wallsMaker = new TileEditor("wallMap.txt", assetManager.manager.get(assetManager.collisionMap));

    }

    public static FillViewport getViewport() {
        return viewport;
    }

    public PirateJoes getPirateJoe() {
        return pirateJoes;
    }

    @Override
    public void levelDrawBorder() {

        for (int i = 0; i < 17; i ++) {
            for (int j = 0; j < 31; ++j) {

            }
        }

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
