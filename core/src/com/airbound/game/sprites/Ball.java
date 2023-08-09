package com.airbound.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public class Ball {
    private Texture texture;
    private Rectangle bounds;
    private Vector2 position;
    private Vector2 velocity;
    private float maxPush;
    private float friction;
    private int maxJumps;
    private int highestBrick;
    private static final float BALL_GRAVITY = -900f;

    public Ball(int x, int y){
        position = new Vector2(x, y);
        velocity = new Vector2(0,0);
        texture = new Texture("ball.png");
        bounds = new Rectangle(x, y, 100, 100);
        maxPush = 300;
        friction = (float) 0.02;
        maxJumps = 2;
        highestBrick = 0;
    }

    public void update(float dt, Bricks bricks){
        Vector2 newPosition = new Vector2(position);

        // Apply friction to the velocity

        velocity.scl(1-friction*dt);
        if(!handleBrickCollision(bricks))
            velocity.y += BALL_GRAVITY * dt;
        newPosition.mulAdd(velocity, dt);
        // Set the new position after applying friction
        position.set(newPosition);
        // check wall collision and reverse direction if needed
        handleWallCollision();
        bounds.setPosition(position.x, position.y);
    }

    private void handleWallCollision(){
        if(position.x < 0 || position.x > 900-bounds.width)
            velocity.set(-velocity.x, velocity.y);
    }
    private boolean handleBrickCollision(Bricks bricks) {
        if (velocity.y < 0) {
            int brickCollision = bricks.collisionCheck(bounds);
            if (brickCollision > 0) {
                maxJumps = 2;

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

    public void push(Vector2 initialTouch, Vector2 lastTouch, float gravity){
        if(maxJumps == 0)
            return;
        maxJumps--;
        Vector2 pushVector = initialTouch.sub(lastTouch);
        // Calculate the magnitude of the pushVector
        float magnitude = pushVector.len();
        if (magnitude>maxPush)
        {
            pushVector.scl(maxPush / magnitude);
        }
        pushVector.y += gravity/7;
        velocity.set(pushVector.scl(3));

    }

    public Texture getTexture() {
        return texture;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public int getHighestBrick() {
        return highestBrick;
    }
    public int getMaxJumps() {
        return maxJumps;
    }

    public void dispose(){
        texture.dispose();
    }
}
