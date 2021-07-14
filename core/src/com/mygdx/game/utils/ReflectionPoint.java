package com.mygdx.game.utils;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.FunctionalityClasses.RayCast;
import com.sun.jdi.connect.Connector;

public class ReflectionPoint {

    private Vector2 start, end, location;

    public Vector2 midpoint;

    private float x,y;

    public Vector2 postion = new Vector2();

    public Vector2 testPoint = new Vector2();

    private boolean xORy;

    public ReflectionPoint(ReflectionPoint reflectionPoint, Vector2 testPoint) {

        this(reflectionPoint.start, reflectionPoint.end, reflectionPoint.location, reflectionPoint.xORy);
        this.testPoint = testPoint;

    }

    // true for Y false for X
    public ReflectionPoint(Vector2 start, Vector2 end, Vector2 location, boolean xORy) {

        this.start = start;
        this.end = end;
        this.midpoint = new Vector2();
        this.location = location;

        midpoint.x = start.x + end.x / 2;
        midpoint.y = start.y + end.y / 2;

        /*
        if (xORy) {

            x = location.x;



            y = location.y - ((location.y - midpoint.y) * 2);

        }

         */

        // for y
        if (xORy) {

            Vector2 mid = new Vector2(location.x, start.y);

            y = (2 * mid.y) - location.y;
            x = mid.x;


        } else {

            Vector2 mid = new Vector2(start.x, location.y);

            x = (2 * mid.x) - location.x;
            y = mid.y;

        }

        postion.x = x;
        postion.y = y;



    }


    public Vector2 getStart() {
        return start;

    }

    public void setTestPoint(Vector2 vec) {

        testPoint = vec;






    }

    public Vector2 getEnd() {
        return end;
    }
}
