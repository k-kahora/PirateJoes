package com.mygdx.game.Tiles;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Enumerators.Collisions;
import com.mygdx.game.FunctionalityClasses.Entity;

import java.util.ArrayList;

public class TileCollision {

    /*
    This class takes an enitity type and calls the entitlys class appropate collider methods based on what
    collision happened a collision happens when the Entity can intersect with any tile without a NONE ollision type
    This also uses a static builder

    A entity can only have one collision but the collision can utilize multiple maps
     */

    private final Rectangle rectangle;


    private final com.mygdx.game.FunctionalityClasses.Entity character;
    private final ArrayList<ArrayList<ArrayList<com.mygdx.game.Tiles.TileData>>> tileMap;


    public static class Builder<T extends Entity> {


        private T character;
        private ArrayList<ArrayList<ArrayList<TileData>>> tileMap = new ArrayList<ArrayList<ArrayList<com.mygdx.game.Tiles.TileData>>>();
        private Rectangle rectangle;


        public Builder tileMap(ArrayList<ArrayList<com.mygdx.game.Tiles.TileData>> a) {

            this.tileMap.add(a);

            return this;
        }

        public Builder tileMap(ArrayList<ArrayList<com.mygdx.game.Tiles.TileData>> a, ArrayList<ArrayList<com.mygdx.game.Tiles.TileData>> b) {


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
        int x = (int) rectangle.getX() / com.mygdx.game.Tiles.TileData.TILE_WIDTH;
        int x2 = (int) (rectangle.getWidth() + rectangle.getX() - 0.01) / com.mygdx.game.Tiles.TileData.TILE_WIDTH;
        int y2 = (int) (rectangle.getHeight() + futureY) / com.mygdx.game.Tiles.TileData.TILE_HEIGHT;
        int y = (int) (futureY) / com.mygdx.game.Tiles.TileData.TILE_HEIGHT;

        if  (x >= 31 || y >= 17 || x2 >= 31 || y2 >= 17) {

            return false;

        }

        for (int i = 0; i < tileMap.size(); ++i) {


        // up
        if (rectangle.getY() - futureY < 0) {




            // topLEft
            if (!tileMap.get(i).get(y2).get(x).getTile().isCollideable().contains(com.mygdx.game.Enumerators.Collisions.NONE)) {


                character.bottomCollision(x, y2);
                return false;
            }

            // topRight
            if (!tileMap.get(i).get(y2).get(x2).getTile().isCollideable().contains(com.mygdx.game.Enumerators.Collisions.NONE)) {

                character.bottomCollision(x2, y2);


                return false;
            }

        }

//        down
        else if (rectangle.getY() - futureY > 0) {


            // topLEft
            if (!tileMap.get(i).get(y).get(x).getTile().isCollideable().contains(com.mygdx.game.Enumerators.Collisions.NONE)) {

                character.topCollision(x, y);

                return false;

            }

            // topRight
            if (!tileMap.get(i).get(y).get(x2).getTile().isCollideable().contains(com.mygdx.game.Enumerators.Collisions.NONE)) {

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

        int x2 = (int) (rectangle.getWidth() + futureX) / com.mygdx.game.Tiles.TileData.TILE_WIDTH;
        int y2 = (int) (rectangle.getHeight() + rectangle.getY()) / com.mygdx.game.Tiles.TileData.TILE_HEIGHT;
        int y = (int) (rectangle.getY() / com.mygdx.game.Tiles.TileData.TILE_HEIGHT);
        int x = (int) (futureX) / TileData.TILE_WIDTH;

        if  (x >= 31 || y >= 17 || x2 >= 31 || y2 >= 17) {

            return false;

        }

        for (int i = 0; i < tileMap.size(); ++ i) {

        // right
        if (rectangle.getX() - futureX < 0) {


            // topRight
            if (!tileMap.get(i).get(y2).get(x2).getTile().isCollideable().contains(com.mygdx.game.Enumerators.Collisions.NONE)) {

                character.leftCollision(x2, y2);


                return false;
            }

            // bottomRight

            if (!tileMap.get(i).get(y).get(x2).getTile().isCollideable().contains(com.mygdx.game.Enumerators.Collisions.NONE)) {

                character.leftCollision(x2, y);
                return false;
            }

        }

//        left
        else if (rectangle.getX() - futureX > 0) {


            // topRight
            if (!tileMap.get(i).get(y2).get(x).getTile().isCollideable().contains(com.mygdx.game.Enumerators.Collisions.NONE)) {

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
