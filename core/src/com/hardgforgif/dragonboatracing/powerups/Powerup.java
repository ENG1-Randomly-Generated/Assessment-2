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

    private final String PATH_PREFIX = "Powerups/";

    private final Texture texture;
    public final String typeName;

    public Sprite sprite;
    public float scale;
    public Body body;


    public Powerup(Texture texture, String typeName) {
        this.texture = texture;
        this.typeName = typeName;
    }

    /**
     * Abstract method to define behaviour of colliding with a boat
     *  This is where we define our powerup-specifics
     * @param boat Boat collided with
     */
    public abstract void onCollide(Boat boat);

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


}
