package com.airbound.game.sprites;

import com.airbound.game.GameConstants;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Background {
    private final Texture background;
    private int parts;
    private int[] heights;

    public Background(int backgroundTextureNumber){
        background = new Texture("backgrounds/background" + backgroundTextureNumber + ".png");
        parts = GameConstants.GAME_HEIGHT/background.getHeight()+2;
        heights = new int[parts];
        for (int i = 0; i < parts; i++) {
            heights[i] = (background.getHeight())*i- GameConstants.GAME_HEIGHT;
        }
    }
    public void draw(SpriteBatch sb, float y)
    {

        for(int i=0; i<parts; i++)
        {
            if(-y > heights[i] + (background.getHeight()))
                heights[i] += parts*(background.getHeight());
            sb.draw(background, 0, heights[i]+y, GameConstants.GAME_WIDTH, background.getHeight());
        }
    }

    public void dispose(){
        background.dispose();
    }

}
