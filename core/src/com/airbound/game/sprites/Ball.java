package com.airbound.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import java.util.Random;


public class Ball {
    private Texture texture;
    private Rectangle bounds;
    private Vector2 position;
    private Vector2 velocity;

    public Ball(int x, int y){
        position = new Vector2(x, y);
        velocity = new Vector2(0,0);
        texture = new Texture("ball.png");
        bounds = new Rectangle(x, y, texture.getWidth(), texture.getHeight());

    }

    public void update(float dt){
        position.mulAdd(velocity, dt);
        bounds.setPosition(position.x, position.y);
    }

    public void push(){
        Random random = new Random();

        // Gen a random number between -50 to 50
        int randomNumber1 = random.nextInt(100) - 50;
        int randomNumber2 = random.nextInt(100) - 50;
        velocity.add(randomNumber1, randomNumber2);
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
