package com.airbound.game.screens;

import com.airbound.game.Airbound;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PauseScreen implements Screen {
    private Airbound game;
    private SpriteBatch sb;
    private GameScreen gameScreen;
    private Texture continueButton;
    private OrthographicCamera guiCam;
    float continueButtonX;
    float continueButtonY;
    // Add any other necessary variables for your menu elements (buttons, labels, etc.).

    public PauseScreen(Airbound game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
        continueButton = new Texture("continue.png");
//        guiCam = game.getGuiCam();
//        sb = new SpriteBatch();
//        continueButtonX = (guiCam.viewportWidth - continueButton.getWidth()) / 2;
//        continueButtonY = (guiCam.viewportHeight - continueButton.getHeight()) / 2;
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        gameScreen.renderFrozenElements();
        if (Gdx.input.justTouched()) {
            // Notify the game to resume when the screen is clicked
            game.setPaused(false);
            game.resume();
            dispose();
        }
        //sb.setProjectionMatrix(game.().getCamera().combined);

//        sb.setProjectionMatrix(guiCam.combined);
//        sb.begin();
//        sb.draw(continueButton, continueButtonX, continueButtonY);
//        sb.end();
//        Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);
//
//        sb.begin();
//        sb.draw(continueButton, continueButtonX, continueButtonY, 300, 300);
//        sb.end();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
        // Implement any necessary logic when the pause screen is paused, if needed.
    }

    @Override
    public void resume() {
        // Implement any necessary logic when the pause screen is resumed, if needed.
    }

    @Override
    public void hide() {
        // Implement any necessary logic when the pause screen is hidden, if needed.
    }

    @Override
    public void dispose() {
        // Dispose of any disposable resources, if needed.
        //sb.dispose();
        continueButton.dispose();
        // Dispose of other resources used in the menu.
    }
}

