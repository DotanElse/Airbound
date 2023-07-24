package com.airbound.game.sprites;

import com.badlogic.gdx.graphics.Texture;
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


    public Ball(int x, int y){
        position = new Vector2(x, y);
        velocity = new Vector2(0,0);
        texture = new Texture("ball.png");
        bounds = new Rectangle(x, y, 100, 100);
        maxPush = 300;
        friction = (float) 0.985;
        maxJumps = 2;

    }

    public void update(float dt, Bricks bricks){
        Vector2 newPosition = new Vector2(position);

        // Apply friction to the velocity
        velocity.scl(friction);
        newPosition.mulAdd(velocity, dt);
        // Set the new position after applying friction
        position.set(newPosition);
        // check wall collision and reverse direction if needed
        handleWallCollision();
        handleBrickCollision(bricks);
        bounds.setPosition(position.x, position.y);

    }

    private void handleWallCollision(){
        if(position.x < 0 || position.x > 900-bounds.width)
        {
            velocity.set(-velocity.x, velocity.y);
        }
    }
    private void handleBrickCollision(Bricks bricks)
    {
        if(velocity.y < 0)
            if(bricks.collisionCheck(bounds) == 1)
            {
                maxJumps = 2;
                velocity.set(velocity.x, -velocity.y);
            }

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
            pushVector.x *= maxPush/magnitude;
            pushVector.y *= maxPush/magnitude;
        }
        pushVector.y += gravity/7;
        velocity.set(pushVector.scl(4));

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

    public void dispose(){
        texture.dispose();
    }
}
