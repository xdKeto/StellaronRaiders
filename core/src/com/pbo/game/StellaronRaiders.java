package com.pbo.game;

import Screen.GameOverScreen;
import Screen.GameScreen;
import Screen.MenuScreen;
import Screen.WinnerScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.awt.*;

public class StellaronRaiders extends Game {
	public static final int WIDTH = 1600;
	public static final int HEIGHT = 900;
	public SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();
		this.setScreen(new MenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		this.dispose();
	}
}
