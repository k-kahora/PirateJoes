package com.mygdx.game.Levels;

import com.badlogic.gdx.Screen;

public abstract class TransitionEffect {

    protected abstract float getAlpha();

    abstract void update(float delta);

    abstract void render(Screen current, Screen next);

    abstract boolean isFinished();

    public TransitionEffect(float duration) {}

}
