package com.randomlygenerated.tests;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.hardgforgif.dragonboatracing.Game;
import com.hardgforgif.dragonboatracing.GameData;
import com.hardgforgif.dragonboatracing.core.Boat;
import com.hardgforgif.dragonboatracing.core.Lane;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(GdxTestRunner.class)
public class Boat_Test {

    @Test
    public void boatMove() {
        Boat boat = new Boat(100, 100, 100, 100, 1, mock(Lane.class));
        World world = new World(new Vector2(0,0), true);
        boat.createBoatBody(world, 0, 0, "Boat1.json");
        Vector2 oldpos = boat.boatBody.getPosition().cpy();
        boat.current_speed = 100;
        boat.moveBoat();
        world.step(1f/60f, 6, 2); // Simulate the world stepping
        assertNotEquals(oldpos, boat.boatBody.getPosition());
    }

}
