package com.hardgforgif.dragonboatracing.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.hardgforgif.dragonboatracing.core.Boat;

public class SpeedPowerup extends Powerup {


    public SpeedPowerup() {
        super(new Texture("Powerups/speedup.png"), "speedup");
    }

    @Override
    public void onCollide(Boat boat) {
        this.registerTempPowerup(new Powerup.TempPowerup() {
            @Override
            public void onStart() {
                boat.speed = boat.speed * 1.5f; // Increase speed by 50%
            }

            @Override
            public void onEnd() {
                boat.speed = boat._speed;
            }
        }, 6000);
    }
}
