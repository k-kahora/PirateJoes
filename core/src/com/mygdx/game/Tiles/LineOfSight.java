package com.mygdx.game.Tiles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.FunctionalityClasses.DebugDrawer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LineOfSight {
    private int u;
    private int q;
    private float slope;

    private Vector2 startPoint, endPoint;

    public LineOfSight(Vector2 startPoint, Vector2 endPoint, ArrayList<ArrayList<TileData>> graph) {

        this.startPoint = startPoint;
        this.endPoint = endPoint;

        this.slope = (endPoint.y - startPoint.y) / (endPoint.x - startPoint.x);/// (endPoint.x - startPoint.x);
        System.out.println(slope);

    }

    public boolean canSee(Rectangle rect) {

        return Intersector.intersectSegmentRectangle(startPoint, endPoint, rect);

    }

    public void setPoints(Vector2 startPoint, Vector2 endPoint) {


        this.slope = (endPoint.y - startPoint.y) / (endPoint.x - startPoint.x);

    }

    public void draw(Matrix4 matrix4) {



        DebugDrawer.DrawDebugLine(startPoint, endPoint, 2, Color.PURPLE, matrix4);



       startPoint.x += 1;
        startPoint.set(startPoint.x, startPoint.x * slope);

    }


}
