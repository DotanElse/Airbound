package com.airbound.game.screens;

import com.airbound.game.Airbound;
import com.airbound.game.sprites.Ball;
import com.airbound.game.sprites.Walls;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen {
    private Airbound game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Texture background;
    private Ball ball;
    private Walls walls;
    private SpriteBatch sb;
    private Vector2 initialTouch;
    private Vector2 lastTouch;
    private boolean isDragging;
    private float gravity;

    public GameScreen(Airbound game) {
        this.game = game;
        System.out.println("0");
        sb = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 900, 1600);
        viewport = new ExtendViewport(900, 1600, camera);
        viewport.setCamera(camera);
        gravity = 300;
        background = new Texture("background.png");
        walls = new Walls();
        ball = new Ball(300, 300);
        isDragging = false; //game init with user not dragging
        initialTouch = new Vector2();
        lastTouch = new Vector2();

    }

    @Override
    public void show() {
        viewport.apply();
    }

    @Override
    public void render(float delta) {
        handleInput();
        ball.update(delta);
        camera.position.y -= gravity * delta;

        float ballY = camera.position.y + ball.getPosition().y;
        Gdx.gl.glClearColor(1,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        //sb.draw(background, 0, 0);
        walls.draw(sb, camera.position.y);

        sb.draw(ball.getTexture(), ball.getPosition().x, ballY, 100, 100);
        sb.end();
    }


    private void handleInput() {
        Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(touchPos);
        if (Gdx.input.justTouched() && !isDragging)
        {
            // Initial touch event
            initialTouch.set(touchPos.x, touchPos.y);
            isDragging = true;
        }

        if (Gdx.input.isTouched() && isDragging) {
            // Touch-dragged event
            lastTouch.set(touchPos.x, touchPos.y);
        }
        if(!Gdx.input.isTouched() && isDragging) {
            ball.push(initialTouch, lastTouch, gravity);
            isDragging = false;
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
        walls.dispose();
        sb.dispose();
    }
}
