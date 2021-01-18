package com.hardgforgif.dragonboatracing.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.hardgforgif.dragonboatracing.Game;
import com.hardgforgif.dragonboatracing.GameData;
import com.hardgforgif.dragonboatracing.core.Player;

public class PauseUI extends UI {

    private ScrollingBackground scrollingBackground = new ScrollingBackground();
    Game game;

    public PauseUI(Game game) {
        scrollingBackground.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        scrollingBackground.setSpeedFixed(true);
        scrollingBackground.setSpeed(ScrollingBackground.DEFAULT_SPEED);

        UI.Button continueButton = new UI.Button(new Texture("ContinueUnselected.png"), new Texture("ContinueSelected.png"), 0.5f, 0.75f, 0.4f, 0.15f, new ButtonListener() {
            @Override
            public void onClick() {
                // Go back to game
                GameData.pauseState = false;
                GameData.gamePlayState = true;
                GameData.currentUI = new GamePlayUI();
            }
        });
        this.addButton(continueButton);

        UI.Button saveButton = new UI.Button(new Texture("SaveUnselected.png"), new Texture("SaveSelected.png"), 0.5f, 0.5f, 0.4f, 0.15f, new ButtonListener() {
            @Override
            public void onClick() {
                game.save();
                GameData.gamePlayState = false;
                GameData.mainMenuState = true;
                GameData.currentUI = new MenuUI(game);
            }
        });
        this.addButton(saveButton);
    }

    @Override
    public void drawUI(Batch batch, Vector2 mousePos, float screenWidth, float delta) {
        batch.begin();
        scrollingBackground.updateAndRender(delta, batch);
        super.drawButtons(batch, mousePos);
        batch.end();
    }

    @Override
    public void drawPlayerUI(Batch batch, Player playerBoat) {}

    @Override
    public void getInput(float screenWidth, Vector2 mousePos) {
        super.getInput(screenWidth, mousePos);
    }

}
