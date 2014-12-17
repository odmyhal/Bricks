package com.odmyhal.ball;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "ball-game";
		cfg.useGL30 = false;
//		cfg.width = 1250;
//		cfg.height = 750;
		cfg.width = 500;
		cfg.height = 750;
		
		new LwjglApplication(new BallGame(cfg.width, cfg.height), cfg);
	}
}
