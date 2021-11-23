package com.mygdx.game.FunctionalityClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class MyAssetManager {


    public AssetManager manager = new AssetManager();   // dont make static for android

    public AssetDescriptor<Skin> skin = new AssetDescriptor<Skin>("gameUI/home_screen_ui.json", Skin.class);

    public AssetDescriptor<TextureAtlas> charecterAtlasLeft = new AssetDescriptor<TextureAtlas>("spriteSheets/managerLeft.atlas", TextureAtlas.class);
    public AssetDescriptor<TextureAtlas> charecterAtlasRight = new AssetDescriptor<TextureAtlas>("spriteSheets/managerRight.atlas", TextureAtlas.class);
    public AssetDescriptor<TextureAtlas> charecterIdleRight = new AssetDescriptor<TextureAtlas>("spriteSheets/idleRight.atlas", TextureAtlas.class);
    public AssetDescriptor<TextureAtlas> charecterIdleLeft = new AssetDescriptor<TextureAtlas>("spriteSheets/idleLeft.atlas", TextureAtlas.class);
    public AssetDescriptor<Texture> skull = new AssetDescriptor<Texture>("spriteSheets/skull.png", Texture.class);
    public AssetDescriptor<TextureAtlas> slime = new AssetDescriptor<TextureAtlas>("spriteSheets/slime_right.atlas", TextureAtlas.class);
    public AssetDescriptor<TextureAtlas> tileMap = new AssetDescriptor<TextureAtlas>("spriteSheets/tileMap_.atlas", TextureAtlas.class);
    public AssetDescriptor<Texture> bulletSprite = new AssetDescriptor<Texture>("spriteSheets/bulletSanitizer.png", Texture.class);
    public AssetDescriptor<TextureAtlas> fluVirus = new AssetDescriptor<TextureAtlas>("spriteSheets/fluVirus.atlas", TextureAtlas.class);
    public AssetDescriptor<Texture> startScreen = new AssetDescriptor<Texture>("sprites/homescreen.png", Texture.class);
    //public AssetDescriptor<TextureAtlas> barrier = new AssetDescriptor<>("spriteSheets/barrier_version_.atlas", TextureAtlas.class);
    public AssetDescriptor<TextureAtlas> bruh = new AssetDescriptor<TextureAtlas>("spriteSheets/collision_map.atlas", TextureAtlas.class);

    public AssetDescriptor<TextureAtlas> splashBullet = new AssetDescriptor<TextureAtlas>("spriteSheets/bulletCollisionBubble.atlas", TextureAtlas.class);
    public AssetDescriptor<TextureAtlas> slimeSploshion = new AssetDescriptor<>("spriteSheets/slimeDetonation.atlas", TextureAtlas.class);

    public AssetDescriptor<TextureAtlas> slimeBlows = new AssetDescriptor<TextureAtlas>("spriteSheets/slimeBlow.atlas", TextureAtlas.class);
    public AssetDescriptor<Texture> storeManager = new AssetDescriptor<Texture>("sprites/Manager.png", Texture.class);

    public AssetDescriptor<Texture> landMine = new AssetDescriptor<Texture>("spriteSheets/landMine.png", Texture.class);
    public AssetDescriptor<TextureAtlas> yogurtBlowUp = new AssetDescriptor<TextureAtlas>("spriteSheets/yogurt.atlas", TextureAtlas.class);

    public AssetDescriptor<TextureAtlas> nozzle= new AssetDescriptor<TextureAtlas>("spriteSheets/nozzle.atlas", TextureAtlas.class);

    public AssetDescriptor<TextureAtlas> container = new AssetDescriptor<TextureAtlas>("spriteSheets/container.atlas", TextureAtlas.class);

    public AssetDescriptor<TextureAtlas> pizzaNozzle = new AssetDescriptor<TextureAtlas>("spriteSheets/pizza_nozzle.atlas", TextureAtlas.class);

    public AssetDescriptor<TextureAtlas> pizza = new AssetDescriptor<TextureAtlas>("spriteSheets/pizza.atlas", TextureAtlas.class);

    public AssetDescriptor<TextureAtlas> bone = new AssetDescriptor<TextureAtlas>("spriteSheets/bonner.atlas", TextureAtlas.class);
    public AssetDescriptor<TextureAtlas> meat = new AssetDescriptor<TextureAtlas>("spriteSheets/meat.atlas", TextureAtlas.class);
    // adds to teh loading que
    public void load() {

        loadCharecter();
        loadEnemys();
        loadGui();
        loadTileMap();

    }

    public void loadCharecter() {
        manager.load(storeManager);
        manager.load(charecterAtlasRight);
        manager.load(charecterIdleRight);
        manager.load(charecterIdleLeft);
        manager.load(charecterAtlasLeft);
        manager.load(bulletSprite);
        manager.load(splashBullet);
        manager.load(landMine);
        manager.load(yogurtBlowUp);
        manager.load(skull);
    }

    public void loadGui() {
        manager.load(skin);
        manager.load(startScreen);
        manager.load(container);
    }

    public void loadTileMap() {
        manager.load(tileMap);
        //manager.load(barrier);
        manager.load(bruh);
    }

    public void loadEnemys() {
        manager.load(slime);
        manager.load(fluVirus);
        manager.load(splashBullet);
        manager.load(slimeBlows);
        manager.load(slimeSploshion);
        manager.load(nozzle);
        manager.load(pizzaNozzle);
        manager.load(pizza);
        manager.load(meat);
        manager.load(bone);

    }

    public void dispose() {
        manager.dispose();
    }



}
