package com.hardgforgif.dragonboatracing.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.hardgforgif.dragonboatracing.BodyEditorLoader;
import com.hardgforgif.dragonboatracing.GameData;
import com.hardgforgif.dragonboatracing.core.Boat;

public abstract class Powerup {

    protected interface TempPowerup {
        void onStart();
        void onEnd();
    }

    private final String PATH_PREFIX = "Powerups/";

    private final Texture texture;
    public final String typeName;

    public Sprite sprite;
    public float scale;
    public Body body;
    public boolean used;

    private TempPowerup tempPowerup;
    private long endTime;


    public Powerup(Texture texture, String typeName) {
        this.texture = texture;
        this.typeName = typeName;
        this.used = false;
    }

    /**
     * Abstract method to define behaviour of colliding with a boat
     *  This is where we define our powerup-specifics
     * @param boat Boat collided with
     */
    public abstract void onCollide(Boat boat);


    /**
     * Register a temporary powerup for the given amount of time
     *  The tp.onStart() will be run, and after time milliseconds, the tp.onEnd() will be run
     * @param tp TempPowerup definition
     * @param time Time in milliseconds before start and end
     */
    public void registerTempPowerup(TempPowerup tp, long time) {
        this.endTime = System.currentTimeMillis() + time;
        this.tempPowerup = tp;
        tp.onStart();
    }



    /**
     * Creates a body for the powerup
     * @param world World to create the body in
     * @param posX x location of the body, in meters
     * @param posY y location of the body, in meters
     */
    public void createObstacleBody(World world, float posX, float posY, float scale){
        this.scale = scale;

        sprite = new Sprite(texture);
        sprite.scale(scale);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        bodyDef.position.set(posX, posY);
        body = world.createBody(bodyDef);

        body.setUserData(this);

        BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal(PATH_PREFIX + typeName + ".json"));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 0f;
        fixtureDef.restitution = 0f;
        fixtureDef.friction = 0f;

        scale = 1/GameData.METERS_TO_PIXELS;
        loader.attachFixture(body, "Name", fixtureDef, scale);

        sprite.setPosition((body.getPosition().x * GameData.METERS_TO_PIXELS) - sprite.getWidth() / 2,
                (body.getPosition().y * GameData.METERS_TO_PIXELS) - sprite.getHeight() / 2);
    }

    /**
     * Draw the powerup
     * @param batch Batch to draw on
     */
    public void draw(Batch batch){

        sprite.setPosition((body.getPosition().x * GameData.METERS_TO_PIXELS) - sprite.getWidth() / 2,
                (body.getPosition().y * GameData.METERS_TO_PIXELS) - sprite.getHeight() / 2);
        batch.begin();
        batch.draw(sprite, sprite.getX(), sprite.getY(), sprite.getOriginX(),
                sprite.getOriginY(),
                sprite.getWidth(), sprite.getHeight(), sprite.getScaleX(),
                sprite.getScaleY(), sprite.getRotation());
        batch.end();
    }

    /**
     * Run every tick of the game, whether visible or not
     */
    public void tick() {
        // If we have a tempPowerup, check it and complete if needed
        if (tempPowerup != null && endTime < System.currentTimeMillis()) {
            tempPowerup.onEnd();
            tempPowerup = null;
        }
    }


}
