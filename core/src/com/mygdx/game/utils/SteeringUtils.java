package com.mygdx.game.utils;

import com.badlogic.gdx.math.Vector2;
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

    public static boolean middleAnimationForExplosion(Object[] objects, int index) {

        int third = objects.length / 5;
        if (index > third * 1 && index < third * 2) {

            System.out.println("Frame of death: " + index);

            return true;
        }
        return false;

    }



}
