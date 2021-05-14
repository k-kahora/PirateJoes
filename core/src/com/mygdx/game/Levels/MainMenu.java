package com.mygdx.game.Levels;

import com.badlogic.gdx.*;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.FunctionalityClasses.MyAssetManager;
import com.mygdx.game.Levels.Level1;
import com.mygdx.game.Levels.PirateJoes;
import com.mygdx.game.Tiles.TileData;

import java.util.ArrayList;

public class MainMenu extends AbstractLevel{

    private static final float PPM = 16f;

    Button startButton, quitButtom;

    Table table;
    Sprite sprite;
    Image background;

    Skin mainMenuSkin;

    com.mygdx.game.FunctionalityClasses.MyAssetManager assetManager;

    Texture texture;
    Stage stage;

    public MainMenu (PirateJoes pirateJoes) {
        super(pirateJoes);
    }

    @Override
    public void show() {

        assetManager = new MyAssetManager();
        assetManager.loadGui();
        assetManager.manager.finishLoading();

  // world size is width divedide by 16 and height
                                                                         // divided by 16
        stage = new Stage(getViewport(),getPirateJoe().batch);

        texture = assetManager.manager.get(assetManager.startScreen);
        background = new Image(texture);
        background.setPosition(0,0);

        mainMenuSkin = assetManager.manager.get(assetManager.skin);

        startButton = new Button(mainMenuSkin, "default");
        quitButtom = new Button(mainMenuSkin, "quit");
        table = new Table();
        table.add(startButton);
        table.add(quitButtom);
        table.setPosition(getViewport().getCamera().viewportWidth/2,getViewport().getCamera().viewportHeight/2);

        stage.addActor(background);
        stage.addActor(table);


        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                getPirateJoe().setScreen(new Level1(getPirateJoe()));
            }
        });

        quitButtom.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        getPirateJoe().batch.setProjectionMatrix(getViewport().getCamera().combined);

        getPirateJoe().batch.begin();

       // background.draw(getPirateJoe().batch,1);

        getPirateJoe().batch.end();

        stage.draw();
        stage.act();
        //viewport.getCamera().update();
    }

    @Override
    public void resize(int width, int height) {

        //gayport.update(width,height);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {

        stage.clear();
        stage.dispose();
        texture.dispose();
        mainMenuSkin.dispose();



    }

    @Override
    public ArrayList<ArrayList<ArrayList<TileData>>> getCollisionMap() {
        return null;
    }

    @Override
    public ArrayList<? extends Steerable<Vector2>> getEnemeyGroup() {
        return null;
    }

    @Override
    public Array<Connection<TileData>> getConnections(TileData fromNode) {
        return null;
    }

    @Override
    public void initMessages() {

    }

    @Override
    public int getIndex(TileData node) {
        return 0;
    }

    @Override
    public int getNodeCount() {
        return 0;
    }

    @Override
    public void setRender() {

    }

    @Override
    public boolean handleMessage(Telegram msg) {
        return false;
    }
}
