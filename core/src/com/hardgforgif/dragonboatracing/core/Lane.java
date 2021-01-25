package com.hardgforgif.dragonboatracing.core;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.hardgforgif.dragonboatracing.GameData;

import java.util.ArrayList;
import java.util.Random;

public class Lane {
    public float[][] leftBoundry;
    public int leftIterator = 0;
    public float[][] rightBoundry;
    public int rightIterator = 0;
    private MapLayer leftLayer;
    private MapLayer rightLayer;
    public ArrayList<Obstacle> obstacles; // CHANGED: Changed to ArrayList for dynamically updating this list
                                          // It is much easier if we have the lane's obstacles ACTUALLY reflect the obstacles, rather than some redundant
                                          // dead ones

    public Lane(int mapHeight, MapLayer left, MapLayer right){
        leftBoundry = new float[mapHeight][2];
        rightBoundry = new float[mapHeight][2];

        leftLayer = left;
        rightLayer = right;

        this.obstacles = new ArrayList<Obstacle>();
    }

    /**
     * Construct bodies that match the lane separators
     * @param unitScale The size of a tile in pixels
     */
    public void constructBoundries(float unitScale){
        MapObjects objects = leftLayer.getObjects();

        for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)){
            Rectangle rectangle = rectangleObject.getRectangle();
            float height = rectangle.getY() * unitScale;
            float limit = (rectangle.getX() * unitScale) + (rectangle.getWidth() * unitScale);
            leftBoundry[leftIterator][0] = height;
            leftBoundry[leftIterator++][1] = limit;
        }

        objects = rightLayer.getObjects();

        for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)){
            Rectangle rectangle = rectangleObject.getRectangle();
            float height = rectangle.getY() * unitScale;
            float limit = rectangle.getX() * unitScale;
            rightBoundry[rightIterator][0] = height;
            rightBoundry[rightIterator++][1] = limit;
        }
    }

    public float[] getLimitsAt(float yPosition){
        float[] lst = new float[2];
        int i;
        for (i = 1; i < leftIterator; i++){
            if (leftBoundry[i][0] > yPosition) {
                break;
            }
        }
        lst[0] = leftBoundry[i - 1][1];

        for (i = 1; i < rightIterator; i++){
            if (rightBoundry[i][0] > yPosition) {
                break;
            }
        }
        lst[1] = rightBoundry[i - 1][1];
        return lst;
    }

    /**
     * Spawn obstacles on the lane
     * @param world World to spawn obstacles in
     * @param mapHeight Height of the map to draw on
     * @param n Number of obstacles to create
     */
    public void spawnObstacles(World world, float mapHeight, int n){
        float segmentLength = mapHeight / n;
        for (int i = 0; i < n; i++){
            int randomIndex = new Random().nextInt(6);
            float scale = 0f;
            if (randomIndex == 0 || randomIndex == 5)
                scale = -0.8f;
            Obstacle newObstacle = new Obstacle(randomIndex + 1);
            float segmentStart = i * segmentLength;
            float yPos = (float) (600f + (segmentStart + Math.random() * segmentLength));

            float[] limits = this.getLimitsAt(yPos);
            float leftLimit = limits[0] + 50;
            float rightLimit = limits[1];
            float xPos = (float) (leftLimit + Math.random() * (rightLimit - leftLimit));


            newObstacle.createObstacleBody(world, xPos / GameData.METERS_TO_PIXELS, yPos / GameData.METERS_TO_PIXELS, scale);

            this.obstacles.add(newObstacle);
        }
    }

}
