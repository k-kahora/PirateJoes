package com.mygdx.game.Viruses;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.FunctionalityClasses.MyAssetManager;

public abstract class AbstractEnemy extends Actor implements Enemy {


    private SpriteBatch sprite;
    public final MyAssetManager assetManager;

    public AbstractEnemy() {
        assetManager = new MyAssetManager();
        assetManager.loadEnemys();
        assetManager.manager.finishLoading();
    }

    @Override
    public void loadFiles() {



    }




}
