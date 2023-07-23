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

    public Ball(int x, int y){
        position = new Vector2(x, y);
        velocity = new Vector2(0,0);
        texture = new Texture("ball.png");
        bounds = new Rectangle(x, y, texture.getWidth(), texture.getHeight());
        maxPush = 300;

    }

    public void update(float dt){
        position.mulAdd(velocity, dt);
        bounds.setPosition(position.x, position.y);
    }

    public void push(Vector2 initialTouch, Vector2 lastTouch){
        Vector2 pushVector = initialTouch.sub(lastTouch);
        System.out.println(pushVector);
        // Calculate the magnitude of the pushVector
        float magnitude = pushVector.len();
        if (magnitude>maxPush)
        {
            pushVector.x *= maxPush/magnitude;
            pushVector.y *= maxPush/magnitude;
        }
        velocity.add(pushVector);

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
