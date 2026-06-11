package com.anticheat.checks.combat;

import com.anticheat.AnticheatPlugin;
import com.anticheat.MathUtils;
import com.anticheat.checks.Check;
import com.anticheat.data.PlayerData;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class AutoClickerCheck extends Check {

    private static final int MAX_CPS = 18;

    public AutoClickerCheck(AnticheatPlugin plugin) {
        super(plugin, "AutoClicker", CheckType.COMBAT);
    }

    @Override
    public void check(Player player, PlayerData data) {
        if (canBypass(player)) return;
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;

        double averageDelta = data.getAverageClickDelta();
        double cps = data.getClickCps();

        // CPS troppo alto indica autoclicker
        if (cps > MAX_CPS && data.getClickDeltas().size() > 10) {
            data.incrementBuffer();
            if (data.getBuffer() > 3) {
                flag(player, "cps=" + MathUtils.round(cps, 1) + " max=" + MAX_CPS);
            }
        } else {
            data.decrementBuffer();
        }

        // Varianza troppo bassa indica click costanti (non umani)
        if (averageDelta > 0 && data.getClickDeltas().size() > 5) {
            double variance = 0;
            for (double delta : data.getClickDeltas()) {
                variance += Math.pow(delta - averageDelta, 2);
            }
            variance /= data.getClickDeltas().size();

            // Click troppo costanti (bassa varianza) possono essere autoclicker
            if (variance < 0.0005 && cps > 8) {
                data.incrementBuffer();
                if (data.getBuffer() > 5) {
                    flag(player, "low_variance variance=" + MathUtils.round(variance, 6) + " cps=" + MathUtils.round(cps, 1));
                }
            } else {
                data.decrementBuffer();
            }
        }
    }
}