package com.anticheat.checks.combat;

import com.anticheat.AnticheatPlugin;
import com.anticheat.MathUtils;
import com.anticheat.checks.Check;
import com.anticheat.data.PlayerData;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class CriticalsCheck extends Check {

    public CriticalsCheck(AnticheatPlugin plugin) {
        super(plugin, "Criticals", CheckType.COMBAT);
    }

    @Override
    public void check(Player player, PlayerData data) {
        if (canBypass(player)) return;
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;
        if (player.getVehicle() != null) return;
        if (player.isInWater() || player.isInLava()) return;
    }

    /**
     * Chiamata dal listener quando un player attacca.
     * Verifica se il critico avviene senza le condizioni corrette.
     */
    public void checkCrit(Player attacker, PlayerData data) {
        if (!attacker.isOnGround()) {
            double deltaY = data.getLastDeltaY();

            // Un critico vanilla avviene quando si salta: deltaY positivo dopo il salto
            // Se il giocatore non è salito e ha fatto critico, è packet spoof
            if (deltaY >= 0 && data.getAirTicks() > 1) {
                // Non è possibile fare critici senza cadere
                data.incrementBuffer();
                if (data.getBuffer() > 3) {
                    flag(attacker, "packet_crit deltaY=" + MathUtils.round(deltaY, 4) +
                            " air=" + data.getAirTicks());
                }
            } else {
                data.decrementBuffer();
            }
        }
    }
}