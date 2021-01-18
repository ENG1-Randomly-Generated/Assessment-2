package com.hardgforgif.dragonboatracing.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.hardgforgif.dragonboatracing.GameData;
import com.hardgforgif.dragonboatracing.core.Player;

import java.util.ArrayList;

public abstract class UI {

    public interface ButtonListener {
        void onClick();
    }

    public static class Button {
        Texture unselected;
        Texture selected;
        float x;
        float y;
        float yFlipped;
        float width;
        float height;
        ButtonListener listener;

        /**
         * Create a button to draw on the UI and capture clicks with the listener
         * - Utility function to reduce redundant code writing
         *
         * @param unselected Texture of button unselected
         * @param selected   Texture of button selected
         * @param x          Ratio of position on screen horizontally (e.g. 0.5 is the middle)
         * @param y          Ratio of position on screen vertically (e.g. 0.5 is the middle)
         * @param width      Ratio of width on screen
         * @param height     Ratio of height on screen
         * @param listener   Listener for callbacks
         */
        public Button(Texture unselected, Texture selected, float x, float y, float width, float height, ButtonListener listener) {
            // Get real floats from screen ratios
            this.width = width * Gdx.graphics.getWidth();
            this.height = height * Gdx.graphics.getHeight();
            this.x = (x * Gdx.graphics.getWidth()) - this.width / 2;
            this.y = (y * Gdx.graphics.getHeight()) - this.height / 2;
            this.yFlipped = Gdx.graphics.getHeight() - y;
            this.unselected = unselected;
            this.selected = selected;
            this.listener = listener;
        }
    }

    private ArrayList<Button> buttons = new ArrayList<>();

    /**
     * Adds a button to this UI for rendering
     *
     * @param button Button class
     */
    public void addButton(Button button) {
        this.buttons.add(button);
    }

    public void drawButtons(Batch batch, Vector2 mousePos) {
        // Hopefully this will be called within class specific batch.begin() and batch.end(), but start it incase
        boolean wasDrawing = true;
        if (!batch.isDrawing()) {
            wasDrawing = false;
            batch.begin();
        }

        float x = Gdx.input.getX();
        float y = Gdx.graphics.getHeight() - Gdx.input.getY();

        for (Button button : this.buttons) {
            if (x < button.x + button.width && x > button.x && y < button.y + button.height && y > button.y) {
                batch.draw(button.selected, button.x, button.y, button.width, button.height);
            } else {
                batch.draw(button.unselected, button.x, button.y, button.width, button.height);
            }
        }

        if (!wasDrawing) {
            batch.end();
        }
    }

    /**
     * This method draws UI elements that are not related to the player boat
     *
     * @param batch       The batch to draw to
     * @param mousePos    The location of the mouse, necessary for buttons
     * @param screenWidth The width of the screen
     * @param delta       The time passed since the last frame
     */
    public abstract void drawUI(Batch batch, Vector2 mousePos, float screenWidth, float delta);

    /**
     * Draws UI elements related to the player boat
     *
     * @param batch      The batch to draw to
     * @param playerBoat The player boat
     */
    public abstract void drawPlayerUI(Batch batch, Player playerBoat);

    /**
     * Handles input given by the user
     *
     * @param screenWidth The width of the screen
     * @param mousePos    the location of the mouse when it is clicked
     */
    public void getInput(float screenWidth, Vector2 mousePos) {
        float x = Gdx.input.getX();
        float y = Gdx.graphics.getHeight() - Gdx.input.getY();

        if (!Gdx.input.justTouched()) {return;}

        for (Button button : this.buttons) {
            if (x < button.x + button.width && x > button.x && y < button.y + button.height && y > button.y) {
                button.listener.onClick();
            }
        }
    }

    /**
     * Plays the current music available in the GameData static class
     */
    public void playMusic() {
        if (!GameData.music.isPlaying()) {
            GameData.music.play();
        }
    }


}
