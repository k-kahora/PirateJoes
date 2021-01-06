package com.mygdx.game;

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

    public AssetDescriptor<Skin> skin = new AssetDescriptor<Skin>("my_ui.json", Skin.class);

    public AssetDescriptor<TextureAtlas> charecterAtlasLeft = new AssetDescriptor<TextureAtlas>("spriteSheets/managerLeft.atlas", TextureAtlas.class);
    public AssetDescriptor<TextureAtlas> charecterAtlasRight = new AssetDescriptor<TextureAtlas>("spriteSheets/managerRight.atlas", TextureAtlas.class);
    public AssetDescriptor<TextureAtlas> charecterIdleRight = new AssetDescriptor<TextureAtlas>("spriteSheets/idleRight.atlas", TextureAtlas.class);
    public AssetDescriptor<TextureAtlas> charecterIdleLeft = new AssetDescriptor<TextureAtlas>("spriteSheets/idleLeft.atlas", TextureAtlas.class);
    public AssetDescriptor<TextureAtlas> slime = new AssetDescriptor<TextureAtlas>("spriteSheets/slime_right.atlas", TextureAtlas.class);
    public AssetDescriptor<TextureAtlas> tileMap = new AssetDescriptor<TextureAtlas>("spriteSheets/tileMap.atlas", TextureAtlas.class);
    public AssetDescriptor<TextureAtlas> secondTileMap = new AssetDescriptor<TextureAtlas>("spriteSheets/tileMapLayerTwo.atlas", TextureAtlas.class);
    public AssetDescriptor<Texture> bulletSprite = new AssetDescriptor<Texture>("spriteSheets/bulletSanitizer.png", Texture.class);

    public AssetDescriptor<Texture> storeManager = new AssetDescriptor<Texture>("sprites/Manager.png", Texture.class);

    // adds to teh loading que
    public void load() {


        // SkinLoader.SkinParameter skinParameter = new SkinLoader.SkinParameter("my_ui.atlas");



        manager.load(charecterAtlasLeft);
        manager.load(skin);
        manager.load(storeManager);
        manager.load(charecterAtlasRight);
        manager.load(charecterIdleRight);
        manager.load(charecterIdleLeft);
        manager.load(slime);
        manager.load(tileMap);
        manager.load(secondTileMap);
        manager.load(bulletSprite);

        //manager.load(skinAtlas);

        //manager.load(skin);



    }

    public void dispose() {
        manager.dispose();

    }



}
