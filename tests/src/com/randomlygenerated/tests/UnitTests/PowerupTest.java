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
     * POWERUP_SPEED_INCREASE
     *      A speed powerup will increase the Boat's speed
     */
    @Test
    public void POWERUP_SPEED_INCREASE() {
        Player player = getMockPlayer();
        SpeedPowerup powerup = new SpeedPowerup();

        player.speed = player._speed; // Set to maximum speed

        powerup.onCollide(player);

        assertTrue(player.speed + " is not greater than previous speed " + player._speed, player.speed > player._speed);
    }

    /**
     * POWERUP_SPEED_REDUCE
     *      Once a speed powerup has been applied and runs out, speed must
     *          reduce back to normal
     */
    @Test
    public void POWERUP_SPEED_REDUCE() throws InterruptedException {
        Player player = getMockPlayer();
        SpeedPowerup powerup = new SpeedPowerup();

        player.speed = player._speed; // Set to max speed

        powerup.onCollide(player); // Start the temporary boost

        mockTicks(powerup::tick, SpeedPowerup.LENGTH + 1000); // Mock ticks in the background

        Thread.sleep(SpeedPowerup.LENGTH + 1000); // Wait until boost runs out, with offset

        assertTrue(player.speed + " is still greater than maximum speed of " + player._speed,
                player.speed <= player._speed);
    }

    /**
     * POWERUP_MANEUV_INCREASE
     *      A maneuverability powerup will increase the Boat's maneuverability
     */
    @Test
    public void POWERUP_MANEUV_INCREASE() {
        Player player = getMockPlayer();
        ManeuverabilityPowerup powerup = new ManeuverabilityPowerup();

        player.maneuverability = player._maneuverability; // Set to maximum speed

        powerup.onCollide(player);

        assertTrue(player.maneuverability + " is not greater than previous maneuverability " +
                player._maneuverability, player.maneuverability > player._maneuverability);
    }

    /**
     * POWERUP_MANEUV_DECREASE
     *      Once a maneuvarability powerup has been applied and runs out, maneuverability must
     *          reduce back to normal
     */
    @Test
    public void POWERUP_MANEUV_DECREASE() throws InterruptedException {
        Player player = getMockPlayer();
        ManeuverabilityPowerup powerup = new ManeuverabilityPowerup();

        player.maneuverability = player._maneuverability; // Set to max speed

        powerup.onCollide(player); // Start the temporary boost

        mockTicks(powerup::tick, ManeuverabilityPowerup.LENGTH + 1000); // Mock ticks in the background

        Thread.sleep(ManeuverabilityPowerup.LENGTH + 1000); // Wait until boost runs out, with offset

        assertTrue(player.maneuverability + " is still greater than maximum maneuverability of "
                + player._maneuverability, player.maneuverability <= player._maneuverability);
    }

    /**
     * POWERUP_ACC_INCREASE
     *      An acceleration powerup will increase the Boat's acceleration
     */
    @Test
    public void POWERUP_ACC_INCREASE() {
        Player player = getMockPlayer();
        AccelerationPowerup powerup = new AccelerationPowerup();

        player.acceleration = player._acceleration; // Set to maximum speed

        powerup.onCollide(player);

        assertTrue(player.acceleration + " is not greater than previous acceleration " +
                player._acceleration, player.acceleration > player._acceleration);
    }

    /**
     * POWERUP_ACC_DECREASE
     *      Once an acceleration powerup has been applied and runs out, acceleration must
     *          reduce back to normal
     */
    @Test
    public void POWERUP_ACC_DECREASE() throws InterruptedException {
        Player player = getMockPlayer();
        AccelerationPowerup powerup = new AccelerationPowerup();

        player.acceleration = player._acceleration; // Set to max acceleration

        powerup.onCollide(player); // Start the temporary boost

        mockTicks(powerup::tick, AccelerationPowerup.LENGTH + 1000); // Mock ticks in the background

        Thread.sleep(AccelerationPowerup.LENGTH + 1000); // Wait until boost runs out, with offset

        assertTrue(player.acceleration + " is still greater than maximum acceleration of "
                + player._acceleration, player.acceleration <= player._acceleration);
    }

    /**
     * POWERUP_SPRINT_INCREASE
     *      A sprint powerup will increase the Boat's current speed and speed
     */
    @Test
    public void POWERUP_SPRINT_INCREASE() {
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
    }

    /**
     * POWERUP_SPRINT_DECREASE
     *      Once a sprint powerup has been taken, both the current speed and speed must decrease back
     *          to normal
     */
    @Test
    public void POWERUP_SPRINT_DECREASE() throws InterruptedException {
        Player player = getMockPlayer();
        SprintPowerup powerup = new SprintPowerup();


        powerup.onCollide(player); // Start the temporary boost

        mockTicks(powerup::tick, SprintPowerup.LENGTH + 1000); // Mock ticks in the background
        Thread.sleep(SprintPowerup.LENGTH + 1000); // Wait until boost runs out, with offset

        assertTrue(player.current_speed + " is still greater than their maximum speed "
                + player._speed, player.current_speed <= player._speed);
    }


}
