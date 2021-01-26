package com.hardgforgif.dragonboatracing.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.hardgforgif.dragonboatracing.core.Boat;

public class AccelerationPowerup extends Powerup {


    public AccelerationPowerup() {
        super(new Texture("Powerups/acceleration.png"), "acceleration");
    }

    @Override
    public void onCollide(Boat boat) {
        this.registerTempPowerup(new Powerup.TempPowerup() {
            @Override
            public void onStart() {
                boat.acceleration = boat.acceleration * 1.5f; // Increase acceleration by 50%
            }

            @Override
            public void onEnd() {
                boat.acceleration = boat._acceleration;
            }
        }, 6000);
    }
}
