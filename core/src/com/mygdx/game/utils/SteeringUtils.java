package com.mygdx.game.utils;

import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

import static java.lang.Math.*;

public final class SteeringUtils {

    public static float vectorToAngle(Vector2 vector) {
        return (float)atan2(-vector.x, vector.y);
    }

    public static Vector2 angleToVector(Vector2 returnVector, float angle) {

        returnVector.x = -(float)sin(angle);
        returnVector.y = (float)cos(angle);

        return returnVector;
    }

    public static Vector2 angleToVector(float angle) {

        Vector2 returnVector = new Vector2();

        returnVector.x = -(float)sin(angle);
        returnVector.y = (float)cos(angle);

        return returnVector;
    }

    public static float calcAngleBetweenVectors(Vector2 a, Vector2 b) {

        double x1 = a.x * b.x;
        double y1 = a.y * b.y;

        double xy = x1 + y1;

        return (float)acos(xy / (a.len() * b.len()));

    }

    public static boolean middleAnimationForExplosion(Object[] objects, int index) {

        int third = objects.length / 5;
        if (index > third * 1 && index < third * 2) {

            System.out.println("Frame of death: " + index);

            return true;
        }
        return false;

    }

    public static boolean inRadius(Vector2 center, Vector2 point, float radius) {

        float x = center.x - point.x;
        float y = center.y - point.y;

        y = (float)Math.sqrt(Math.pow(x,2) + Math.pow(y,2));

        if (y <= radius) {
            return true;
        }

        return false;

    }

    public static boolean randomPercent(int percent) {

        double chance = Math.random() * 100;

        if (percent < chance)
            return true;
        return false;

    }

    public static float rangeOfTimes(float a, float b) {

        float time = (float)((Math.random() * (b - a)) + a);



        return time;

    }



}
