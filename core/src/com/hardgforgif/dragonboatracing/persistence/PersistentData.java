package com.hardgforgif.dragonboatracing.persistence;


import com.hardgforgif.dragonboatracing.Game;
import com.hardgforgif.dragonboatracing.core.AI;
import com.hardgforgif.dragonboatracing.core.Boat;
import com.hardgforgif.dragonboatracing.core.Player;

/**
 * This class encapsulates all the data required to save and load a game state
 */
public class PersistentData {

    PersistentBoatData player;
    PersistentBoatData[] opponents;
    PersistentGameData gameData;


    /**
     * Generate persistent data for the entire game
     *      This should encapsulate the entire game data for reloading
     */
    public PersistentData(Game game) {
        this.player = new PersistentBoatData(game.player, 0);
        this.opponents = new PersistentBoatData[game.opponents.length];
        for(int i = 0; i < game.opponents.length; i++) {
            this.opponents[i] = new PersistentBoatData(game.opponents[i], i + 1);
        }

        this.gameData = new PersistentGameData();
    }

    /**
     * Load this saved game state
     * @param game Game to load into
     */
    public void load(Game game) {
        this.gameData.loadGameData();
        game.player = this.player.toPlayer(game);
        game.opponents = new AI[opponents.length];
        for(int i = 0; i < game.opponents.length; i++) {
            game.opponents[i] = this.opponents[i].toAI(game);
        }
    }

}
