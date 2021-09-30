package com.mygdx.game.utils;

import com.badlogic.gdx.math.Vector2;

public class Vector2Compare extends Vector2 implements Comparable<Vector2Compare>{

    private float angleToCompare;

    public Vector2Compare(float a) {

        angleToCompare = a;

    }

    public Vector2Compare(Vector2 a) {

        super(a);

    }

    public Vector2Compare() {

        super();
        angleToCompare = 0f;


    }

    public Vector2Compare(float x, float y) {

        super(x,y);
        angleToCompare = 0f;

    }

    public Vector2Compare(int x, int y) {

        super(x,y);
        angleToCompare = 0f;

    }

    public void setAngleToCompare(float ang) {

        angleToCompare = ang;

    }

    public float getAngleToCompare() {

        return angleToCompare;

    }

    @Override
    public int compareTo(Vector2Compare o) {


        Float vecFloat = o.getAngleToCompare();

        return Float.compare(vecFloat, angleToCompare);

    }

    @Override
    public String toString() {
        return super.toString() + " angle: " + angleToCompare;
    }
}
