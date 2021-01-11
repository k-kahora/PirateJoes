package com.mygdx.game.Levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.mygdx.game.MainCharacter;
import com.mygdx.game.Slime;
import com.mygdx.game.Tiles.TileCollision;
import com.mygdx.game.Tiles.TileData;
import com.mygdx.game.Tiles.TileEditor;
import com.mygdx.game.Viruses.FluVirus;

import java.util.ArrayList;

public class Level1 extends AbstractLevel {


    TileEditor baseLayer;
    TileEditor secondLayer;
    TileCollision collider;
    TileCollision wallCollider;

    private Slime slime;
    private MainCharacter character;
    private FluVirus fluVirus;
    private final Group enemeyGroup;

    public Level1(PirateJoes pirateJoes) {
        super(pirateJoes);
        enemeyGroup = new Group();
    }

    @Override
    public void show() {

        loadAssets();

        baseLayer = new com.mygdx.game.Tiles.TileEditor("level1.txt", getAssetManager().manager.get(getAssetManager().tileMap) );
        secondLayer = new TileEditor("interact.txt", getAssetManager().manager.get(getAssetManager().collisionMap));

        ArrayList<ArrayList<ArrayList<TileData>>> collisonMaps = new ArrayList<ArrayList<ArrayList<TileData>>>();
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




        enemeyGroup.addActor(new FluVirus.Builder(character, this).collisionInit(collisonMaps).build());


        enemeyGroup.getChild(0).setPosition(160,100);

        // cast to a sterable
        Arrive<Vector2> arrive = new Arrive<Vector2>((Steerable<Vector2>) enemeyGroup.getChild(0));



    }

    @Override
    public void render(float delta) {



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

        getPirateJoe().batch.end();
        character.drawDebugBox();

        enemeyGroup.act(delta);


        //viewport.getCamera().position.set(character.getX(),character.getY(),0);
        getViewport().getCamera().update();




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
}
