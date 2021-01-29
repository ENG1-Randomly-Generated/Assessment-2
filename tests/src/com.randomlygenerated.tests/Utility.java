package com.randomlygenerated.tests;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.hardgforgif.dragonboatracing.Game;
import com.hardgforgif.dragonboatracing.GameData;
import com.hardgforgif.dragonboatracing.core.*;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;

/**
 * Utility package for testing DragonBoatGame
 */
public class Utility {

    public interface TickCommand {
        void tick();
    }

    /**
     * Reset GameData almost entirely for testing
     */
    public static void resetGameData() {
        GameData.mainMenuState = false;
        GameData.choosingBoatState = false;
        GameData.choosingDifficultyState = false;
        GameData.gamePlayState = false;
        GameData.showResultsState = false;
        GameData.resetGameState = false;
        GameData.GameOverState = false;
        GameData.pauseState = false;
        GameData.penalties = new float[4];
        GameData.results = new ArrayList<>();
        GameData.currentLeg = 0;
        GameData.currentTimer = 0f;
        GameData.standings = new int[4];
    }

    /**
     * Mock the ticking of an Entity or class for the
     *  purpose of testing
     *
     * @param command - TickCommand interface with given command to run every tick
     * @param time - Time in milliseconds to run mockticks for
     */
    public static void mockTicks(TickCommand command, long time) {
        new Thread(new Runnable() {
            long endTime = System.currentTimeMillis() + time;
            @Override
            public void run() {
                while (System.currentTimeMillis() < endTime) {
                    command.tick();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ignored) {}
                }
            }
        }).start();
    }


    public static World getMockWorld() {
        return new World(new Vector2(0,0), true);
    }

    public static Lane getMockLane() {
        return new Lane(9000, mock(MapLayer.class), mock(MapLayer.class));
    }

    public static Boat getMockBoat() {
        return new Boat(100, 100, 100, 100, 1, getMockLane());
    }

    public static Player getMockPlayer() {
        return new Player(100, 100, 100, 100, 1, getMockLane());
    }

    public static Boat getMockBoat(int robustness, int speed, int acceleration, int maneuverability) {
        return new Boat(robustness, speed, acceleration, maneuverability, 1, mock(Lane.class));
    }

}
