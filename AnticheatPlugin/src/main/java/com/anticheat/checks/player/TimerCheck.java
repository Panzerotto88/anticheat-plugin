package com.anticheat.checks.player;

import com.anticheat.AnticheatPlugin;
import com.anticheat.MathUtils;
import com.anticheat.checks.Check;
import com.anticheat.data.PlayerData;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class TimerCheck extends Check {

    private static final double MIN_PACKET_DELTA = 40.0;

    public TimerCheck(AnticheatPlugin plugin) {
        super(plugin, "Timer", CheckType.PLAYER);
    }

    @Override
    public void check(Player player, PlayerData data) {
        if (canBypass(player)) return;
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;

        long now = System.currentTimeMillis();
        long packetTime = data.getLastPacketTime();

        if (packetTime == 0) {
            data.setLastPacketTime(now);
            return;
        }

        data.addPacketTime(now);
        data.setLastPacketTime(now);

        if (data.getPacketTimes().size() < 10) return;

        double packetDelta = data.getAveragePacketDelta();
        if (packetDelta < MIN_PACKET_DELTA) {
            data.incrementBuffer();
            if (data.getBuffer() > 5) {
                flag(player, "avg_packet=" + MathUtils.round(packetDelta, 1) + "ms");
            }
        } else {
            data.decrementBuffer();
        }
    }
}