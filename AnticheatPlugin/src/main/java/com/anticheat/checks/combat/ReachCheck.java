package com.anticheat.checks.combat;

import com.anticheat.AnticheatPlugin;
import com.anticheat.MathUtils;
import com.anticheat.checks.Check;
import com.anticheat.data.PlayerData;
import org.bukkit.GameMode;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class ReachCheck extends Check {

    private static final double MAX_REACH = 3.5;
    private static final double MAX_REACH_HITBOX = 4.5;

    public ReachCheck(AnticheatPlugin plugin) {
        super(plugin, "Reach", CheckType.COMBAT);
    }

    @Override
    public void check(Player player, PlayerData data) {
        if (canBypass(player)) return;
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;

        // Questo check viene attivato dall'event listener sui danni
        // La logica principale è in PlayerListener
    }

    /**
     * Metodo chiamato dal listener quando un player attacca un'entità.
     */
    public void checkReach(Player attacker, LivingEntity target, PlayerData data) {
        double distance = attacker.getLocation().distance(target.getLocation());
        double maxReach = MAX_REACH;

        // Aggiungi la larghezza della hitbox del bersaglio (approssimazione)
        if (target instanceof Player) {
            maxReach = MAX_REACH_HITBOX;
        } else {
            maxReach = MAX_REACH_HITBOX + 0.5;
        }

        // Ping compensation
        int ping = attacker.getPing();
        if (ping > 100) {
            maxReach += (ping - 100) * 0.002;
        }

        if (distance > maxReach) {
            data.incrementBuffer();
            if (data.getBuffer() > 3) {
                flag(attacker, "reach=" + MathUtils.round(distance, 3) + " max=" + MathUtils.round(maxReach, 3));
            }
        } else {
            data.decrementBuffer();
        }
    }
}