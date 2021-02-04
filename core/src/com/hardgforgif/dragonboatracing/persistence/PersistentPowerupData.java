package com.hardgforgif.dragonboatracing.persistence;

import com.badlogic.gdx.math.Vector2;
import com.hardgforgif.dragonboatracing.Game;
import com.hardgforgif.dragonboatracing.GameData;
import com.hardgforgif.dragonboatracing.core.Obstacle;
import com.hardgforgif.dragonboatracing.powerups.*;

public class PersistentPowerupData {

    public Vector2 position;
    public String type;
    public float scale;

    /**
     * Generate persistent data for a given powerup
     * @param powerup Powerup to generate persistent data for
     */
    public PersistentPowerupData(Powerup powerup) {
        this.position = powerup.body.getPosition();
        this.type = powerup.typeName;
        this.scale = powerup.scale;
    }

    /**
     * Create a Powerup object for this given saved powerup state
     * @param game Game we are loading into
     * @return Powerup - Powerup object that reflects this saved state
     */
    public Powerup toPowerup(Game game) {
        Powerup powerup;
        switch(this.type) {
            case "health":
                powerup = new HealthPowerup();
                break;
            case "acceleration":
                powerup = new AccelerationPowerup();
                break;
            case "maneuv":
                powerup = new ManeuverabilityPowerup();
                break;
            case "speed":
                powerup = new SpeedPowerup();
                break;
            case "sprint":
                powerup = new SprintPowerup();
                break;
            default:
                powerup = new HealthPowerup();
        }
        powerup.createObstacleBody(game.world[GameData.currentLeg], this.position.x, this.position.y, scale);
        return powerup;
    }

}
