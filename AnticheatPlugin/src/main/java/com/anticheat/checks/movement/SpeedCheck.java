package com.anticheat.checks.movement;

import com.anticheat.AnticheatPlugin;
import com.anticheat.MathUtils;
import com.anticheat.checks.Check;
import com.anticheat.data.PlayerData;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class SpeedCheck extends Check {

    private static final double BASE_MAX_SPEED = 0.287;
    private static final double SPRINT_SPEED = 0.287;
    private static final double ICE_MULTIPLIER = 1.4;
    private static final double SPEED_POTION_MULTIPLIER = 0.058;

    public SpeedCheck(AnticheatPlugin plugin) {
        super(plugin, "Speed", CheckType.MOVEMENT);
    }

    @Override
    public void check(Player player, PlayerData data) {
        if (canBypass(player)) return;
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;
        if (player.isFlying()) return;
        if (player.getVehicle() != null) return;

        double deltaXZ = data.getLastDeltaXZ();
        double maxSpeed = BASE_MAX_SPEED;

        // Applica modificatori di velocità vanilla
        if (player.isSprinting()) {
            maxSpeed = SPRINT_SPEED;
        }

        // Ice speed boost
        if (data.getIceTicks() > 0) {
            maxSpeed *= ICE_MULTIPLIER;
        }

        // Effetti pozioni
        if (player.hasPotionEffect(PotionEffectType.SPEED)) {
            int amplifier = player.getPotionEffect(PotionEffectType.SPEED).getAmplifier();
            maxSpeed += (amplifier + 1) * SPEED_POTION_MULTIPLIER * 2;
        }

        // Slow potion
        if (player.hasPotionEffect(PotionEffectType.SLOWNESS)) {
            int amplifier = player.getPotionEffect(PotionEffectType.SLOWNESS).getAmplifier();
            maxSpeed *= (1.0 - (amplifier + 1) * 0.15);
        }

        // Packet velocity
        if (data.isVelocityTaken() && data.getSinceVelocityTicks() < 5) {
            maxSpeed += Math.sqrt(data.getVelocityX() * data.getVelocityX() + data.getVelocityZ() * data.getVelocityZ());
        }

        // Aggiungi tolleranza
        maxSpeed += 0.12;

        // Controllo suolo
        if (!player.isOnGround()) {
            maxSpeed *= 0.75;
        }

        // Liquidi
        if (player.isInWater() || player.isInLava()) {
            maxSpeed += 0.1;
        }

        if (deltaXZ > maxSpeed && deltaXZ > 0.5) {
            data.incrementBuffer();
            if (data.getBuffer() > 4) {
                flag(player, "speed=" + MathUtils.round(deltaXZ, 4) + " max=" + MathUtils.round(maxSpeed, 4));
            }
        } else {
            data.decrementBuffer();
        }
    }
}