package com.randomlygenerated.tests.UnitTests;


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

    public static Game getMockGame(boolean createBodies) {
        Game game = new Game();
        game.map = new Map[] {getMockMap()};
        game.player = getMockPlayer(game.map[0].lanes[0]);
        game.world = new World[] {getMockWorld()};
        game.opponents = new AI[] {getMockAI(game.map[0].lanes[1]), getMockAI(game.map[0].lanes[2]),
                getMockAI(game.map[0].lanes[3])};

        if (createBodies) {
            game.player.createBoatBody(game.world[0], 0, 0, "Boat1.json");
            game.opponents[0].createBoatBody(game.world[0], 0, 0, "Boat1.json");
            game.opponents[1].createBoatBody(game.world[0], 0, 0, "Boat1.json");
            game.opponents[2].createBoatBody(game.world[0], 0, 0, "Boat1.json");
        }

        return game;
    }

    public static Map getMockMap() {
        Map map = mock(Map.class);
        map.lanes = new Lane[] {getMockLane(), getMockLane(), getMockLane(), getMockLane()};
        return map;
    }

    public static World getMockWorld() {
        return new World(new Vector2(0,0), true);
    }

    public static Lane getMockLane() {
        return new Lane(9000, mock(MapLayer.class), mock(MapLayer.class));
    }

    public static AI getMockAI(Lane lane) {
        return new AI(100, 100, 100, 100, 1, lane);
    }

    public static AI getMockAI() {
        return getMockAI(getMockLane());
    }

    public static Player getMockPlayer(Lane lane) { return new Player(100, 100, 100, 100, 1, lane); }

    public static Player getMockPlayer() { return getMockPlayer(getMockLane()); }

}
