package com.mygdx.game.FunctionalityClasses;

import com.mygdx.game.Tiles.TileData;

import java.util.ArrayList;

public class Ray {

    private float x,y,x2,y2;

    public Ray(float x, float y, float x2, float y2) {

        this.x = x;
        this.x2 = x2;
        this.y = y;
        this.y2 = y2;

    }

    public void drawRay() {



    }

    public boolean testRay(EntityLocation target, ArrayList<ArrayList<TileData>> map) {


        return true;

    }

}
