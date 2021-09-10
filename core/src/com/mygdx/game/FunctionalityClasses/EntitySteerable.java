package com.mygdx.game.FunctionalityClasses;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.math.Vector2;

public interface EntitySteerable extends Steerable<Vector2>, Entity {

    public void update(float delta);

    public void applySteering(SteeringAcceleration<Vector2> a, float delta);

}
