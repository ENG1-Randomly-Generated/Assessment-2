package com.hardgforgif.dragonboatracing.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.hardgforgif.dragonboatracing.core.Boat;

public class HealthPowerup extends Powerup {


    public HealthPowerup() {
        super(new Texture("Powerups/health.png"), "health");
    }

    @Override
    public void onCollide(Boat boat) {
        // Add robustness :)
        boat.robustness = Math.min(boat.robustness + 20, boat.max_robustness);
    }
}
