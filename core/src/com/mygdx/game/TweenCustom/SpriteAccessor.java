package com.mygdx.game.TweenCustom;

import aurelienribon.tweenengine.TweenAccessor;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class SpriteAccessor implements TweenAccessor<Sprite> {

    public static final int ALPHA = 0;
    public static final int POSITION_X = 2;
    public static final int POSTION_Y = 1;

    @Override
    public int getValues(Sprite target, int tweenType, float[] returnValues) {

        switch (tweenType) {
            case ALPHA:
                returnValues[0] = target.getColor().a;
                return 1;
            case POSITION_X:
                returnValues[1] = target.getX();
                return 2;
            case POSTION_Y:
                returnValues[2] = target.getY();
                return 2;
            default:
                assert false;
                return -1;
        }
    }

    @Override
    public void setValues(Sprite target, int tweenType, float[] newValues) {

        switch (tweenType) {

            case ALPHA:
                target.setColor(target.getColor().r, target.getColor().g, target.getColor().b, newValues[0]);
                break;

            case POSITION_X:
                target.setX(newValues[1]);

            case POSTION_Y:
                target.setY(newValues[2]);


            default:
                assert false;

        }

    }
}
