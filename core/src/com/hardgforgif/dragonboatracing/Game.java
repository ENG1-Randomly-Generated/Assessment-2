package com.hardgforgif.dragonboatracing;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.google.gson.Gson;
import com.hardgforgif.dragonboatracing.UI.GamePlayUI;
import com.hardgforgif.dragonboatracing.UI.MenuUI;
import com.hardgforgif.dragonboatracing.UI.PauseUI;
import com.hardgforgif.dragonboatracing.UI.ResultsUI;
import com.hardgforgif.dragonboatracing.core.*;
import com.hardgforgif.dragonboatracing.persistence.PersistentData;
import com.hardgforgif.dragonboatracing.powerups.HealthPowerup;
import com.hardgforgif.dragonboatracing.powerups.Powerup;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.function.Predicate;

public class Game extends ApplicationAdapter implements InputProcessor {

	private static final String SAVE_DIR = System.getProperty("user.dir") + "/saves/";
	private static final String SAVE_FILE = SAVE_DIR + "data.json";

	public Player player;
	public AI[] opponents = new AI[3];
	public Map[] map;
	public Batch batch;
	public Batch UIbatch;
	public OrthographicCamera camera;
	public World[] world;


	private Vector2 mousePosition = new Vector2();
	private Vector2 clickPosition = new Vector2();
	private boolean[] pressedKeys = new boolean[4]; // W, A, S, D buttons status

	private ArrayList<Body> toBeRemovedBodies = new ArrayList<>();
	private ArrayList<Body> toUpdateHealth = new ArrayList<>();


	@Override
	public void create() {
		// Initialize the sprite batches
		batch = new SpriteBatch();
		UIbatch = new SpriteBatch();

		// Get the values of the screen dimensions
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		// Initialize the camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, w, h);

		// Initialise the world and the map arrays
		this.resetWorld();

		// Set the app's input processor
		Gdx.input.setInputProcessor(this);

		GameData.currentUI = new MenuUI(this);
	}

	/**
	 * This method creates new ContactListener who's methods are executed when objects collide
	 * @param world This is the physics world in which the collisions happen
	 */
	private void createContactListener(World world){
		world.setContactListener(new ContactListener() {
			@Override
			public void beginContact(Contact contact) {
				Fixture fixtureA = contact.getFixtureA();
				Fixture fixtureB = contact.getFixtureB();

				// CHANGED: Adding extra contact listeners for powerups, but also reducing code redundencies here
				Object aObj = fixtureA.getBody().getUserData();
				Object bObj = fixtureB.getBody().getUserData();

				if (aObj instanceof Obstacle || aObj instanceof Powerup) {
					toBeRemovedBodies.add(fixtureA.getBody());
				} else if (bObj instanceof Obstacle || bObj instanceof Powerup) {
					toBeRemovedBodies.add(fixtureB.getBody());
				}

				if (aObj instanceof Obstacle && bObj instanceof Boat) {
					toUpdateHealth.add(fixtureB.getBody());
				} else if (bObj instanceof Obstacle && aObj instanceof Boat) {
					toUpdateHealth.add(fixtureA.getBody());
				}

				if (aObj instanceof Powerup && bObj instanceof Boat) {
					((Powerup) aObj).onCollide((Boat)bObj);
					((Powerup) aObj).used = true;
				} else if (bObj instanceof Powerup && aObj instanceof Boat) {
					((Powerup) bObj).onCollide((Boat)aObj);
					((Powerup) bObj).used = true;
				}
			}

			@Override
			public void endContact(Contact contact) {

			}

			@Override
			public void preSolve(Contact contact, Manifold manifold) {

			}

			@Override
			public void postSolve(Contact contact, ContactImpulse contactImpulse) {

			}
		});
	}

	/**
	 * Sets the camera y position at the y position of a player's sprite
	 * @param player The target player
	 */
	private void updateCamera(Player player) {
		camera.position.set(camera.position.x, player.boatSprite.getY() + 600, 0);
		camera.update();
	}

	/**
	 * Updates the GameData.standings array by comparing boats positions
	 */
	private void updateStandings(){
		// If the player hasn't finished the race...
		if(!player.hasFinished()){
			// Reset his position
			GameData.standings[0] = 1;

			// For every AI that is ahead, increment by 1
			for (Boat boat: opponents)
				if  (boat.boatSprite.getY() + boat.boatSprite.getHeight() / 2 > player.boatSprite.getY() + player.boatSprite.getHeight() / 2){
					GameData.standings[0]++;
				}

		}

		// Iterate through all the AIs to update their standings too
		for (int i = 0; i < 3; i++)
			// If the AI hasn't finished the race...
			if(!opponents[i].hasFinished()){
				// Reset his position
				GameData.standings[i + 1] = 1;

				// If the player is ahead, increment the standing by 1
				if (player.boatSprite.getY() > opponents[i].boatSprite.getY())
					GameData.standings[i + 1]++;

				// For every other AI that is ahead, increment by 1
				for (int j = 0; j < 3; j++)
					if(opponents[j].boatSprite.getY() > opponents[i].boatSprite.getY())
						GameData.standings[i + 1]++;
			}
	}

