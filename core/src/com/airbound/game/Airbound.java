package com.airbound.game;

import com.airbound.game.screens.GameScreen;
import com.airbound.game.screens.MainMenuScreen;
import com.airbound.game.screens.PauseScreen;
import com.airbound.game.screens.SettingScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class Airbound extends Game {
	private MainMenuScreen mainMenuScreen;
	private PauseScreen pauseScreen;
	private GameScreen gameScreen;
	private SettingScreen settingScreen;
	private PreferencesManager preferencesManager;
	private boolean paused;

	@Override
	public void create () {
		preferencesManager = new PreferencesManager();
		settingScreen = new SettingScreen(this);
		this.showMainMenuScreen(0);
	}

	public void showGameScreen() {
		setScreen(gameScreen);
	}

	public void showMainMenuScreen(int score) {
		mainMenuScreen = new MainMenuScreen(this, score);
		gameScreen = new GameScreen(this, preferencesManager.getDifficulty());
		pauseScreen = new PauseScreen(this, gameScreen);
		setScreen(mainMenuScreen);
	}

	public void showSettingsScreen() {
		setScreen(settingScreen);
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

	public PreferencesManager getPreferencesManager() {
		return preferencesManager;
	}

	@Override
	public void dispose () {
	}

	public boolean isPaused() {
		return paused;
	}

	public void setNewScore(int highestBrick) {
		if(preferencesManager.getHighScore() < highestBrick)
			preferencesManager.setHighScore(highestBrick);
	}
}
