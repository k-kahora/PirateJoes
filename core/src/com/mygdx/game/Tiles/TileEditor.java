package com.mygdx.game.Tiles;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.ai.steer.behaviors.FollowFlowField;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Enumerators.Tile;
import com.mygdx.game.Levels.AbstractLevel;
import com.mygdx.game.Levels.Level;
import com.mygdx.game.utils.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class TileEditor  {

    private String commentSymbol;
    private Scanner fileReader;
    private File file;
    private ArrayList<ArrayList<com.mygdx.game.Enumerators.Tile>> tileMap;
    private ArrayList<ArrayList<com.mygdx.game.Tiles.TileData>> tileMapData;
    private List<TileData> weakPoints = new LinkedList<>();
    private int startingPoint;
    private TextureAtlas atlas;
    private Level level;
    private boolean isWalls;

    public void addLevel(Level level) {

        this.level = level;
    }

    public TileEditor(String fileName, TextureAtlas atlas, boolean isWalls) {

        //System.out.println(atlas);

        try {
            file = new File(AbstractLevel.tileDir + fileName);
            fileReader = new Scanner(file);

            if (atlas == null)
                //System.out.println("NULL NULL !!");
                throw new Exception("Null Atlas");
        } catch (Exception exception) {
            System.out.println("!BRUH");
        }


        startingPoint = 0;
        commentSymbol = "/";

        this.atlas = atlas;
        tileMap = new ArrayList<ArrayList<com.mygdx.game.Enumerators.Tile>>();
        this.isWalls = isWalls;
        loadTiles();


    }



    public TileEditor(String fileName, TextureAtlas atlas) {

       this(fileName, atlas, false);

    }


    private int countLines() throws FileNotFoundException {

        int lines = 0;


        while (fileReader.hasNextLine()) {

            if (fileReader.next().equals("/")) {
                fileReader.nextLine();
                startingPoint++;
            } else {

                fileReader.nextLine();
                lines++;
            }
        }

        fileReader.close();
        fileReader = new Scanner(file);

        return lines;
    }

    private ArrayList<ArrayList<com.mygdx.game.Enumerators.Tile>> loadTiles() {

        com.mygdx.game.Enumerators.Tile tile = com.mygdx.game.Enumerators.Tile.AIR;



        String delim = "";
        int arrayNum = 0;

        try {

            arrayNum = countLines();

        } catch (FileNotFoundException e) {

            e.printStackTrace();

        }

        // initalizes the array withe emoty arrays
        for (int i = 0; i < arrayNum; ++i) {

            tileMap.add(new ArrayList<com.mygdx.game.Enumerators.Tile>());

        }


        // this statement skips the comments
        for (int i = 0; i < startingPoint; ++i) {
            fileReader.nextLine();
        }

        for (int i = 0; i < arrayNum; ++i) {


            do {

                delim = fileReader.next();


                // another test no glitched tiles
                if (delim.equals("|")) {
                    break;
                }

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
                        System.out.println("Tile Error");
                }

                tileMap.get(i).add(tile);

            } while (!delim.equals("|"));

        }

        //System.out.println(tileMap);
        loadTileData();

        return tileMap;

    }

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

//                System.out.println(tileDataMap.get(i).get(j).INDEX);

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
