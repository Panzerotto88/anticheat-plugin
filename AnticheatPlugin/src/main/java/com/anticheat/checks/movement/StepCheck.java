package com.anticheat.checks.movement;

import com.anticheat.AnticheatPlugin;
import com.anticheat.MathUtils;
import com.anticheat.checks.Check;
import com.anticheat.data.PlayerData;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class StepCheck extends Check {

    private static final double MAX_STEP_HEIGHT = 0.6;
    private static final double MAX_STEP_ACCELERATION = 0.8;

    public StepCheck(AnticheatPlugin plugin) {
        super(plugin, "Step", CheckType.MOVEMENT);
    }

    @Override
    public void check(Player player, PlayerData data) {
        if (canBypass(player)) return;
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;
        if (player.isFlying()) return;
        if (player.getVehicle() != null) return;

        // Non controllare se in liquidi
        if (player.isInWater() || player.isInLava()) return;

        // Controlla solo quando il giocatore passa da non a terra
        if (!data.isWasOnGround() && player.isOnGround()) {
            // È appena atterrato
            double deltaY = Math.abs(data.getLastDeltaY());
            double deltaXZ = data.getLastDeltaXZ();

            // Un salto normale vanilla sale al massimo ~0.42 blocchi/tick
            // Uno step è al massimo 0.6 blocchi
            if (deltaY > 0 && deltaY < 1.0) {
                // Step eccessivo: salire più di 0.6 blocchi in un tick
                if (deltaY > MAX_STEP_HEIGHT) {
                    data.incrementBuffer();
                    if (data.getBuffer() > 2) {
                        flag(player, "step=" + MathUtils.round(deltaY, 4) +
                                " xz=" + MathUtils.round(deltaXZ, 4));
                    }
                } else {
                    data.decrementBuffer();
                }
            }

            // Step combinato con XZ eccessivo
            if (deltaY > 0.5 && deltaXZ > MAX_STEP_ACCELERATION) {
                data.incrementBuffer();
                if (data.getBuffer() > 2) {
                    flag(player, "high_step step=" + MathUtils.round(deltaY, 4) +
                            " xz_speed=" + MathUtils.round(deltaXZ, 4));
                }
            }
        } else {
            // Se non sta atterrando, riduci buffer gradualmente
            if (data.getBuffer() > 0) {
                data.decrementBuffer();
            }
        }
    }
}