package com.hardgforgif.dragonboatracing.persistence;

import com.badlogic.gdx.math.Vector2;
import com.hardgforgif.dragonboatracing.Game;
import com.hardgforgif.dragonboatracing.GameData;
import com.hardgforgif.dragonboatracing.core.Obstacle;

public class PersistentObstacleData {

    public Vector2 position;
    public int type;
    public float scale;


    public PersistentObstacleData(Obstacle obstacle) {
        this.position = new Vector2(obstacle.obstacleSprite.getX() / GameData.METERS_TO_PIXELS, obstacle.obstacleSprite.getY() / GameData.METERS_TO_PIXELS);
        this.type = obstacle.type;
        this.scale = obstacle.scale;
    }

    public Obstacle toObstacle(Game game) {
        Obstacle ob = new Obstacle(type);
        ob.createObstacleBody(game.world[GameData.currentLeg], this.position.x, this.position.y, this.scale);
        return ob;
    }

}
