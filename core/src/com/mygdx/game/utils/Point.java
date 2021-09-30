package com.mygdx.game.utils;


public class Point<K extends Number, V extends Number> {

    private  K indexi;
    private  V indexj;

    private Point<K, V> next = null, prev = null;

    public Point() {
        this.indexi = null;
        this.indexj = null;
    }
    public Point(K indexi, V indexj) {

        this.indexi = indexi;
        this.indexj = indexj;

    }

    public Point<K,V> getNext() {

        return next;

    }

    public Point<K,V> getPrev() {

        return prev;

    }

    public void setNext(Point<K,V> next) {

        this.next = next;

    }

    public void setPrev(Point<K,V> prev) {

        this.prev = prev;

    }

    public K getIndexi() {
        return indexi;
    }

    public V getIndexj() {
        return indexj;
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof Point)) {
            return false;
        }

        Point<K,V> pair = (Point<K,V>) obj;

        if (pair.indexi.equals(indexi) && pair.indexj.equals(indexj)) {
            return true;
        }

        return false;

    }

    @Override
    public String toString() {
        return "<" + indexi + ", " + indexj + ">";
    }

    public void setIndexi(K a) {

        this.indexi = a;

    }

    public void setIndexJ(V a) {

        this.indexj = a;

    }
}
