package com.airbound.game.screens;

import com.airbound.game.Airbound;
import com.airbound.game.GameConstants;
import com.airbound.game.GameUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class PauseScreen implements Screen {
    private Airbound game;
    private SpriteBatch sb;
    private GameScreen gameScreen;
    private Texture continueButton;
    private OrthographicCamera guiCam;
    float continueButtonX;
    float continueButtonY;


    public PauseScreen(Airbound game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
        continueButton = new Texture("Misc/continue.png");
        sb = new SpriteBatch();
        guiCam = new OrthographicCamera();
        guiCam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        continueButtonX = (guiCam.viewportWidth - GameConstants.CONTINUE_BUTTON_SIZE) / 2;
        continueButtonY = (guiCam.viewportHeight - GameConstants.CONTINUE_BUTTON_SIZE) / 2;
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        gameScreen.renderFrozenElements();
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            guiCam.unproject(touchPos);

            if(GameUtils.InputTouch(touchPos.x, touchPos.y, GameConstants.CONTINUE_BUTTON_SIZE, continueButtonX, continueButtonY))
            {
                game.setPaused(false);
                game.resume();
            }
        }

        sb.setProjectionMatrix(guiCam.combined);
        sb.begin();
        sb.draw(continueButton, continueButtonX, continueButtonY, GameConstants.CONTINUE_BUTTON_SIZE, GameConstants.CONTINUE_BUTTON_SIZE);
        sb.end();
    }

    @Override
    public void resize(int width, int height) {
        guiCam.setToOrtho(false, width, height);
        continueButtonX = (guiCam.viewportWidth - GameConstants.CONTINUE_BUTTON_SIZE) / 2;
        continueButtonY = (guiCam.viewportHeight - GameConstants.CONTINUE_BUTTON_SIZE) / 2;
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        sb.dispose();
        continueButton.dispose();
    }
}
