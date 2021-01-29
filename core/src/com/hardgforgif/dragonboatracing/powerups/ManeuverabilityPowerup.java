package com.hardgforgif.dragonboatracing.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.hardgforgif.dragonboatracing.core.Boat;

public class ManeuverabilityPowerup extends Powerup {

    public static final int LENGTH = 6000; // Time the powerup lasts for

    public ManeuverabilityPowerup() {
        super(new Texture("Powerups/maneuv.png"), "maneuv");
    }

    @Override
    public void onCollide(Boat boat) {
        this.registerTempPowerup(new Powerup.TempPowerup() {
            @Override
            public void onStart() {
                boat.maneuverability = boat.maneuverability * 1.5f; // Increase maneuverability by 50%
            }

            @Override
            public void onEnd() {
                boat.maneuverability = boat._maneuverability;
            }
        }, LENGTH);
    }
}
