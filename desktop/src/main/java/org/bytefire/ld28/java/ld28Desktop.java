package org.bytefire.ld28.java;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import org.bytefire.ld28.core.LD28;
import org.bytefire.ld28.core.screen.GameScreen;

public class ld28Desktop {
	public static void main (String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.useGL20 = true;
                config.resizable = false;
                config.width = GameScreen.WINDOW_WIDTH;
                config.height = GameScreen.WINDOW_HEIGHT;
		new LwjglApplication(new LD28(), config);
	}
}
