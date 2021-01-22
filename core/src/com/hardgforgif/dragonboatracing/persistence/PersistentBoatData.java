package com.hardgforgif.dragonboatracing.persistence;

import com.badlogic.gdx.math.Vector2;
import com.hardgforgif.dragonboatracing.Game;
import com.hardgforgif.dragonboatracing.GameData;
import com.hardgforgif.dragonboatracing.core.*;

public class PersistentBoatData {

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

    public PersistentObstacleData[] obstacles;


    public PersistentBoatData(Boat boat, int laneNumber) {
        this.robustness = boat.robustness;
        this.stamina = boat.stamina;
        this.maneuverability = boat.maneuverability;
        this.speed = boat.speed;
        this.acceleration = boat.acceleration;
        this.current_speed = boat.current_speed;
        this.turningSpeed = boat.turningSpeed;
        this.targetAngle = boat.targetAngle;
        this.boatType = boat.boatType;
        this.laneNumber = laneNumber;

        this.position = boat.boatBody.getPosition();

        // Get obstacles in this boat's lane and convert them to persistent information
        this.obstacles = new PersistentObstacleData[boat.lane.obstacles.length];
        for (int i = 0; i < boat.lane.obstacles.length; i++) {
            this.obstacles[i] = new PersistentObstacleData(boat.lane.obstacles[i]);
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
        boat.turningSpeed = this.turningSpeed;
        boat.targetAngle = this.targetAngle;

        // Now overwrite the lane with our obstacles
        lane.obstacles = new Obstacle[lane.obstacles.length];
        for (int i = 0; i < lane.obstacles.length; i++) {
            lane.obstacles[i] = this.obstacles[i].toObstacle(game);
        }


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
        boat.turningSpeed = this.turningSpeed;
        boat.targetAngle = this.targetAngle;

        // Delete all currently created obstacles
        for (int i = 0; i < lane.obstacles.length; i++) {
            // Set this to be deleted
            game.removeBody(lane.obstacles[i].obstacleBody);
        }

        // Now fill the lane with our obstacles
        lane.obstacles = new Obstacle[lane.obstacles.length];
        for (int i = 0; i < lane.obstacles.length; i++) {
            lane.obstacles[i] = this.obstacles[i].toObstacle(game);
        }


        return boat;
    }

}
