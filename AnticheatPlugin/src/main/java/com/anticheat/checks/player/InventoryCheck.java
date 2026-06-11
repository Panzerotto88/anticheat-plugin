package com.anticheat.checks.player;

import com.anticheat.AnticheatPlugin;
import com.anticheat.MathUtils;
import com.anticheat.checks.Check;
import com.anticheat.data.PlayerData;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class InventoryCheck extends Check {

    private static final double MAX_INVENTORY_SPEED = 0.22;

    public InventoryCheck(AnticheatPlugin plugin) {
        super(plugin, "Inventory", CheckType.PLAYER);
    }

    @Override
    public void check(Player player, PlayerData data) {
        if (canBypass(player)) return;
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;

        // Se il giocatore ha l'inventario aperto, non dovrebbe muoversi rapidamente
        if (player.isSleeping()) return;

        double deltaXZ = data.getLastDeltaXZ();
        double deltaY = Math.abs(data.getLastDeltaY());

        // Se il movimento è eccessivo mentre l'inventario è aperto (controllato dal listener)
        if (deltaXZ > MAX_INVENTORY_SPEED && deltaY < 0.1) {
            data.incrementBuffer();
            if (data.getBuffer() > 5) {
                flag(player, "move_inv speed=" + MathUtils.round(deltaXZ, 4));
            }
        } else {
            data.decrementBuffer();
        }
    }
}