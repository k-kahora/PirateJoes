package com.mygdx.game;

import com.sun.tools.javac.code.Attribute;

import java.util.EnumSet;

public enum Tile {



    AIR("tile_map1", EnumSet.of(Collisions.BOTTOM),false),
    GRASS("tile_map2",EnumSet.of(Collisions.BOTTOM),false),
    STONE("tile_map3",  EnumSet.of(Collisions.BOTTOM),false),
    DOOR("tile_layer_two2",  EnumSet.of(Collisions.BOTTOM),false),
    BASKET_FULL("tile_layer_two4",EnumSet.of(Collisions.SOLID, Collisions.SOLID),false),
    INVISIBLE("tile_layer_two3", EnumSet.of(Collisions.NONE),false);


    private String atlasReference;
    //private Collisions collide;
    private boolean interact;
    private EnumSet<Collisions> set;




    private Tile(String atlasReference, EnumSet<Collisions> set, boolean interact) {
        this.atlasReference = atlasReference;
        this.set = set;
        this.interact = interact;



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
