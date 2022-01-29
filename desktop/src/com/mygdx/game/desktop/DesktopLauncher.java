package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.Levels.PirateJoes;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();


		config.title = "Quimbers";
		config.width = 4096;
		config.height = 2160;
		config.resizable = false;


		new LwjglApplication(new PirateJoes(), config);
	}
}
