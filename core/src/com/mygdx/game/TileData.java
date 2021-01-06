package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TileData {

    private Map<String ,TextureRegion> textures;
    private TextureRegion textureRegion;
    private TextureAtlas atlas;
    private int bottom;
    private int top;
    private int right;
    private int left;
    public static int TILE_WIDTH;
    public static int TILE_HEIGHT;
    private Tile tile;
    private boolean collisionLock;

    static {

        TILE_WIDTH = 16;
        TILE_HEIGHT = 16;

    }

    public TileData() {
        this.tile = Tile.BASKET_FULL;
        this.bottom = 1;



    }

    public TileData(Tile tile, TextureAtlas atlas, int x, int y) {

        this.tile = tile;
        this.bottom = y;
        this.top = y + TILE_HEIGHT;
        this.right = x + TILE_WIDTH;
        this.left = x;
        this.collisionLock = false;

        this.atlas = atlas;
        textureRegion = atlas.findRegion(tile.getAtlasReference());


    }

    // scale deals with spacing
    public TileData(Tile tile, TextureAtlas atlas, int scale) {
        this(tile, atlas, 0,0);
        TILE_HEIGHT *= scale;
        TILE_WIDTH *= scale;
    }




        // this sets the size of textures bases on how many regions in the atlas
        // it then adds each texture to the list of TextureRegions


    public TextureRegion getTextureRegion() {

        return textureRegion;
    }

    public int getRightEdge() {
        return this.right;
    }

    public int getBottomEdge() {
        return this.bottom;
    }

    public int getLeftEdge() {
        return this.left;
    }

    public int getTopEdge() {
        return this.top;
    }


    public boolean isCollidedDown() {
        return isCollidedDown();
    }

    public Tile getTile() {
        return tile;
    }

    // returns true if teh tiles are at the same position

}
