package com.hardgforgif.dragonboatracing.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.hardgforgif.dragonboatracing.Game;
import com.hardgforgif.dragonboatracing.GameData;
import com.hardgforgif.dragonboatracing.core.Player;

public class MenuUI extends UI {

    //Sets the dimensions for all the UI components
    private static final int LOGO_WIDTH = 400;
    private static final int LOGO_HEIGHT = 200;
    private static final int LOGO_Y = 450;

    private static final int PLAY_BUTTON_WIDTH = 300;
    private static final int PLAY_BUTTON_HEIGHT = 120;
    private static final int PLAY_BUTTON_Y = 230;

    private static final int EXIT_BUTTON_WIDTH = 250;
    private static final int EXIT_BUTTON_HEIGHT = 120;
    private static final int EXIT_BUTTON_Y = 100;

    Texture playButtonActive;
    Texture playButtonInactive;
    Texture exitButtonActive;
    Texture exitButtonInactive;
    Texture logo;

    ScrollingBackground scrollingBackground = new ScrollingBackground();


    public MenuUI(Game game){
        scrollingBackground.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        scrollingBackground.setSpeedFixed(true);
        scrollingBackground.setSpeed(ScrollingBackground.DEFAULT_SPEED);

        logo = new Texture("Title.png");

        UI.Button play = new UI.Button(new Texture("PlayUnselected.png"), new Texture("PlaySelected.png"), 0.5f, 0.5f, 0.3f, 0.15f, new ButtonListener() {
            @Override
            public void onClick() {
                GameData.mainMenuState = false;
                GameData.choosingBoatState = true;
                GameData.currentUI = new ChoosingUI();
            }
        });
        this.addButton(play);

        UI.Button load = new UI.Button(new Texture("LoadUnselected.png"), new Texture("LoadSelected.png"), 0.5f, 0.34f, 0.3f, 0.15f, new ButtonListener() {
            @Override
            public void onClick() {
                game.load();
            }
        });
        this.addButton(load);

        UI.Button exit = new UI.Button(new Texture("ExitUnselected.png"), new Texture("ExitSelected.png"), 0.5f, 0.18f, 0.3f, 0.15f, new ButtonListener() {
            @Override
            public void onClick() {
                Gdx.app.exit();
            }
        });
        this.addButton(exit);

    }

    @Override
    public void drawUI(Batch batch, Vector2 mousePos, float screenWidth, float delta) {
        batch.begin();
            scrollingBackground.updateAndRender(delta, batch);
            this.drawButtons(batch, mousePos);
            batch.draw(logo, screenWidth / 2 - LOGO_WIDTH / 2, LOGO_Y, LOGO_WIDTH, LOGO_HEIGHT);
        batch.end();

        playMusic();
    }

    @Override
    public void drawPlayerUI(Batch batch, Player playerBoat) {

    }

    @Override
    public void getInput(float screenWidth, Vector2 clickPos) {
        super.getInput(screenWidth, clickPos);
    }
}
