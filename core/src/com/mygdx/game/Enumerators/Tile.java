package com.mygdx.game.Enumerators;

import java.util.EnumSet;

public enum Tile {



    AIR("tile_map1", EnumSet.of(Collisions.BOTTOM),false),
    GRASS("tile_map2",EnumSet.of(Collisions.BOTTOM),false),
    STONE("tile_map3",  EnumSet.of(Collisions.BOTTOM),false),
    DOOR("collision_tile1",  EnumSet.of(Collisions.BOTTOM),false),
    BASKET_FULL("collision_tile3",EnumSet.of(Collisions.SOLID, Collisions.SOLID),false),
    INVISIBLE("collision_tile2", EnumSet.of(Collisions.NONE),false),

    LEFT_WALL("collision_tile4"),
    RIGHT_WALL("collision_tile5"),
    BOTTOM_WALL("collision_tile6"),
    TOP_WALL("collision_tile7"),
    TOP_RIGHT_WALL("collision_tile8"),
    TOP_LEFT_WALL("collision_tile9"),
    BOTTOM_LEFT_WALL("collision_tile10"),
    BOTTOM_RIGHT_WALL("collision_tile11");


    private String atlasReference;
    //private Collisions collide;
    private boolean interact;
    private EnumSet<Collisions> set;




    private Tile(String atlasReference, EnumSet<Collisions> set, boolean interact) {
        this.atlasReference = atlasReference;
        this.set = set;
        this.interact = interact;
    }

    private Tile(String atlasReference) {
        this.atlasReference = atlasReference;
        this.set = EnumSet.of(Collisions.SOLID);
        this.interact = false;
    }

    public String getAtlasReference() {
        return atlasReference;
    }

    public EnumSet<Collisions> isCollideable() {
        return set;
    }

    public boolean isInteractable() {
        return interact;
    }



}
