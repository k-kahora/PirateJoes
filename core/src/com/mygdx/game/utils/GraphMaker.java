package com.mygdx.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Enumerators.Tile;
import com.mygdx.game.Enumerators.plane;
import com.mygdx.game.FunctionalityClasses.Ray;
import com.mygdx.game.FunctionalityClasses.RayCast;
import com.mygdx.game.Tiles.TileData;
import com.mygdx.game.Tiles.TilePath;
import sun.awt.image.ImageWatched;

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




        }

        Collections.sort(arrayList);

        //System.out.println(arrayList);

        Array<Vector2> returnArray = new Array<>();

        for (Vector2 vec : arrayList) {

            returnArray.add(vec);
            //System.out.println(vec);

        }


        return returnArray;




    }

    public static LinkedList<Vector2> reducePoints(Array<Vector2> listOfLines) {

        LinkedList<Vector2> list = new LinkedList<>();


        for (int i = 0; i < listOfLines.size; ++i) {

            if (list.size() < 2) {

                list.add(listOfLines.get(i));
                continue;

            }

            Vector2 point3, point2, point1;

            if (list.size() > 1 ) {

                point1 = new Vector2(list.get(list.size() - 2));
                point2 = new Vector2(list.get(list.size() - 1));
                point3 = new Vector2(listOfLines.get(i));


                if (Math.abs((point2.x - point1.x) * (point3.y - point2.y) - ((point2.y - point1.y) * (point3.x - point2.x))) < 0.1) {

                    list.remove(list.size() - 1);
                    list.add(point3);
                    continue;

                } else {

                    list.add(point3);

                }

            }

        }

        // checks first and last pointsw
        if ((int)list.get(0).x == (int)list.get(list.size() - 1).x && (int)list.get(0).y == (int)list.get(list.size() - 1).y)
            list.remove(list.get(0));

        return list;

    }

    public static LinkedList<LinkedList<Vector2>> makeLines(Array<Vector2> listOfLines, boolean xORy) {

        // x is true y is false

        LinkedList<LinkedList<Vector2>> returnLines = new LinkedList<>();

        for (int j = 0; j < listOfLines.size; ++j) {

            Vector2 point = listOfLines.get(j);

            LinkedList<Vector2> inList = new LinkedList<>();

            for (int i = j + 1; i < listOfLines.size; ++i) {

                Vector2 point1 = listOfLines.get(i);

                if (xORy) {
                    if (Math.abs(point.y - point1.y) < 0.1) {

                        inList.add(point);
                        inList.add(point1);

                    } else {

                        break;
                    }
                } else {

                    if (Math.abs(point.x - point1.x) < 0.1) {

                        inList.add(point);
                        inList.add(point1);

                    } else {

                        break;
                    }

                }

            }

            if (!inList.isEmpty())
                returnLines.add(inList);

        }

        // connects end and fron

        Vector2 start = listOfLines.get(0);
        Vector2 end = listOfLines.get(listOfLines.size - 1);

        LinkedList<Vector2> lastLine = new LinkedList<>();



        if (!xORy && start.x - end.x < 0.9) {

            lastLine.add(start);
            lastLine.add(end);
            returnLines.add(lastLine);

        }

        if (xORy && start.y - end.y < 0.9) {

            lastLine.add(start);
            lastLine.add(end);
            returnLines.add(lastLine);

        }





        return returnLines;

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

    // make sure testPoint is set
    public static LinkedList<ReflectionPoint> betweenrays(Array<ReflectionPoint> reflectPoints, Vector2 target, ArrayList<ArrayList<TileData>> map) {

        LinkedList<ReflectionPoint> goodPoints = new LinkedList<>();

        for (ReflectionPoint point : reflectPoints) {

            // check y positions


                /*
                if (point.getLimitEnd().y < target.y && point.getLimitStart().y > target.y) {

                    point.finalPoint = RayCast.castRay(point.postion, RayCast.castDirection(point.postion, target), map);
                    goodPoints.add(point);


                }

                 */



                /*
                if (point.getLimitEnd().x < target.x && point.getLimitStart().x > target.x) {

                    point.finalPoint = RayCast.castRay(point.postion, RayCast.castDirection(point.postion, target), map);
                    goodPoints.add(point);

                }

                 */


                float slope, y1, y2, x1, x2, xIntersection, yIntersection, intersectionEquation;

                switch (point.getPlane()) {

                    case HORZ:
                        //point.finalPoint = RayCast.castRayWithLimit(point.postion, target, point.getStart().y, plane.HORZ);

                        // this code fins the intercept of the given reflection point.
                        // using the position of the target and the position of the reflection point a line in
                        // point slope form is given

                        y1 = point.postion.y; y2 = target.y; x1 = point.postion.x; x2 = target.x; intersectionEquation = point.getStart().y;

                        slope = (y2 - y1) / (x2 - x1);



                        xIntersection = ((intersectionEquation - y2) / slope) + x2;


                        point.finalPoint = new Vector2(xIntersection, intersectionEquation);

                        // this makes sure the point is in bounds between teh reflectable surface
                        if (point.finalPoint.x < point.getStart().x && point.finalPoint.x > point.getEnd().x
                                || point.finalPoint.x > point.getStart().x && point.finalPoint.x < point.getEnd().x) {

                            //if (!RayCast.castRay(point.finalPoint, RayCast.castDirection(point.finalPoint, target), target, map))
                            //    goodPoints.add(point);

                        }


                        break;


                    case VERT:

                        y1 = point.postion.y; y2 = target.y; x1 = point.postion.x; x2 = target.x; intersectionEquation = point.getStart().x;

                        slope = (y2 - y1) / (x2 - x1);



                        yIntersection = ((intersectionEquation - x2) * slope) + y2;


                        point.finalPoint = new Vector2(intersectionEquation, yIntersection);

                        if (point.finalPoint.y < point.getStart().y && point.finalPoint.y > point.getEnd().y
                        || point.finalPoint.y > point.getStart().y && point.finalPoint.y < point.getEnd().y) {

                            goodPoints.add(point);

                        }



                        break;


                    default:
                        System.out.println("EROOR");

                }


        }

        return goodPoints;


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

    public static <T> Array<T> listToArray(LinkedList<T> list) {

        Array<T> returnArray = new Array<>();

        for (T data : list) {

            returnArray.add(data);

        }

        return returnArray;

    }




}
