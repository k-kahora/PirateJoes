package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.mygdx.game.FunctionalityClasses.AbstractLevel;
import com.mygdx.game.FunctionalityClasses.MyAssetManager;
import com.mygdx.game.Tiles.TileCollision;
import com.mygdx.game.Tiles.TileEditor;
import com.mygdx.game.Viruses.FluVirus;

public class Level1 extends AbstractLevel {


    com.mygdx.game.Tiles.TileEditor baseLayer;
    com.mygdx.game.Tiles.TileEditor secondLayer;
    com.mygdx.game.Tiles.TileCollision collider;
    com.mygdx.game.Tiles.TileCollision wallCollider;

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




        character = new MainCharacter();
        character.setPosition(32,32);
        // adss teh collision map
        character.addTileMap(secondLayer.getTileMap());
        character.addTileMap(getWalls().getTileMap());
        Rectangle characterBoundingBox = character.getBoundingBox();

        collider = new TileCollision.Builder().calcCorners(characterBoundingBox).tileMap(secondLayer.getTileMap(), getWalls().getTileMap()).charecter(character).build();
        // enables collision
        character.addCollider(collider);


        for (int i = 0; i < 6; ++i ) {

            enemeyGroup.addActor(new FluVirus());

        }

        for (int i = 0; i < 6; ++i) {

            int x = i * 16;

            enemeyGroup.getChild(i).setPosition(x , 32);

        }




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
