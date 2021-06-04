package com.mygdx.game.utils;


import java.util.Objects;

public class Edge<K extends Number, V extends Number> {

    private final K indexi;
    private final V indexj;

    public Edge() {
        this.indexi = null;
        this.indexj = null;
    }
    public Edge(K indexi, V indexj) {

        this.indexi = indexi;
        this.indexj = indexj;

    }

    public K getIndexi() {
        return indexi;
    }

    public V getIndexj() {
        return indexj;
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof Edge)) {
            return false;
        }

        Edge<K,V> pair = (Edge<K,V>) obj;

        if (pair.indexi.equals(indexi) && pair.indexj.equals(indexj)) {
            return true;
        }

        return false;

    }

    @Override
    public String toString() {
        return "<" + indexi + ", " + indexj + ">";
    }
}
