package com.airbound.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class Wall {
    private final Texture leftTexture;
    private final Texture rightTexture;
    private int parts;
    int[] heights;

    public Wall(){
        leftTexture = new Texture("wall_left.png");
        rightTexture = new Texture("wall_right.png");
        parts = 1600/leftTexture.getHeight()+2;
        heights = new int[parts];
        for (int i = 0; i < parts; i++) {
            heights[i] = (leftTexture.getHeight())*i;
            System.out.println((leftTexture.getHeight())*i);
        }
    }
    public void draw(SpriteBatch sb, float y)
    {

        for(int i=0; i<parts; i++)
        {
            if(-y > heights[i] + (leftTexture.getHeight()))
            {
                System.out.println("Repositioned" + heights[i]);
                heights[i] += parts*(leftTexture.getHeight());
            }
            sb.draw(leftTexture, 0, heights[i]+y, 20, leftTexture.getHeight());
            sb.draw(rightTexture, 880, heights[i]+y, 20, rightTexture.getHeight());
        }
    }

    public void dispose(){
        rightTexture.dispose();
        leftTexture.dispose();
    }
}
