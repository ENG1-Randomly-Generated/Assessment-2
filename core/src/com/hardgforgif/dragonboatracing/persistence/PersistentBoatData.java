package com.hardgforgif.dragonboatracing.persistence;

import com.badlogic.gdx.math.Vector2;
import com.hardgforgif.dragonboatracing.Game;
import com.hardgforgif.dragonboatracing.GameData;
import com.hardgforgif.dragonboatracing.core.*;
import com.hardgforgif.dragonboatracing.powerups.Powerup;

import java.util.ArrayList;

public class PersistentBoatData {

    float robustness;
    float stamina;
    public float maneuverability;
    public float speed;
    public float acceleration;
    public float current_speed;
    public float targetAngle;
    public int boatType;

    public Vector2 position;
    public int laneNumber;

    public PersistentObstacleData[] obstacles;
    public PersistentPowerupData[] powerups;


    public PersistentBoatData(Boat boat, int laneNumber) {
        this.robustness = boat.robustness;
        this.stamina = boat.stamina;
        this.maneuverability = boat.maneuverability;
        this.speed = boat.speed;
        this.acceleration = boat.acceleration;
        this.current_speed = boat.current_speed;
        this.targetAngle = boat.targetAngle;
        this.boatType = boat.boatType;
        this.laneNumber = laneNumber;

        this.position = boat.boatBody.getPosition();

        // Get obstacles in this boat's lane and convert them to persistent information
        this.obstacles = new PersistentObstacleData[boat.lane.obstacles.size()];
        for (int i = 0; i < boat.lane.obstacles.size(); i++) {
            this.obstacles[i] = new PersistentObstacleData(boat.lane.obstacles.get(i));
        }

        // Get powerups in this boat's lane
        this.powerups = new PersistentPowerupData[boat.lane.powerups.size()];
        for (int i = 0; i < boat.lane.powerups.size(); i++) {
            this.powerups[i] = new PersistentPowerupData(boat.lane.powerups.get(i));
        }
    }

    private void loadObstacles(Game game, Lane lane) {
        // Start by removing all the current obstacles
        for (Obstacle obstacle : lane.obstacles) {
            game.world[GameData.currentLeg].destroyBody(obstacle.obstacleBody);
        }
        lane.obstacles.clear();

        // Now add our new obstacles
        for (int i = 0; i < this.obstacles.length; i++) {
            lane.obstacles.add(this.obstacles[i].toObstacle(game));
        }
    }

    private void loadPowerups(Game game, Lane lane) {
        // Start by removing all current powerups
        for (Powerup powerup : lane.powerups) {
            game.world[GameData.currentLeg].destroyBody(powerup.body);
        }
        lane.powerups.clear();

        // Now add our saved powerups
        for (int i = 0; i < this.powerups.length; i++) {
            lane.powerups.add(this.powerups[i].toPowerup(game));
        }
    }


    public Player toPlayer(Game game) {
        Lane lane = game.map[GameData.currentLeg].lanes[this.laneNumber];

        // Create our Player
        Player boat = new Player(this.robustness, this.speed, this.acceleration, this.maneuverability, this.boatType, lane);
        boat.createBoatBody(game.world[GameData.currentLeg], this.position.x, this.position.y, "Boat1.json");
        boat.acceleration = this.acceleration;
        boat.stamina = this.stamina;
        boat.current_speed = this.current_speed;
        boat.targetAngle = this.targetAngle;

        this.loadObstacles(game, lane);
        this.loadPowerups(game, lane);

        return boat;
    }

    public AI toAI(Game game) {
        Lane lane = game.map[GameData.currentLeg].lanes[this.laneNumber];

        // Create our AI
        AI boat = new AI(this.robustness, this.speed, this.acceleration, this.maneuverability, this.boatType, lane);
        boat.createBoatBody(game.world[GameData.currentLeg], this.position.x, this.position.y, "Boat1.json");
        boat.acceleration = this.acceleration;
        boat.stamina = this.stamina;
        boat.current_speed = this.current_speed;
        boat.targetAngle = this.targetAngle;

        this.loadObstacles(game, lane);
        this.loadPowerups(game, lane);
        return boat;
    }

}
