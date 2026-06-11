package com.anticheat.checks.other;

import com.anticheat.AnticheatPlugin;
import com.anticheat.MathUtils;
import com.anticheat.checks.Check;
import com.anticheat.data.PlayerData;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class BadPacketsCheck extends Check {

    public BadPacketsCheck(AnticheatPlugin plugin) {
        super(plugin, "BadPackets", CheckType.OTHER);
    }

    @Override
    public void check(Player player, PlayerData data) {
        if (canBypass(player)) return;

        // Controlla yaw impossibile
        float yaw = player.getLocation().getYaw();
        float pitch = player.getLocation().getPitch();

        // Pitch impossibile (oltre i limiti vanilla)
        if (pitch > 90 || pitch < -90) {
            data.incrementBuffer();
            if (data.getBuffer() > 1) {
                flag(player, "impossible_pitch pitch=" + pitch);
            }
        } else {
            data.decrementBuffer();
        }

        // Yaw NaN
        if (Float.isNaN(yaw) || Float.isNaN(pitch)) {
            flag(player, "nan_rotation yaw=" + yaw + " pitch=" + pitch);
        }

        // Controlla packet ground spoof: false poi true consecutivamente
        if (data.isWasLastOnGround() && !data.isWasOnGround() && player.isOnGround()) {
            // Toggle ground troppo rapido è sospetto
            data.incrementBuffer();
            if (data.getBuffer() > 5) {
                flag(player, "ground_spoof rapid_toggle");
            }
        } else {
            // Riduci gradualmente
            if (data.getBuffer() > 0) {
                data.decrementBuffer();
            }
        }

        // Controlla deltaY infiniti/NaN
        double deltaY = data.getLastDeltaY();
        if (!MathUtils.isFinite(deltaY)) {
            flag(player, "invalid_deltaY deltaY=" + deltaY);
        }

        // Controlla se è su un veicolo ma anche su un cavallo/entità
        if (player.isInsideVehicle()) {
            // Alcuni hack permettono di muovere il veicolo troppo velocemente
            double deltaXZ = data.getLastDeltaXZ();
            if (deltaXZ > 1.5) {
                data.incrementBuffer();
                if (data.getBuffer() > 3) {
                    flag(player, "vehicle_speed=" + MathUtils.round(deltaXZ, 4));
                }
            } else {
                data.decrementBuffer();
            }
        }
    }
}