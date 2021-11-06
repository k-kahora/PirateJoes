package com.mygdx.game.Enumerators;

public enum LEVELS {

        LEVEL1("Wow"),
        LEVEL2("More"),
        LEVEL3("Slop"),
        LEVEL4("Rocket Bois"),
        LEVEL5("Bounce Shors"),
        FINISH("You win"),
        GAME_OVER("Game Over");

        private LEVELS(String levelDescription) {

                this.levelDescription = levelDescription;


        }

        private String levelDescription;

        public String getLevelDescription() {

                return levelDescription;

        }






}
