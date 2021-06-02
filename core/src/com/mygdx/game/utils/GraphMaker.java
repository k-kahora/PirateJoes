package com.mygdx.game.utils;

import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Enumerators.Collisions;
import com.mygdx.game.Enumerators.Tile;
import com.mygdx.game.Tiles.TileConnection;
import com.mygdx.game.Tiles.TileData;
import com.mygdx.game.Tiles.TilePath;
import org.graalvm.compiler.hotspot.phases.LoadJavaMirrorWithKlassPhase;
import sun.awt.image.ImageWatched;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public final class GraphMaker {



    private GraphMaker() {
        throw new ClassCastException();
    }



    public static Array<TileData> createGraph(ArrayList<ArrayList<TileData>> graphMap) {



        Array<TileData> listOfNodes = new Array<>();
        TileData above = null;
        TileData below = null;
        TileData right = null;
        TileData left = null;

        TileData upperRight = null;
        TileData upperLeft = null;
        TileData lowerRight = null;
        TileData lowerLeft = null;


        for (int i = 1; i < graphMap.size() - 1; ++i) {




            // this logic checks if its a border tiles and avoids a null pointer exception

            for (int j = 1; j < graphMap.get(i).size() - 1; ++j) {

                    TileData tileData = graphMap.get(i).get(j);

                    below = graphMap.get(i - 1).get(j);
                    above = graphMap.get(i + 1).get(j);

                    right = graphMap.get(i).get(j + 1);
                    left = graphMap.get(i).get(j - 1);


                    upperLeft = graphMap.get(i - 1).get(j - 1);
                    upperRight = graphMap.get(i - 1).get(j + 1);

                    lowerLeft = graphMap.get(i + 1).get(j - 1);
                    lowerRight = graphMap.get(i + 1).get(j + 1);



                if (tileData.getTile().equals(Tile.INVISIBLE)) {


                    if (above != null && above.getTile() == Tile.INVISIBLE )
                        tileData.createConnections(above, 1);
                    if (below != null && below.getTile() == Tile.INVISIBLE )
                        tileData.createConnections(below, 1);
                    if (right != null && right.getTile() == Tile.INVISIBLE )
                        tileData.createConnections(right, 1);
                    if (left != null && left.getTile() == Tile.INVISIBLE )
                        tileData.createConnections(left, 1);


                    if (upperLeft != null && upperLeft.getTile() == Tile.INVISIBLE )
                        tileData.createConnections(upperLeft, 1);
                    if (upperRight != null && upperRight.getTile() == Tile.INVISIBLE )
                        tileData.createConnections(upperRight, 1);
                    if (lowerLeft != null && lowerLeft.getTile() == Tile.INVISIBLE )
                        tileData.createConnections(lowerLeft, 1);
                    if (lowerRight != null && lowerRight.getTile() == Tile.INVISIBLE )
                        tileData.createConnections(lowerRight, 1);


                }

                if (tileData.getTile() != Tile.NULL) {
                    listOfNodes.add(tileData);
                }



            }

        }

        System.out.println(listOfNodes.size);
        return listOfNodes;

    }

    public static LinePath parsePath(TilePath graphPath) {

        Array<Vector2> wayPoints = new Array<>();

        for (TileData tile : graphPath.getArray()) {

            wayPoints.add(convertIndexToPoint(tile.getIndex()));

        }

        return new LinePath(wayPoints);



    }

    public static Vector2 convertIndexToPoint(Pair pair) {


        float x = (float)pair.getIndexi();
        float y = (float)pair.getIndexj();

        return new Vector2(x,y);


    }

    public static void parseMap(ArrayList<ArrayList<TileData>> map) {




        System.out.println(map.get(1).get(3).getTile());

        map.remove(0);
        map.remove(map.size() - 1);

        System.out.println(map.get(1).get(3).getTile());

        System.out.println("BRUH");

/*

        int count = map.size();

        for (int i = 0; i < count; ++i) {

            // removes column of ?
            map.get(i).remove(0);
            map.get(i).remove(count - 1);


        }

 */







    }


}
