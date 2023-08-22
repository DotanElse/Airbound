package com.airbound.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class PreferencesManager {
    private static final String PREFERENCES_NAME = "AirboundPreferences";
    private static final String HIGH_SCORE = "HighScore";
    private static final String DIFF = "Difficulty";
    private static final String SOUND_ON = "SoundOn";

    private Preferences preferences;

    public PreferencesManager() {
        preferences = Gdx.app.getPreferences(PREFERENCES_NAME);
    }

    public int getHighScore() {
        return preferences.getInteger(HIGH_SCORE, 0);
    }

    public int getDifficulty() { return preferences.getInteger(DIFF, 0);}

    public void setDifficulty(int diff)
    {
        preferences.putInteger(DIFF, diff);
        preferences.flush();
    }

    public void setHighScore(int score) {
        preferences.putInteger(HIGH_SCORE, score);
        preferences.flush();
    }

    public boolean getSoundOn() { return preferences.getBoolean(SOUND_ON, true);}

    public void toggleSound() {
        preferences.putBoolean(SOUND_ON, !getSoundOn());
        preferences.flush();
    }

}

