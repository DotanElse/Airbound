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
}
