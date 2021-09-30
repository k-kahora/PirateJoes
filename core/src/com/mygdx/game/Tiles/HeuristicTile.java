package com.mygdx.game.Tiles;


import com.badlogic.gdx.ai.pfa.Heuristic;

// manhattan called every frame make it simple

public class HeuristicTile implements Heuristic<TileData> {


    @Override
    public float estimate(TileData node, TileData endNode) {

        float startX = (float)node.getIndex().getIndexi();
        float startY = (float)node.getIndex().getIndexj();

        float endX = (float)endNode.getIndex().getIndexi();
        float endy = (float)endNode.getIndex().getIndexj();

        return (float)Math.sqrt((Math.pow(startX - endX, 2) + Math.pow(startY - endy, 2)));


    }
}
