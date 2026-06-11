package com.anticheat.checks.movement;

import com.anticheat.AnticheatPlugin;
import com.anticheat.MathUtils;
import com.anticheat.checks.Check;
import com.anticheat.data.PlayerData;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class VelocityCheck extends Check {

    private static final double MIN_VELOCITY_RATIO = 0.05;

    public VelocityCheck(AnticheatPlugin plugin) {
        super(plugin, "Velocity", CheckType.MOVEMENT);
    }

    @Override
    public void check(Player player, PlayerData data) {
        if (canBypass(player)) return;
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;
        if (player.getVehicle() != null) return;

        // Controlla se il giocatore ha ricevuto velocity di recente
        if (!data.isVelocityTaken()) return;
        if (data.getSinceVelocityTicks() > 5) return;

        double horizontalVelocity = Math.sqrt(
                data.getVelocityX() * data.getVelocityX() +
                data.getVelocityZ() * data.getVelocityZ()
        );

        // Se la velocity applicata era orizzontale significativa
        if (horizontalVelocity > 0.1) {
            double deltaXZ = data.getLastDeltaXZ();
            double ratio = deltaXZ / horizontalVelocity;

            // Se il giocatore ha preso pochissima velocity orizzontale
            if (ratio < MIN_VELOCITY_RATIO) {
                data.incrementBuffer();
                if (data.getBuffer() > 2) {
                    flag(player, "ratio=" + MathUtils.round(ratio, 4) +
                            " applied=" + MathUtils.round(horizontalVelocity, 4) +
                            " taken=" + MathUtils.round(deltaXZ, 4));
                }
            } else {
                data.decrementBuffer();
            }
        }
    }
}