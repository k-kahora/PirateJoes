package com.mygdx.game.Tiles;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.utils.Array;

import java.util.*;
import java.util.function.Consumer;

public class TilePath implements GraphPath<TileData> {

    Array<TileData> nodes;

    public TilePath() {

        nodes = new Array<>();
    }

    public TilePath(Array<TileData> nodeConnections) {

        nodes = nodeConnections;

    }



    @Override
    public int getCount() {

        return nodes.size;
    }

    @Override
    public TileData get(int index) {

        return nodes.get(index);
    }



    @Override
    public void add(TileData node) {
        nodes.add(node);
    }

    @Override
    public void clear() {
        nodes.clear();
    }

    @Override
    public void reverse() {
        nodes.reverse();
    }


    @Override
    public Iterator<TileData> iterator() {
        return null;
    }

    @Override
    public void forEach(Consumer<? super TileData> action) {

    }

    @Override
    public Spliterator<TileData> spliterator() {
        return null;
    }

    public Array<TileData> getArray() {
        return nodes;
    }
}
