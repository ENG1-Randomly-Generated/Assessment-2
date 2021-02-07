package com.randomlygenerated.tests.UnitTests;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.hardgforgif.dragonboatracing.Game;
import com.hardgforgif.dragonboatracing.GameData;
import com.hardgforgif.dragonboatracing.UI.GamePlayUI;
import com.hardgforgif.dragonboatracing.UI.MenuUI;
import com.hardgforgif.dragonboatracing.core.*;
import com.hardgforgif.dragonboatracing.powerups.SprintPowerup;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;

/**
 * Utility package for testing DragonBoatGame
 */
public class Utility {

    /**
     * Interface to mock ticks in the game,
     *      use with mockTicks() to simulate repeated
     *          render cycles or code execution on a separate thread
     */
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
     * @param command - TickCommand interface with given code to run every tick
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

    /**
     * Create and return a mocked version of Game that can perform and run correctly
     *      Note: World collisions do not perform in this mocked-game, but you can perform the main render loop
     * @param createBodies - Whether to create the World bodies for each boat in the game. Set to
     *                      false if you want to spawn boats at pre-defined positions
     * @return Game - Mocked Game ready for use in testing
     */
    public static Game getMockGame(boolean createBodies) {
        Game game = new Game();
        game.map = new Map[] {getMockMap(), getMockMap(), getMockMap()};
        game.player = getMockPlayer(game.map[0].lanes[0]);
        game.world = new World[] {getMockWorld(), getMockWorld(), getMockWorld()};
        game.opponents = new AI[] {getMockAI(game.map[0].lanes[1]), getMockAI(game.map[0].lanes[2]),
                getMockAI(game.map[0].lanes[3])};
        game.UIbatch = mock(SpriteBatch.class);
        game.batch = mock(SpriteBatch.class);
        game.camera = new OrthographicCamera();
        resetGameData();
        GameData.currentUI = new GamePlayUI();
        GameData.gamePlayState = true;

        if (createBodies) {
            game.player.createBoatBody(game.world[0], 0, 0, "Boat1.json");
            game.opponents[0].createBoatBody(game.world[0], 0, 0, "Boat1.json");
            game.opponents[1].createBoatBody(game.world[0], 0, 0, "Boat1.json");
            game.opponents[2].createBoatBody(game.world[0], 0, 0, "Boat1.json");
        }

        return game;
    }

    /**
     * Return a mocked Map object that contains mocked lanes
     * @return Map - Mocked Map
     */
    public static Map getMockMap() {
        Map map = mock(Map.class);
        map.lanes = new Lane[] {getMockLane(), getMockLane(), getMockLane(), getMockLane()};
        return map;
    }

    /**
     * Return a World object for testing
     * @return World
     */
    public static World getMockWorld() {
        return new World(new Vector2(0,0), true);
    }

    /**
     * Return a Lane object with mocked MapLayers
     * @return Lane
     */
    public static Lane getMockLane() {
        return new Lane(9000, mock(MapLayer.class), mock(MapLayer.class));
    }

    /**
     * Return an AI object with the given Lane
     *      The Boat will have stats 100,100,100,100
     * @param lane - Lane to assign this AI to
     * @return AI
     */
    public static AI getMockAI(Lane lane) {
        return new AI(100, 100, 100, 100, 1, lane);
    }

    /**
     * Return an AI object with a mocked Lane
     * @return AI
     */
    public static AI getMockAI() {
        return getMockAI(getMockLane());
    }

    /**
     * Return a Player object with the given Lane
     * @param lane - Lane to assign Player to
     * @return Player
     */
    public static Player getMockPlayer(Lane lane) { return new Player(100, 100, 100, 100, 1, lane); }

    /**
     * Return a Player with a mocked Lane
     * @return Player
     */
    public static Player getMockPlayer() { return getMockPlayer(getMockLane()); }

}
