package com.mygdx.game;




import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;


public class Character extends Actor  {

    // Actor needs to know its size

    Sprite sprite = new Sprite(new Texture(Gdx.files.internal("creep.png")));

    public Character() {

        setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight()); // sets the bound baby

        addListener(new InputListener() {  // adds listener to the Actor

            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                return super.keyUp(event, keycode);
            }

            @Override
            public boolean keyDown(InputEvent event, int keycode) {

                if (Input.Keys.RIGHT == keycode) {
                    MoveByAction nba = new MoveByAction();
                    nba.setAmount(100f,0);  // moves 100px over a duration of 5s
                    nba.setDuration(5f);

                    Character.this.addAction(nba);

                }

                return false;
            }
        });

    }

    @Override  // whenever the actors positioon changes this is called.
    protected void positionChanged() {
        sprite.setPosition(getX(), getY());
        super.positionChanged();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.draw(batch);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

}
