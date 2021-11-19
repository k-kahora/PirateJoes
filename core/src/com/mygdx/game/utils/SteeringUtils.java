package com.mygdx.game.utils;

import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Enumerators.Tile;
import com.mygdx.game.FunctionalityClasses.EntityLocation;
import com.mygdx.game.Tiles.TileData;
import com.mygdx.game.Viruses.WanderVirus;

import java.util.ArrayList;
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

    private static boolean parsed = false;

    public static void bounceShot(Vector2 returnVector, EntityLocation target, WanderVirus virus, ArrayList<ArrayList<ArrayList<TileData>>> map) throws CloneNotSupportedException {

        final int WIDTH = 29, HEIGHT = 15;


        // clones it
        ArrayList<ArrayList<TileData>> originalDimension = new ArrayList<ArrayList<TileData>>(17);

        originalDimension.add(new ArrayList<>());

        // makes a copy then parses the map getting rid of all the ?




        if (!parsed) {
            deepCopy(originalDimension, map.get(0));
            parsed = true;
        }

        // valid copy




        //DONE! WE HAVE A COPYIED MAP not to reflect

        // originalDimension.get(0).get(0).setToInvisible(); its a copy nothing changes









    }


    public static void deepCopy(ArrayList<ArrayList<TileData>> list, ArrayList<ArrayList<TileData>> template) throws CloneNotSupportedException {



        for (int i = 0; i < template.size(); ++i) {

            if (i == 0 || i == template.size() - 1)
                continue;

            for (int j = 0; j < template.get(0).size(); ++j) {

                if (j == 0 || j == (template.get(0).size() - 1))
                    continue;

                TileData clone = template.get(i).get(j).clone();
                list.get(i - 1).add(clone);



                //list.add(template.get(i).get(j))
            }
            list.add(new ArrayList<TileData>());


        }

    }



}
