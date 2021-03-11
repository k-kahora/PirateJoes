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
import com.mygdx.game.Viruses.AbstractEnemy;
import com.mygdx.game.Viruses.Enemy;
import com.mygdx.game.Viruses.FluVirus;
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
        secondLayer = new TileEditor("interact.txt", getAssetManager().manager.get(getAssetManager().collisionMap));
        secondLayer.addLevel(this);



        // adss teh collision map for this level
        collisonMaps.add(secondLayer.getTileMap());

        // need this for every level so its clear what the charecter can interact with
        character.addTileMap(collisonMaps.get(0));


        Rectangle characterBoundingBox = character.getBoundingBox();

        collider = new TileCollision.Builder().calcCorners(characterBoundingBox).tileMap(secondLayer.getTileMap(), getWalls().getTileMap()).charecter(character).build();
        // enables collision for this particular level want to add an abstract method that takes a collider and does all the work
        character.addCollider(collider);

        collisionTiles = GraphMaker.createGraph(secondLayer.getTileMap());

        character.setPosition(200,200);
        getFluViruses().add(new FluVirus.Builder(character, this).collisionInit(collisonMaps).build());
        getFluViruses().add(new FluVirus.Builder(character, this).collisionInit(collisonMaps).build());
        getFluViruses().add(new FluVirus.Builder(character, this).collisionInit(collisonMaps).build());
        getFluViruses().add(new FluVirus.Builder(character, this).collisionInit(collisonMaps).build());

        getFluViruses().get(0).setPosition(32, 32);
        getFluViruses().get(1).setPosition(400, 32);
        getFluViruses().get(2).setPosition(50, 32);
        getFluViruses().get(3).setPosition(200, 32);


        // cast to a sterable
        // calls this so all Flu viruses have enabled AI
        initMessages();




        // adds teh connections





        //System.out.println(getNodeCount());


    }

    @Override
    public void render(float delta) {



        // when the char presses space
       timeElapsed += delta;

        // sets mouse cord relative to pixels
        PirateJoes.mouseCordinates.x = Gdx.input.getX();
        PirateJoes.mouseCordinates.y = Gdx.input.getY();

        getPirateJoe().batch.setProjectionMatrix(getViewport().getCamera().combined);

        // turns pixle cordinates to world cordinates
        getViewport().getCamera().unproject(PirateJoes.mouseCordinates);

        getPirateJoe().batch.begin();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        baseLayer.draw(getPirateJoe().batch);
        secondLayer.draw(getPirateJoe().batch);
        getWalls().draw(getPirateJoe().batch);
//        slime.draw(pirateJoes.batch, 100);






        update(delta);

        getPirateJoe().batch.end();
        character.drawDebugBox();






        //viewport.getCamera().position.set(character.getX(),character.getY(),0);
        getViewport().getCamera().update();




    }

    @Override
    public void initMessages() {

        // adds the flue virus as a listener
        getMessageDispatcherAI().addListener((Telegraph) getEnemeyGroup().get(0 ), Messages.CHASE);
        getMessageDispatcherAI().addListener((Telegraph) getEnemeyGroup().get(1 ), Messages.CHASE);
        getMessageDispatcherAI().addListener((Telegraph) getEnemeyGroup().get(0 ), Messages.DETONATING);
        getMessageDispatcherAI().addListener((Telegraph) getEnemeyGroup().get(1 ), Messages.DETONATING);

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
    public int getIndex(TileData node) {
        return node.INDEX;
    }


    // get node count returns the size of the collison map excluding null tiles for easier A* implementation
    @Override
    public int getNodeCount() {

        return collisionTiles.size;
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
    public Array<Connection<TileData>> getConnections(TileData fromNode) {
        return fromNode.getConnectionArray();
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
