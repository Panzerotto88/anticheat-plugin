package com.anticheat.checks.other;

import com.anticheat.AnticheatPlugin;
import com.anticheat.checks.Check;
import com.anticheat.data.PlayerData;
import org.bukkit.entity.Player;

public class PacketCheck extends Check {

    private static final int MAX_PACKETS_PER_SECOND = 25;
    private static final int MAX_KEEPALIVE_DELAY = 5000;

    public PacketCheck(AnticheatPlugin plugin) {
        super(plugin, "Packet", CheckType.OTHER);
    }

    @Override
    public void check(Player player, PlayerData data) {
        if (canBypass(player)) return;

        // Controlla troppi pacchetti al secondo (DoS via movement)
        double packetDelta = data.getAveragePacketDelta();
        if (packetDelta > 0) {
            double packetsPerSecond = 1000.0 / packetDelta;

            // Massimo ~20 pacchetti al secondo normali, se troppo è anomalo
            if (packetsPerSecond > MAX_PACKETS_PER_SECOND && data.getPacketTimes().size() > 20) {
                data.incrementBuffer();
                if (data.getBuffer() > 5) {
                    flag(player, "packet_flood pps=" + Math.round(packetsPerSecond) +
                            " avg_delta=" + Math.round(packetDelta) + "ms");
                }
            } else {
                data.decrementBuffer();
            }
        }
    }
}