package com.airbound.game.screens;

import com.airbound.game.Airbound;
import com.airbound.game.GameConstants;
import com.airbound.game.GameUtils;
import com.airbound.game.PreferencesManager;
import com.airbound.game.sprites.Background;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class SettingScreen implements Screen {
    private Airbound game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Background background;
    private Texture table;
    private SpriteBatch sb;
    private Vector2[] buttonPositions;
    private TextureRegion[] textureRegions;
    private TextureRegion soundOff;
    private GlyphLayout layout;
    private Sound settingChange;
    private PreferencesManager preferencesManager;
    private FreeTypeFontGenerator fontGenerator;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
    private BitmapFont scoreMultiplierFont;
    private BitmapFont modifiersFont;


    public SettingScreen(Airbound game) {
        this.game = game;
        preferencesManager = game.getPreferencesManager();
        sb = new SpriteBatch();
        settingChange = Gdx.audio.newSound(Gdx.files.internal("Sounds/settingChange.wav"));
        buttonPositions = new Vector2[ButtonType.values().length];
        textureRegions = new TextureRegion[ButtonType.values().length];
        soundOff = new TextureRegion(new Texture("Misc/soundOff.png"));
        table = new Texture("Misc/table.png");
        initializeButtonPositions();
        initializeButtonTextures();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT);
        viewport = new ExtendViewport(GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT, camera);
        viewport.setCamera(camera);
        background = new Background(0);
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("Misc/myfont.otf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.color = Color.WHITE;

        // Generate the fonts with different sizes
        fontParameter.size = 60;
        scoreMultiplierFont = fontGenerator.generateFont(fontParameter);

        fontParameter.size = 90;
        modifiersFont = fontGenerator.generateFont(fontParameter);

        layout = new GlyphLayout();
    }

    public enum ButtonType {
        RETURN,
        SPEED1,
        SPEED2,
        SPEED3,
        SPEED4,
        HARDCORE,
        FADE,
        SOUND,
    }

    @Override
    public void show() {

    }

    public void render(float delta) {
        handleInput();
        Gdx.gl.glClearColor(1,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        background.draw(sb, 0);
        sb.draw(table, (GameConstants.GAME_WIDTH-500)/2f, GameConstants.GAME_HEIGHT-GameConstants.RETURN_BUTTON_SIZE, 500, 100);

        String scoreMultiplierText = getScoreMultiplier();
        layout.setText(scoreMultiplierFont, scoreMultiplierText);
        scoreMultiplierFont.draw(sb, scoreMultiplierText, (GameConstants.GAME_WIDTH-layout.width)/2, GameConstants.GAME_HEIGHT-GameConstants.RETURN_BUTTON_SIZE/2f);
        for (ButtonType buttonType : ButtonType.values()) {
            Vector2 position = buttonPositions[buttonType.ordinal()];
            if(buttonType.ordinal() == preferencesManager.getDifficulty() ||
                    (buttonType == ButtonType.FADE && preferencesManager.getFade()) ||
                    (buttonType == ButtonType.HARDCORE && preferencesManager.getHardcore())
            )
            {
                sb.draw(textureRegions[buttonType.ordinal()], position.x, position.y, GameConstants.DIFFICULTY_BUTTON_SIZE / 2f, GameConstants.DIFFICULTY_BUTTON_SIZE / 2f,
                        GameConstants.DIFFICULTY_BUTTON_SIZE, GameConstants.DIFFICULTY_BUTTON_SIZE, 1.3f, 1.3f, -20);
            }
            else
            {
                sb.draw(textureRegions[buttonType.ordinal()], position.x, position.y, GameConstants.DIFFICULTY_BUTTON_SIZE, GameConstants.DIFFICULTY_BUTTON_SIZE);
                if(buttonType == ButtonType.SOUND && !preferencesManager.getSoundOn())
                {
                    sb.draw(soundOff, (position.x+GameConstants.SOUND_TOGGLE_BUTTON_SIZE+10), (position.y+GameConstants.SOUND_TOGGLE_BUTTON_SIZE), GameConstants.SOUND_TOGGLE_BUTTON_SIZE, GameConstants.SOUND_TOGGLE_BUTTON_SIZE);
                }

            }
        }
        sb.draw(table, (GameConstants.GAME_WIDTH-500)/2f, GameConstants.GAME_HEIGHT - 6.5f * GameConstants.DIFFICULTY_BUTTON_SIZE, 500, 100);
        String modifiersText = "Modifiers:";
        layout.setText(modifiersFont, modifiersText);
        modifiersFont.draw(sb, modifiersText, (GameConstants.GAME_WIDTH-layout.width)/2, GameConstants.GAME_HEIGHT - 6f * GameConstants.DIFFICULTY_BUTTON_SIZE);
        String modifiersDescription = "";
        if(preferencesManager.getHardcore())
            modifiersDescription += "Miss a coin and fail. \n";
        if(preferencesManager.getFade())
            modifiersDescription += "Blocks are now fading.";
        layout.setText(scoreMultiplierFont, modifiersDescription);
        scoreMultiplierFont.draw(sb, modifiersDescription, (GameConstants.GAME_WIDTH-layout.width)/2, GameConstants.GAME_HEIGHT - 9f * GameConstants.DIFFICULTY_BUTTON_SIZE);

        sb.end();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            if (GameUtils.InputTouch(touchPos.x, touchPos.y, GameConstants.RETURN_BUTTON_SIZE, 0, GameConstants.GAME_HEIGHT-GameConstants.RETURN_BUTTON_SIZE))
                game.showMainMenuScreen(0);
            if (GameUtils.InputTouch(touchPos.x, touchPos.y, GameConstants.DIFFICULTY_BUTTON_SIZE, GameConstants.GAME_WIDTH-GameConstants.DIFFICULTY_BUTTON_SIZE, GameConstants.GAME_HEIGHT-GameConstants.DIFFICULTY_BUTTON_SIZE))
                game.toggleSound();
            for (ButtonType buttonType : ButtonType.values()) {
                Vector2 buttonPosition = buttonPositions[buttonType.ordinal()];
                if (buttonPosition != null) {
                    if (GameUtils.InputTouch(touchPos.x, touchPos.y, GameConstants.DIFFICULTY_BUTTON_SIZE, buttonPosition.x, buttonPosition.y)) {
                        if(preferencesManager.getSoundOn())
                            settingChange.play(GameConstants.SOUND_STRENGTH);
                        setDifficultyForButtonType(buttonType);
                    }
                }
            }
        }
    }

    private String getScoreMultiplier() {
        String res = "Score multiplier: ";
        float diff = GameUtils.GetGameDiff(preferencesManager);
        res+= diff;
        return res;
    }

    private void setDifficultyForButtonType(ButtonType buttonType) {
        switch (buttonType) {
            case SPEED1:
                preferencesManager.setDifficulty(1);
                GameUtils.SetGameSpeed(1);
                break;
            case SPEED2:
                preferencesManager.setDifficulty(2);
                GameUtils.SetGameSpeed(2);
                break;
            case SPEED3:
                preferencesManager.setDifficulty(3);
                GameUtils.SetGameSpeed(3);
                break;
            case SPEED4:
                preferencesManager.setDifficulty(4);
                GameUtils.SetGameSpeed(4);
                break;
            case HARDCORE:
                preferencesManager.toggleHardcore();
                break;
            case FADE:
                preferencesManager.toggleFade();
                break;
            default:
                break;
        }
    }

    private void initializeButtonPositions() {
        buttonPositions[0] = new Vector2(0, GameConstants.GAME_HEIGHT - GameConstants.RETURN_BUTTON_SIZE);
        buttonPositions[1] = new Vector2((GameConstants.DIFFICULTY_BUTTON_SIZE * 1.5f), GameConstants.GAME_HEIGHT - 3 * GameConstants.DIFFICULTY_BUTTON_SIZE);
        buttonPositions[2] = new Vector2((GameConstants.DIFFICULTY_BUTTON_SIZE * 3.5f), GameConstants.GAME_HEIGHT - 3 * GameConstants.DIFFICULTY_BUTTON_SIZE);
        buttonPositions[3] = new Vector2((GameConstants.DIFFICULTY_BUTTON_SIZE * 1.5f), GameConstants.GAME_HEIGHT - 5 * GameConstants.DIFFICULTY_BUTTON_SIZE);
        buttonPositions[4] = new Vector2((GameConstants.DIFFICULTY_BUTTON_SIZE * 3.5f), GameConstants.GAME_HEIGHT - 5 * GameConstants.DIFFICULTY_BUTTON_SIZE);
        buttonPositions[5] = new Vector2((GameConstants.DIFFICULTY_BUTTON_SIZE * 1.5f), GameConstants.GAME_HEIGHT - 8 * GameConstants.DIFFICULTY_BUTTON_SIZE);
        buttonPositions[6] = new Vector2((GameConstants.DIFFICULTY_BUTTON_SIZE * 3.5f), GameConstants.GAME_HEIGHT - 8 * GameConstants.DIFFICULTY_BUTTON_SIZE);
        buttonPositions[7] = new Vector2(GameConstants.GAME_WIDTH-GameConstants.DIFFICULTY_BUTTON_SIZE, GameConstants.GAME_HEIGHT-GameConstants.DIFFICULTY_BUTTON_SIZE);
    }

    private void initializeButtonTextures() {
        textureRegions[0] = new TextureRegion(new Texture("Misc/return.png"));
        textureRegions[1] = new TextureRegion(new Texture("Misc/diff1.png"));
        textureRegions[2] = new TextureRegion(new Texture("Misc/diff2.png"));
        textureRegions[3] = new TextureRegion(new Texture("Misc/diff3.png"));
        textureRegions[4] = new TextureRegion(new Texture("Misc/diff4.png"));
        textureRegions[5] = new TextureRegion(new Texture("Misc/hardcore.png"));
        textureRegions[6] = new TextureRegion(new Texture("Misc/fade.png"));
        textureRegions[7] = new TextureRegion(new Texture("Misc/soundOn.png"));
    }


    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        fontGenerator.dispose();
    }
}
