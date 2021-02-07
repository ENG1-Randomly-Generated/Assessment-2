package com.randomlygenerated.tests.UnitTests;

import com.badlogic.gdx.math.Vector2;
import com.hardgforgif.dragonboatracing.Game;
import com.hardgforgif.dragonboatracing.GameData;
import com.hardgforgif.dragonboatracing.UI.GameOverUI;
import com.hardgforgif.dragonboatracing.UI.ResultsUI;
import com.hardgforgif.dragonboatracing.core.Obstacle;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;

import static com.randomlygenerated.tests.UnitTests.Utility.getMockGame;
import static com.randomlygenerated.tests.UnitTests.Utility.mockTicks;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertArrayEquals;

/**
 * Unit Tests for Game
 */
@RunWith(GdxTestRunner.class)
public class GameTest {


    /**
     * BOAT_END
     *      Does the game detect that a boat has reached the end and mark their results accordingly
     */
    @Test
    public void BOAT_END() {
        Game game = getMockGame(true);

        // Place player at the end
        game.player.boatBody.setTransform(0, 9100, 0);
        game.player.acceleration = 10;

        // Set a time for testing purposes
        GameData.currentTimer = 10;
        int oldResultsSize = GameData.results.size();

        game.render();

        float time = GameData.currentTimer;

        TestCase.assertTrue(GameData.results.size() > oldResultsSize); // Has this boat been registered?
        assertEquals(time, GameData.results.get(0)[1]); // Is the time correct?
    }


}
