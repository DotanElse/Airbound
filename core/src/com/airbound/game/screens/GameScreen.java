package com.airbound.game.screens;

import com.airbound.game.Airbound;
import com.airbound.game.GameConstants;
import com.airbound.game.sprites.Background;
import com.airbound.game.sprites.Ball;
import com.airbound.game.sprites.Bricks;
import com.airbound.game.sprites.Walls;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen {
    private Airbound game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private OrthographicCamera guiCam;
    private Background background;
    private Texture ballPush;
    private Ball ball;
    private Walls walls;
    private Bricks bricks;
    private SpriteBatch sb;
    private Vector2 initialTouch;
    private Vector2 lastTouch;
    private boolean isDragging;
    private float gravity;
    private boolean gameEnded;
    private BitmapFont font;

    public GameScreen(Airbound game) {
        this.game = game;
        sb = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT);
        guiCam = new OrthographicCamera();
        guiCam.setToOrtho(false, GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT);
        viewport = new ExtendViewport(GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT, camera);
        viewport.setCamera(camera);
        gravity = GameConstants.GRAVITY;
        background = new Background();
        ballPush = new Texture("ballPush.png");
        walls = new Walls();
        bricks = new Bricks();
        ball = new Ball(GameConstants.BALL_STARTING_X, GameConstants.BALL_STARTING_Y);
        isDragging = false; //game init with user not dragging
        initialTouch = new Vector2();
        lastTouch = new Vector2();
        gameEnded = false;
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
        camera.position.y -= gravity * delta;
        ball.update(delta, bricks);
        float ballY = camera.position.y + ball.getPosition().y;
        if (ballY < -GameConstants.BALL_SIZE) {
            game.setNewScore(ball.getHighestBrick());
            // The ball is not visible on the screen anymore, show the main menu screen
            game.showMainMenuScreen();
        }
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        background.draw(sb, camera.position.y);
        walls.draw(sb, camera.position.y);
        bricks.draw(sb, camera.position.y);

        sb.draw(ball.getTexture(), ball.getPosition().x, ballY, GameConstants.BALL_SIZE, GameConstants.BALL_SIZE);
        sb.end();
        drawGui();

    }

    public void drawGui()
    {
        sb.setProjectionMatrix(guiCam.combined);
        sb.begin();
        font.draw(sb, "" + ball.getHighestBrick(), GameConstants.WALL_SIZE, GameConstants.GAME_HEIGHT);
        if(ball.getJumpsLeft() >= 1)
        {
            sb.draw(ballPush, GameConstants.GAME_WIDTH-GameConstants.BALL_PUSH_SIZE-30, GameConstants.GAME_HEIGHT-GameConstants.BALL_PUSH_SIZE-10, GameConstants.BALL_PUSH_SIZE, GameConstants.BALL_PUSH_SIZE);
            if(ball.getJumpsLeft() == 2)
                sb.draw(ballPush, GameConstants.GAME_WIDTH-GameConstants.BALL_PUSH_SIZE*2-30, GameConstants.GAME_HEIGHT-GameConstants.BALL_PUSH_SIZE-10, GameConstants.BALL_PUSH_SIZE, GameConstants.BALL_PUSH_SIZE);
        }

        sb.end();
    }

    public void renderFrozenElements() {
        float ballY = camera.position.y + ball.getPosition().y;
        Gdx.gl.glClearColor(1,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        background.draw(sb, camera.position.y);
        walls.draw(sb, camera.position.y);
        bricks.draw(sb, camera.position.y);
        sb.draw(ball.getTexture(), ball.getPosition().x, ballY, GameConstants.BALL_SIZE, GameConstants.BALL_SIZE);
        sb.end();
        drawGui();
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
        // Update the viewport with the new dimensions
        float cameraYBeforeResize = camera.position.y;

        // Update the camera viewport
        viewport.update(width, height, true);

        // Apply the new camera position
        camera.position.set(viewport.getWorldWidth() / 2, cameraYBeforeResize, 0);
//
//        viewport.update(width, height, true);
//
//        // Calculate the ratio of the old height to the new height
//        float heightRatio = (float) height / viewport.getWorldHeight();
//
//        // Update the camera position to maintain the focus on the same point
//        camera.position.y *= heightRatio;
    }

    @Override
    public void pause() {

    }

    public OrthographicCamera getCamera()
    {
        return camera;
    }
    public Viewport getViewport()
    {
        return viewport;
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

    public boolean isGameEnded()
    {
        return gameEnded;
    }
}