	/**
	 * Updates the GameData.results list by adding a new result every time a boat finishes the game
	 */
	private void checkForResults(){
		// If the player has finished and we haven't added his result already...
		if(player.hasFinished() && player.acceleration > 0 && GameData.results.size() < 4){
			// Add the result to the list with key 0, the player's lane
			GameData.results.add(new Float[]{0f, GameData.currentTimer});

			// Transition to the results UI
			GameData.showResultsState = true;
			GameData.currentUI = new ResultsUI();

			// Change the player's acceleration so the boat stops moving
			player.acceleration = -200f;
		}

		// Iterate through the AI to see if any of them finished the race
		for (int i = 0; i < 3; i++){
			// If the AI has finished and we haven't added his result already...
			if(opponents[i].hasFinished() && opponents[i].acceleration > 0 && GameData.results.size() < 4){
				// Add the result to the list with the his lane numer as key
				GameData.results.add(new Float[]{Float.valueOf(i + 1), GameData.currentTimer});

				// Change the AI's acceleration so the boat stops moving
				opponents[i].acceleration = -200f;
			}
		}
	}

	/**
	 * This method checks the position of all the boats to add penalties if necessary
	 */
	private void updatePenalties() {
		// Update the penalties for the player, if he is outside his lane
		float boatCenter = player.boatSprite.getX() + player.boatSprite.getWidth() / 2;
		if (!player.hasFinished() && player.robustness > 0 && (boatCenter < player.leftLimit || boatCenter > player.rightLimit)){
			GameData.penalties[0] += Gdx.graphics.getDeltaTime();
		}

		// Update the penalties for the opponents, if they are outside the lane
		for (int i = 0; i < 3; i++){
			boatCenter = opponents[i].boatSprite.getX() + opponents[i].boatSprite.getWidth() / 2;
			if (!opponents[i].hasFinished() && opponents[i].robustness > 0 &&(boatCenter < opponents[i].leftLimit || boatCenter > opponents[i].rightLimit)){
				GameData.penalties[i + 1] += Gdx.graphics.getDeltaTime();
			}
		}
	}


	/**
	 * This method marks all the boats that haven't finished the race as dnfs
	 */
	private void dnfRemainingBoats() {
		// If the player hasn't finished
		if (!player.hasFinished() && player.robustness > 0 && GameData.results.size() < 4){
			// Add a dnf result
			GameData.results.add(new Float[]{0f, Float.MAX_VALUE});

			// Transition to the showResult screen
			GameData.showResultsState = true;
			GameData.currentUI = new ResultsUI();
		}

		// Iterate through the AI and add a dnf result for any who haven't finished
		for (int i = 0; i < 3; i++){
			if (!opponents[i].hasFinished() && opponents[i].robustness > 0 && GameData.results.size() < 4)
				GameData.results.add(new Float[]{Float.valueOf(i + 1), Float.MAX_VALUE});
		}
	}

