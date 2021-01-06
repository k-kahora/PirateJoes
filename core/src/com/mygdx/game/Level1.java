package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import org.graalvm.compiler.core.common.type.ArithmeticOpTable;
import org.w3c.dom.css.Rect;

import java.util.ArrayList;

public class Level1 implements Screen {

    private PirateJoes pirateJoes;

    MyAssetManager assetManager;

    TileEditor baseLayer;
    TileEditor secondLayer;
    TileCollision collider;

    private OrthographicCamera camera;
    private Slime slime;
    private MainCharacter character;

    private static FillViewport viewport;
    public final static ShapeRenderer shapeRender = new ShapeRenderer();

    private static final int cameraWidth;
    private static final int cameraHeight;
    private static final int worldUnits;

    static {
        cameraHeight = 17;
        cameraWidth = 31;
        worldUnits = 16;
    }


    public Level1(PirateJoes pirateJoes) {
        this.pirateJoes = pirateJoes;
    }




    @Override
    public void show() {



        assetManager = new MyAssetManager();

        assetManager.load();

        assetManager.manager.finishLoading();

        baseLayer = new TileEditor("android/assets/tileMaps/level1.txt", assetManager.manager.get(assetManager.tileMap) );
        secondLayer = new TileEditor("android/assets/tileMaps/interact.txt", assetManager.manager.get(assetManager.secondTileMap));

        camera = new OrthographicCamera(cameraWidth * worldUnits,cameraHeight * worldUnits);
        camera.translate((31 * 16)/2, (17*16)/2);
        //camera.translate(0,0,0);

        viewport = new FillViewport(camera.viewportWidth,camera.viewportHeight, camera);

        slime = new Slime();

        character = new MainCharacter();
        character.setPosition(0,0);
        // adss teh collision map
        character.addTileMap(secondLayer.getTileMap());
        Rectangle characterBoundingBox = character.getBoundingBox();


        collider = new TileCollision.Builder().calcCorners(characterBoundingBox).tileMap(secondLayer.getTileMap()).charecter(character).build();

        // enables collision
        character.addCollider(collider);




    }

    @Override
    public void render(float delta) {

        // sets mouse cord relative to pixels
        PirateJoes.mouseCordinates.x = Gdx.input.getX();
        PirateJoes.mouseCordinates.y = Gdx.input.getY();

        pirateJoes.batch.setProjectionMatrix(viewport.getCamera().combined);

        // turns pixle cordinates to world cordinates
        viewport.getCamera().unproject(PirateJoes.mouseCordinates);


        pirateJoes.batch.begin();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        baseLayer.draw(pirateJoes.batch);
        secondLayer.draw(pirateJoes.batch);
//        slime.draw(pirateJoes.batch, 100);
        slime.act(Gdx.graphics.getDeltaTime());

        character.draw(pirateJoes.batch, 0);
        character.act(Gdx.graphics.getDeltaTime());

        pirateJoes.batch.end();
        character.drawDebugBox();


        //viewport.getCamera().position.set(character.getX(),character.getY(),0);
        viewport.getCamera().update();


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
        shapeRender.dispose();
    }

    // Figure out cordinate system right now

    public static FillViewport getViewport() {
        return viewport;
    }

}
