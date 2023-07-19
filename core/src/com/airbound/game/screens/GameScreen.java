package com.airbound.game.screens;

import com.airbound.game.Airbound;
import com.airbound.game.sprites.Ball;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen {
    private Airbound game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Texture background;
    private Ball ball;
    private SpriteBatch sb;

    public GameScreen(Airbound game) {
        this.game = game;
        System.out.println("0");
        sb = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 900, 1600);
        viewport = new ExtendViewport(900, 1600, camera);
        viewport.setCamera(camera);
        background = new Texture("background.png");
        ball = new Ball(300, 300);
    }

    @Override
    public void show() {
        System.out.println("3");
        viewport.apply();
    }

    @Override
    public void render(float delta) {
        handleInput();
        ball.update(delta);
        //camera.position.y -= 300 * delta;

        float ballY = camera.position.y + ball.getPosition().y;
        Gdx.gl.glClearColor(1,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        //sb.draw(background, 0, 0);
        sb.draw(ball.getTexture(), ball.getPosition().x, ballY, 100, 100);
        sb.end();

    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            // Unproject touch coordinates based on the camera's projection matrix
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            float touchX = touchPos.x;
            float touchY = touchPos.y;
            ball.push();
            System.out.println("X " + touchX + " Y " + touchY);

        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
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
        ball.dispose();
        sb.dispose();
    }
}
