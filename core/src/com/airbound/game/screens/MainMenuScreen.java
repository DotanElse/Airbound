package com.airbound.game.screens;

import com.airbound.game.Airbound;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainMenuScreen implements Screen {
    private Airbound game;
    private OrthographicCamera camera;
    private OrthographicCamera guiCam;
    private Viewport viewport;
    private Texture background;
    private Texture playButton;
    private SpriteBatch sb;
    private float playButtonX;
    private float playButtonY;
    private BitmapFont font;


    public MainMenuScreen(Airbound game) {
        this.game = game;
        sb = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 900, 1600);
        guiCam = new OrthographicCamera();
        guiCam.setToOrtho(false, 900, 1600);
        viewport = new ExtendViewport(900, 1600, camera);
        background = new Texture("background.png");
        playButton = new Texture("playButton.png");
        font = new BitmapFont(); // default
        font.getData().setScale(2.5f);
        font.setColor(Color.WHITE);
    }

    @Override
    public void show() {
        viewport.apply();
    }

    @Override
    public void render(float delta) {
        handleInput();
        Gdx.gl.glClearColor(1,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        for(int i=0; i<6; i++)
        {
            sb.draw(background, 0, (background.getHeight())*i-900, 900, background.getHeight());
        }
        sb.draw(playButton, playButtonX, playButtonY);
        sb.end();
        sb.setProjectionMatrix(guiCam.combined);
        sb.begin();
        font.draw(sb, "High Score: " + game.getPreferencesManager().getHighScore(), 20, 1600);
        sb.end();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            // Unproject touch coordinates based on the camera's projection matrix
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            float touchX = touchPos.x;
            float touchY = touchPos.y;

            //System.out.println((touchX) + " " + touchY + " " + playButtonX + " " + playButtonY);

            if (touchX >= playButtonX && touchX <= playButtonX + playButton.getWidth() &&
                    touchY >= playButtonY && touchY <= playButtonY + playButton.getHeight()) {
                game.showGameScreen();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        playButtonX = (camera.viewportWidth - playButton.getWidth()) / 2;
        playButtonY = (camera.viewportHeight - playButton.getHeight()) / 2;
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
        background.dispose();
        playButton.dispose();
        sb.dispose();
    }
}
