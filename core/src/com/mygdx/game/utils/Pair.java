package com.mygdx.game.utils;

public class Pair<K extends Integer, V extends Integer> {

    private final K indexi;
    private final V indexj;

    public Pair() {
        this.indexi = null;
        this.indexj = null;
    }
    public Pair(K indexi, V indexj) {

        this.indexi = indexi;
        this.indexj = indexj;

    }

    public K getIndexi() {
        return indexi;
    }

    public V getIndexj() {
        return indexj;
    }
}
