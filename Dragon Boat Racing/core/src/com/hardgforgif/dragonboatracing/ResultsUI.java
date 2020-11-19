package com.hardgforgif.dragonboatracing;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class ResultsUI extends UI{
    private Texture background;
    private Sprite backgroundSprite;
    private Texture entryTexture;
    private Sprite[] entrySprites = new Sprite[4];
    private BitmapFont[] resultFonts = new BitmapFont[4];
    private BitmapFont titleFont;

    public ResultsUI(){
        entrySprites = new Sprite[4];
        resultFonts = new BitmapFont[4];
        titleFont = new BitmapFont();
        titleFont.getData().setScale(1.8f);
        titleFont.setColor(Color.BLACK);


        background = new Texture(Gdx.files.internal("Background.png"));
        backgroundSprite = new Sprite(background);
        backgroundSprite.setPosition(200,150);
        backgroundSprite.setSize(Gdx.graphics.getWidth() - 400,Gdx.graphics.getHeight() - 300);

        entryTexture = new Texture(Gdx.files.internal("Background.png"));

        for (int i = 0; i < 4; i++){
            entrySprites[i] = new Sprite(entryTexture);
            entrySprites[i].setSize(backgroundSprite.getWidth() - 100,50);
            entrySprites[i].setPosition(backgroundSprite.getX() + 50,
                    backgroundSprite.getY() + backgroundSprite.getHeight() - 200 - (i * 60));


            resultFonts[i] = new BitmapFont();
            resultFonts[i].getData().setScale(1.2f);
            resultFonts[i].setColor(Color.BLACK);
        }

    }

    @Override
    public void drawUI(Batch batch, Vector2 mousePos, float screenWidth, float delta) {
        batch.begin();
        backgroundSprite.draw(batch);
        titleFont.draw(batch, "Results", backgroundSprite.getX() + backgroundSprite.getWidth() / 2 - 30,
                backgroundSprite.getY() + backgroundSprite.getHeight() - 50);

        Collections.sort(GameData.results, new Comparator<Pair<Integer, Float>>() {
            @Override
            public int compare(Pair<Integer, Float> o1, Pair<Integer, Float> o2) {
                if (o1.getValue() > o2.getValue())
                    return 1;
                return 0;
            }
        });
        for (int i = 0; i < GameData.results.size(); i++){
            int boatNr = GameData.results.get(i).getKey();
            float result = GameData.results.get(i).getValue();

            entrySprites[i].draw(batch);

            String text = (i + 1) + ". ";
            if (boatNr == 0)
                text += "Player: ";
            else{
                text += "Opponent" + boatNr + ": ";
            }
            if (result != Float.MAX_VALUE)
                text += result;
            else
                text += "DNF";
            resultFonts[i].draw(batch, text, entrySprites[i].getX() + 50,  entrySprites[i].getY() + 30);

        }

        batch.end();

        playMusic();
    }

    @Override
    public void drawPlayerUI(Batch batch, Player playerBoat) {

    }

    @Override
    public void getInput(float screenWidth, Vector2 mousePos) {
        if(mousePos.x != 0f && mousePos.y != 0f && GameData.results.size() == 4) {
            if (GameData.currentLeg == 2)
                Gdx.app.exit();
            GameData.showResults = false;
            GameData.gamePlay = false;
            GameData.resetGame = true;
            GameData.currentLeg += 1;
            GameData.results.clear();
            GameData.currentTimer = 0f;
            GameData.currentUI = new GamePlayUI();
        }
    }
}
