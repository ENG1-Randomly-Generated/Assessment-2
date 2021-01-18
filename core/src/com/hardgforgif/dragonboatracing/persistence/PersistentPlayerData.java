package com.hardgforgif.dragonboatracing.persistence;

import com.badlogic.gdx.math.Vector2;
import com.hardgforgif.dragonboatracing.Game;
import com.hardgforgif.dragonboatracing.GameData;
import com.hardgforgif.dragonboatracing.core.Boat;
import com.hardgforgif.dragonboatracing.core.Player;

public class PersistentPlayerData {

    float robustness;
    float stamina;
    public float maneuverability;
    public float speed;
    public float acceleration;
    public float current_speed;
    public float turningSpeed;
    public float targetAngle;
    public int boatType;

    public Vector2 position;
    public int laneNumber;


    public PersistentPlayerData(Boat boat) {
        this.robustness = boat.robustness;
        this.stamina = boat.stamina;
        this.maneuverability = boat.maneuverability;
        this.speed = boat.speed;
        this.acceleration = boat.acceleration;
        this.current_speed = boat.current_speed;
        this.turningSpeed = boat.turningSpeed;
        this.targetAngle = boat.targetAngle;
        this.boatType = boat.boatType;

        this.position = boat.boatBody.getPosition();
    }

    public Player toPlayer(Game game) {
        Player boat = new Player(this.robustness, this.speed, this.acceleration, this.maneuverability, this.boatType,
                game.map[GameData.currentLeg].lanes[0]);
        boat.createBoatBody(game.world[GameData.currentLeg], this.position.x, this.position.y, "Boat1.json");
        boat.acceleration = this.acceleration;
        boat.stamina = this.stamina;
        boat.current_speed = this.current_speed;
        boat.turningSpeed = this.turningSpeed;
        boat.targetAngle = this.targetAngle;
        return boat;
    }

}
