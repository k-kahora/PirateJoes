package com.mygdx.game.Levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MainCharacter;
import com.mygdx.game.Particles.BulletSplash;
import com.mygdx.game.Particles.ParticleManager;
import com.mygdx.game.Slime;
import com.mygdx.game.Tiles.TileCollision;
import com.mygdx.game.Tiles.TileData;
import com.mygdx.game.Tiles.TileEditor;
import com.mygdx.game.Viruses.*;
import com.mygdx.game.utils.GraphMaker;
import com.mygdx.game.utils.Messages;

import java.util.ArrayList;

/*
abstract level is a skeltal implemntaion for all levels
all levels must extend AbstractLevel
Core functionality is kept in Abstract Level
 */

public class Level1 extends AbstractLevel {


    TileEditor baseLayer;
    TileEditor secondLayer;
    TileCollision collider;
    TileCollision wallCollider;

    Array<TileData> collisionTiles;

    private Slime slime;

    private FluVirus fluVirus;
    private final ArrayList<ArrayList<ArrayList<TileData>>> collisonMaps;

    private float timeElapsed = 0;

    public Level1(PirateJoes pirateJoes) {
        super(pirateJoes);
        collisonMaps = new ArrayList<ArrayList<ArrayList<TileData>>>();
    }

    @Override
    public void show() {

        baseLayer = new TileEditor("level1.txt", getAssetManager().manager.get(getAssetManager().tileMap), true);
        secondLayer = new TileEditor("interact.txt", getAssetManager().manager.get(getAssetManager().bruh));

        setRender();

        secondLayer.addLevel(this);

        // adss teh collision map for this level
        collisonMaps.add(secondLayer.getTileMap());

        // need this for every level so its clear what the charecter can interact with
        character.addTileMap(collisonMaps.get(0));
        collisonMaps.add(getWalls().getTileMap());

        Rectangle characterBoundingBox = character.getBoundingBox();

        collider = new TileCollision.Builder().calcCorners(characterBoundingBox).tileMap(secondLayer.getTileMap(), getWalls().getTileMap()).charecter(character).build();
        // enables collision for this particular level want to add an abstract method that takes a collider and does all the work
        character.addCollider(collider);

        collisionTiles = GraphMaker.createGraph(secondLayer.getTileMap());

        character.setPosition(50,150);

        getFluViruses().add(new WanderVirus.Builder(character, this, collisonMaps).build());
        //getFluViruses().add(new WanderVirus.Builder(character, this, collisonMaps).build());
        //getFluViruses().add(new FluVirus.Builder(character, this).collisionInit(collisonMaps).build());
        //getFluViruses().add(new WanderVirus.Builder(character, this, collisonMaps).build());
        //getFluViruses().add(new WanderVirus.Builder(character, this, collisonMaps).build());

        //getFluViruses().get(0).setPosition(430, 125);
        getFluViruses().get(0).setPosition(260, 125);
        //getFluViruses().get(2).setPosition(200, 125);
        //getFluViruses().get(2).setPosition(230, 125);
        //getFluViruses().get(3).setPosition(130, 125);


        // cast to a sterable
        // calls this so all Flu viruses have enabled AI
        initMessages();

        System.out.println("after init");




        // adds teh connections





        //System.out.println(getNodeCount());


    }

    @Override
    public void setRender() {
        super.baseLayer = baseLayer;
        super.secondLayer = secondLayer;
        character.setWeakPoints(secondLayer.getWeakPoints());
        //levelEdges = GraphMaker.edgeMap(secondLayer.getTileMap());
    }

    @Override
    public void initMessages() {

        // adds the flue virus as a listener


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



    // get node count returns the size of the collison map excluding null tiles for easier A* implementation
    @Override
    public int getNodeCount() {

        return collisionTiles.size;
    }

    @Override
    public int getIndex(TileData node) {
        return node.INDEX;
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        switch (msg.message) {

            case (Messages.FINISH):


                return true;

            default:{

                return false;

            }
        }

    }





    @Override
    public ArrayList<ArrayList<ArrayList<TileData>>> getCollisionMap() {
        return collisonMaps;
    }

    public ArrayList<AbstractEnemy> getEnemeyGroup() {
        ArrayList<AbstractEnemy> steers = new ArrayList<>();
        for (AbstractEnemy a : getFluViruses()) {

            steers.add(a);

        }

        return steers;
    }



}
