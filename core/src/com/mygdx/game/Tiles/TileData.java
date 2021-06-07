package com.mygdx.game.Tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Enumerators.Tile;
import com.mygdx.game.utils.Edge;
import com.mygdx.game.utils.Point;

// 9000 means the tile is not indexed

public class TileData implements Cloneable{

    private TextureRegion textureRegion;
    private TextureAtlas atlas;
    private int bottom;
    private int top;
    private int right;
    private int left;
    public static int TILE_WIDTH;
    public static int TILE_HEIGHT;
    public final boolean broken;
    private Tile tile;
    private Array<Connection<TileData>> connectionArray = new Array<>();
    public int INDEX = 9000;
    private final Point<Float, Float> position;
    private final boolean isWalls;
    public boolean stable = true;
    private Array<Edge<Integer>> edges = new Array<>();

    private Vector2 weakPoint = new Vector2();

    private final Rectangle box;

    static {

        TILE_WIDTH = 16;
        TILE_HEIGHT = 16;

    }

    public TileData() {

        this.tile = Tile.BASKET_FULL;
        this.bottom = 1;
        this.INDEX = 0;
        this.position = new Point<>(0f,0f);
        this.box = new Rectangle(0,0,16,16);
        this.isWalls = false;
        broken = false;

    }

    public TileData(Tile tile, TextureAtlas atlas, int x, int y, boolean isWalls) {

        this.isWalls = isWalls;
        this.tile = tile;
        this.bottom = y;
        this.top = y + TILE_HEIGHT;
        this.right = x + TILE_WIDTH;
        this.left = x;

        this.atlas = atlas;

        // should be clockwise
        edges.add(new Edge<Integer>(new Point<>(left, bottom), new Point<>(left, top)));
        edges.add(new Edge<Integer>(new Point<>(left, top), new Point<>(right, top)));
        edges.add(new Edge<Integer>(new Point<>(right, top), new Point<>(right, bottom)));
        edges.add(new Edge<Integer>(new Point<>(right, bottom), new Point<>(left, bottom)));






        if (tile.getAtlasReference().equals("barrier14")) {
            textureRegion = atlas.findRegion(tile.getAtlasReference());
            broken = true;
            weakPoint.x = x + TILE_WIDTH / 2;
            weakPoint.y = y + TILE_HEIGHT / 2;
        }

        else if (tile.getAtlasReference().equals("barrier10")) {

            String bar = "barrier1";

            int slopWeight = 8, orangeWeight = 13, bannaWeight = 17, fullWeight = 40;

            // full is 0
            // bannana is 1
            // slop is 2
            // orange is 3
            // broken is 4

            int randomTexture = (int)(Math.random() * 40);

            if (randomTexture < slopWeight) {
                broken = false;
                bar += "2";
            }
            else if (randomTexture < orangeWeight) {
                broken = false;
                bar += "3";
            }
            else if (randomTexture < bannaWeight) {
                broken = false;
                bar += "1";
            }
            else {
                broken = false;
                bar += "0";
            }

            textureRegion = atlas.findRegion(bar);

        } else {
            textureRegion = atlas.findRegion(tile.getAtlasReference()); //tile.getAtlasReference()
            broken = false;
        }

        // id there collidbale then add them to the INDEXER
        if (!isWalls) {
            //System.out.println(TileData.Indexer.getIndex());
            //System.out.println(tile);
            this.INDEX = tile != Tile.NULL ? TileData.Indexer.getIndex() : 0;
        }

        //System.out.println(INDEX);

        this.position = new Point<>((float)x,(float)y);
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

    public Point<Float, Float> getIndex() {
        return position;
    }

    // have to reset index when a new level is made
    public static class Indexer {

        private static int index = 0;

        public static int getIndex() {

            return index++;
        }

        public static void reset() {

            index = 0;

        }

    }

    public void setTextureRegion() {
        textureRegion = new TextureRegion(new Texture(Gdx.files.internal("test_tile.png")));
    }

    public Vector2 getWeakPoint() {
        return weakPoint;
    }

    public void setToInvisible() {
        tile = Tile.INVISIBLE;
    }

    /*
    private TextureRegion textureRegion;
    private TextureAtlas atlas;
    private int bottom;
    private int top;
    private int right;
    private int left;
    public static int TILE_WIDTH;
    public static int TILE_HEIGHT;
    public final boolean broken;
    private Tile tile;
    private Array<Connection<TileData>> connectionArray = new Array<>();
    public int INDEX = 9000;
    private final Pair<Float, Float> position;
    private final boolean isWalls;
    public boolean stable = true;

    private Vector2 weakPoint = new Vector2();
     */
    @Override
    public TileData clone() throws CloneNotSupportedException {
        TileData clone = (TileData)super.clone();
        return  clone;
    }

    public Array<Edge<Integer>> getEdges() {

        return edges;

    }
}
