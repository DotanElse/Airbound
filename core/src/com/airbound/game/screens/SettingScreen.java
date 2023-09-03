package com.airbound.game.screens;

import com.airbound.game.Airbound;
import com.airbound.game.GameConstants;
import com.airbound.game.GameUtils;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class SettingScreen implements Screen {
    private Airbound game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Texture background;
    private SpriteBatch sb;
    private Texture returnButton;
    private TextureRegion diffButton;
    private BitmapFont font;
    private Vector2[] buttonPositions;
    GlyphLayout layout;


    public SettingScreen(Airbound game) {
        this.game = game;
        sb = new SpriteBatch();
        buttonPositions = new Vector2[ButtonType.values().length];
        initializeButtonPositions();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT);
        viewport = new ExtendViewport(GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT, camera);
        viewport.setCamera(camera);
        background = new Texture("background.png");
        returnButton = new Texture("buttonDebug.png");
        diffButton = new TextureRegion(new Texture("buttonDebug.png"));
        font = new BitmapFont(); // default
        font.getData().setScale(GameConstants.FONT_SCALE);
        font.setColor(Color.WHITE);
        layout = new GlyphLayout(); // Used to calculate text dimensions
    }

    public enum ButtonType {
        RETURN,
        SPEED1,
        SPEED2,
        SPEED3,
        SPEED4,
        HARDCORE,
        FADE,
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
        for(int i=0; i<6; i++)
        {
            sb.draw(background, 0, (background.getHeight())*i-80, GameConstants.GAME_WIDTH, background.getHeight());
        }

        String scoreMultiplierText = getScoreMultiplier();
        layout.setText(font, scoreMultiplierText);
        font.draw(sb, scoreMultiplierText, (GameConstants.GAME_WIDTH-layout.width)/2, GameConstants.GAME_HEIGHT-3*layout.height);
        for (ButtonType buttonType : ButtonType.values()) {
            Vector2 position = buttonPositions[buttonType.ordinal()];
            if(buttonType.ordinal() == game.getPreferencesManager().getDifficulty() ||
                    (buttonType == ButtonType.FADE && game.getPreferencesManager().getFade()) ||
                    (buttonType == ButtonType.HARDCORE && game.getPreferencesManager().getHardcore())
            )
            {
                sb.draw(diffButton, position.x, position.y, GameConstants.DIFFICULTY_BUTTON_SIZE / 2, GameConstants.DIFFICULTY_BUTTON_SIZE / 2,
                        GameConstants.DIFFICULTY_BUTTON_SIZE, GameConstants.DIFFICULTY_BUTTON_SIZE, 1, 1, -20);
            }
            else
                sb.draw(diffButton, position.x, position.y, GameConstants.DIFFICULTY_BUTTON_SIZE, GameConstants.DIFFICULTY_BUTTON_SIZE);
        }

        String modifiersText = "Modifiers:";
        layout.setText(font, modifiersText);
        font.draw(sb, modifiersText, (GameConstants.GAME_WIDTH-layout.width)/2, GameConstants.GAME_HEIGHT - 6f * GameConstants.DIFFICULTY_BUTTON_SIZE);

        sb.end();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            if (GameUtils.InputTouch(touchPos.x, touchPos.y, GameConstants.RETURN_BUTTON_SIZE, 0, GameConstants.GAME_HEIGHT-GameConstants.RETURN_BUTTON_SIZE))
                game.showMainMenuScreen(0);
            for (ButtonType buttonType : ButtonType.values()) {
                Vector2 buttonPosition = buttonPositions[buttonType.ordinal()];
                if (buttonPosition != null) {
                    if (GameUtils.InputTouch(touchPos.x, touchPos.y, GameConstants.DIFFICULTY_BUTTON_SIZE, buttonPosition.x, buttonPosition.y)) {
                        System.out.println(game.getPreferencesManager().getHardcore());
                        System.out.println(game.getPreferencesManager().getFade());
                        setDifficultyForButtonType(buttonType);
                    }
                }
            }
//            if (touchPos.x <= GameConstants.RETURN_BUTTON_SIZE && touchPos.y >= GameConstants.GAME_HEIGHT-GameConstants.RETURN_BUTTON_SIZE)
        }
    }

    private String getScoreMultiplier() {
        String res = "Score multiplier: ";
        float diff = GameUtils.GetGameDiff(game.getPreferencesManager());
        res+= diff;
        return res;
    }

    private void setDifficultyForButtonType(ButtonType buttonType) {
        switch (buttonType) {
            case SPEED1:
                game.getPreferencesManager().setDifficulty(1);
                GameUtils.SetGameSpeed(1);
                break;
            case SPEED2:
                game.getPreferencesManager().setDifficulty(2);
                GameUtils.SetGameSpeed(2);
                break;
            case SPEED3:
                game.getPreferencesManager().setDifficulty(3);
                GameUtils.SetGameSpeed(3);
                break;
            case SPEED4:
                game.getPreferencesManager().setDifficulty(4);
                GameUtils.SetGameSpeed(4);
                break;
            case HARDCORE:
                game.getPreferencesManager().toggleHardcore();
                break;
            case FADE:
                game.getPreferencesManager().toggleFade();
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

    }
}
