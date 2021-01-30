package com.randomlygenerated.tests;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.hardgforgif.dragonboatracing.Game;
import com.hardgforgif.dragonboatracing.GameData;
import com.hardgforgif.dragonboatracing.core.AI;
import com.hardgforgif.dragonboatracing.core.Obstacle;
import com.hardgforgif.dragonboatracing.powerups.HealthPowerup;
import com.hardgforgif.dragonboatracing.powerups.Powerup;

/**
 * Utility functions and features for performing integrated testing
 *
 * IntegratedTesting extends Game, but performs testing features
 * IntegratedTesting, as part of Test package, should not be built for deployment
 *
 *
 */
public class IntegratedTesting extends Game {

    BitmapFont font;

    public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1280;
        config.height = 720;
        config.fullscreen = false;
        new LwjglApplication(new IntegratedTesting(), config);
    }

    @Override
    public void create() {
        super.create();
        font = new BitmapFont();
    }

    /**
     * Override render() to implement testing functions here
     */
    @Override
    public void render() {
        super.render();

        if (!GameData.gamePlayState || player == null) return;

        // Draw statistic information during game for testing
        UIbatch.begin();

        font.draw(UIbatch, "Penalty: " + GameData.penalties[0], 10, 30);
        font.draw(UIbatch, "Obstacles: " + player.lane.obstacles.size(), 10, 50);

        UIbatch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            // F1 = Spawn Obstacle in front of me
            Vector2 plyPos = player.boatBody.getPosition();
            Obstacle obstacle = new Obstacle(1);
            obstacle.createObstacleBody(world[GameData.currentLeg], plyPos.x, (plyPos.y + 2), -0.8f);
            player.lane.obstacles.add(obstacle);

        } else if (Gdx.input.isKeyJustPressed(Input.Keys.F2)) {
            // F2 = Spawn Powerup in front of me
            Vector2 plyPos = player.boatBody.getPosition();
            Powerup powerup = new HealthPowerup();
            powerup.createObstacleBody(world[GameData.currentLeg], plyPos.x, (plyPos.y + 2), 0f);
            player.lane.powerups.add(powerup);

        } else if (Gdx.input.isKeyJustPressed(Input.Keys.F3) && GameData.currentLeg < 3) {
            // F3 = skip leg
            GameData.results.add(new Float[] {0f,0f,0f,0f});
            GameData.gamePlayState = false;
            GameData.resetGameState = true;
            GameData.showResultsState = true;
        }
    }
}
