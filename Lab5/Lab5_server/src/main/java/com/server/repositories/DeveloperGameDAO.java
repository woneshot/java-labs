package com.server.repositories;

import com.server.interfaces.DAO;
import com.server.model.entities.Developer;
import com.server.model.entities.Game;
import com.server.utils.Pair;

import java.util.ArrayList;
import java.util.List;

public class DeveloperGameDAO {
    private static List<Pair<Integer, Integer>> links = new ArrayList<>();

    public void addLink(int gameId, int developerId) {
        Pair<Integer, Integer> pair = new Pair<>(gameId, developerId);
        if (!links.contains(pair)) {
            links.add(pair);
        }
    }

    public void removeLink(int gameId, int developerId) {
        links.remove(new Pair<>(gameId, developerId));
    }

    public List<Integer> getDeveloperIdsByGame(int gameId) {
        List<Integer> developerIds = new ArrayList<>();
        for (Pair<Integer, Integer> pair : links) {
            if (pair.getFirst().equals(gameId)) {
                developerIds.add(pair.getSecond());
            }
        }
        return developerIds;
    }

    public List<Integer> getGameIdsByDeveloper(int developerId) {
        List<Integer> gameIds = new ArrayList<>();
        for (Pair<Integer, Integer> pair : links) {
            if (pair.getSecond().equals(developerId)) {
                gameIds.add(pair.getFirst());
            }
        }
        return gameIds;
    }
}