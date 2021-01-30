package com.randomlygenerated.tests;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.hardgforgif.dragonboatracing.Game;

/**
 * Utility functions and features for performing integrated testing
 *
 * IntegratedTesting extends Game, but performs testing features
 * IntegratedTesting, as part of Test package, should not be built for deployment
 *
 *
 */
public class IntegratedTesting extends Game {

    public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1280;
        config.height = 720;
        config.fullscreen = false;
        new LwjglApplication(new IntegratedTesting(), config);
    }

    /**
     * Override render() to implement testing functions here
     */
    @Override
    public void render() {
        super.render();

        // F1 = Spawn Obstacle in front of me
        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            
        }
    }
}
