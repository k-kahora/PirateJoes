package com.mygdx.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Enumerators.Tile;
import com.mygdx.game.Enumerators.plane;
import com.mygdx.game.FunctionalityClasses.DebugDrawer;
import com.mygdx.game.FunctionalityClasses.Ray;
import com.mygdx.game.FunctionalityClasses.RayCast;
import com.mygdx.game.Levels.AbstractLevel;
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

                    if (Math.abs(point.y - point1.y) < 0.9) {

                        inList.add(point);
                        inList.add(point1);

                    } else {

                        break;

                    }

                } else {

                    if (Math.abs(point.x - point1.x) < 0.9) {

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

        if (!xORy && Math.abs(start.x - end.x) < 0.9) {

            lastLine.add(start);
            lastLine.add(end);
            returnLines.add(lastLine);

        }

        if (xORy && Math.abs(start.y - end.y) < 0.9) {

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

                float slope, y1, y2, x1, x2, xIntersection, yIntersection, intersectionEquation, max, min;

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
                        if (point.getStart().x > point.getEnd().x) {

                            min = point.getEnd().x;
                            max = point.getStart().x;

                        } else {

                            max = point.getEnd().x;
                            min = point.getStart().x;

                        }

                        if (point.finalPoint.x > min && point.finalPoint.x < max) {

                            if (RayCast.castRay(point.finalPoint, RayCast.castDirection(point.finalPoint, target), target, map))
                                goodPoints.add(point);


                        }



                        break;


                    case VERT:

                        y1 = point.postion.y; y2 = target.y; x1 = point.postion.x; x2 = target.x; intersectionEquation = point.getStart().x;

                        slope = (y2 - y1) / (x2 - x1);



                        yIntersection = ((intersectionEquation - x2) * slope) + y2;


                        point.finalPoint = new Vector2(intersectionEquation, yIntersection);

                        if (point.getStart().y > point.getEnd().y) {

                            min = point.getEnd().y;
                            max = point.getStart().y;

                        } else {

                            max = point.getEnd().y;
                            min = point.getStart().y;

                        }

                        if (point.finalPoint.y > min && point.finalPoint.y < max) {

                            if (RayCast.castRay(point.finalPoint, RayCast.castDirection(point.finalPoint, target), target, map))
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

    public static void returnEdgesInReflection(ReflectionPoint reflectionPoint, Array<Point<Integer, Integer>> edgeMap, Array<Vector2> returnEdges, ArrayList<ArrayList<TileData>> map) {

        returnEdges.clear();

        Vector2 endVec = new Vector2(reflectionPoint.getEnd());

        Vector2 startVec = new Vector2(reflectionPoint.getStart());

        endVec.sub(reflectionPoint.postion);
        startVec.sub(reflectionPoint.postion);

        double angle1 = Math.atan2(endVec.x, endVec.y), angle2 = Math.atan2(startVec.x, startVec.y), max, min;

        if (angle1 > angle2) {

            max = angle1;
            min = angle2;

        } else {

            max = angle2;
            min = angle1;

        }

        System.out.println("Max: " + max + " " + "Min: " + min);

        // the four points

        edgeMap.add(new Point<Integer, Integer>(16, 256));
        edgeMap.add(new Point<Integer, Integer>(16, 16));
        edgeMap.add(new Point<Integer, Integer>(480, 256));
        edgeMap.add(new Point<Integer, Integer>(480, 16));

        for (Point point : edgeMap) {

            Vector2 ppoint = new Vector2((int)point.getIndexi(), (int)point.getIndexj());
            Vector2 voint = new Vector2((int)point.getIndexi(), (int)point.getIndexj()).sub(reflectionPoint.postion);

            double ang = Math.atan2(voint.x, voint.y);

            double minimumLevel = 0, maxLevel = 0;

            if (reflectionPoint.getPlane() == plane.HORZ) {

                if (reflectionPoint.getStart().y - reflectionPoint.postion.y > 0) {

                    if (ppoint.y < reflectionPoint.getStart().y) {

                        continue;

                    }

                } else {

                    if (ppoint.y > reflectionPoint.getStart().y) {

                        continue;

                    }

                }

            }

            if (reflectionPoint.getPlane() == plane.VERT) {

                if (reflectionPoint.getStart().x - reflectionPoint.postion.x > 0) {

                    if (ppoint.x < reflectionPoint.getStart().x) {

                        continue;

                    }


                } else {

                    if (ppoint.x > reflectionPoint.getStart().x) {

                        continue;

                    }

                }

            }

            if (ang > min && ang < max ) {

                returnEdges.add(voint.add(reflectionPoint.postion));

            }

        }

        // good up to here

        Array<Vector2> castPoints = new Array<Vector2>();

        castPoints.add(RayCast.castRay(reflectionPoint.getStart(), RayCast.castDirection(reflectionPoint.postion, reflectionPoint.getStart()), map));
        castPoints.add(RayCast.castRay(reflectionPoint.getEnd(), RayCast.castDirection(reflectionPoint.postion, reflectionPoint.getEnd()), map));

        //DebugDrawer.DrawDebugLine(reflectionPoint.getStart(), castPoints.get(0), 8, Color.OLIVE, AbstractLevel.getViewport().getCamera().combined);
        //DebugDrawer.DrawDebugLine(reflectionPoint.getEnd(), castPoints.get(1), 8, Color.OLIVE, AbstractLevel.getViewport().getCamera().combined);

        if (!returnEdges.isEmpty()) {

            for (Vector2 vec : returnEdges) {

                Vector2 startPoint = calcStartPoint(reflectionPoint, vec);

                if (RayCast.castRay(startPoint, RayCast.castDirection(startPoint, vec), map) == null) {

                    continue;

                }

                castPoints.add(RayCast.castRay(startPoint, RayCast.castDirection(startPoint, vec), map));
                castPoints.add(RayCast.castRay(startPoint, RayCast.castDirection(startPoint, vec).rotateRad(-0.001f), map));
                castPoints.add(RayCast.castRay(startPoint, RayCast.castDirection(startPoint, vec).rotateRad(0.001f), map));

                //DebugDrawer.DrawDebugCircle(vec, 10f, 5, Color.PURPLE, AbstractLevel.getViewport().getCamera().combined);

            }

        }



        System.out.println(castPoints.size + "cast points");
        reflectionPoint.addDoublePoints(castPoints);

    }

    private static Vector2 calcStartPoint(ReflectionPoint point, Vector2 target) {

        LinkedList<ReflectionPoint> goodPoints = new LinkedList<>();

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
            Vector2 returnPoint = new Vector2();

            switch (point.getPlane()) {

                case HORZ:
                    //point.finalPoint = RayCast.castRayWithLimit(point.postion, target, point.getStart().y, plane.HORZ);

                    // this code fins the intercept of the given reflection point.
                    // using the position of the target and the position of the reflection point a line in
                    // point slope form is given

                    y1 = point.postion.y; y2 = target.y; x1 = point.postion.x; x2 = target.x; intersectionEquation = point.getStart().y;

                    slope = (y2 - y1) / (x2 - x1);



                    xIntersection = ((intersectionEquation - y2) / slope) + x2;


                    returnPoint = new Vector2(xIntersection, intersectionEquation);

                    // this makes sure the point is in bounds between teh reflectable surface

                    return returnPoint;


                case VERT:

                    y1 = point.postion.y; y2 = target.y; x1 = point.postion.x; x2 = target.x; intersectionEquation = point.getStart().x;

                    slope = (y2 - y1) / (x2 - x1);



                    yIntersection = ((intersectionEquation - x2) * slope) + y2;


                    returnPoint = new Vector2(intersectionEquation, yIntersection);


                    return returnPoint;



                default:
                    return null;

            }


    }

    public static <T> Array<T> listToArray(LinkedList<T> list) {

        Array<T> returnArray = new Array<>();

        for (T data : list) {

            returnArray.add(data);

        }

        return returnArray;

    }




}
