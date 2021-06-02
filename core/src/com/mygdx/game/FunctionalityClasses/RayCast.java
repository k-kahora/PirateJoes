package com.mygdx.game.FunctionalityClasses;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Enumerators.Tile;
import com.mygdx.game.Tiles.TileData;
import jdk.internal.joptsimple.ValueConversionException;
import sun.jvm.hotspot.runtime.ConstructionException;

import java.util.ArrayList;

public class RayCast {

    private RayCast() {



    }

    public static boolean castRay(Vector2 start, Vector2 direction, Vector2 target, ArrayList<ArrayList<TileData>> map) {

        // the x is the length of teh ray for unit in the x direction and the y is the length of the array one unit in the y direction
        Vector2 scaler = new Vector2((float)Math.sqrt(1 + Math.pow((direction.y / direction.x),2)), (float)Math.sqrt(1 + Math.pow((direction.x / direction.y),2)));

        Vector2 mapCheckPos = new Vector2((int)start.x/16, (int)start.y/16);

        boolean canShoot = false;

        Vector2 vectorRayLength1D = new Vector2();

        Vector2 charecterMap = new Vector2();

        Vector2 vectorStep = new Vector2();

        if (direction.x < 0) {

            vectorStep.x = -1;
            vectorRayLength1D.x = (start.x/16 - mapCheckPos.x) * scaler.x;

        } else {

            vectorStep.x = 1;
            vectorRayLength1D.x = ((mapCheckPos.x + 1) - start.x/16) * scaler.x;

        }

        if (direction.y < 0) {

            vectorStep.y = -1;
            vectorRayLength1D.y = (start.y/16 - mapCheckPos.y) * scaler.y;

        } else {

            vectorStep.y = 1;
            vectorRayLength1D.y = ((mapCheckPos.y + 1) - start.y/16) * scaler.y;

        }

        boolean foundTile = false;
        final float MAX_DISTANCE = direction.len();
        float fDistance = 0f;



        while(!canShoot && fDistance < MAX_DISTANCE) {

            if (vectorRayLength1D.x < vectorRayLength1D.y) {

                mapCheckPos.x += vectorStep.x;
                fDistance = vectorRayLength1D.x * 16;
                vectorRayLength1D.x += scaler.x;

            } else {

                mapCheckPos.y += vectorStep.y;
                fDistance = vectorRayLength1D.y * 16;
                vectorRayLength1D.y += scaler.y;

            }



            if (fDistance > MAX_DISTANCE) {

               // return true;
            }

            if (mapCheckPos.x >= 0 && mapCheckPos.x < map.get(0).size() && mapCheckPos.y >= 0 && mapCheckPos.y < map.size()) {


                if (map.get((int) mapCheckPos.y).get((int) mapCheckPos.x).getTile() != Tile.INVISIBLE) {

                    System.out.println(fDistance);

                    //System.out.println(map.get((int) mapCheckPos.y).get((int) mapCheckPos.x).getTile());

                    return false;

                }


            }





        }

        return true;










        //System.out.println("RayLOcation ( " + (int)mapCheckPos.x + ", " + (int)mapCheckPos.y + ")");
        //System.out.println("Charecter Local ( " + (int)charecterMap.x + ", " + (int)charecterMap.y + ")");

        /*

        Vector2 endPoint = new Vector2();

        if (foundTile) {

            direction.nor();

            endPoint = direction.scl(fDistance * 16).add(start);

            System.out.println(endPoint);

            return endPoint;

        }

        return null;


         */


    }

}
