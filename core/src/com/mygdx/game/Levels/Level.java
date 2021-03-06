package com.mygdx.game.Levels;

import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Tiles.TileData;

import java.util.ArrayList;

public interface Level extends IndexedGraph<TileData> {


    public void loadAssets();


    public ArrayList<ArrayList<ArrayList<TileData>>> getCollisionMap();

    public ArrayList<? extends Steerable<Vector2>> getEnemeyGroup();
}
