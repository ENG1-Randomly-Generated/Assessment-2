package com.randomlygenerated.tests.UnitTests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.hardgforgif.dragonboatracing.Game;
import com.hardgforgif.dragonboatracing.GameData;
import com.hardgforgif.dragonboatracing.core.AI;
import com.hardgforgif.dragonboatracing.core.Map;
import com.hardgforgif.dragonboatracing.core.Obstacle;
import com.hardgforgif.dragonboatracing.powerups.HealthPowerup;
import com.hardgforgif.dragonboatracing.powerups.SprintPowerup;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Random;

import static com.randomlygenerated.tests.UnitTests.Utility.*;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.mock;

@RunWith(GdxTestRunner.class)
public class PersistentDataTest {

    Random random = new Random();

    /**
     * SAVE_POSITIONS
     *      Check whether positions of the boats are correctly saved and loaded
     */
    @Test
    public void SAVE_POSITIONS() {
        Game game = getMockGame(false);

        // Randomise positions and save them
        Vector2 playerPosition = new Vector2(random.nextInt(100), random.nextInt(100));
        game.player.createBoatBody(game.world[0], playerPosition.x, playerPosition.y, "Boat1.json");
        Vector2[] opponentPositions = new Vector2[game.opponents.length];
        for (int i = 0; i < game.opponents.length; i++) {
            opponentPositions[i] = new Vector2(random.nextInt(100), random.nextInt(100));
            game.opponents[i].createBoatBody(game.world[0], opponentPositions[i].x, opponentPositions[i].y, "Boat1.json");
        }

        // Now save the game
        game.save();

        // Now recreate our game
        game.dispose();
        game = getMockGame(false);

        // Now load the game again
        game.load();

        assertEquals(playerPosition, game.player.boatBody.getPosition());
        for (int i = 0; i < game.opponents.length; i++) {
            assertEquals(opponentPositions[i], game.opponents[i].boatBody.getPosition());
        }
    }

    /**
     * SAVE_OBSTACLES
     *      Check whether obstacles are correctly saved (pos, type) and loaded
     */
    @Test
    public void SAVE_OBSTACLES() {
        Game game = getMockGame(true);

        Vector2[] obstaclePositions = new Vector2[30];
        int[] obstacleTypes = new int[30];

        for (int i = 0; i < obstaclePositions.length; i++) {
            Vector2 vec = new Vector2(random.nextInt(100), random.nextInt(100));
            obstaclePositions[i] = vec;
            int type = 1 + random.nextInt(6);
            obstacleTypes[i] = type;
            Obstacle obs = new Obstacle(type);
            obs.createObstacleBody(game.world[0], vec.x, vec.y, 0f);
            game.player.lane.obstacles.add(obs);
        }

        // Now save the game
        game.save();

        // Now recreate our game
        game.dispose();
        game = getMockGame(true);

        // Now load the game again
        game.load();

        assertEquals(game.player.lane.obstacles.size(), 30);
        for (int i = 0; i < game.player.lane.obstacles.size(); i++) {
            Obstacle obst = game.player.lane.obstacles.get(i);
            assertEquals(obstaclePositions[i], obst.obstacleBody.getPosition());
            assertEquals(obstacleTypes[i], obst.type);
        }
    }

    /**
     * SAVE_POWERUPS
     *      Check whether powerups are correctly saved (pos, type) and loaded
     */
    @Test
    public void SAVE_POWERUPS() {
        Game game = getMockGame(true);

        HealthPowerup powerup = new HealthPowerup();
        powerup.createObstacleBody(game.world[0], 100, 100, 0);

        SprintPowerup powerup1 = new SprintPowerup();
        powerup1.createObstacleBody(game.world[0], 200, 150, 0);

        game.player.lane.powerups.add(powerup);
        game.player.lane.powerups.add(powerup1);

        // Now save the game
        game.save();

        // Now recreate our game
        game.dispose();
        game = getMockGame(true);

        // Now load the game again
        game.load();

        assertTrue(game.player.lane.powerups.get(0) instanceof HealthPowerup);
        assertEquals(new Vector2(100, 100), game.player.lane.powerups.get(0).body.getPosition());
        assertTrue(game.player.lane.powerups.get(1) instanceof SprintPowerup);
        assertEquals(new Vector2(200, 150), game.player.lane.powerups.get(1).body.getPosition());
    }

    /**
     * SAVE_GAMEDATA
     *      Check whether important gamedata like penalties and results are saved and loaded correctly
     */
    @Test
    public void SAVE_GAMEDATA() {
        Game game = getMockGame(true);

        // Place some fake data in GameData
        Float[] results = new Float[]{60f, 21f, 10f, 8f};
        GameData.results.add(new Float[]{60f, 21f, 10f, 8f});
        float[] penalties = new float[]{5.5f, 0, 0, 1.2f};
        GameData.penalties = new float[]{5.5f, 0, 0, 1.2f};

        // Now save the game
        game.dispose();
        game.save();

        // Now recreate our game
        game = getMockGame(true);

        // Now load the game again
        game.load();

        assertArrayEquals(results, GameData.results.get(0));
        assertArrayEquals(penalties, GameData.penalties,0);
    }
}
