package com.airbound.game.sprites;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Bricks {

    private final Texture texture;
    private List<Brick> bricks;
    private Random rng;

    public Bricks(){
        texture = new Texture("brick.png");
        bricks = new ArrayList<>();
        rng = new Random();

        for(int i=0; i<6; i++)
        {
            bricks.add(createRandomBrick(i*1000));
        }
    }
    public void draw(SpriteBatch sb, float y)
    {
        System.out.println(y);
        for(Brick brick : bricks)
        {
            brick.draw(sb, texture, y);
            if(brick.getY()+brick.getHeight() < -y) // vision of the brick is gone
            {
                Brick newBrick = createRandomBrick(-y+6000);
                brick.replace(newBrick);
            }

        }
    }

    private Brick createRandomBrick(float y) {
        int width = rng.nextInt(400 - 100 + 1) + 100;
        int x = rng.nextInt((880 - width) - 20 + 1) + 20;
        int height = 40;
        int angle = 0;

        return new Brick(x, y, width, height, angle);
    }

    public void collisionCheck(Rectangle ball) {

        for (Brick brick : bricks) {
            if (ball.overlaps(brick.getShape().getBoundingRectangle())) {
                // Check if the center of the ball is above the brick
                float ballCenterY = ball.y + ball.height / 2;
                float brickTopY = brick.getShape().getY() + brick.getHeight();

                if (ballCenterY >= brickTopY) {
                    // Ball is above the brick, handle the collision
                    // Implement logic here to handle the collision with the upper part of the brick, e.g., bounce the ball, remove the brick, etc.
                }
            }
        }
    }

    public void debugRender(Rectangle ball) {
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true); // This sets the shape type to "Filled" by default

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Draw the bricks
        shapeRenderer.setColor(Color.RED); // Set the color for the bricks
        for (Brick brick : bricks) {
            shapeRenderer.rect(brick.getShape().getX(), brick.getShape().getY(), brick.getWidth(), brick.getHeight());
        }

        // Draw the ball
        shapeRenderer.setColor(Color.BLUE); // Set the color for the ball
        shapeRenderer.rect(ball.x, ball.y, ball.width, ball.height);

        // Draw the collided bricks in a different color (if any)
        shapeRenderer.setColor(Color.GREEN);
        for (Brick brick : bricks) {
            shapeRenderer.rect(brick.getShape().getX(), brick.getShape().getY(), brick.getWidth(), brick.getHeight());
        }

        shapeRenderer.end();

        shapeRenderer.dispose(); // Remember to dispose of the ShapeRenderer after using it.
    }
}
