package com.mygdx.game;

import com.badlogic.gdx.*;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.FunctionalityClasses.MyAssetManager;

public class MainMenu implements Screen {

    private static final float PPM = 16f;

    PirateJoes pirateJoes;

    public MainMenu (PirateJoes pirateJoes) {
        this.pirateJoes = pirateJoes;
    }

    Button startButton;
    TextButton endButton;
    Table table;

    Skin mainMenuSkin;

    com.mygdx.game.FunctionalityClasses.MyAssetManager assetManager;

    Texture texture;
    Stage stage;
    ScreenViewport viewport;




    @Override
    public void show() {



        assetManager = new MyAssetManager();

        assetManager.load();

        assetManager.manager.finishLoading();

	viewport = new ScreenViewport();

	viewport.setUnitsPerPixel(1/PPM);
	viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());  // world size is width divedide by 16 and height
                                                                         // divided by 16
	
        stage = new Stage(viewport);





        texture = new Texture(Gdx.files.internal("badlogic.jpg"));




        mainMenuSkin = assetManager.manager.get(assetManager.skin);

        startButton = new Button(mainMenuSkin, "default");
        endButton = new TextButton("Quit", mainMenuSkin, "default");
        table = new Table();
        table.add(startButton, endButton);
        table.setPosition((Gdx.graphics.getWidth()/2)/PPM,(Gdx.graphics.getHeight()/2)/PPM);

        stage.addActor(table);

        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                pirateJoes.setScreen(new Level1(pirateJoes));
            }
        });

        Gdx.input.setInputProcessor(stage);


    }


    @Override
    public void render(float delta) {



        Gdx.gl.glClearColor(0, 1, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        pirateJoes.batch.setProjectionMatrix(viewport.getCamera().combined);
        pirateJoes.batch.begin();




        stage.draw();
        stage.act();
        pirateJoes.batch.draw(texture,40,0, texture.getWidth()/PPM, texture.getHeight()/PPM);

        pirateJoes.batch.end();


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
}
