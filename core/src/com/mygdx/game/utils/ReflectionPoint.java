package com.mygdx.game.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Enumerators.plane;
import com.mygdx.game.FunctionalityClasses.DebugDrawer;
import com.mygdx.game.FunctionalityClasses.RayCast;
import com.mygdx.game.Levels.AbstractLevel;
import com.sun.jdi.connect.Connector;

import java.util.LinkedList;

public class ReflectionPoint {

    private Array<ReflectionPoint> reflectionPoints = new Array<>();

    private final Vector2 start, end, location;

    public Vector2 midpoint;

    private float x,y;

    public Vector2 finalPoint = new Vector2();

    private boolean validShot = false;

    public final Vector2 postion = new Vector2();

    public Vector2 testPoint = new Vector2();

    private Vector2 limitStart, limitEnd;

    private plane xORy = plane.HORZ;

    private Array<Vector2> secondRays = new Array<Vector2>();

    public ReflectionPoint(ReflectionPoint reflectionPoint, Vector2 limitStart, Vector2 limitEnd) {

        this(reflectionPoint.start, reflectionPoint.end, reflectionPoint.location, reflectionPoint.xORy);
        this.limitStart = limitStart;
        this.limitEnd = limitEnd;

    }

    // true for Y false for X
    public ReflectionPoint(Vector2 start, Vector2 end, Vector2 location, plane xORy) {

        this.start = start;
        this.end = end;
        this.midpoint = new Vector2();
        this.location = location;

        this.xORy = xORy;

        midpoint.x = start.x + end.x / 2;
        midpoint.y = start.y + end.y / 2;

        /*
        if (xORy) {

            x = location.x;



            y = location.y - ((location.y - midpoint.y) * 2);

        }

         */

        // for y
        if (xORy == plane.HORZ) {

            Vector2 mid = new Vector2(location.x, start.y);

            postion.y = (2 * mid.y) - location.y;
            postion.x = mid.x;


        } else {

            Vector2 mid = new Vector2(start.x, location.y);

            postion.x = (2 * mid.x) - location.x;
            postion.y = mid.y;

        }

    }


    public Vector2 getStart() {
        return start;

    }

    public void setTestPoint(Vector2 vec) {

        testPoint = vec;






    }

    public boolean isValidShot() {

        return validShot;

    }

    public Vector2 getLimitStart() {

        return limitStart;

    }

    public Vector2 getLimitEnd() {

        return limitEnd;

    }

    public Vector2 getEnd() {
        return end;
    }

    public plane getPlane() {

        return xORy;

    }

    public void addDoublePoints(Array<Vector2> rays) {

        Polygon polygon = new Polygon();

        DebugDrawer.DrawDebugCircle(postion, 10f, 5, Color.PURPLE, AbstractLevel.getViewport().getCamera().combined);



        rays = GraphMaker.sortRays(rays, postion);

        Array<Vector2> reducedRays = GraphMaker.listToArray(GraphMaker.reducePoints(rays));

        LinkedList<LinkedList<Vector2>> verticalLines = GraphMaker.makeLines(reducedRays, false);
        LinkedList<LinkedList<Vector2>> horizontalLines = GraphMaker.makeLines(reducedRays, true);

        for (LinkedList<Vector2> vec : verticalLines) {

            reflectionPoints.add(new ReflectionPoint(vec.getFirst(), vec.getLast(), postion, plane.VERT));

        }

        for (LinkedList<Vector2> vec : horizontalLines) {

            reflectionPoints.add(new ReflectionPoint(vec.getFirst(), vec.getLast(), postion, plane.HORZ));

        }

        for (ReflectionPoint ref : reflectionPoints) {

            DebugDrawer.DrawDebugLine(ref.getStart(), ref.getEnd(), 8, Color.YELLOW, AbstractLevel.getViewport().getCamera().combined);

        }

        System.out.println(reflectionPoints.size + "size");

        reflectionPoints.clear();

        /*

        Array<Float> arrayOfRays = new Array<>();



        for (Vector2 floater : reducedRays) {

            arrayOfRays.add(floater.x);
            arrayOfRays.add(floater.y);

        }

        float[] points = new float[arrayOfRays.size];



        for (int i = 0; i < points.length; i += 1) {

            points[i] = arrayOfRays.get(i);

        }


        polygon.setVertices(points);

        // DebugDrawer.DrawDebugCircle(rays.get(rays.size - 1), 5f, 4, Color.RED, AbstractLevel.getViewport().getCamera().combined);

        DebugDrawer.DrawDebugPolygon(polygon, 5, Color.PURPLE, AbstractLevel.getViewport().getCamera().combined);



         */





        secondRays = rays;

    }

    public Array<Vector2> getSecondRays() {

        return secondRays;

    }

}
