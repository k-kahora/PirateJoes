package com.mygdx.game.utils;

public class Edge<T extends Number> {

    public final Point a, b;

    public Edge(Point<T, T> a, Point<T, T> b) {

        this.a = a;
        this.b = b;

    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof Edge)) {
            return false;
        }

        Edge<T> pair = (Edge<T>) obj;

        if (pair.a.equals(a) && pair.b.equals(b)) {
            return true;
        }

        return false;
    }

    public boolean equalsReverse(Object obj) {

        if (!(obj instanceof Edge)) {
            return false;
        }

        Edge<T> pair = (Edge<T>) obj;

        if (pair.a.equals(b) && pair.b.equals(a)) {
            return true;
        }

        return false;

    }

    public void changeEdge(Point<T, T> point) {

        this.b.setIndexi(point.getIndexi());
        this.b.setIndexJ(point.getIndexj());

    }

    public void changeEdgeComplete(Edge<T> edge) {

        this.a.setIndexJ(edge.a.getIndexj());
        this.a.setIndexi(edge.a.getIndexi());


        this.b.setIndexi(edge.b.getIndexi());
        this.b.setIndexJ(edge.b.getIndexj());

    }

    @Override
    public String toString() {

        return "[ " + a + ", " + b + " ]";

    }
}
