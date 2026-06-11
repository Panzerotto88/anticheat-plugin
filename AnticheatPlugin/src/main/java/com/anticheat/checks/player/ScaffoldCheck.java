package com.anticheat.checks.player;

import com.anticheat.AnticheatPlugin;
import com.anticheat.MathUtils;
import com.anticheat.checks.Check;
import com.anticheat.data.PlayerData;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class ScaffoldCheck extends Check {

    public ScaffoldCheck(AnticheatPlugin plugin) {
        super(plugin, "Scaffold", CheckType.PLAYER);
    }

    @Override
    public void check(Player player, PlayerData data) {
        if (canBypass(player)) return;
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;
        if (!data.isPlacingBlocks()) return;

        float pitch = player.getLocation().getPitch();
        float yaw = player.getLocation().getYaw();
        float yawDelta = data.getLastYawDelta();
        float pitchDelta = data.getLastPitchDelta();

        // Scaffold direction check: il giocatore guarda sempre verso il basso
        if (pitch > 75 && data.getPitchTicks() > 5) {
            // Se il giocatore guarda sempre verso il basso e ha rotazioni costanti
            if (Math.abs(yawDelta) < 0.1 && Math.abs(pitchDelta) < 0.1) {
                data.incrementBuffer();
                if (data.getBuffer() > 10) {
                    flag(player, "constant_rotation pitch=" + MathUtils.round(pitch, 2) +
                            " yaw_delta=" + MathUtils.round(yawDelta, 4));
                }
            } else {
                data.decrementBuffer();
            }
        } else {
            data.decrementBuffer();
        }

        // Angolo giroscopico: scaffold modifica yaw a 45° esatti o altre divisioni perfette
        if (data.isPlacingBlocks() && data.getPitchTicks() > 3) {
            float normalizedYaw = Math.abs(yaw % 90);
            // Scaffold spesso si allinea a multipli di 45, 90, 180 gradi
            if (normalizedYaw < 1 || Math.abs(normalizedYaw - 45) < 1 || Math.abs(normalizedYaw - 90) < 1) {
                data.incrementBuffer();
                if (data.getBuffer() > 15) {
                    flag(player, "perfect_angle yaw=" + MathUtils.round(yaw, 2));
                }
            } else {
                data.decrementBuffer();
            }
        }
    }
}