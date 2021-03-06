package com.mygdx.game.Tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Enumerators.Tile;
import com.mygdx.game.utils.Pair;
import org.w3c.dom.css.Rect;

// 9000 means the tile is not indexed

public class TileData {

    private TextureRegion textureRegion;
    private TextureAtlas atlas;
    private int bottom;
    private int top;
    private int right;
    private int left;
    public static int TILE_WIDTH;
    public static int TILE_HEIGHT;
    private Tile tile;
    private Array<Connection<TileData>> connectionArray = new Array<>();
    public int INDEX = 9000;
    private final Pair<Float, Float> position;
    private final boolean isWalls;

    private final Rectangle box;

    static {

        TILE_WIDTH = 16;
        TILE_HEIGHT = 16;

    }

    public TileData() {
        this.tile = Tile.BASKET_FULL;
        this.bottom = 1;
        this.INDEX = 0;
        this.position = new Pair<>(0f,0f);
        this.box = new Rectangle(0,0,16,16);
        this.isWalls = false;

    }

    public TileData(Tile tile, TextureAtlas atlas, int x, int y, boolean isWalls) {

        this.isWalls = isWalls;
        this.tile = tile;
        this.bottom = y;
        this.top = y + TILE_HEIGHT;
        this.right = x + TILE_WIDTH;
        this.left = x;

        this.atlas = atlas;
        textureRegion = atlas.findRegion(tile.getAtlasReference());



        // id there collidbale then add them to the INDEXER
        if (!isWalls) {
            //System.out.println(TileData.Indexer.getIndex());
            //System.out.println(tile);
            this.INDEX = tile != Tile.NULL ? TileData.Indexer.getIndex() : 0;
        }

        //System.out.println(INDEX);

        this.position = new Pair<>((float)x,(float)y);
        this.box = new Rectangle(x * 16, y * 16, 16, 16);


    }

    // This makes the wall just this constructor

    // scale deals with spacing
    public TileData(com.mygdx.game.Enumerators.Tile tile, TextureAtlas atlas, int scale) {
        this(tile, atlas, 0,0, true);
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


    @Override
    public String toString() {
        return tile + " index: " + INDEX;
    }

    public void createConnections(TileData toTile, float cost) {
         connectionArray.add(new TileConnection(toTile, this,cost));
    }

    public Array<Connection<TileData>> getConnectionArray() {
        return connectionArray;
    }

    public Pair getIndex() {
        return position;
    }

    private static class Indexer {

        private static int index = 0; // not sure why I have to make -1 to work but other wise its an error :\

        public static int getIndex() {

            return index++;
        }

    }

    public void setTextureRegion() {
        textureRegion = new TextureRegion(new Texture(Gdx.files.internal("test_tile.png")));
    }
}
