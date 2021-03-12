package com.mygdx.game.Viruses;

import com.badlogic.gdx.ai.msg.Telegram;
import com.mygdx.game.FunctionalityClasses.Entity;
import com.mygdx.game.Levels.Level;

public class WanderVirus extends AbstractEnemy{


    private WanderVirus(Builder builder) {
        super(builder.target, builder.currentLevel);
    }

    public static class Builder {

        private final Level currentLevel;
        private final Entity target;

        Builder(Entity target, Level currentLevel) {

            this.currentLevel = currentLevel;
            this.target = target;

        }

        WanderVirus build() {

            return new WanderVirus(this);

        }

    }

    @Override
    public boolean isDead() {
        return false;
    }

    @Override
    public void setDetonationToInstant() {

    }

    @Override
    public void setDeath() {

    }

    @Override
    public boolean handleMessage(Telegram telegram) {
        return false;
    }
}
