package com.airbound.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class Walls {
    private final Texture leftTexture;
    private final Texture rightTexture;
    private int parts;
    private int[] heights;

    public Walls(){
        leftTexture = new Texture("wall_left.png");
        rightTexture = new Texture("wall_right.png");
        parts = 1600/leftTexture.getHeight()+2;
        heights = new int[parts];
        for (int i = 0; i < parts; i++) {
            heights[i] = (leftTexture.getHeight())*i-900;
        }
    }
    public void draw(SpriteBatch sb, float y)
    {

        for(int i=0; i<parts; i++)
        {
            if(-y > heights[i] + (leftTexture.getHeight()))
                heights[i] += parts*(leftTexture.getHeight());
            sb.draw(leftTexture, 0, heights[i]+y, 20, leftTexture.getHeight());
            sb.draw(rightTexture, 880, heights[i]+y, 20, rightTexture.getHeight());
        }
    }

    public void dispose(){
        rightTexture.dispose();
        leftTexture.dispose();
    }
}
