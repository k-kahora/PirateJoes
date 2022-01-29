package com.mygdx.game.Enumerators;

import com.badlogic.gdx.Screen;
import com.mygdx.game.Levels.*;

public enum LEVELS {

        LEVEL1("Welcome to Quimbers", new Level1(AbstractLevel.getPirateJoe())),
        LEVEL2("Glizzy Check", new Level2(AbstractLevel.getPirateJoe())),
        LEVEL3("Meat Muffin", new Level3(AbstractLevel.getPirateJoe())),
        LEVEL4("Rocket", new Level4(AbstractLevel.getPirateJoe())),
        LEVEL5("Bounce Shots", new Level5(AbstractLevel.getPirateJoe())),
        LEVEL6("Cringe+Based", new Level5(AbstractLevel.getPirateJoe())),
        LEVEL7("Challenge", new Level5(AbstractLevel.getPirateJoe())),
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

                switch (this) {

                        case LEVEL1:
                                return new Level1(AbstractLevel.getPirateJoe());
                        case LEVEL2:
                                return new Level2(AbstractLevel.getPirateJoe());
                        case LEVEL3:
                                return new Level3(AbstractLevel.getPirateJoe());
                        case LEVEL4:
                                return new Level4(AbstractLevel.getPirateJoe());
                        case LEVEL5:
                                return new Level5(AbstractLevel.getPirateJoe());
                        case LEVEL6:
                                return new Level6(AbstractLevel.getPirateJoe());
                        case LEVEL7:
                                return new Level7(AbstractLevel.getPirateJoe());
                        default:
                                return new Level1(AbstractLevel.getPirateJoe());

                }


        }



}
