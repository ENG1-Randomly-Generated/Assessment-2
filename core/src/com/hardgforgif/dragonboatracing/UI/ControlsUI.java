package com.hardgforgif.dragonboatracing.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.hardgforgif.dragonboatracing.GameData;
import com.hardgforgif.dragonboatracing.core.Player;

public class ControlsUI extends UI {

    private BitmapFont titleFont;
    private BitmapFont font;
    private ScrollingBackground background;

    private GlyphLayout titleText;
    private GlyphLayout wasdKeys;
    private GlyphLayout clickToContinue;

    public ControlsUI() {
        this.titleFont = new BitmapFont();
        this.font = new BitmapFont();
        this.background = new ScrollingBackground();
        this.background.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.background.setSpeedFixed(true);
        this.background.setSpeed(ScrollingBackground.DEFAULT_SPEED);

        this.titleFont.getData().setScale(3f);
        this.titleFont.setColor(Color.WHITE);
        this.font.getData().setScale(1.5f);
        this.font.setColor(Color.WHITE);

        this.titleText = this.registerText(this.titleFont, "CONTROLS");
        this.wasdKeys = this.registerText(this.font, "Use WASD to move the boat");
        this.clickToContinue = this.registerText(this.font, "Click to start the game");
    }


    @Override
    public void drawUI(Batch batch, Vector2 mousePos, float screenWidth, float delta) {
        batch.begin();

        this.background.updateAndRender(delta, batch);
        this.titleFont.draw(batch, titleText, Gdx.graphics.getWidth() * 0.5f - titleText.width/2, Gdx.graphics.getHeight() * 0.85f);
        this.font.draw(batch, wasdKeys, Gdx.graphics.getWidth() * 0.5f - wasdKeys.width/2, Gdx.graphics.getHeight() * 0.6f);
        this.font.draw(batch, clickToContinue, Gdx.graphics.getWidth() * 0.5f - clickToContinue.width/2, Gdx.graphics.getHeight() * 0.4f);

        batch.end();
    }

    @Override
    public void getInput(float screenWidth, Vector2 mousePos) {

        if (!Gdx.input.justTouched()) return;

        // Change the music
        GameData.music.stop();
        GameData.music = Gdx.audio.newMusic(Gdx.files.internal("Love_Drama.ogg"));

        // Set the game state to the game play state
        GameData.choosingDifficultyState = false;
        GameData.gamePlayState = true;
        GameData.currentUI = new GamePlayUI();
    }

    @Override
    public void drawPlayerUI(Batch batch, Player playerBoat) {
    }
}
