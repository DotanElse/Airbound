package com.airbound.game.sprites;

import com.airbound.game.GameConstants;
import com.airbound.game.GameUtils;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
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
    private int blockNum;

    public Bricks(){
        texture = new Texture("Misc/brick.png");
        bricks = new ArrayList<>();
        rng = new Random();
        Brick firstBrick = new Brick(20, 0, 400, GameConstants.BRICK_HEIGHT, 0, 1);
        bricks.add(firstBrick);
        blockNum = MathUtils.ceil((float)GameConstants.GAME_HEIGHT/(GameConstants.BRICK_GAP_SIZE * GameConstants.GAME_SPEED))+1;
        for(int i=1; i<blockNum; i++)
        {
            bricks.add(createRandomBrick(i*(GameConstants.BRICK_GAP_SIZE * GameConstants.GAME_SPEED), i+1));
        }
    }
    public void draw(SpriteBatch sb, float y, boolean fade)
    {
        for(Brick brick : bricks)
        {
            brick.draw(sb, texture, y, fade);
            if(brick.getY()+brick.getHeight() < -y) // vision of the brick is gone
            {
                Brick newBrick = createRandomBrick(-y+(blockNum * (GameConstants.BRICK_GAP_SIZE * GameConstants.GAME_SPEED)),  brick.getBrickHeight()+blockNum); //Could have more precision if not using y values but calc manually.
                brick.replace(newBrick);
            }
        }
    }

    private Brick createRandomBrick(float y, int brickHeight) {
        int maxWidth = GameConstants.BRICK_MAX_WIDTH - brickHeight*3;
        int width = Math.max(rng.nextInt(maxWidth - maxWidth/2 + 1) + maxWidth/2, GameConstants.BRICK_MIN_WIDTH);
        int x = rng.nextInt((GameConstants.GAME_WIDTH-GameConstants.WALL_SIZE - width) - GameConstants.WALL_SIZE + 1) + GameConstants.WALL_SIZE;
        int height = GameConstants.BRICK_HEIGHT;
        boolean orientation = rng.nextBoolean();
        int angle = rng.nextInt(GameConstants.BRICK_MAX_ANGLE);
        if (orientation)
            angle = 180-angle;
        return new Brick(x, y, width, height, angle, brickHeight);
    }

    public int collisionCheck(Rectangle ball) {
        for (Brick brick : bricks) {
            if (Intersector.overlapConvexPolygons(GameUtils.ballVertices(ball), brick.getShape())) {
                float ballCenterY = ball.y + ball.height;
                float brickTopY = brick.getY() + brick.getHeight();

                if (ballCenterY >= brickTopY) {
                    return brick.getBrickHeight();
                }
            }
        }
        return 0;
    }




    public float getBrickAngle(int brickHeight) {
        for (Brick brick : bricks) {
            if (brick.getBrickHeight() == brickHeight) {
                return brick.getAngle();
            }
        }
        return 0;
    }
}
