package com.airbound.game;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;


public class GameUtils {
    private GameUtils() {}
    public static boolean InputTouch(float x, float y, float objectSize, float objectX, float objectY)
    {
        return (x > objectX && x < objectX+objectSize && y > objectY && y< objectY+objectSize);
    }
    public static Polygon ballVertices(Rectangle ball) {
        float[] vertices = {
                ball.x, ball.y,
                ball.x + ball.width, ball.y,
                ball.x + ball.width, ball.y + ball.height,
                ball.x, ball.y + ball.height
        };
        return new Polygon(vertices);
    }

    public static void SetGameSpeed(int difficulty) {
        float speed;
        speed = 0.7f + (difficulty * 0.3f);
        GameConstants.GAME_SPEED = Math.round(speed * 10.0f) / 10.0f;
    }

    public static float GetGameDiff(PreferencesManager preferencesManager) {
        float diff = GameConstants.GAME_SPEED;
        if(diff > 1)
            diff = 1 + (diff-1)/3f;
        if (preferencesManager.getHardcore())
            diff*=1.5;
        if (preferencesManager.getFade())
            diff*=1.2;
        return Math.round(diff * 100.0f) / 100.0f;
    }
}
