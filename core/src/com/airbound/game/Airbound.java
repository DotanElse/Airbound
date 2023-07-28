package com.airbound.game;

import com.airbound.game.screens.GameScreen;
import com.airbound.game.screens.MainMenuScreen;
import com.airbound.game.screens.PauseScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class Airbound extends Game {
	private MainMenuScreen mainMenuScreen;
	private PauseScreen pauseScreen;
	private GameScreen gameScreen;
	private boolean paused;

	@Override
	public void create () {
		mainMenuScreen = new MainMenuScreen(this);
		gameScreen = new GameScreen(this);
		this.setScreen(mainMenuScreen);
		pauseScreen = new PauseScreen(this, gameScreen);
	}

	public void showGameScreen() {
		setScreen(gameScreen);
	}

	public void showMainMenuScreen() {
		gameScreen = new GameScreen(this);
		pauseScreen = new PauseScreen(this, gameScreen);
		setScreen(mainMenuScreen);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// Call the super method to render the active screen
		super.render();
	}
	@Override
	public void pause() {
		super.pause();
		paused = true;
		setScreen(pauseScreen);
	}

	@Override
	public void resume() {
		super.resume();
		if(paused)
			return;
		setScreen(gameScreen);
	}
	public void setPaused(boolean paused) {
		this.paused = paused;
	}
	
	@Override
	public void dispose () {
	}

	public boolean isPaused() {
		return paused;
	}
}
