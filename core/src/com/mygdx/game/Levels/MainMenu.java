package com.mygdx.game.Levels;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.*;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.FunctionalityClasses.MyAssetManager;
import com.mygdx.game.Tiles.TileData;
import com.mygdx.game.TweenCustom.SpriteAccessor;

import java.util.ArrayList;

public class MainMenu extends AbstractLevel{

    private static final float PPM = 16f;

    Button startButton, quitButtom;



    private TweenManager tweenManager;

    Table table;
    Sprite sprite = new Sprite();
    Image background;

    Skin mainMenuSkin;

    com.mygdx.game.FunctionalityClasses.MyAssetManager assetManager;

    Texture texture;

    Sprite startLogo;

    Animation<TextureRegion> startAnimae;

    Stage stage;

    public MainMenu (PirateJoes pirateJoes) {
        super(pirateJoes);
    }

    @Override
    public void show() {



        tweenManager = new TweenManager();
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());

        assetManager = new MyAssetManager();
        assetManager.load();
        assetManager.manager.finishLoading();

        startAnimae = new Animation<TextureRegion>(1/12f, assetManager.manager.get(assetManager.melk).getRegions());

        startLogo = new Sprite(new Texture(Gdx.files.internal("spriteSheets/melk.png")));
        startLogo.setPosition(0,0);


        //startLogo.scale(20f);


        sprite.setTexture(assetManager.manager.get(assetManager.skull));
        sprite.setBounds(100,100,100,100);

        //Tween.set(startLogo, SpriteAccessor.ALPHA).target(0).start(tweenManager);


  // world size is width divedide by 16 and height
                                                                         // divided by 16
        stage = new Stage(getViewport(),getPirateJoe().batch);

        texture = assetManager.manager.get(assetManager.startScreen);
        startLogo.setBounds(0, 0, texture.getWidth(), texture.getHeight());
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

    private void runPirate() {

        getPirateJoe().setScreen(new Level1(getPirateJoe()));

    }

    float timeElasped = 0;
    boolean ran = false;

    @Override
    public void render(float delta) {

        if (timeElasped > 15f ) {

            stage.act();

            if  (!ran) {
                Tween.to(startLogo, SpriteAccessor.ALPHA, 4).target(0).start(tweenManager);
                ran = true;

            }




        }


        tweenManager.update(delta);

        timeElasped += delta;

        startLogo.setRegion(startAnimae.getKeyFrame(timeElasped, false));


        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        getPirateJoe().batch.setProjectionMatrix(getViewport().getCamera().combined);




        stage.draw();

        getPirateJoe().batch.begin();

       // getPirateJoe().batch.setColor(sprite.getColor());


        startLogo.draw(getPirateJoe().batch);

       // background.draw(getPirateJoe().batch,1);
        //getPirateJoe().batch.draw(sprite.getTexture(), 10, 10);

        getPirateJoe().batch.end();





        //stage.getBatch().setColor(sprite.getColor());
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