	@Override
	public void render() {
		// Reset the screen
		Gdx.gl.glClearColor(0.15f, 0.15f, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// If the game is in one of the static state
		if (GameData.mainMenuState || GameData.choosingBoatState || GameData.GameOverState || GameData.choosingDifficultyState || GameData.pauseState) {
			// Draw the UI and wait for the input

			GameData.currentUI.drawUI(UIbatch, mousePosition, Gdx.graphics.getWidth(), Gdx.graphics.getDeltaTime());
			GameData.currentUI.getInput(Gdx.graphics.getWidth(), clickPosition);

		}

		// Otherwise, if we are in the game play state
		else if (GameData.gamePlayState){

			// Check if the user just pressed ESC
			if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
				// Create our PauseUI and set it to the current UI
				GameData.pauseState = true;
				GameData.gamePlayState = false;
				GameData.currentUI = new PauseUI(this);
			}

			// If it's the first iteration in this state, the boats need to be created at their starting positions
			if (player == null){
				// Create the player boat
				int playerBoatType = GameData.boatTypes[0];
				player = new Player(GameData.boatsStats[playerBoatType][0], GameData.boatsStats[playerBoatType][1],
						GameData.boatsStats[playerBoatType][2], GameData.boatsStats[playerBoatType][3],
						playerBoatType, map[GameData.currentLeg].lanes[0]);
				player.createBoatBody(world[GameData.currentLeg], GameData.startingPoints[0][0], GameData.startingPoints[0][1], "Boat1.json");
				// Create the AI boats
				for (int i = 1; i <= 3; i++){
					int AIBoatType = GameData.boatTypes[i];
					opponents[i - 1] = new AI(GameData.boatsStats[AIBoatType][0], GameData.boatsStats[AIBoatType][1],
							GameData.boatsStats[AIBoatType][2], GameData.boatsStats[AIBoatType][3],
							AIBoatType, map[GameData.currentLeg].lanes[i]);
					opponents[i - 1].createBoatBody(world[GameData.currentLeg], GameData.startingPoints[i][0], GameData.startingPoints[i][1], "Boat1.json");
				}
			}

			// Iterate through the bodies that need to be removed from the world after a collision
			for (Body body : toBeRemovedBodies){
				// Find the obstacle that has this body and mark it as null
				// so it's sprite doesn't get rendered in future frames
				for (Lane lane : map[GameData.currentLeg].lanes) {
					// CHANGED: Now let's just remove the Obstacle object all-together, as it's not actually needed anymore
					lane.obstacles.removeIf(obstacle -> (obstacle.obstacleBody == body));
				}

				// Remove the body from the world to avoid other collisions with it
				world[GameData.currentLeg].destroyBody(body);
			}

			// Iterate through the bodies marked to be damaged after a collision
			for (Body body : toUpdateHealth){
				// if it's the player body
				if (player.boatBody == body && !player.hasFinished()){
					// Reduce the health and the speed
					player.robustness -= 10f;
					player.current_speed -= 30f;

					// If all the health is lost
					if(player.robustness <= 0 && GameData.results.size() < 4){
						// Remove the body from the world, but keep it's sprite in place
						world[GameData.currentLeg].destroyBody(player.boatBody);

						// Add a DNF result
						GameData.results.add(new Float[]{0f, Float.MAX_VALUE});

						// Transition to the show result screen
						GameData.showResultsState = true;
						GameData.currentUI = new ResultsUI();
					}
				}

				// Otherwise, one of the AI has to be updated similarly
				else {
					for (int i = 0; i < 3; i++){
						if (opponents[i].boatBody == body && !opponents[i].hasFinished()) {

							opponents[i].robustness -= 10f;
							opponents[i].current_speed -= 30f;

							if(opponents[i].robustness < 0 && GameData.results.size() < 4){
								world[GameData.currentLeg].destroyBody(opponents[i].boatBody);
								GameData.results.add(new Float[]{Float.valueOf(i + 1), Float.MAX_VALUE});
							}
						}

					}
				}

			}

			toBeRemovedBodies.clear();
			toUpdateHealth.clear();

			// Advance the game world physics
			world[GameData.currentLeg].step(1f/60f, 6, 2);
			// Update the timer
			GameData.currentTimer += Gdx.graphics.getDeltaTime();

			// Update the player's and the AI's movement
			player.updatePlayer(pressedKeys, Gdx.graphics.getDeltaTime());
			for (AI opponent : opponents)
				opponent.updateAI(Gdx.graphics.getDeltaTime());

			// Set the camera as the batches projection matrix
			batch.setProjectionMatrix(camera.combined);

			// Render the map
			map[GameData.currentLeg].renderMap(camera, batch);

			// Render the player and the AIs
			player.drawBoat(batch);
			for (AI opponent : opponents)
				opponent.drawBoat(batch);

			// Render the objects that weren't destroyed yet
			for (Lane lane : map[GameData.currentLeg].lanes)
				for (Obstacle obstacle : lane.obstacles){
					obstacle.drawObstacle(batch);
				}

			// Render powerups that aren't used
			for (Lane lane : map[GameData.currentLeg].lanes) {
				for (Powerup powerup : lane.powerups) {
					if (!powerup.used) {
						powerup.draw(batch);
					}
					powerup.tick();
				}
			}

			// Update the camera at the player's position
			updateCamera(player);

			updatePenalties();

			// Update the standings of each boat
			updateStandings();

			// If it's been 15 seconds since the winner completed the race, dnf all boats who haven't finished yet
			// Then transition to the result state
			if(GameData.results.size() > 0 && GameData.results.size() < 4 &&
					GameData.currentTimer > GameData.results.get(0)[1] + 15f){
				dnfRemainingBoats();
				GameData.showResultsState = true;
				GameData.currentUI = new ResultsUI();
			}
			// Otherwise keep checking for new results
			else {
				checkForResults();
			}


			// Choose which UI to display based on the current state
			if(!GameData.showResultsState)
				GameData.currentUI.drawPlayerUI(UIbatch, player);
			else {
				GameData.currentUI.drawUI(UIbatch, mousePosition, Gdx.graphics.getWidth(), Gdx.graphics.getDeltaTime());
				GameData.currentUI.getInput(Gdx.graphics.getWidth(), clickPosition);
			}

		}

		// Otherwise we need need to reset elements of the game to prepare for the next race
		else if(GameData.resetGameState){
			player = null;
			for (int i = 0; i < 3; i++)
				opponents[i] = null;
			GameData.results.clear();
			GameData.currentTimer = 0f;
			GameData.penalties = new float[4];

			// If we're coming from the result screen, then we need to advance to the next leg
			if (GameData.showResultsState){
				GameData.currentLeg += 1;
				GameData.showResultsState = false;
				GameData.gamePlayState = true;
				GameData.currentUI = new GamePlayUI();

			}
			// Otherwise we're coming from the endgame screen so we need to return to the main menu
			else{
				// Reset everything for the next game
				this.resetWorld();
				GameData.currentLeg = 0;
				GameData.mainMenuState = true;
				GameData.currentUI = new MenuUI(this);
			}
			GameData.resetGameState = false;

		}

		// If we haven't clicked anywhere in the last frame, reset the click position
		if(clickPosition.x != 0f && clickPosition.y != 0f)
			clickPosition.set(0f,0f);
	}

