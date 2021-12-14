package com.mygdx.game.Levels;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Tiles.TileCollision;
import com.mygdx.game.Tiles.TileData;
import com.mygdx.game.Tiles.TileEditor;
import com.mygdx.game.Viruses.AbstractEnemy;
import com.mygdx.game.Viruses.FluVirus;
import com.mygdx.game.Viruses.WanderVirus;
import com.mygdx.game.utils.GraphMaker;

import java.util.ArrayList;

public class Level6 extends AbstractLevel{

    TileEditor baseLayer, secondLayer;
    ArrayList<ArrayList<ArrayList<TileData>>> collisonMaps;
    TileCollision collider;
    Array<TileData> collisionTiles;

    public Level6(PirateJoes pirateJoes) {
        super(pirateJoes);
    }

    @Override
    public void initMessages() {

    }

    @Override
    public int getIndex(TileData node) {
        return node.INDEX;
    }

    @Override
    public int getNodeCount() {
        return collisionTiles.size;
    }

    @Override
    public void setRender() {

        super.baseLayer = baseLayer;
        super.secondLayer = secondLayer;
        character.setWeakPoints(secondLayer.getWeakPoints());
        levelEdges = GraphMaker.edgeMap(secondLayer.getTileMap());

    }

    @Override
    public boolean handleMessage(Telegram msg) {
        return false;
    }

    @Override
    public void show() {
        baseLayer = new TileEditor(StaticLevels.background, getAssetManager().manager.get(getAssetManager().tileMap), true);

        secondLayer = new TileEditor(StaticLevels.level6, getAssetManager().manager.get(getAssetManager().bruh));
        //GraphMaker.edgeMap(secondLayer.getTileMap());
        setRender();
        secondLayer.addLevel(this);

        collisonMaps = new ArrayList<>();
        collisonMaps.add(secondLayer.getTileMap());

        character.addTileMap(collisonMaps.get(0));
        collisonMaps.add(getWalls().getTileMap());

        Rectangle characterBoundingBox = character.getBoundingBox();

        collider = new TileCollision.Builder().calcCorners(characterBoundingBox).tileMap(secondLayer.getTileMap(), getWalls().getTileMap()).charecter(character).build();
        // enables collision for this particular level want to add an abstract method that takes a collider and does all the work
        character.addCollider(collider);

        collisionTiles = GraphMaker.createGraph(secondLayer.getTileMap());

        character.setPosition(30,180);

        //getFluViruses().add(new FluVirus.Builder(character, this).collisionInit(collisonMaps).build());

        //getFluViruses().add(new WanderVirus.Builder(character, this, collisonMaps).hyper(levelEdges).build());

        getFluViruses().add(new WanderVirus.Builder(character, this, collisonMaps, assetManager).hyper(levelEdges).build());

        //getFluViruses().add(new WanderVirus.Builder(character, this, collisonMaps).wander().build());

        //getFluViruses().add(new FluVirus.Builder(character, this).collisionInit(collisonMaps).build());//
        //getFluViruses().get(3).setPosition(40, 40);

        //getFluViruses().get(1).setPosition(200, 100);
        //getFluViruses().get(0).setPosition(400, 100);
        getFluViruses().get(0).setPosition(390, 90);


    }


    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public ArrayList<ArrayList<ArrayList<TileData>>> getCollisionMap() {
        return collisonMaps;
    }

    @Override
    public ArrayList<? extends Steerable<Vector2>> getEnemeyGroup() {
        ArrayList<AbstractEnemy> steers = new ArrayList<>();
        for (AbstractEnemy a : getFluViruses()) {

            steers.add(a);

        }

        return steers;
    }
}
