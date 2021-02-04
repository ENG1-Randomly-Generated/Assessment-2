package com.hardgforgif.dragonboatracing.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.hardgforgif.dragonboatracing.GameData;
import com.hardgforgif.dragonboatracing.core.Player;

import java.util.ArrayList;
import java.util.Collections;

public class ChoosingDifficultyUI extends UI{
    private Texture background;
    private Sprite background_sprite;
    private Sprite easyBG_sprite;
    private Sprite mediumBG_sprite;
    private Sprite hardBG_sprite;
    private BitmapFont label;
    private BitmapFont selectedLabel;
    ScrollingBackground scrollingBackground = new ScrollingBackground();


    public ChoosingDifficultyUI() {
        scrollingBackground.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        scrollingBackground.setSpeedFixed(true);
        scrollingBackground.setSpeed(ScrollingBackground.DEFAULT_SPEED);

        background = new Texture(Gdx.files.internal("Background.png"));
        background_sprite = new Sprite(background);
        background_sprite.setPosition(450,410);
        background_sprite.setSize(400,80);

        easyBG_sprite = new Sprite(background);
        easyBG_sprite.setPosition(200,200);
        easyBG_sprite.setSize(200,80);

        mediumBG_sprite = new Sprite(background);
        mediumBG_sprite.setPosition(550,200);
        mediumBG_sprite.setSize(200,80);

        hardBG_sprite = new Sprite(background);
        hardBG_sprite.setPosition(900,200);
        hardBG_sprite.setSize(200,80);



        label = new BitmapFont();
        label.getData().setScale(1.4f);
        label.setColor(Color.BLACK);

        selectedLabel = new BitmapFont();
        selectedLabel.getData().setScale(1.4f);
        selectedLabel.setColor(Color.WHITE);
    }

    @Override
    public void drawUI(Batch batch, Vector2 mousePos, float screenWidth, float delta) {
        batch.begin();

        scrollingBackground.updateAndRender(delta, batch);

        background_sprite.draw(batch);
        easyBG_sprite.draw(batch);
        mediumBG_sprite.draw(batch);
        hardBG_sprite.draw(batch);

        label.draw(batch, "Easy", 270, 250);
        label.draw(batch, "Medium", 610, 250);
        label.draw(batch, "Hard", 970, 250);
        label.draw(batch, "Choose Enemy Difficulty", 540, 460);

        //Check if the mouse is hovering over a button, and change the colour of the button's text if the mouse if hovering over
        for (int i = 0; i < 3; i++){
            // Get the position of the button
            float buttonX = 200 + (350*i);
            float buttonY = 200;
            float buttonWidth = 200;
            float buttonHeight = 80;

            // Check if the mouse is hovered over it
            if (mousePos.x > buttonX && mousePos.x < buttonX + buttonWidth &&
                    mousePos.y > buttonY && mousePos.y < buttonY + buttonHeight){
                //Covers the previously black text with white text
                if (i == 0){
                    selectedLabel.draw(batch, "Easy", 270, 250);
                }
                else if (i == 1){
                    selectedLabel.draw(batch, "Medium", 610, 250);
                }
                else{
                    selectedLabel.draw(batch, "Hard", 970, 250);
                }
            }
        }


        batch.end();

        playMusic();
    }

    @Override
    public void getInput(float screenWidth, Vector2 mousePos) {
        //Check if the click is on a button, select difficulty depending on which button
        for (int i = 0; i < 3; i++){
            // Get the position of the button
            float buttonX = 200 + (350*i);
            float buttonY = 200;
            float buttonWidth = 200;
            float buttonHeight = 80;

            // Check if the click is on it
            if (mousePos.x > buttonX && mousePos.x < buttonX + buttonWidth &&
                    mousePos.y > buttonY && mousePos.y < buttonY + buttonHeight){
                //Updates the Gamedata with the AI's difficulty multiplier
                if (i == 0){
                    GameData.difficulty = new float[]{1f, 0.9f, 0.9f, 0.9f, 0.9f};
                }
                else if (i == 1){
                    GameData.difficulty = new float[]{1f, 1f, 1f, 1f, 1f};
                }
                else {
                    GameData.difficulty = new float[]{1f, 1.1f, 1.1f, 1f, 1.05f};
                }

                // Change to Controls UI
                GameData.currentUI = new ControlsUI();
            }
        }
    }

    @Override
    public void drawPlayerUI(Batch batch, Player playerBoat) {
    }
}
