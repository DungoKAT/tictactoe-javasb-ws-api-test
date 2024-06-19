package com.tictactoe.back_end.storage;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;

import com.tictactoe.back_end.model.Game;

public class GameStorage {
    private static GameStorage instance;
    private static Map<String, Game> games;
    
    private GameStorage() {
        games = new HashMap<>();
    }

    public static synchronized GameStorage getInstance() {
        if(instance == null) {
            instance = new GameStorage();
        }
        return instance;
    }

    public void addGame(Game game) {
        games.put(game.getGameId(), game);
    }

    public Collection<Game> getAllGames() {
        return games.values();
    }

    public Game getGameById(String gameId) {
        return games.get(gameId);
    }

    public Set<String> getAllGameIdKeys() {
        return games.keySet();
    }

    public boolean containsGameById(String gameId) {
        return games.containsKey(gameId);
    }

    public void deleteGameById(String gameId) {
        games.remove(gameId);
    }
}
