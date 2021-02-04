package com.hardgforgif.dragonboatracing.persistence;

import com.badlogic.gdx.math.Vector2;
import com.hardgforgif.dragonboatracing.Game;
import com.hardgforgif.dragonboatracing.GameData;
import com.hardgforgif.dragonboatracing.core.Obstacle;

public class PersistentObstacleData {

    public Vector2 position;
    public int type;
    public float scale;


    /**
     * Generate persistent data for a given obstacle
     * @param obstacle Obstacle to generate persistent data for
     */
    public PersistentObstacleData(Obstacle obstacle) {
        this.position = obstacle.obstacleBody.getPosition();
        this.type = obstacle.type;
        this.scale = obstacle.scale;
    }

    /**
     * Create an Obstacle object for this given saved obstacle state
     * @param game Game we are loading into
     * @return Obstacle - Obstacle object that reflects this saved state
     */
    public Obstacle toObstacle(Game game) {
        Obstacle ob = new Obstacle(type);
        ob.createObstacleBody(game.world[GameData.currentLeg], this.position.x, this.position.y, this.scale);
        return ob;
    }

}
