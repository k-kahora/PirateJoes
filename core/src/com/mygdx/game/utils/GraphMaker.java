package com.mygdx.game.utils;

import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Enumerators.Collisions;
import com.mygdx.game.Enumerators.Tile;
import com.mygdx.game.Tiles.TileConnection;
import com.mygdx.game.Tiles.TileData;
import sun.awt.image.ImageWatched;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public final class GraphMaker {



    private GraphMaker() {
        throw new ClassCastException();
    }



    public static Array<TileConnection> createGraph(ArrayList<ArrayList<TileData>> graphMap) {

        Array<TileConnection> listOfConnections = new Array<>();
        TileData above = null;
        TileData below = null;
        TileData right = null;
        TileData left = null;

        for (int i = 0; i < graphMap.size(); ++i) {


            // this logic checks if its a border tiles and avoids a null pointer exception

            for (int j = 0; j < graphMap.get(0).size(); ++j) {

                TileData tileData = graphMap.get(i).get(j);
                if (i == 0) {
                    below = graphMap.get(i + 1).get(j);
                }
                else if (i == graphMap.size() - 1) {
                    above = graphMap.get(i - 1).get(j);
                } else {
                    below = graphMap.get(i + 1).get(j);
                    above = graphMap.get(i - 1).get(j);
                }
                if (j == 0) {
                    right = graphMap.get(i).get(j + 1);
                }
                else if (j == graphMap.get(0).size() - 1) {
                    left = graphMap.get(i).get(j - 1);
                } else {
                    right = graphMap.get(i).get(j + 1);
                    left = graphMap.get(i).get(j - 1);
                }

                if (above != null && above.getTile() == Tile.INVISIBLE)
                    tileData.createConnections(above, 1);
                if (below != null && below.getTile() == Tile.INVISIBLE)
                    tileData.createConnections(below, 1);
                if (right != null && right.getTile() == Tile.INVISIBLE)
                    tileData.createConnections(right, 1);
                if (left != null && left.getTile() == Tile.INVISIBLE)
                    tileData.createConnections(left, 1);

            }

        }

        System.out.println(listOfConnections);
        return listOfConnections;

    }

}
