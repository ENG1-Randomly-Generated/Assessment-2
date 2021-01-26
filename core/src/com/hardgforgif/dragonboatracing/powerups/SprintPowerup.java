package com.hardgforgif.dragonboatracing.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.hardgforgif.dragonboatracing.core.Boat;

public class SprintPowerup extends Powerup {


    public SprintPowerup() {
        super(new Texture("Powerups/sprint.png"), "sprint");
    }

    @Override
    public void onCollide(Boat boat) {
        this.registerTempPowerup(new Powerup.TempPowerup() {
            @Override
            public void onStart() {
                boat.speed = boat._speed * 3;
                boat.current_speed = boat._speed * 3; // Big temp boost to speed
            }

            @Override
            public void onEnd() {
                boat.speed = boat._speed;
            }
        }, 500);
    }
}
