package com.airbound.game.screens;

import com.airbound.game.Airbound;
import com.airbound.game.GameConstants;
import com.airbound.game.GameUtils;
import com.airbound.game.sprites.Background;
import com.airbound.game.sprites.Ball;
import com.airbound.game.sprites.Bricks;
import com.airbound.game.sprites.Coin;
import com.airbound.game.sprites.Walls;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Random;

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
    private Coin coin;
    private SpriteBatch sb;
    private Vector2 initialTouch;
    private Vector2 lastTouch;
    private boolean isDragging;
    private float gravity;
    private boolean gameEnded;
    private BitmapFont font;
    private Texture pauseButton;
    private boolean fade;
    private boolean hardcore;
    private Sound gameOverSound;
    private  Texture overlayTexture;

    public GameScreen(Airbound game, int difficulty) {
        this.game = game;
        GameUtils.SetGameSpeed(difficulty);
        gameOverSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/gameOver.wav"));
        fade = game.getPreferencesManager().getFade();
        hardcore = game.getPreferencesManager().getHardcore();
        sb = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT);
        guiCam = new OrthographicCamera();
        guiCam.setToOrtho(false, GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT);
        viewport = new ExtendViewport(GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT, camera);
        viewport.setCamera(camera);
        gravity = GameConstants.GRAVITY;
        Random random = new Random();
        int textureNumber = random.nextInt(GameConstants.TEXTURE_NUMBER) + 1;
        walls = new Walls(textureNumber);
        bricks = new Bricks(textureNumber);
        ball = new Ball(game.getPreferencesManager().getSoundOn(), textureNumber);
        background = new Background(textureNumber);
        ballPush = new Texture("Misc/ballPush.png");
        pauseButton = new Texture("Misc/pause.png");
        isDragging = false; //game init with user not dragging
        initialTouch = new Vector2();
        lastTouch = new Vector2();
        coin = new Coin(300, 600+(GameConstants.BRICK_GAP_SIZE*GameConstants.GAME_SPEED));
        gameEnded = false;
        font = new BitmapFont(); // default
        font.getData().setScale(GameConstants.FONT_SCALE);
        font.setColor(Color.WHITE);
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0.5f, 0.5f, 0.5f, 0.3f);
        pixmap.fill();
        overlayTexture = new Texture(pixmap);

    }

    @Override
    public void show() {
        viewport.apply();
    }

    @Override
    public void render(float delta) {
        handleInput();
        camera.position.y -= gravity * delta * GameConstants.GAME_SPEED;
        ball.update(delta, bricks, coin);
        float ballY = camera.position.y + ball.getPosition().y;
        if (ballY < -GameConstants.BALL_SIZE) {
            // The ball is not visible on the screen anymore, show the main menu screen
            gameOver();
        }
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        background.draw(sb, camera.position.y);
        walls.draw(sb, camera.position.y);
        bricks.draw(sb, camera.position.y, fade);
        if(coin.update(sb, camera.position.y, hardcore))
            gameOver();
        ball.draw(sb, camera.position.y);
        sb.end();
        drawGui(false);

    }

    private void gameOver() {
        if(game.getPreferencesManager().getSoundOn())
            gameOverSound.play(GameConstants.SOUND_STRENGTH);
        game.setNewScore((int) (ball.getHighestBrick()*GameUtils.GetGameDiff(game.getPreferencesManager())) + coin.getCoinCounter());
        game.showMainMenuScreen((int) (ball.getHighestBrick()*GameUtils.GetGameDiff(game.getPreferencesManager())) + coin.getCoinCounter());
    }

    public void drawGui(Boolean paused)
    {
        sb.setProjectionMatrix(guiCam.combined);
        sb.begin();
        if(!paused)
            sb.draw(pauseButton, (GameConstants.GAME_WIDTH-GameConstants.PAUSE_BUTTON_SIZE)-GameConstants.WALL_SIZE, GameConstants.GAME_HEIGHT-GameConstants.PAUSE_BUTTON_SIZE, GameConstants.PAUSE_BUTTON_SIZE, GameConstants.PAUSE_BUTTON_SIZE);
        String score = "" + (int) (ball.getHighestBrick()*GameUtils.GetGameDiff(game.getPreferencesManager()));
        if(coin.getCoinCounter() > 0)
            score +=  "+" + coin.getCoinCounter();
        font.draw(sb, score, GameConstants.WALL_SIZE, GameConstants.GAME_HEIGHT);
        if(ball.getJumpsLeft() >= 1)
        {
            sb.draw(ballPush, GameConstants.GAME_WIDTH-GameConstants.BALL_PUSH_SIZE-30, 5, GameConstants.BALL_PUSH_SIZE, GameConstants.BALL_PUSH_SIZE);
            if(ball.getJumpsLeft() == 2)
                sb.draw(ballPush, GameConstants.GAME_WIDTH-GameConstants.BALL_PUSH_SIZE*2-30, 5, GameConstants.BALL_PUSH_SIZE, GameConstants.BALL_PUSH_SIZE);
        }
        sb.end();
    }

    public void renderFrozenElements() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        background.draw(sb, camera.position.y);
        walls.draw(sb, camera.position.y);
        bricks.draw(sb, camera.position.y, fade);
        ball.draw(sb, camera.position.y);
        coin.update(sb, camera.position.y, hardcore);
        sb.draw(overlayTexture, 0, 0, GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT*2);
        sb.end();

        drawGui(true);
    }
    private void handleInput() {
        Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        Vector3 guiTouchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        guiCam.unproject(guiTouchPos);
        if (Gdx.input.justTouched()) {
            if(GameUtils.InputTouch(guiTouchPos.x, guiTouchPos.y, GameConstants.PAUSE_BUTTON_SIZE, (GameConstants.GAME_WIDTH-GameConstants.PAUSE_BUTTON_SIZE)-GameConstants.WALL_SIZE, GameConstants.GAME_HEIGHT-GameConstants.PAUSE_BUTTON_SIZE))
            {
                game.pause();
                return;
            }
        }
        camera.unproject(touchPos);

        if (Gdx.input.justTouched() && !isDragging) {
            // Initial touch event
                initialTouch.set(touchPos.x, touchPos.y);
                isDragging = true;
        }

        if (Gdx.input.isTouched() && isDragging) {
            // Touch-dragged event
            lastTouch.set(touchPos.x, touchPos.y);
        }

        if (!Gdx.input.isTouched() && isDragging) {
            // Lift event
            ball.push(initialTouch, lastTouch);
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
        gameOverSound.dispose();
    }

    public boolean isGameEnded()
    {
        return gameEnded;
    }
}
