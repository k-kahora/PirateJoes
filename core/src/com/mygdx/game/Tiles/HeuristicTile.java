package com.mygdx.game.Tiles;


import com.badlogic.gdx.ai.pfa.Heuristic;

// manhattan called every frame make it simple

public class HeuristicTile implements Heuristic<TileData> {


    @Override
    public float estimate(TileData node, TileData endNode) {

        float startX = node.getIndex().getIndexi();
        float startY = node.getIndex().getIndexj();

        float endX = endNode.getIndex().getIndexi();
        float endy = endNode.getIndex().getIndexj();

        return Math.abs(startX - endX) + Math.abs(startY - endy);


    }
}
