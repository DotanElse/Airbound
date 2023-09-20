package com.airbound.game.sprites;

import com.airbound.game.GameConstants;
import com.airbound.game.GameUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public class Ball {
    private TextureRegion texture;
    private Rectangle bounds;
    private Vector2 position;
    private Vector2 velocity;
    private int highestBrick;
    private int jumpsLeft;
    private float rotationAngle;
    private boolean soundOn;
    private Sound pushSound;
    private Sound collisionSound;
    private Sound coinPickup;
    private Sound wallCollision;
    private float collisionSoundTimer = 0f;
    private float wallCollisionSoundTimer = 0f;


    public Ball(boolean soundOn, int ballTextureNumber){
        position = new Vector2(GameConstants.BALL_STARTING_X, GameConstants.BALL_STARTING_Y);
        this.soundOn = soundOn;
        velocity = new Vector2(0,0);
        texture = new TextureRegion(new Texture("balls/ball" + ballTextureNumber + ".png"));
        bounds = new Rectangle(position.x, position.y, GameConstants.BALL_SIZE, GameConstants.BALL_SIZE);
        highestBrick = 0;
        jumpsLeft = 2;
        rotationAngle = 0;
        pushSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/push.wav"));
        collisionSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/brickCollision.wav"));
        wallCollision = Gdx.audio.newSound(Gdx.files.internal("Sounds/wallCollision.wav"));
        coinPickup = Gdx.audio.newSound(Gdx.files.internal("Sounds/coinPickup.wav"));
    }

    public void update(float dt, Bricks bricks, Coin coin){
        Vector2 newPosition = new Vector2(position);
        collisionSoundTimer+=dt;
        wallCollisionSoundTimer+=dt;
        // Apply friction to the velocity

        velocity.scl(1-GameConstants.BALL_FRICTION*dt*GameConstants.GAME_SPEED);
        if(!handleBrickCollision(bricks))
            velocity.y -= (GameConstants.BALL_GRAVITY_FACTOR * GameConstants.GRAVITY * GameConstants.GAME_SPEED * dt);
        newPosition.mulAdd(velocity, dt);
        // Set the new position after applying friction
        position.set(newPosition);
        // check wall collision and reverse direction if needed
        handleWallCollision();
        handleCoinCollision(coin);
        rotationAngle -= velocity.x * GameConstants.BALL_SPIN;
        bounds.setPosition(position.x, position.y);
    }

    private void handleCoinCollision(Coin coin) {
        if(Intersector.overlapConvexPolygons(GameUtils.ballVertices(bounds), coin.getShape()))
        {
            if(soundOn)
                coinPickup.play(GameConstants.SOUND_STRENGTH/7f);
            coin.incCoinCounter();
            coin.replace();
        }
    }

    private void handleWallCollision(){
        if(position.x < GameConstants.WALL_SIZE || position.x+bounds.width > GameConstants.GAME_WIDTH - GameConstants.WALL_SIZE)
        {
            if(position.x > GameConstants.GAME_WIDTH/2f && velocity.x > 0 || position.x < GameConstants.GAME_WIDTH/2f && velocity.x < 0)
            {
                if(soundOn)
                {
                    if(wallCollisionSoundTimer > GameConstants.WALL_SOUND_CD)
                    {
                        wallCollision.play(GameConstants.SOUND_STRENGTH/4f);
                        wallCollisionSoundTimer = 0;
                    }
                }
                velocity.set(-velocity.x, velocity.y);
            }
        }
    }
    private boolean handleBrickCollision(Bricks bricks) {
        if (velocity.y < 0) {
            int brickCollision = bricks.collisionCheck(bounds);
            if (brickCollision > 0) {
                jumpsLeft = GameConstants.MAX_JUMPS;
                if(soundOn)
                    if(collisionSoundTimer > GameConstants.BRICK_SOUND_CD)
                    {
                        collisionSound.play(GameConstants.SOUND_STRENGTH/1.5f);
                        collisionSoundTimer = 0;
                    }

                // Get the brick's angle in radians
                float brickAngleRadians = (float) Math.toRadians(bricks.getBrickAngle(brickCollision));

                // Calculate new velocity direction based on brick's angle
                float currentSpeed = velocity.len();
                float newAngle = brickAngleRadians * 2 - velocity.angleRad(); // Reflect angle
                Vector2 newVelocity = new Vector2(MathUtils.cos(newAngle), MathUtils.sin(newAngle)).nor().scl(currentSpeed);

                velocity.set(newVelocity);

                highestBrick = Math.max(highestBrick, brickCollision);
                return true;
            }
        }
        return false;
    }

    public void push(Vector2 initialTouch, Vector2 lastTouch){
        if(jumpsLeft == 0)
            return;
        jumpsLeft--;
        if(soundOn)
            pushSound.play(GameConstants.SOUND_STRENGTH/10);
        Vector2 pushVector = initialTouch.sub(lastTouch);
        // Calculate the magnitude of the pushVector
        float magnitude = pushVector.len();
        if (magnitude>GameConstants.MAX_PUSH_STRENGTH)
        {
            pushVector.scl(GameConstants.MAX_PUSH_STRENGTH / magnitude);
        }
        pushVector.y += GameConstants.GRAVITY/GameConstants.BALL_GRAVITY_NEGATION*GameConstants.GAME_SPEED;
        velocity.set(pushVector.scl(GameConstants.BALL_GRAVITY_SCALE*GameConstants.GAME_SPEED));
    }
    public void draw(SpriteBatch sb, float y) {
        sb.draw(texture, position.x, y + position.y, GameConstants.BALL_SIZE/2, GameConstants.BALL_SIZE/2, GameConstants.BALL_SIZE, GameConstants.BALL_SIZE, 1.0f, 1.0f, rotationAngle);
    }

    public TextureRegion getTexture() {
        return texture;
    }

    public Vector2 getPosition() {
        return position;
    }

    public int getHighestBrick() {
        return highestBrick;
    }
    public int getJumpsLeft() {
        return jumpsLeft;
    }

    public void dispose(){
        texture.getTexture().dispose();
    }

}
