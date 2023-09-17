package com.airbound.game.sprites;

import com.airbound.game.GameConstants;
import com.airbound.game.screens.GameScreen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;

import java.util.Random;

public class Coin {
    private Polygon shape;
    private float x, y;
    private Texture texture;
    private int coinCounter;
    private Random rng;

    public Coin(float x, float y) {
        this.x = x;
        this.y = y;
        rng = new Random();
        coinCounter = 0;
        texture = new Texture("Misc/coin.png");

        // Create the polygon shape for the coin
        float[] vertices = {
                0, 0,
                GameConstants.COIN_SIZE, 0,
                GameConstants.COIN_SIZE, GameConstants.COIN_SIZE,
                0, GameConstants.COIN_SIZE
        };
        shape = new Polygon(vertices);
        shape.setOrigin(GameConstants.COIN_SIZE / 2, GameConstants.COIN_SIZE / 2);
        shape.setPosition(x, y);
    }

    public boolean update(SpriteBatch batch, float cam, boolean hardcore) {
        if(GameConstants.COIN_SIZE+y < -cam)
        {
            if(hardcore)
                return true;
            replace();
        }
        batch.draw(texture, x, y+cam, GameConstants.COIN_SIZE / 2, GameConstants.COIN_SIZE / 2, GameConstants.COIN_SIZE, GameConstants.COIN_SIZE, 1, 1, 0, 0, 0, texture.getWidth(), texture.getHeight(), false, false);
        return false;
    }

    public Polygon getShape() {
        return shape;
    }

    public void replace()
    {
        int gaps = (int) (GameConstants.GAME_HEIGHT/(GameConstants.BRICK_GAP_SIZE*GameConstants.GAME_SPEED)) + 1;
        System.out.println(gaps);
        y += GameConstants.BRICK_GAP_SIZE*GameConstants.GAME_SPEED * gaps;
        x = rng.nextInt(GameConstants.GAME_WIDTH-2*GameConstants.WALL_SIZE-GameConstants.COIN_SIZE) + GameConstants.WALL_SIZE;
        System.out.println(y);
        shape.setPosition(x, y);
    }

    public int getCoinCounter() {
        return coinCounter;
    }

    public void incCoinCounter() {
        coinCounter++;
    }
}
