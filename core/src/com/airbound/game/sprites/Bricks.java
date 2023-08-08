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
    private final int MIN_WIDTH = 100;

    public Bricks(){
        texture = new Texture("brickDebug.png");
        bricks = new ArrayList<>();
        rng = new Random();
        Brick firstBrick = new Brick(20, -200, 400, 40, 0, 1);
        bricks.add(firstBrick);
        for(int i=1; i<12; i++)
        {
            bricks.add(createRandomBrick(i*500, i+1));
        }
    }
    public void draw(SpriteBatch sb, float y)
    {
        for(Brick brick : bricks)
        {
            brick.draw(sb, texture, y);
            if(brick.getY()+brick.getHeight() < -y) // vision of the brick is gone
            {
                Brick newBrick = createRandomBrick(-y+6000,  brick.getBrickHeight()+12);
                brick.replace(newBrick);
            }

        }
    }

    private Brick createRandomBrick(float y, int brickHeight) {
        int maxWidth = 300 - brickHeight*3;
        int width = Math.max(rng.nextInt(maxWidth - maxWidth/2 + 1) + maxWidth/2, MIN_WIDTH);
        int x = rng.nextInt((880 - width) - 20 + 1) + 20;
        int height = 40;
        int angle = 40;


        return new Brick(x, y, width, height, angle, brickHeight);
    }

    public int collisionCheck(Rectangle ball) {
        for (Brick brick : bricks) {
            if (Intersector.overlapConvexPolygons(ballVertices(ball), brick.getShape())) {
                System.out.println("Touching");
                float ballCenterY = ball.y + ball.height / 2;
                float brickTopY = brick.getY();

                if (ballCenterY >= brickTopY) {
                    return brick.getBrickHeight();
                }
            }
        }
        System.out.println("Not touching");
        return 0;
    }

    private Polygon ballVertices(Rectangle ball) {
        float[] vertices = {
                ball.x, ball.y,
                ball.x + ball.width, ball.y,
                ball.x + ball.width, ball.y + ball.height,
                ball.x, ball.y + ball.height
        };
        return new Polygon(vertices);
    }


    public float getBrickTopY(int brickHeight) {
        for (Brick brick : bricks) {
            if (brick.getBrickHeight() == brickHeight) {
                return brick.getShape().getY() + brick.getHeight();
            }
        }
        return 0;
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
