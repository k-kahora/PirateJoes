package com.mygdx.game.Enumerators;

import java.util.EnumSet;

public enum Tile {

    AIR("tileMap1", EnumSet.of(Collisions.BOTTOM),false),
    GRASS("tileMap2",EnumSet.of(Collisions.BOTTOM),false),
    STONE("tileMap3",  EnumSet.of(Collisions.BOTTOM),false),
    DOOR("barrier10",  EnumSet.of(Collisions.BOTTOM),false),
    BASKET_FULL("barrier10",EnumSet.of(Collisions.SOLID, Collisions.SOLID),false),
    INVISIBLE("barrier1", EnumSet.of(Collisions.NONE),false),
    BROKEN("barrier14", EnumSet.of(Collisions.SOLID, Collisions.SOLID), false),

    LEFT_WALL("barrier2"),
    RIGHT_WALL("barrier3"),
    BOTTOM_WALL("barrier4"),
    TOP_WALL("barrier5"),
    TOP_RIGHT_WALL("barrier6"),
    TOP_LEFT_WALL("barrier7"),
    BOTTOM_LEFT_WALL("barrier8"),
    BOTTOM_RIGHT_WALL("barrier9"),

    NULL();

    private String atlasReference;
    //private Collisions collide;
    private boolean interact, wall;
    private EnumSet<Collisions> set;

    private Tile() {
        this.atlasReference = "barrier1";
        this.set = EnumSet.of(Collisions.NONE);
        this.interact = false;
        this.wall = false;
    }

    private Tile(String atlasReference, EnumSet<Collisions> set, boolean interact) {
        this.atlasReference = atlasReference;
        this.set = set;
        this.interact = interact;
        this.wall = false;
    }

    private Tile(String atlasReference) {
        this.atlasReference = atlasReference;
        this.set = EnumSet.of(Collisions.SOLID);
        this.interact = false;
        this.wall = true;
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

    public boolean isWall() {
        return wall;
    }

}
