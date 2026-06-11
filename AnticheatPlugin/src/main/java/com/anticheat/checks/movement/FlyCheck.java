package com.anticheat.checks.movement;

import com.anticheat.AnticheatPlugin;
import com.anticheat.MathUtils;
import com.anticheat.checks.Check;
import com.anticheat.data.PlayerData;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class FlyCheck extends Check {

    private static final double MAX_VERTICAL_ACCELERATION = 0.005;

    public FlyCheck(AnticheatPlugin plugin) {
        super(plugin, "Fly", CheckType.MOVEMENT);
    }

    @Override
    public void check(Player player, PlayerData data) {
        if (canBypass(player)) return;
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;
        if (player.isFlying()) return;
        if (player.getVehicle() != null) return;
        if (player.isInsideVehicle()) return;

        // Non controllare se in liquidi o su scalini
        if (player.isInWater() || player.isInLava() || data.getLiquidTicks() > 0) return;
        if (data.getClimbTicks() > 0) return;
        if (data.getWebTicks() > 0) return;

        // Non controllare se ha velocity recente
        if (data.getSinceVelocityTicks() < 10) return;

        double deltaY = data.getLastDeltaY();
        double lastDeltaY = data.getLastLastDeltaY();
        double predictedDeltaY = (lastDeltaY - 0.08) * 0.98;

        // Controllo se era a terra e ora non lo è più (iniziato a cadere)
        if (data.isWasOnGround() && !player.isOnGround()) {
            return; // Lascia un tick per inizializzare
        }

        // Se è a terra, non flaggare
        if (player.isOnGround()) {
            data.decrementBuffer();
            return;
        }

        // Controllo verticale: il movimento verticale deve seguire la gravità
        if (deltaY > 0 && data.getAirTicks() > 2) {
            // Climbing up without jump - possibile fly
            if (deltaY > 0.6) {
                data.incrementBuffer();
                if (data.getBuffer() > 5) {
                    flag(player, "deltaY=" + MathUtils.round(deltaY, 4) + " air=" + data.getAirTicks());
                }
                return;
            }

            // Controlla se il movimento verticale è consistente con la gravità
            double acceleration = deltaY - predictedDeltaY;
            if (Math.abs(acceleration) > MAX_VERTICAL_ACCELERATION && deltaY > 0) {
                data.incrementBuffer();
                if (data.getBuffer() > 5) {
                    flag(player, "vertical_acceleration=" + MathUtils.round(acceleration, 6) +
                            " deltaY=" + MathUtils.round(deltaY, 4));
                }
            } else {
                data.decrementBuffer();
            }
        } else {
            data.decrementBuffer();
        }

        // Controllo orizzontale: fly scoppia anche se il movimento XZ è troppo alto in aria
        if (data.getAirTicks() > 10) {
            double deltaXZ = data.getLastDeltaXZ();
            double maxAirSpeed = 0.42; // Velocità massima in aria vanilla

            if (deltaXZ > maxAirSpeed) {
                data.incrementBuffer();
                if (data.getBuffer() > 8) {
                    flag(player, "air_speed=" + MathUtils.round(deltaXZ, 4) + " air=" + data.getAirTicks());
                }
            }
        }
    }
}