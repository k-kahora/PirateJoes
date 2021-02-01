package com.mygdx.game.Levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
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
import com.mygdx.game.Viruses.FluVirus;
import com.mygdx.game.utils.GraphMaker;
import com.mygdx.game.utils.Messages;

import java.util.ArrayList;

public class Level1 extends AbstractLevel {


    TileEditor baseLayer;
    TileEditor secondLayer;
    TileCollision collider;
    TileCollision wallCollider;

    Array<TileData> collisionTiles;

    private Slime slime;
    private MainCharacter character;
    private FluVirus fluVirus;
    private final Group enemeyGroup;
    private final ArrayList<ArrayList<ArrayList<TileData>>> collisonMaps;

    private float timeElapsed = 0;

    public Level1(PirateJoes pirateJoes) {
        super(pirateJoes);
        enemeyGroup = new Group();
        collisonMaps = new ArrayList<ArrayList<ArrayList<TileData>>>();
    }

    @Override
    public void show() {

        loadAssets();
        baseLayer = new TileEditor("level1.txt", getAssetManager().manager.get(getAssetManager().tileMap), true);
        secondLayer = new TileEditor("interact.txt", getAssetManager().manager.get(getAssetManager().collisionMap));
        secondLayer.addLevel(this);

        character = new MainCharacter();
        character.setPosition(32,32);
        // adss teh collision map
        collisonMaps.add(secondLayer.getTileMap());
        collisonMaps.add(getWalls().getTileMap());

        character.addTileMap(collisonMaps.get(0));
        character.addTileMap(collisonMaps.get(1));

        Rectangle characterBoundingBox = character.getBoundingBox();

        collider = new TileCollision.Builder().calcCorners(characterBoundingBox).tileMap(secondLayer.getTileMap(), getWalls().getTileMap()).charecter(character).build();
        // enables collision
        character.addCollider(collider);

        collisionTiles = GraphMaker.createGraph(secondLayer.getTileMap());

        character.setPosition(200,200);
        enemeyGroup.addActor(new FluVirus.Builder(character, this).collisionInit(collisonMaps).build());
        enemeyGroup.addActor(new FluVirus.Builder(character, this).collisionInit(collisonMaps).build());

        enemeyGroup.getChild(0).setPosition(32, 32);
        enemeyGroup.getChild(1).setPosition(12, 32);


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

        character.draw(getPirateJoe().batch, 0);
        enemeyGroup.draw(getPirateJoe().batch, 0);
        character.act(Gdx.graphics.getDeltaTime());

        for (BulletSplash a : ParticleManager.splashBullets) {
            if (!a.isDone()) {
                a.update(delta);
                a.draw(getPirateJoe().batch);
            }

        }

        getPirateJoe().batch.end();
        character.drawDebugBox();

        enemeyGroup.act(delta);





        //viewport.getCamera().position.set(character.getX(),character.getY(),0);
        getViewport().getCamera().update();




    }

    @Override
    public void initMessages() {

        // adds the flue virus as a listener
        getMessageDispatcherAI().addListener((Telegraph) enemeyGroup.getChild(0 ), Messages.CHASE);
        getMessageDispatcherAI().addListener((Telegraph) enemeyGroup.getChild(1 ), Messages.CHASE);

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
    public Array<Connection<TileData>> getConnections(TileData fromNode) {
        return fromNode.getConnectionArray();
    }

    @Override
    public ArrayList<ArrayList<ArrayList<TileData>>> getCollisionMap() {
        return collisonMaps;
    }


}
