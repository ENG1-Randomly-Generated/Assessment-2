package com.hardgforgif.dragonboatracing.persistence;

import com.hardgforgif.dragonboatracing.GameData;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates the GameData for loading and saving
 */
public class PersistentGameData {

    public int[] boatTypes = new int[4];
    public int[] standings = new int[4];
    public float[] penalties = new float[4];
    public List<Float[]> results = new ArrayList<>();
    public int currentLeg = 0;
    public float currentTimer = 0f;
    public float[] difficulty;

    /**
     * Generate persistent data from GameData
     *      This saves important GameData that is required for pertaining a game state
     */
    public PersistentGameData() {
        this.boatTypes = GameData.boatTypes;
        this.standings = GameData.standings;
        this.penalties = GameData.penalties;
        this.results = GameData.results;
        this.currentLeg = GameData.currentLeg;
        this.currentTimer = GameData.currentTimer;
        this.difficulty = GameData.difficulty;
    }

    /**
     * Load this saved state into the GameData
     */
    public void loadGameData() {
        GameData.boatTypes = this.boatTypes;
        GameData.standings = this.standings;
        GameData.penalties = this.penalties;
        GameData.results = this.results;
        GameData.currentLeg = this.currentLeg;
        GameData.currentTimer = this.currentTimer;
        GameData.difficulty = this.difficulty;
    }

}
