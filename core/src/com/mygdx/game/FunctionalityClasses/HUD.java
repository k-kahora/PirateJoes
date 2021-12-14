package com.mygdx.game.FunctionalityClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Levels.AbstractLevel;

import java.util.ArrayList;
import java.util.Stack;

public class HUD {

    public Stage stage;
    private FitViewport port;
    private Label killLabel;
    private Table tabs;

    private int lives;
    private int kills;

    private Image fullLife, noLife;

    private Array<Image> livesArray = new Array<Image>();

    public HUD(SpriteBatch batch) {



        lives = 3;
        kills = 0;
        port = new FitViewport(AbstractLevel.viewport.getCamera().viewportWidth, AbstractLevel.viewport.getCamera().viewportHeight, new OrthographicCamera());
        stage = new Stage(port, batch);

        killLabel = new Label(String.format("Kills: %02d", kills), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        fullLife = new Image(new Texture(Gdx.files.internal("spriteSheets/heart_full.png")));
        noLife = new Image(new Texture(Gdx.files.internal("spriteSheets/heart_empty.png")));
        tabs = new Table();
        tabs.bottom();

        tabs.setFillParent(true);  // Size of the stage

        tabs.add(killLabel).expandX().padTop(10);

        for (int i = 0; i < lives; ++ i) {

            livesArray.add(staticFactoryFull());

            tabs.add(livesArray.peek());
        }




        stage.addActor(tabs);




    }

    public boolean updateLives() {

        if (lives == 0) {

            return true;

        }

        tabs.clearChildren();

        tabs.add(killLabel).expandX().padTop(10);

        livesArray.set(lives - 1, staticFactoryEmpty());

        for (int i = 0; i < 3; ++ i) {

            tabs.add(livesArray.get(i));

        }

        --lives;


        //stage.clear();

        return false;




    }

    private static Image staticFactoryFull() {

        return new Image(new Texture(Gdx.files.internal("spriteSheets/heart_full.png")));

    }

    private static Image staticFactoryEmpty() {

        return new Image(new Texture(Gdx.files.internal("spriteSheets/heart_empty.png")));

    }

}
