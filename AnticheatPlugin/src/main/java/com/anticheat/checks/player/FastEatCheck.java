package com.anticheat.checks.player;

import com.anticheat.AnticheatPlugin;
import com.anticheat.checks.Check;
import com.anticheat.data.PlayerData;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class FastEatCheck extends Check {

    private static final long MIN_EAT_TIME = 1500; // ms minimi per mangiare

    public FastEatCheck(AnticheatPlugin plugin) {
        super(plugin, "FastEat", CheckType.PLAYER);
    }

    @Override
    public void check(Player player, PlayerData data) {
        if (canBypass(player)) return;
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;

        long lastEat = data.getLastEatTime();
        if (lastEat == 0) return;

        long now = System.currentTimeMillis();
        long eatDuration = now - lastEat;

        // Il cibo richiede ~1.5 secondi (30 tick) per essere consumato
        if (eatDuration < MIN_EAT_TIME && eatDuration > 0) {
            data.incrementBuffer();
            if (data.getBuffer() > 2) {
                flag(player, "fast_eat time=" + eatDuration + "ms min=" + MIN_EAT_TIME + "ms");
            }
        } else {
            data.decrementBuffer();
        }
    }
}