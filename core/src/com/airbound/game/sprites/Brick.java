package com.airbound.game.sprites;

import com.airbound.game.GameConstants;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;

public class Brick {
    private Polygon shape;
    private float x, y;
    private float width, height;
    private float angle; // Angle in degrees
    private int brickHeight;

    public Brick(float x, float y, float width, float height, float angle, int brickHeight) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.angle = angle;
        this.brickHeight = brickHeight;

        // Create the polygon shape for the brick
        float[] vertices = {
                0, 0,
                width, 0,
                width, height,
                0, height
        };
        shape = new Polygon(vertices);
        shape.setOrigin(width / 2, height / 2);
        shape.setRotation(angle);
        shape.setPosition(x, y);
    }

    public void draw(SpriteBatch batch, Texture texture, float cam, boolean fade) {
        if(fade)
        {
            float opacity = ((y+cam) /GameConstants.GAME_HEIGHT)/2f;
            if(opacity < GameConstants.BRICK_MIN_OPACITY)
                opacity = 0;
            batch.setColor(1, 1, 1, opacity);
        }
        batch.draw(texture, x, y+cam, width / 2, height / 2, width, height, 1, 1, angle, 0, 0, texture.getWidth(), texture.getHeight(), false, angle>90);
        batch.setColor(1, 1, 1, 1);
    }

    public Polygon getShape() {
        return shape;
    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

    public float getY() {
        return y;
    }

    public void setPosition(float newX, float newY) {
        x = newX;
        y = newY;
    }

    public void replace(Brick newBrick)
    {
        x = newBrick.x;
        y = newBrick.y;
        width = newBrick.width;
        height = newBrick.height;
        angle = newBrick.angle;
        brickHeight = newBrick.brickHeight;
        float[] vertices = {
                0, 0,
                newBrick.width, 0,
                newBrick.width, newBrick.height,
                0, newBrick.height
        };
        shape = new Polygon(vertices);
        shape.setOrigin(width / 2, height / 2);
        shape.setRotation(angle);
        shape.setPosition(x, y);
    }

    public int getBrickHeight() {
        return brickHeight;
    }

    public float getAngle() {
        return angle;
    }
}