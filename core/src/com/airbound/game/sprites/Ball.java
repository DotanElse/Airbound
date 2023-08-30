package com.airbound.game.sprites;

import com.airbound.game.GameConstants;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
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

    public Ball(int x, int y){
        position = new Vector2(x, y);
        velocity = new Vector2(0,0);
        texture = new TextureRegion(new Texture("ball.png"));
        bounds = new Rectangle(x, y, GameConstants.BALL_SIZE, GameConstants.BALL_SIZE);
        highestBrick = 0;
        jumpsLeft = 2;
        rotationAngle = 0;
    }

    public void update(float dt, Bricks bricks){
        Vector2 newPosition = new Vector2(position);

        // Apply friction to the velocity

        velocity.scl(1-GameConstants.BALL_FRICTION*dt*GameConstants.GAME_SPEED);
        if(!handleBrickCollision(bricks))
            velocity.y -= (GameConstants.BALL_GRAVITY_FACTOR * GameConstants.GRAVITY * GameConstants.GAME_SPEED * dt);
        newPosition.mulAdd(velocity, dt);
        // Set the new position after applying friction
        position.set(newPosition);
        // check wall collision and reverse direction if needed
        handleWallCollision();
        rotationAngle -= velocity.x * GameConstants.BALL_SPIN  * GameConstants.GAME_SPEED;
        bounds.setPosition(position.x, position.y);
    }

    private void handleWallCollision(){
        if(position.x < GameConstants.WALL_SIZE || position.x+bounds.width > GameConstants.GAME_WIDTH - GameConstants.WALL_SIZE)
            velocity.set(-velocity.x, velocity.y);
    }
    private boolean handleBrickCollision(Bricks bricks) {
        if (velocity.y < 0) {
            int brickCollision = bricks.collisionCheck(bounds);
            if (brickCollision > 0) {
                jumpsLeft = GameConstants.MAX_JUMPS;

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
