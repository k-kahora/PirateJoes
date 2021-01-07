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

public class Level1 extends AbstractLevel {


    MyAssetManager assetManager;

    TileEditor baseLayer;
    TileEditor secondLayer;
    TileCollision collider;
    TileCollision wallCollider;

    private Slime slime;
    private MainCharacter character;







    public Level1(PirateJoes pirateJoes) {
        super(pirateJoes);
    }




    @Override
    public void show() {

        assetManager = new MyAssetManager();

        assetManager.load();

        assetManager.manager.finishLoading();

        baseLayer = new TileEditor("level1.txt", assetManager.manager.get(assetManager.tileMap) );
        secondLayer = new TileEditor("interact.txt", assetManager.manager.get(assetManager.collisionMap));



        //camera.translate(0,0,0);



        slime = new Slime();

        character = new MainCharacter();
        character.setPosition(32,32);
        // adss teh collision map
        character.addTileMap(secondLayer.getTileMap());
        character.addTileMap(wallsMaker.getTileMap());
        Rectangle characterBoundingBox = character.getBoundingBox();

        collider = new TileCollision.Builder().calcCorners(characterBoundingBox).tileMap(secondLayer.getTileMap(), wallsMaker.getTileMap()).charecter(character).build();
        // enables collision
        character.addCollider(collider);




    }

    @Override
    public void render(float delta) {



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
        wallsMaker.draw(getPirateJoe().batch);
//        slime.draw(pirateJoes.batch, 100);
        slime.act(Gdx.graphics.getDeltaTime());

        character.draw(getPirateJoe().batch, 0);
        character.act(Gdx.graphics.getDeltaTime());

        getPirateJoe().batch.end();
        character.drawDebugBox();


        //viewport.getCamera().position.set(character.getX(),character.getY(),0);
        getViewport().getCamera().update();

        super.render(delta);


    }
}
