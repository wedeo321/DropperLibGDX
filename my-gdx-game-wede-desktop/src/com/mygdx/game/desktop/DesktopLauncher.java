package com.mygdx.game.desktop;

import com.badlogic.drop.Drop;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.MyGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.title = "Drop";
		config.width = 800;
		config.height = 400;
		new LwjglApplication(new Drop(), config);

	}
}
