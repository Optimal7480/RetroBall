package org.bytefire.ld28.java;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import org.bytefire.ld28.core.LD28;

public class ld28Desktop {
	public static void main (String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.useGL20 = true;
                config.resizable = false;
                config.width = 800;
                config.height = 600;
		new LwjglApplication(new LD28(), config);
	}
}
