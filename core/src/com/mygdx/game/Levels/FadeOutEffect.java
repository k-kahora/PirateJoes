package com.mygdx.game.Levels;

import com.badlogic.gdx.Screen;

import java.awt.*;

import com.badlogic.gdx.graphics.Color;

public class FadeOutEffect extends TransitionEffect {

    Color color = new Color();

    public FadeOutEffect(float duration) {
        super(duration);
    }


    @Override
    protected float getAlpha() {
        return 0;
    }

    @Override
    void update(float delta) {

    }

    @Override
    void render(Screen current, Screen next) {
        current.render(1f);
        color.set(0f, 0f, 0f, getAlpha());
        // draw a quad over the screen using the color
    }

    @Override
    boolean isFinished() {
        return false;
    }
}
