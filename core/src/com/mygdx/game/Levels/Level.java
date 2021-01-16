package com.mygdx.game.Levels;

import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Tiles.TileData;

public interface Level extends IndexedGraph<TileData> {


    public void loadAssets();

    public Viewport getViewport();

}
