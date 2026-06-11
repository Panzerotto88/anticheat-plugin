package com.anticheat.checks.player;

import com.anticheat.AnticheatPlugin;
import com.anticheat.MathUtils;
import com.anticheat.checks.Check;
import com.anticheat.data.PlayerData;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class NoSlowCheck extends Check {

    public NoSlowCheck(AnticheatPlugin plugin) {
        super(plugin, "NoSlow", CheckType.PLAYER);
    }

    @Override
    public void check(Player player, PlayerData data) {
        if (canBypass(player)) return;
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;
        if (!player.isHandRaised()) return;

        double deltaXZ = data.getLastDeltaXZ();
        double maxSpeed = 0.12;

        if (player.isSprinting()) {
            maxSpeed = 0.15;
        }

        maxSpeed += 0.08;

        if (deltaXZ > maxSpeed && deltaXZ > 0.2) {
            data.incrementBuffer();
            if (data.getBuffer() > 4) {
                flag(player, "speed=" + MathUtils.round(deltaXZ, 4) + " max=" + MathUtils.round(maxSpeed, 4));
            }
        } else {
            data.decrementBuffer();
        }
    }
}