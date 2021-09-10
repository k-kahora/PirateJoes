package com.mygdx.game.FunctionalityClasses;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Enumerators.Tile;
import com.mygdx.game.Enumerators.plane;
import com.mygdx.game.Tiles.TileData;
import jdk.internal.joptsimple.ValueConversionException;
import sun.jvm.hotspot.runtime.ConstructionException;

import java.util.ArrayList;

public class RayCast {

    private Vector2 start, end;
    private float radius;

    private RayCast(Vector2 start, Vector2 end, float radius) {

        this.start = start;
        this.end = end;
        this.radius = radius;

    }

    public static Vector2 castRay(Vector2 start, Vector2 direction, ArrayList<ArrayList<TileData>> map) {

        Vector2 endPoint = new Vector2();

        boolean foundTile = false;

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

        final float MAX_DISTANCE = 100;
        float fDistance = 0f;



        while(!foundTile && fDistance <= MAX_DISTANCE) {

            if (vectorRayLength1D.x < vectorRayLength1D.y) {

                mapCheckPos.x += vectorStep.x;
                fDistance = vectorRayLength1D.x;
                vectorRayLength1D.x += scaler.x;

            } else {

                mapCheckPos.y += vectorStep.y;
                fDistance = vectorRayLength1D.y;
                vectorRayLength1D.y += scaler.y;

            }


            if (mapCheckPos.x >= 0 && mapCheckPos.x < map.get(0).size() && mapCheckPos.y >= 0 && mapCheckPos.y < map.size()) {


                if (map.get((int) mapCheckPos.y).get((int) mapCheckPos.x).getTile() != Tile.INVISIBLE) {


                    foundTile = true;


                }

            }

        }

        if (foundTile) {

            direction.nor();

            endPoint = direction.scl(fDistance * 16).add(start);

            return endPoint;

        }

        return null;

    }


    public static Vector2 castRayNoObsatcle(Vector2 start, Vector2 endPoint, ArrayList<ArrayList<TileData>> map) {

        Vector2 direction = castDirection(start, endPoint);

        start = endPoint;

        Vector2 returnPoint = new Vector2();

        float newMax = new Vector2(start.x - endPoint.x, start.y - endPoint.y).len();

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
        final float MAX_DISTANCE = 100;
        float fDistance = 0f;



        while(!foundTile && fDistance <= MAX_DISTANCE) {

            if (vectorRayLength1D.x < vectorRayLength1D.y) {

                mapCheckPos.x += vectorStep.x;
                fDistance = vectorRayLength1D.x;
                vectorRayLength1D.x += scaler.x;

            } else {

                mapCheckPos.y += vectorStep.y;
                fDistance = vectorRayLength1D.y;
                vectorRayLength1D.y += scaler.y;

            }


            if (mapCheckPos.x >= 0 && mapCheckPos.x < map.get(0).size() && mapCheckPos.y >= 0 && mapCheckPos.y < map.size()) {


                if (map.get((int) mapCheckPos.y).get((int) mapCheckPos.x).getTile() != Tile.INVISIBLE) {


                    foundTile = true;


                }

            }

        }

        if (foundTile) {

            //returnPoint = direction.scl(fDistance * 16).add(start);

            direction.nor();

            returnPoint = direction.scl(fDistance * 16).add(start);

            return returnPoint;
        }

        return null;




    }

    public static Vector2 castRayWithLimit(Vector2 start, Vector2 direction, ArrayList<ArrayList<TileData>> map, float MAX_DISTANCE) {

        Vector2 endPoint = new Vector2();



        boolean foundTile = false;

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


        float fDistance = 0f;



        while(!foundTile && fDistance <= MAX_DISTANCE) {

            if (vectorRayLength1D.x < vectorRayLength1D.y) {

                mapCheckPos.x += vectorStep.x;
                fDistance = vectorRayLength1D.x;
                vectorRayLength1D.x += scaler.x;

            } else {

                mapCheckPos.y += vectorStep.y;
                fDistance = vectorRayLength1D.y;
                vectorRayLength1D.y += scaler.y;

            }


            if (mapCheckPos.x >= 0 && mapCheckPos.x < map.get(0).size() && mapCheckPos.y >= 0 && mapCheckPos.y < map.size()) {


                if (map.get((int) mapCheckPos.y).get((int) mapCheckPos.x).getTile() != Tile.INVISIBLE) {


                    foundTile = true;


                }

            }

        }

        direction.nor();

        endPoint = direction.scl(MAX_DISTANCE).add(start);

        if (foundTile) {

            return endPoint;

            //returnPoint = direction.scl(fDistance * 16).add(start);






        }

        return endPoint;




    }


    // target based raycast
    public static boolean castRay(Vector2 start, Vector2 direction, Vector2 limit, ArrayList<ArrayList<TileData>> map) {

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


        while(!canShoot && fDistance <= MAX_DISTANCE) {

            if (vectorRayLength1D.x < vectorRayLength1D.y) {

                mapCheckPos.x += vectorStep.x;
                fDistance = vectorRayLength1D.x * 16;
                vectorRayLength1D.x += scaler.x;

            } else {

                mapCheckPos.y += vectorStep.y;
                fDistance = vectorRayLength1D.y * 16;
                vectorRayLength1D.y += scaler.y;

            }



            if (fDistance >= MAX_DISTANCE) {

               return true;
            }

            if (mapCheckPos.x >= 0 && mapCheckPos.x < map.get(0).size() && mapCheckPos.y >= 0 && mapCheckPos.y < map.size()) {


                if (map.get((int) mapCheckPos.y).get((int) mapCheckPos.x).getTile() != Tile.INVISIBLE) {


                    //System.out.println(map.get((int) mapCheckPos.y).get((int) mapCheckPos.x).getTile());

                    return false;

                }


            }





        }

        return true;










        //System.out.println("RayLOcation ( " + (int)mapCheckPos.x + ", " + (int)mapCheckPos.y + ")");
        //System.out.println("Charecter Local ( " + (int)charecterMap.x + ", " + (int)charecterMap.y + ")");









    }




    public static Vector2 castDirection(Vector2 start, Vector2 end) {

        return new Vector2(end.x - start.x, end.y - start.y);

    }

    public static Vector2 castDirectionOpposite(Vector2 end, Vector2 start) {

        return new Vector2(end.x - start.x, end.y - start.y);

    }

}
