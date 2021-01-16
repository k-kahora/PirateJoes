package com.mygdx.game.Tiles;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.GraphPath;

import java.util.Iterator;

public class TilePath implements GraphPath<Connection<TileData>> {
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Connection<TileData> get(int index) {
        return null;
    }

    @Override
    public void add(Connection<TileData> node) {

    }

    @Override
    public void clear() {

    }

    @Override
    public void reverse() {

    }

    @Override
    public Iterator<Connection<TileData>> iterator() {
        return null;
    }
}
