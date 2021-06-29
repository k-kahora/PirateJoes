package com.mygdx.game.utils;

import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Enumerators.Tile;
import com.mygdx.game.Tiles.TileData;
import com.mygdx.game.Tiles.TilePath;

import java.util.*;

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


        return listOfNodes;

    }

    public static LinePath parsePath(TilePath graphPath) {

        Array<Vector2> wayPoints = new Array<>();

        for (TileData tile : graphPath.getArray()) {

            wayPoints.add(convertIndexToPoint(tile.getIndex()));

        }

        return new LinePath(wayPoints);



    }

    public static Vector2 convertIndexToPoint(Point pair) {


        float x = (float)pair.getIndexi();
        float y = (float)pair.getIndexj();

        return new Vector2(x,y);


    }

    public static void parseMap(ArrayList<ArrayList<TileData>> map) {




        System.out.println(map.get(1).get(3).getTile());

        map.remove(0);
        map.remove(map.size() - 1);




/*

        int count = map.size();

        for (int i = 0; i < count; ++i) {

            // removes column of ?
            map.get(i).remove(0);
            map.get(i).remove(count - 1);


        }

 */







    }

    private static Array<Point<Integer, Integer>> edgeMapToPointMap(Array<Edge<Integer>> map) {

        Array<Point<Integer, Integer>> returnArray = new Array<>();

        for (Edge edge : map) {

            returnArray.add(edge.a);
            returnArray.add(edge.b);

        }

        return returnArray;

    }

    public static Array<Point<Integer, Integer>> edgeMap(ArrayList<ArrayList<TileData>> map) {

        Array<Edge<Integer>> edges = new Array<>(), removedEdges = new Array<>();

        Array<Point> returnEdges = new Array<>();

        for (ArrayList<TileData> list : map) {

            for (TileData data : list) {

                if (data.getTile() != Tile.INVISIBLE && data.getTile() != Tile.NULL)
                    for (Edge pointInTile : data.getEdges()) {
                        edges.add(pointInTile);
                    }

               // System.out.println(edges);
;
            }

        }

        for (Edge edge : edges) {

            for (int i = 0; i < edges.size; ++i) {

                // same referce then skip
                if (edges.get(i) == edge) continue;

                if (edges.get(i).equalsReverse(edge)) {

                    removedEdges.add(edge);

                }

            }

        }

        edges.removeAll(removedEdges, false);

        //System.out.println(edges);

        smoothHorizontal(edges);

        return edgeMapToPointMap(edges);


    }

    public static Edge nextEdge(Point a, Array<Edge<Integer>> map) {

        for (int i = 0; i < map.size; ++i) {

            Edge edge = map.get(i);

            if (edge.a.equals(a)) {

                return edge;

            }

        }

        return null;

    }

    public static Array<Vector2> sortRays(Array<Vector2> array, Vector2 centerpos) {

        ArrayList<Vector2Compare> arrayList = new ArrayList<>();

        float count = 0;

        for (Vector2 vec : array) {

            arrayList.add(new Vector2Compare(vec));

        }

        for (Vector2Compare vec : arrayList) {

            float angle = vec.x - centerpos.x;
            float det = vec.y - centerpos.y;



            // calculates the angle with direction in mind
            float radian = (float)Math.toDegrees(Math.atan2(det, angle));





            vec.setAngleToCompare(radian);


            System.out.println(radian);




        }

        Collections.sort(arrayList);

        //System.out.println(arrayList);

        Array<Vector2> returnArray = new Array<>();

        for (Vector2Compare vec : arrayList) {

            //System.out.println(vec);
            returnArray.add(vec);

        }

        return returnArray;




    }

    public static void smoothHorizontal(Array<Edge<Integer>> map) {

        Array<Edge<Integer>> removeEdges = new Array<>();

        for (Edge edge : map) {

            //if(removeEdges.contains(edge, false)) continue;

            Point edgeStart = edge.a;
            Point edgeEnd = edge.b;



            Edge nextEdge = nextEdge(edgeEnd, map);



            if (nextEdge == null) continue;

            Point nextPoint = nextEdge.b;

            if (edgeStart.getIndexj().equals(edgeEnd.getIndexj()) && nextPoint.getIndexj().equals(edgeEnd.getIndexj())) {



                removeEdges.add(nextEdge);
                edge.changeEdge(nextPoint);

                //  nextEdge.changeEdgeComplete(edge);;

            }


            if (edgeStart.getIndexi().equals(edgeEnd.getIndexi()) && nextPoint.getIndexi().equals(edgeEnd.getIndexi())) {



                removeEdges.add(nextEdge);
                edge.changeEdge(nextPoint);

            }




        }

        map.removeAll(removeEdges, false);

        //map.clear();

    }

    public static <T extends Point> Array<T> removeDupes(Array<T> edges) {

        int dupeCount = 0;

        Array<T> noDupe = new Array<>();

        for (T edge : edges) {

            if (!noDupe.contains(edge,false)) {

                noDupe.add(edge);

            }

        }

        return noDupe;


    }




}