	/**
	 * Reset the world to default state
	 */
	public void resetWorld() {
		// CHANGED: Added this as this exact code was repeated twice

		camera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
		camera.update();
		world = new World[3];
		map = new Map[3];
		for (int i = 0; i < 3; i++){
			// Initialize the physics game World
			world[i] = new World(new Vector2(0f, 0f), true);

			// Initialize the map
			map[i] = new Map("Map1/Map1.tmx", Gdx.graphics.getWidth());

			// Calculate the ratio between pixels, meters and tiles
			GameData.TILES_TO_METERS = map[i].getTilesToMetersRatio();
			GameData.PIXELS_TO_TILES = 1/(GameData.METERS_TO_PIXELS * GameData.TILES_TO_METERS);

			// Create the collision with the land
			map[i].createMapCollisions("CollisionLayerLeft", world[i]);
			map[i].createMapCollisions("CollisionLayerRight", world[i]);

			// Create the lanes, and the obstacles in the physics game world
			map[i].createLanes(world[i], 20 + (10 * i), 5); // CHANGED; num of obstacles change
																			// as legs progress

			// Create the finish line
			map[i].createFinishLine("finishLine.png");

			// Create a new collision handler for the world
			createContactListener(world[i]);
		}
	}

	/**
	 * Save the game's current state
	 */
	public void save() {
		// Create directory if required
		File saveDir = new File(SAVE_DIR);
		if (!saveDir.exists()) {
			saveDir.mkdir();
		}

		Gson gson = new Gson();
		String data;
		try {
			data = gson.toJson(new PersistentData(this));
		} catch(Exception e) {
			e.printStackTrace();
			return;
		}
		try {
			FileWriter writer = new FileWriter(SAVE_FILE);
			writer.write(data);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Load the game from save file
	 */
	public void load() {
		// Create directory if required
		File saveDir = new File(SAVE_DIR);
		if (!saveDir.exists()) {
			return;
		}

		Gson gson = new Gson();
		try {
			String raw = new String(Files.readAllBytes(Paths.get(SAVE_FILE)));
			PersistentData data = gson.fromJson(raw, PersistentData.class);
			data.load(this);
			GameData.mainMenuState = false;
			GameData.gamePlayState = true;
			GameData.currentUI = new GamePlayUI();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Return whether there exists a save which can be loaded
	 */
	public boolean hasSave() {
		return new File(SAVE_DIR).exists();
	}


	public void dispose() {
		world[GameData.currentLeg].dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Input.Keys.W)
			pressedKeys[0] = true;
		if (keycode == Input.Keys.A)
			pressedKeys[1] = true;
		if (keycode == Input.Keys.S)
			pressedKeys[2] = true;
		if (keycode == Input.Keys.D)
			pressedKeys[3] = true;
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Input.Keys.W)
			pressedKeys[0] = false;
		if (keycode == Input.Keys.A)
			pressedKeys[1] = false;
		if (keycode == Input.Keys.S)
			pressedKeys[2] = false;
		if (keycode == Input.Keys.D)
			pressedKeys[3] = false;
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Vector3 position = camera.unproject(new Vector3(screenX, screenY, 0));
		clickPosition.set(position.x, position.y);
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		Vector3 position = camera.unproject(new Vector3(screenX, screenY, 0));
		mousePosition.set(position.x, position.y);
		return true;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
