package com.randomlygenerated.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.hardgforgif.dragonboatracing.Game;
import com.hardgforgif.dragonboatracing.GameData;
import com.hardgforgif.dragonboatracing.core.Boat;
import com.hardgforgif.dragonboatracing.core.Lane;
import com.hardgforgif.dragonboatracing.core.Player;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.randomlygenerated.tests.Utility.*;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Unit Tests for Boat
 */
@RunWith(GdxTestRunner.class)
public class BoatTest {

    /**
     *  BOAT_MOVE
     *      Does the boat move upon being given an input?
     */
    @Test
    public void BOAT_MOVE() {
        Player player = getMockPlayer();
        World world = getMockWorld();
        player.createBoatBody(world, 0, 0, "Boat1.json");
        Vector2 oldpos = player.boatBody.getPosition().cpy();
        player.updatePlayer(new boolean[] {true, false, false, false}, 1); // Simulate the player pressing W
        world.step(1f/60f, 6, 2); // Simulate the world stepping

        assertNotEquals(oldpos, player.boatBody.getPosition()); // The boat must have moved
    }

    /**
     *  BOAT_STAMINA
     *      Does the boat move upon being given an input?
     */
    @Test
    public void BOAT_STAMINA() {
        Player player = getMockPlayer();
        World world = getMockWorld();
        player.createBoatBody(world, 0, 0, "Boat1.json");

        float oldStamina = player.stamina;

        player.updatePlayer(new boolean[] {true, false, false, false}, 1); // Simulate the player pressing W
        world.step(1f/60f, 6, 2); // Simulate the world stepping

        assertTrue(oldStamina + " is not greater than " + player.stamina,
                oldStamina > player.stamina); // The boat must have moved
    }

    /**
     * BOAT_STAMINA_MOVEMENT
     *      Does the boat turn slower with lower stamina?
     */
    @Test
    public void BOAT_STAMINA_MOVEMENT() {
        Player player1 = getMockPlayer();
        Player player2 = getMockPlayer();
        World world = getMockWorld();
        player1.createBoatBody(world, 0, 0, "Boat1.json");
        player2.createBoatBody(world, 0, 0, "Boat1.json");

        player1.stamina = 100;
        player2.stamina = 50;

        player1.updatePlayer(new boolean[] {false, true, false, false}, 1); // Simulate player1 pressing A
        player2.updatePlayer(new boolean[] {false, true, false, false}, 1); // Simulate player2 pressing A

        world.step(1f/60f, 6, 2); // Simulate the world stepping

        assertTrue(player1.boatBody.getAngle() + " not greater than " + player2.boatBody.getAngle(),
                player1.boatBody.getAngle() > player2.boatBody.getAngle()); // Stamina must have decreased
    }


}
