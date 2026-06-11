package com.anticheat.checks.combat;

import com.anticheat.AnticheatPlugin;
import com.anticheat.MathUtils;
import com.anticheat.checks.Check;
import com.anticheat.data.PlayerData;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class KillAuraCheck extends Check {

    public KillAuraCheck(AnticheatPlugin plugin) {
        super(plugin, "KillAura", CheckType.COMBAT);
    }

    @Override
    public void check(Player player, PlayerData data) {
        if (canBypass(player)) return;
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;

        float yawDelta = data.getLastYawDelta();
        float pitchDelta = data.getLastPitchDelta();

        if (yawDelta > 0 && pitchDelta > 0) {
            if (yawDelta > 150 && data.getYawTicks() < 2) {
                data.incrementBuffer();
                if (data.getBuffer() > 3) {
                    flag(player, "head_snap yaw=" + MathUtils.round(yawDelta, 2) +
                            " pitch=" + MathUtils.round(pitchDelta, 2));
                }
            } else {
                data.decrementBuffer();
            }

            if (Math.abs(pitchDelta) > 85) {
                data.incrementBuffer();
                if (data.getBuffer() > 2) {
                    flag(player, "impossible_pitch pitch=" + MathUtils.round(pitchDelta, 2));
                }
            }
        }

        if (data.getLastAttackTime() > 0 && (System.currentTimeMillis() - data.getLastAttackTime()) < 1000) {
            if (yawDelta > 0 && yawDelta < 0.5 && data.getYawTicks() > 5) {
                data.incrementBuffer();
                if (data.getBuffer() > 10) {
                    flag(player, "aim_assist yaw_delta=" + MathUtils.round(yawDelta, 4));
                }
            } else {
                data.decrementBuffer();
            }
        }
    }
}