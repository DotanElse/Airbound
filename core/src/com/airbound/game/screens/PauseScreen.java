package com.airbound.game.screens;

import com.airbound.game.Airbound;
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
    float textureWidth = 200;
    float textureHeight = 200;

    public PauseScreen(Airbound game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
        continueButton = new Texture("continue.png");
        sb = new SpriteBatch();
        guiCam = new OrthographicCamera();
        guiCam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        System.out.println(Gdx.graphics.getWidth());
        continueButtonX = (guiCam.viewportWidth - textureWidth) / 2;
        continueButtonY = (guiCam.viewportHeight - textureHeight) / 2;
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
            float touchX = touchPos.x;
            float touchY = touchPos.y;

            if (touchX >= continueButtonX && touchX <= continueButtonX + textureWidth &&
                    touchY >= continueButtonY && touchY <= continueButtonY + textureHeight) {
                game.setPaused(false);
                game.resume();
            }
        }

        sb.setProjectionMatrix(guiCam.combined);
        sb.begin();
        sb.draw(continueButton, continueButtonX, continueButtonY, textureWidth, textureHeight);
        sb.end();
    }

    @Override
    public void resize(int width, int height) {
        guiCam.setToOrtho(false, width, height);
        continueButtonX = (guiCam.viewportWidth - textureWidth) / 2;
        continueButtonY = (guiCam.viewportHeight - textureHeight) / 2;
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
