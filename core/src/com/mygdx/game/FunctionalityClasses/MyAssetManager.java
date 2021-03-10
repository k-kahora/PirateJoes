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
    public AssetDescriptor<TextureAtlas> slime = new AssetDescriptor<TextureAtlas>("spriteSheets/slime_right.atlas", TextureAtlas.class);
    public AssetDescriptor<TextureAtlas> tileMap = new AssetDescriptor<TextureAtlas>("spriteSheets/tileMap.atlas", TextureAtlas.class);
    public AssetDescriptor<TextureAtlas> collisionMap = new AssetDescriptor<TextureAtlas>("spriteSheets/collision_map.atlas", TextureAtlas.class);
    public AssetDescriptor<Texture> bulletSprite = new AssetDescriptor<Texture>("spriteSheets/bulletSanitizer.png", Texture.class);
    public AssetDescriptor<Texture> fluVirus = new AssetDescriptor<Texture>("spriteSheets/FluVirus.png", Texture.class);
    public AssetDescriptor<Texture> startScreen = new AssetDescriptor<Texture>("sprites/homescreen.png", Texture.class);

    public AssetDescriptor<TextureAtlas> splashBullet = new AssetDescriptor<TextureAtlas>("spriteSheets/bulletCollisionBubble.atlas", TextureAtlas.class);
    public AssetDescriptor<TextureAtlas> slimeSploshion = new AssetDescriptor<>("spriteSheets/slimeDetonation.atlas", TextureAtlas.class);

    public AssetDescriptor<TextureAtlas> slimeBlows = new AssetDescriptor<TextureAtlas>("spriteSheets/slimeBlow.atlas", TextureAtlas.class);
    public AssetDescriptor<Texture> storeManager = new AssetDescriptor<Texture>("sprites/Manager.png", Texture.class);

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
    }

    public void loadGui() {
        manager.load(skin);
        manager.load(startScreen);
    }

    public void loadTileMap() {
        manager.load(tileMap);
        manager.load(collisionMap);
    }

    public void loadEnemys() {
        manager.load(slime);
        manager.load(fluVirus);
        manager.load(splashBullet);
        manager.load(slimeBlows);
        manager.load(slimeSploshion);
    }

    public void dispose() {
        manager.dispose();
    }



}
