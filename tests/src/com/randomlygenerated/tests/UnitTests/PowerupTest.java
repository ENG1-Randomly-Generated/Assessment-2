package com.randomlygenerated.tests.UnitTests;

import com.hardgforgif.dragonboatracing.core.Player;
import com.hardgforgif.dragonboatracing.powerups.*;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.randomlygenerated.tests.UnitTests.Utility.getMockPlayer;
import static com.randomlygenerated.tests.UnitTests.Utility.mockTicks;
import static junit.framework.TestCase.assertTrue;

/**
 * Tests for Powerup classes
 */
@RunWith(GdxTestRunner.class)
public class PowerupTest {

    /**
     * POWER_HEALTH_INCREASE
     *      A health powerup increases the robustness of a boat upon contact
     */
    @Test
    public void POWERUP_HEALTH_INCREASE() {
        Player player = getMockPlayer();
        HealthPowerup powerup = new HealthPowerup();

        player.robustness = 1; // Set to low robustness
        powerup.onCollide(player);

        assertTrue(player.robustness + " is not greater than 1", player.robustness > 1);
    }

    /**
     * POWER_HEALTH_MAX
     *      A Boat cannot have a robustness over their maximum
     */
    @Test
    public void POWERUP_HEALTH_MAX() {
        Player player = getMockPlayer();
        HealthPowerup powerup = new HealthPowerup();

        player.robustness = player.max_robustness; // Set to max robustness
        powerup.onCollide(player);

        assertTrue(player.robustness + " is above their maximum " + player.max_robustness, player.robustness <= player.max_robustness);
    }

    /**
     * POWERUP_SPEED_INCREASE_TEMP
     *      A speed powerup will increase the Boat's speed temporarily
     */
    @Test
    public void POWERUP_SPEED_INCREASE_TEMP() throws InterruptedException {
        Player player = getMockPlayer();
        SpeedPowerup powerup = new SpeedPowerup();

        player.speed = player._speed; // Set to maximum speed

        powerup.onCollide(player);

        assertTrue(player.speed + " is not greater than previous speed " + player._speed, player.speed > player._speed);

        mockTicks(powerup::tick, SpeedPowerup.LENGTH + 1000); // Mock ticks in the background

        Thread.sleep(SpeedPowerup.LENGTH + 1000); // Wait until boost runs out, with offset

        assertTrue(player.speed + " is still greater than maximum speed of " + player._speed,
                player.speed <= player._speed);
    }


    /**
     * POWERUP_MANEUV_INCREASE_TEMP
     *      A maneuverability powerup will increase the Boat's maneuverability temporarily
     */
    @Test
    public void POWERUP_MANEUV_INCREASE_TEMP() throws InterruptedException {
        Player player = getMockPlayer();
        ManeuverabilityPowerup powerup = new ManeuverabilityPowerup();

        player.maneuverability = player._maneuverability; // Set to maximum speed

        powerup.onCollide(player);

        assertTrue(player.maneuverability + " is not greater than previous maneuverability " +
                player._maneuverability, player.maneuverability > player._maneuverability);

        mockTicks(powerup::tick, ManeuverabilityPowerup.LENGTH + 1000); // Mock ticks in the background

        Thread.sleep(ManeuverabilityPowerup.LENGTH + 1000); // Wait until boost runs out, with offset

        assertTrue(player.maneuverability + " is still greater than maximum maneuverability of "
                + player._maneuverability, player.maneuverability <= player._maneuverability);
    }

    /**
     * POWERUP_ACC_INCREASE_TEMP
     *      An acceleration powerup will increase the Boat's acceleration temporarily
     */
    @Test
    public void POWERUP_ACC_INCREASE_TEMP() throws InterruptedException {
        Player player = getMockPlayer();
        AccelerationPowerup powerup = new AccelerationPowerup();

        player.acceleration = player._acceleration; // Set to maximum speed

        powerup.onCollide(player);

        assertTrue(player.acceleration + " is not greater than previous acceleration " +
                player._acceleration, player.acceleration > player._acceleration);

        mockTicks(powerup::tick, AccelerationPowerup.LENGTH + 1000); // Mock ticks in the background

        Thread.sleep(AccelerationPowerup.LENGTH + 1000); // Wait until boost runs out, with offset

        assertTrue(player.acceleration + " is still greater than maximum acceleration of "
                + player._acceleration, player.acceleration <= player._acceleration);
    }

    /**
     * POWERUP_SPRINT_INCREASE_TEMP
     *      A sprint powerup will increase the Boat's current speed and speed temporarily
     */
    @Test
    public void POWERUP_SPRINT_INCREASE_TEMP() throws InterruptedException {
        Player player = getMockPlayer();
        SprintPowerup powerup = new SprintPowerup();

        player.current_speed = player._speed; // Player is going max speed
        player.speed = player._speed; // Player can go max speed
        float previousSpeed = player.speed;
        float previousCurrentSpeed = player.current_speed;

        powerup.onCollide(player);

        String output = String.format("Player's current_speed(%f) and speed(%f) has not increased, " +
                "old current_speed(%f) speed(%f)", player.current_speed, player.speed, previousCurrentSpeed, previousSpeed);
        assertTrue(output, player.current_speed > previousCurrentSpeed && player.speed > previousCurrentSpeed);

        mockTicks(powerup::tick, SprintPowerup.LENGTH + 1000); // Mock ticks in the background

        Thread.sleep(SprintPowerup.LENGTH + 1000); // Wait until boost runs out, with offset

        assertTrue(player.current_speed + " is still greater than their maximum speed "
                + player._speed, player.current_speed <= player._speed);
    }

}
