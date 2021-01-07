package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class TileCollision {

    /*
    This class takes an enitity type and calls the entitlys class appropate collider methods based on what
    collision happened a collision happens when the Entity can intersect with any tile without a NONE ollision type
    This also uses a static builder

    A entity can only have one collision but the collision can utilize multiple maps
     */

    private int x;
    private int y;
    private int x2;
    private int y2;

    private enum direction {

        UP,
        DOWN,
        RIGHT,
        LEFT;

    }

    private int count = 0;
    private final Rectangle rectangle;


    private final Entity character;
    private final ArrayList<ArrayList<ArrayList<TileData>>> tileMap;


    public static class Builder<T extends Entity> {


        private T character;
        private ArrayList<ArrayList<ArrayList<TileData>>> tileMap = new ArrayList<ArrayList<ArrayList<TileData>>>();
        private Rectangle rectangle;


        public Builder tileMap(ArrayList<ArrayList<TileData>> a) {


            this.tileMap.add(a);

            return this;
        }

        public Builder tileMap(ArrayList<ArrayList<TileData>> a, ArrayList<ArrayList<TileData>> b) {


            this.tileMap.add(a);
            this.tileMap.add(b);
            return this;
        }


        public Builder charecter(T a) {

            this.character = a;
            return this;
        }



        public Builder calcCorners(Rectangle a) {

            this.rectangle = a;
            return this;

        }

        public TileCollision build() {
            return new TileCollision(this);
        }




    }

    private TileCollision(Builder builder) {

        this.tileMap = builder.tileMap;
        this.rectangle = builder.rectangle;
        this.character = builder.character;


    }





    /*
    tileVertical checks for for movement up and right if up check the top right tiles where the player is going to go using the charecters velocity
    before the position is changed.  If the future position detetcs a collision its blocked and position is changed.  Rectangle is the charecters
    bounding box.  Returns false when there a collision prohibing the sprite from moving which is why we can use rectangle to checkf or position
    Copy and paste using the tileHorizontal

    The routing function is used to detect what type of collision data the tile has and whether it will let the sprite through (NOT YET IMPLEMENTED)
     */


    public boolean tileVertical(Vector2 velocity) {

        float futureY = 0;
        futureY = rectangle.getY() + velocity.y;


        // BUG WHERE x2 was always looking at a future tile for some reason
        int x = (int) rectangle.getX() / TileData.TILE_WIDTH;
        int x2 = (int) (rectangle.getWidth() + rectangle.getX() - 0.01) / TileData.TILE_WIDTH;
        int y2 = (int) (rectangle.getHeight() + futureY) / TileData.TILE_HEIGHT;
        int y = (int) (futureY) / TileData.TILE_HEIGHT;


        for (int i = 0; i < tileMap.size(); ++i) {


        // up
        if (rectangle.getY() - futureY < 0) {




            // topLEft
            if (!tileMap.get(i).get(y2).get(x).getTile().isCollideable().contains(Collisions.NONE)) {

                System.out.println("COLLISION ");


                character.bottomCollision(x, y2);
                return false;
            }

            // topRight
            if (!tileMap.get(i).get(y2).get(x2).getTile().isCollideable().contains(Collisions.NONE)) {

                character.bottomCollision(x2, y2);


                return false;
            }

        }

//        down
        else if (rectangle.getY() - futureY > 0) {


            // topLEft
            if (!tileMap.get(i).get(y).get(x).getTile().isCollideable().contains(Collisions.NONE)) {

                character.topCollision(x, y);

                return false;

            }

            // topRight
            if (!tileMap.get(i).get(y).get(x2).getTile().isCollideable().contains(Collisions.NONE)) {

                character.topCollision(x2, y);
                return false;
            }

        }
        }

        return true;


    }

    public boolean tileHorizontal(Vector2 velocity) {


        float futureX = 0;
        futureX = rectangle.getX() + velocity.x;

        int x2 = (int) (rectangle.getWidth() + futureX) / TileData.TILE_WIDTH;
        int y2 = (int) (rectangle.getHeight() + rectangle.getY()) / TileData.TILE_HEIGHT;
        int y = (int) (rectangle.getY() / TileData.TILE_HEIGHT);
        int x = (int) (futureX) / TileData.TILE_WIDTH;


        for (int i = 0; i < tileMap.size(); ++ i) {

        // right
        if (rectangle.getX() - futureX < 0) {


            // topRight
            if (!tileMap.get(i).get(y2).get(x2).getTile().isCollideable().contains(Collisions.NONE)) {

                character.leftCollision(x2, y2);


                return false;
            }

            // bottomRight

            if (!tileMap.get(i).get(y).get(x2).getTile().isCollideable().contains(Collisions.NONE)) {

                character.leftCollision(x2, y);
                return false;
            }

        }

//        left
        else if (rectangle.getX() - futureX > 0) {


            // topRight
            if (!tileMap.get(i).get(y2).get(x).getTile().isCollideable().contains(Collisions.NONE)) {

                character.rightCollision(x, y2);


                return false;
            }

            // bottomRight

            if (!tileMap.get(i).get(y).get(x).getTile().isCollideable().contains(Collisions.NONE)) {

                character.rightCollision(x, y);
                return false;
            }

        }
        }


        return true;


    }

    public boolean rightTile() {


        return false;
    }

    public boolean leftTile() {
        return false;
    }

    // this knows we collided with a top tile
    // this is called when there a rigt


}
