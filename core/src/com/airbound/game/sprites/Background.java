package com.airbound.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Background {
    private final Texture background;
    private int parts;
    private int[] heights;

    public Background(){
        background = new Texture("background.png");
        parts = 1600/background.getHeight()+2;
        heights = new int[parts];
        for (int i = 0; i < parts; i++) {
            heights[i] = (background.getHeight())*i-900;
        }
    }
    public void draw(SpriteBatch sb, float y)
    {

        for(int i=0; i<parts; i++)
        {
            if(-y > heights[i] + (background.getHeight()))
                heights[i] += parts*(background.getHeight());
            sb.draw(background, 0, heights[i]+y, 900, background.getHeight());
        }
    }

    public void dispose(){
        background.dispose();
    }

}
