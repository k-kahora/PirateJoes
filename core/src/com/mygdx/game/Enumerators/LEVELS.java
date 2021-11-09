package com.mygdx.game.Enumerators;

import com.badlogic.gdx.Screen;
import com.mygdx.game.Levels.*;

public enum LEVELS {

        LEVEL1("Wow", new Level1(AbstractLevel.getPirateJoe())),
        LEVEL2("More", new Level2(AbstractLevel.getPirateJoe())),
        LEVEL3("Slop", new Level3(AbstractLevel.getPirateJoe())),
        LEVEL4("Rocket Bois", new Level4(AbstractLevel.getPirateJoe())),
        LEVEL5("Bounce Shors", new Level5(AbstractLevel.getPirateJoe())),
        FINISH("You win", new Level1(AbstractLevel.getPirateJoe())),
        GAME_OVER("Game Over", new Level1(AbstractLevel.getPirateJoe()));

        private LEVELS(String levelDescription, Screen level) {

                this.levelDescription = levelDescription;
                this.currentLevel = level;



        }

        private String levelDescription;

        private Screen currentLevel;

        public String getLevelDescription() {

                return levelDescription;

        }

        public Screen getCurrentLevel() {
                return currentLevel;
        }


}
