package com.mygdx.game.Tiles;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.utils.Array;

public class TileConnection implements Connection<TileData> {

    private TileData toNode;
    private TileData fromNode;
    private float cost;

    public TileConnection(TileData toNode, TileData fromNode, float cost) {
        this.cost = cost;
        this.toNode = toNode;
        this.fromNode = fromNode;
    }

    @Override
    public float getCost() {
        return cost;
    }

    @Override
    public TileData getFromNode() {
        return fromNode;
    }

    @Override
    public TileData getToNode() {
        return toNode;
    }

    @Override
    public String toString() {
        return toNode + " --> " + fromNode + " cost: " + cost;
    }


}
