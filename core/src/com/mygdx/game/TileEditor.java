package com.mygdx.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import jdk.internal.module.SystemModuleFinders;
import jdk.internal.net.http.RequestPublishers;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.*;

public class TileEditor {


  
    
    private String commentSymbol;
    private Scanner fileReader;
    private File file;
    private ArrayList<ArrayList<Tile>> tileMap;
    private ArrayList<ArrayList<TileData>> tileMapData;
    private int startingPoint;
    private TextureAtlas atlas;


    public TileEditor(String fileName, TextureAtlas atlas) {


        try {
            file = new File(fileName);
            fileReader = new Scanner(file);
        } catch (Exception exception) {
            System.out.println("BRUH");
        }


        startingPoint = 0;
        commentSymbol = "/";
        this.atlas = atlas;
        tileMap = new ArrayList<ArrayList<Tile>>();

        loadTiles();


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

    private ArrayList<ArrayList<Tile>> loadTiles() {

        Tile tile = Tile.AIR;



        String delim = "";
        int arrayNum = 0;

        try {

            arrayNum = countLines();

        } catch (FileNotFoundException e) {

            e.printStackTrace();

        }

        // initalizes the array withe emoty arrays
        for (int i = 0; i < arrayNum; ++i) {

            tileMap.add(new ArrayList<Tile>());

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
                        tile = Tile.GRASS;
                        break;
                    case ("*"):
                        tile = Tile.AIR;
                        break;
                    case ("@"):
                        tile = Tile.STONE;
                        break;
                    case ("B"):
                        tile = Tile.DOOR;
                        break;
                    case ("0"):
                        tile = Tile.INVISIBLE;
                        break;
                    case("&"):
                        tile = Tile.BASKET_FULL;
                        break;
                    default:
                        tile = Tile.STONE;
                        System.out.println("ERROR");
                }

                tileMap.get(i).add(tile);


            } while (!delim.equals("|"));


        }


;
        //System.out.println(tileMap);
        loadTileData();

        return tileMap;


    }

    // iterates through arrayList of tileData with enum vales and sets apprpiate tile data to a new arraay
    // using the tiledata class

//    this also assigns tile data with the appropiate positon based on the static vars in the class
    private ArrayList<ArrayList<TileData>> loadTileData() {

        int xPosition = 0;
        int yPosition = 0;

        ArrayList<ArrayList<TileData>> tileDataMap = new ArrayList<ArrayList<TileData>>();

        // adds the arrays
        for (int i = 0; i < tileMap.size(); ++i) {
            tileDataMap.add(new ArrayList<TileData>());
        }

        Collections.reverse(tileMap);

        for (int i = 0; i < tileMap.size(); ++i) {

            yPosition += i == 0 ? 0 : TileData.TILE_HEIGHT;
            xPosition = 0;


            for (int j = 0; j < tileMap.get(i).size(); ++j) {
                tileDataMap.get(i).add(new TileData(tileMap.get(i).get(j), atlas, xPosition, yPosition));
                xPosition += TileData.TILE_WIDTH;
            }
        }




        this.tileMapData = tileDataMap;
        return tileDataMap;

    }


    public void draw (SpriteBatch batch) {

        int countx = 0;
        int county = 0;
        int yEnumerate = 0;

        ArrayList<ArrayList<TileData>> tilesToRender = tileMapData;



        for (ArrayList<TileData> array1 : tilesToRender) {
            county += yEnumerate;
            countx = 0;

            for (TileData data : array1) {
                batch.draw(data.getTextureRegion(),countx,county);
                countx += data.getTextureRegion().getRegionWidth();
                yEnumerate = data.getTextureRegion().getRegionHeight();

            }


        }



    }


    public ArrayList<ArrayList<TileData>> getTileMap() {
        return tileMapData;
    }


}
