package com.airbound.game.screens;

import com.airbound.game.Airbound;
import com.airbound.game.GameConstants;
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
    private Texture settingButton;
    private BitmapFont font;


    public MainMenuScreen(Airbound game) {
        this.game = game;
        sb = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT);
        guiCam = new OrthographicCamera();
        guiCam.setToOrtho(false, GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT);
        viewport = new ExtendViewport(GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT, camera);
        viewport.setCamera(camera);
        background = new Texture("background.png");
        playButton = new Texture("playButton.png");
        settingButton = new Texture("buttonDebug.png");
        font = new BitmapFont(); // default
        font.getData().setScale(GameConstants.FONT_SCALE);
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
            sb.draw(background, 0, (background.getHeight())*i-80, GameConstants.GAME_WIDTH, background.getHeight());
        }
        sb.draw(playButton, playButtonX, playButtonY, GameConstants.PLAY_BUTTON_SIZE, GameConstants.PLAY_BUTTON_SIZE);
        sb.end();
        sb.setProjectionMatrix(guiCam.combined);
        sb.begin();
        font.draw(sb, "High Score: " + game.getPreferencesManager().getHighScore(), GameConstants.WALL_SIZE, GameConstants.GAME_HEIGHT);
        sb.draw(settingButton, GameConstants.GAME_WIDTH-GameConstants.SETTING_BUTTON_SIZE, GameConstants.GAME_HEIGHT-GameConstants.SETTING_BUTTON_SIZE,
                GameConstants.SETTING_BUTTON_SIZE, GameConstants.SETTING_BUTTON_SIZE);
        sb.end();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            // Unproject touch coordinates based on the camera's projection matrix
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            Vector3 guiTouchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            guiCam.unproject(guiTouchPos);
            camera.unproject(touchPos);
            System.out.println(guiTouchPos);

            float touchX = touchPos.x;
            float touchY = touchPos.y;

            if (touchX >= playButtonX && touchX <= playButtonX + GameConstants.PLAY_BUTTON_SIZE &&
                    touchY >= playButtonY && touchY <= playButtonY + GameConstants.PLAY_BUTTON_SIZE) {
                game.showGameScreen();
            }
            if (guiTouchPos.x >= GameConstants.GAME_WIDTH-GameConstants.SETTING_BUTTON_SIZE &&
            guiTouchPos.y >= GameConstants.GAME_HEIGHT-GameConstants.SETTING_BUTTON_SIZE)
            {
                game.showSettingsScreen();

            }
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        playButtonX = (camera.viewportWidth - GameConstants.PLAY_BUTTON_SIZE) / 2;
        playButtonY = (camera.viewportHeight - GameConstants.PLAY_BUTTON_SIZE) / 2;
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
