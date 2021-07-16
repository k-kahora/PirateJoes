package com.mygdx.game.utils;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Enumerators.plane;
import com.mygdx.game.FunctionalityClasses.RayCast;
import com.sun.jdi.connect.Connector;

public class ReflectionPoint {

    private Vector2 start, end, location;

    public Vector2 midpoint;

    private float x,y;

    public Vector2 finalPoint = new Vector2();

    private boolean validShot = false;

    public Vector2 postion = new Vector2();

    public Vector2 testPoint = new Vector2();

    private Vector2 limitStart, limitEnd;

    private plane xORy = plane.HORZ;

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
}
