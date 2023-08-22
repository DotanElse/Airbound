package com.airbound.game.screens;

import com.airbound.game.Airbound;
import com.airbound.game.GameConstants;
import com.airbound.game.GameUtils;
import com.badlogic.gdx.Game;
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

public class SettingScreen implements Screen {
    private Airbound game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Texture background;
    private SpriteBatch sb;
    private Texture returnButton;
    private BitmapFont font;

    public SettingScreen(Airbound game) {
        this.game = game;
        sb = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT);
        viewport = new ExtendViewport(GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT, camera);
        viewport.setCamera(camera);
        background = new Texture("background.png");
        returnButton = new Texture("buttonDebug.png");
        font = new BitmapFont(); // default
        font.getData().setScale(GameConstants.FONT_SCALE);
        font.setColor(Color.WHITE);
    }

    @Override
    public void show() {

    }

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
        sb.draw(returnButton, 0, GameConstants.GAME_HEIGHT-GameConstants.RETURN_BUTTON_SIZE, GameConstants.RETURN_BUTTON_SIZE, GameConstants.RETURN_BUTTON_SIZE);
        sb.draw(returnButton, (GameConstants.GAME_WIDTH-GameConstants.DIFFICULTY_BUTTON_SIZE)/2, GameConstants.GAME_HEIGHT-3*GameConstants.DIFFICULTY_BUTTON_SIZE, GameConstants.DIFFICULTY_BUTTON_SIZE, GameConstants.DIFFICULTY_BUTTON_SIZE);
        sb.draw(returnButton, (GameConstants.GAME_WIDTH-GameConstants.DIFFICULTY_BUTTON_SIZE)/2, GameConstants.GAME_HEIGHT-5*GameConstants.DIFFICULTY_BUTTON_SIZE, GameConstants.DIFFICULTY_BUTTON_SIZE, GameConstants.DIFFICULTY_BUTTON_SIZE);
        sb.draw(returnButton, (GameConstants.GAME_WIDTH-GameConstants.DIFFICULTY_BUTTON_SIZE)/2, GameConstants.GAME_HEIGHT-7*GameConstants.DIFFICULTY_BUTTON_SIZE, GameConstants.DIFFICULTY_BUTTON_SIZE, GameConstants.DIFFICULTY_BUTTON_SIZE);
        sb.draw(returnButton, (GameConstants.GAME_WIDTH-GameConstants.DIFFICULTY_BUTTON_SIZE)/2, GameConstants.GAME_HEIGHT-9*GameConstants.DIFFICULTY_BUTTON_SIZE, GameConstants.DIFFICULTY_BUTTON_SIZE, GameConstants.DIFFICULTY_BUTTON_SIZE);
        font.draw(sb, "difficulty: " + game.getPreferencesManager().getDifficulty(), GameConstants.GAME_WIDTH/2, GameConstants.GAME_HEIGHT);
        sb.end();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            if (GameUtils.InputTouch(touchPos.x, touchPos.y, GameConstants.RETURN_BUTTON_SIZE, 0, GameConstants.GAME_HEIGHT-GameConstants.RETURN_BUTTON_SIZE))
                game.showMainMenuScreen();
            TouchDifficulty(touchPos);

//            if (touchPos.x <= GameConstants.RETURN_BUTTON_SIZE && touchPos.y >= GameConstants.GAME_HEIGHT-GameConstants.RETURN_BUTTON_SIZE)
        }
    }

    private void TouchDifficulty(Vector3 touchPos) {
        if(GameUtils.InputTouch(touchPos.x, touchPos.y, GameConstants.DIFFICULTY_BUTTON_SIZE, (GameConstants.GAME_WIDTH-GameConstants.DIFFICULTY_BUTTON_SIZE)/2, GameConstants.GAME_HEIGHT-3*GameConstants.DIFFICULTY_BUTTON_SIZE))
            game.getPreferencesManager().setDifficulty(1);
        if(GameUtils.InputTouch(touchPos.x, touchPos.y, GameConstants.DIFFICULTY_BUTTON_SIZE, (GameConstants.GAME_WIDTH-GameConstants.DIFFICULTY_BUTTON_SIZE)/2, GameConstants.GAME_HEIGHT-5*GameConstants.DIFFICULTY_BUTTON_SIZE))
            game.getPreferencesManager().setDifficulty(2);

        if(GameUtils.InputTouch(touchPos.x, touchPos.y, GameConstants.DIFFICULTY_BUTTON_SIZE, (GameConstants.GAME_WIDTH-GameConstants.DIFFICULTY_BUTTON_SIZE)/2, GameConstants.GAME_HEIGHT-7*GameConstants.DIFFICULTY_BUTTON_SIZE))
            game.getPreferencesManager().setDifficulty(3);
        if(GameUtils.InputTouch(touchPos.x, touchPos.y, GameConstants.DIFFICULTY_BUTTON_SIZE, (GameConstants.GAME_WIDTH-GameConstants.DIFFICULTY_BUTTON_SIZE)/2, GameConstants.GAME_HEIGHT-9*GameConstants.DIFFICULTY_BUTTON_SIZE))
            game.getPreferencesManager().setDifficulty(4);
    }

    @Override
    public void resize(int width, int height) {

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

    }
}
