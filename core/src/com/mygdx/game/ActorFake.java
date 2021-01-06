package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

// imports the static class

public class ActorFake extends Image {

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(this.getColor()); // draw and updates the color change
                                         //

        ((TextureRegionDrawable)getDrawable()).draw(batch, getX(), getY(),
                getOriginX(), getOriginY(),
                getWidth(), getHeight(),
                getScaleX(), getScaleY(),
                getRotation());
        // getDrawable is implemented by image
        // this tells draw everything about actor and is passed to draw
        // an actor doesnt render rotaion or sceled so it has to be passed in
    }

    public ActorFake(Texture texture) {
        super(texture);

        setBounds(getX(), getY(), getWidth(), getHeight());

        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {

                switch(keycode) {
                    case(Input.Keys.NUM_1) :
                        MoveToAction mta = new MoveToAction();
                    mta.setPosition(200f,200f);
                    mta.setDuration(5f);
                    ActorFake.this.addAction(mta);
                    break;

                    case(Input.Keys.NUM_2) :
                        MoveByAction mba = new MoveByAction();
                        mba.setAmount(-200f,0);
                        mba.setDuration(2f);
                        ActorFake.this.addAction(mba);
                        break;


                    case (Input.Keys.NUM_3) :
                        ColorAction colorAction = new ColorAction();
                        colorAction.setEndColor(Color.GREEN);
                        colorAction.setDuration(5f);

                        ActorFake.this.addAction(colorAction);
                        break;

                    case (Input.Keys.NUM_4) :

                        // multiple actions

                        MoveToAction moveToAction  = new MoveToAction();
                        moveToAction.setPosition(Gdx.graphics.getWidth() - 200, Gdx.graphics.getHeight() - 200);
                        moveToAction.setDuration(2f);

                        ScaleByAction scaleByAction = new ScaleByAction();
                        scaleByAction.setAmount(2f,2f);
                        scaleByAction.setDuration(2f);

                        RotateToAction rotateToAction = new RotateToAction();
                        rotateToAction.setRotation(90f);
                        rotateToAction.setDuration(2f);

                        ParallelAction parallelAction = new ParallelAction(moveToAction, scaleByAction, rotateToAction);

                        ActorFake.this.addAction(parallelAction);
                        break;

                    case (Input.Keys.NUM_5) :
                        MoveToAction moveToAction2  = new MoveToAction();
                        moveToAction2.setPosition(Gdx.graphics.getWidth() - 200, Gdx.graphics.getHeight() - 200);
                        moveToAction2.setDuration(2f);

                        ScaleByAction scaleByAction2 = new ScaleByAction();
                        scaleByAction2.setAmount(2f,2f);
                        scaleByAction2.setDuration(2f);

                        RotateToAction rotateToAction2 = new RotateToAction();
                        rotateToAction2.setRotation(90f);
                        rotateToAction2.setDuration(2f);

                        SequenceAction sequenceAction = new SequenceAction(moveToAction2, scaleByAction2, rotateToAction2);

                        ActorFake.this.addAction(sequenceAction);

                        break;

                    case (Input.Keys.NUM_6) :
                        RunnableAction runnableAction = new RunnableAction();
                        runnableAction.setRunnable(new Runnable() {
                            @Override
                            public void run() {
                                ActorFake.this.setPosition(0,0);
                                ActorFake.this.setScale(1f);
                                ActorFake.this.setRotation(0f);
                            }
                        });

                        ActorFake.this.addAction(runnableAction);
                        break;

                    case (Input.Keys.SPACE) :
                        addAction(parallel(

                                moveTo(200f,200f, 3f),
                                scaleTo(1.3f,1.5f, 3f),
                                 rotateTo(120f, 3f, Interpolation.bounceOut)

                        ));

                }

                return super.keyDown(event, keycode);
            }
        });

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                addAction(parallel(
                        rotateTo(180f),
                        moveTo(200f,200f)
                ));
            }
        });
    }

}
