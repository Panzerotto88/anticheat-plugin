package com.anticheat.checks.movement;

import com.anticheat.AnticheatPlugin;
import com.anticheat.MathUtils;
import com.anticheat.checks.Check;
import com.anticheat.data.PlayerData;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class NoFallCheck extends Check {

    public NoFallCheck(AnticheatPlugin plugin) {
        super(plugin, "NoFall", CheckType.MOVEMENT);
    }

    @Override
    public void check(Player player, PlayerData data) {
        if (canBypass(player)) return;
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;
        if (player.isFlying()) return;
        if (player.getVehicle() != null) return;

        // Non controllare in liquidi o su scale/rampicanti
        if (player.isInWater() || player.isInLava()) return;
        if (data.getClimbTicks() > 0) return;

        double deltaY = data.getLastDeltaY();
        double lastDeltaY = data.getLastLastDeltaY();

        // Se il giocatore è in aria e non subisce danno da caduta quando dovrebbe
        if (!player.isOnGround() && data.getAirTicks() > 2) {
            // Calcola il damage tick previsto: caduta > 3 blocchi = danno
            double fallDistance = player.getFallDistance();

            // NoFall packet check: se manda ground=true ma deltaY non corrisponde
            if (player.isOnGround() && deltaY < -0.5) {
                data.incrementBuffer();
                if (data.getBuffer() > 3) {
                    flag(player, "ground_spoof deltaY=" + MathUtils.round(deltaY, 4) +
                            " fall=" + MathUtils.round(fallDistance, 2));
                }
            } else {
                data.decrementBuffer();
            }

            // Se il giocatore aveva deltaY molto negativo e ora è quasi zero in aria (blink/reset)
            if (data.getAirTicks() > 5 && lastDeltaY < -0.5 && Math.abs(deltaY) < 0.01 && !player.isOnGround()) {
                data.incrementBuffer();
                if (data.getBuffer() > 2) {
                    flag(player, "motion_reset last_deltaY=" + MathUtils.round(lastDeltaY, 4));
                }
            }
        } else {
            data.decrementBuffer();
        }
    }
}