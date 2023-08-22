package com.airbound.game;

public class GameUtils {
    private GameUtils() {}
    public static boolean InputTouch(float x, float y, float objectSize, float objectX, float objectY)
    {
        return (x > objectX && x < objectX+objectSize && y > objectY && y< objectY+objectSize);
    }
}
