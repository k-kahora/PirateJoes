package com.mygdx.game.Tiles;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.GraphPath;

import java.util.*;

public class TilePath implements GraphPath<TileData> {

    private final List<TileData> nodeConnections;

    public TilePath() {
        this.nodeConnections = new LinkedList<>();
    }

    public TilePath(ArrayList<TileData> nodeConnections) {

        this.nodeConnections = nodeConnections;

    }


    @Override
    public int getCount() {
        return nodeConnections.size();
    }

    @Override
    public TileData get(int index) {

        return nodeConnections.get(index);
    }

    @Override
    public void add(TileData node) {
        nodeConnections.add(node);
    }

    @Override
    public void clear() {
        nodeConnections.clear();
    }

    @Override
    public void reverse() {
        Collections.reverse(nodeConnections);
    }

    @Override
    public Iterator<TileData> iterator() {
        return nodeConnections.iterator();
    }
}
