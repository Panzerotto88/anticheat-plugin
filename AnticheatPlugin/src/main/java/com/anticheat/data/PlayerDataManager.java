package com.anticheat.data;

import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDataManager {

    private final Map<UUID, PlayerData> dataMap;

    public PlayerDataManager() {
        this.dataMap = new ConcurrentHashMap<>();
    }

    public PlayerData getData(Player player) {
        return dataMap.computeIfAbsent(player.getUniqueId(), k -> new PlayerData(player));
    }

    public PlayerData getData(UUID uuid) {
        return dataMap.get(uuid);
    }

    public PlayerData createData(Player player) {
        PlayerData data = new PlayerData(player);
        dataMap.put(player.getUniqueId(), data);
        return data;
    }

    public void removeData(Player player) {
        dataMap.remove(player.getUniqueId());
    }

    public void removeData(UUID uuid) {
        dataMap.remove(uuid);
    }

    public boolean hasData(Player player) {
        return dataMap.containsKey(player.getUniqueId());
    }

    public void clear() {
        dataMap.clear();
    }

    public int size() {
        return dataMap.size();
    }
}