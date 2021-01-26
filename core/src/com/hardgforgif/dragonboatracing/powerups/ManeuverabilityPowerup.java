package com.hardgforgif.dragonboatracing.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.hardgforgif.dragonboatracing.core.Boat;

public class ManeuverabilityPowerup extends Powerup {


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
        }, 6000);
    }
}
