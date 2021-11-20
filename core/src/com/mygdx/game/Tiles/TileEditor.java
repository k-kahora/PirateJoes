package com.mygdx.game.Tiles;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.ai.steer.behaviors.FollowFlowField;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Enumerators.Tile;
import com.mygdx.game.Levels.AbstractLevel;
import com.mygdx.game.Levels.Level;
import com.mygdx.game.utils.GraphMaker;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.*;

public class TileEditor  {

    private static int lie = 0;

    private FileHandle fileHandle;

    private String commentSymbol;
    private Reader reader;
    //private FileReader fileReader;
    private File file;
    private ArrayList<ArrayList<Tile>> tileMap;
    private ArrayList<ArrayList<com.mygdx.game.Tiles.TileData>> tileMapData;
    private List<TileData> weakPoints = new LinkedList<>();
    private int startingPoint;
    private TextureAtlas atlas;
    private Level level;
    private boolean isWalls;

    private Array<TileData> collidableTiles = new Array<>();



    public void addLevel(Level level) {

        this.level = level;
    }

    public TileEditor(String fileName, TextureAtlas atlas, boolean isWalls) {

        //System.out.println(atlas);

        try {
            file = new File(AbstractLevel.tileDir + fileName);
            //fileReader = new FileReader(file);

            if (atlas == null)
                
                throw new Exception("Null Atlas");
        } catch (Exception exception) {
            
        }


        startingPoint = 0;
        commentSymbol = "/";

        this.atlas = atlas;
        tileMap = new ArrayList<ArrayList<com.mygdx.game.Enumerators.Tile>>();
        this.isWalls = isWalls;

        this.fileHandle = Gdx.files.internal(AbstractLevel.tileDir + fileName);  // new FileHandle(file, Files.FileType.Internal);
        this.reader = fileHandle.reader();

        loadTiles();


    }

    int rows = 0;


    public TileEditor(String fileName, TextureAtlas atlas) {

       this(fileName, atlas, false);

    }


    private ArrayList<ArrayList<com.mygdx.game.Enumerators.Tile>> loadTiles() {


        com.mygdx.game.Enumerators.Tile tile = com.mygdx.game.Enumerators.Tile.AIR;



        String delim = "";
        int arrayNum = 17;

        // initalizes the array withe emoty arrays
        for (int i = 0; i < arrayNum; ++i) {

            tileMap.add(new ArrayList<com.mygdx.game.Enumerators.Tile>());

        }


        char charMan = 'f';

        for (int i = 0; i < arrayNum; ++i) {


            lie++;

            rows = 0;

            System.out.println();


            do {


                try {

                  delim = Character.toString((char)reader.read());


                    if (delim.equals("|")) {

                        break;

                    }

                   System.out.print(delim);


                }
                catch (Exception e) {

                }
                rows++;
                // another test no glitched tiles


                switch (delim) {

                    case ("#"):
                        tile = com.mygdx.game.Enumerators.Tile.GRASS;
                        break;
                    case ("*"):
                        tile = Tile.AIR;
                        break;
                    case ("@"):
                        tile = com.mygdx.game.Enumerators.Tile.STONE;
                        break;
                    case  ("+"):
                        tile = Tile.BROKEN;
                        break;

                    case ("0"):
                        tile = com.mygdx.game.Enumerators.Tile.INVISIBLE;
                        break;
                    case("&"):
                        tile = com.mygdx.game.Enumerators.Tile.BASKET_FULL;
                        break;
                    case("%"):
                        tile = com.mygdx.game.Enumerators.Tile.BASKET_FULL;
                        break;
                    case("?"):
                        tile = Tile.NULL;
                        break;
                    case("1"):
                        tile = com.mygdx.game.Enumerators.Tile.LEFT_WALL;
                        break;
                    case("2"):
                        tile = com.mygdx.game.Enumerators.Tile.RIGHT_WALL;
                        break;
                    case("3"):
                        tile = com.mygdx.game.Enumerators.Tile.BOTTOM_WALL;
                        break;
                    case("4"):
                        tile = com.mygdx.game.Enumerators.Tile.TOP_WALL;
                        break;
                    case("5"):
                        tile = com.mygdx.game.Enumerators.Tile.TOP_RIGHT_WALL;
                        break;
                    case("6"):
                        tile = com.mygdx.game.Enumerators.Tile.TOP_LEFT_WALL;
                        break;
                    case("7"):
                        tile = com.mygdx.game.Enumerators.Tile.BOTTOM_LEFT_WALL;
                        break;
                    case("8"):
                        tile = com.mygdx.game.Enumerators.Tile.BOTTOM_RIGHT_WALL;
                        break;

                    default:
                        tile = Tile.NULL;
                        flag = false;
                        break;
                }

                if (flag) {
                    tileMap.get(i).add(tile);

                } else {

                    flag = true;

                }

            } while (!delim.equals("|"));

        }
        try {reader.close();}
        catch (Exception e) {}

        //System.out.println(tileMap);
        loadTileData();








        return tileMap;

    }

    boolean flag = true;

    // iterates through arrayList of tileData with enum vales and sets apprpiate tile data to a new arraay
    // using the tiledata class

//    this also assigns tile data with the appropiate positon based on the static vars in the class
    private ArrayList<ArrayList<com.mygdx.game.Tiles.TileData>> loadTileData() {

        int xPosition = 0;
        int yPosition = 0;
        int index = 0;

        ArrayList<ArrayList<com.mygdx.game.Tiles.TileData>> tileDataMap = new ArrayList<ArrayList<com.mygdx.game.Tiles.TileData>>();

        // adds the arrays
        for (int i = 0; i < tileMap.size(); ++i) {
            tileDataMap.add(new ArrayList<com.mygdx.game.Tiles.TileData>());
        }

        Collections.reverse(tileMap);

        for (int i = 0; i < tileMap.size(); ++i) {

            yPosition += i == 0 ? 0 : com.mygdx.game.Tiles.TileData.TILE_HEIGHT;
            xPosition = 0;

            for (int j = 0; j < tileMap.get(i).size(); ++j) {

                tileDataMap.get(i).add(new TileData(tileMap.get(i).get(j), atlas, xPosition, yPosition, isWalls));

                if (tileDataMap.get(i).get(j).broken) {
                    weakPoints.add(tileDataMap.get(i).get(j));
                }

                xPosition += TileData.TILE_WIDTH;



            }
        }

        // delete



        this.tileMapData = tileDataMap;
        return tileDataMap;

    }

    public void draw (SpriteBatch batch) {

        int countx = 0;
        int county = 0;
        int yEnumerate = 0;

        ArrayList<ArrayList<com.mygdx.game.Tiles.TileData>> tilesToRender = tileMapData;

        for (ArrayList<com.mygdx.game.Tiles.TileData> array1 : tilesToRender) {

            county += yEnumerate;
            countx = 0;

            for (com.mygdx.game.Tiles.TileData data : array1) {

               // System.out.println(data.getTextureRegion() + " the texture");

                if (data.stable) {

                    batch.draw(data.getTextureRegion(), countx, county);


                } else {
                    data.setToInvisible();
                }

                countx += data.getTextureRegion().getRegionWidth();
                yEnumerate = data.getTextureRegion().getRegionHeight();

            }

        }

    }


    public ArrayList<ArrayList<TileData>> getTileMap() {
        return tileMapData;
    }

    public List<TileData> getWeakPoints() {
        return weakPoints;
    }


}
