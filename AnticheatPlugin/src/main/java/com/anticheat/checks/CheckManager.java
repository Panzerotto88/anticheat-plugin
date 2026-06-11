package com.anticheat.checks;

import com.anticheat.AnticheatPlugin;
import com.anticheat.checks.combat.*;
import com.anticheat.checks.movement.*;
import com.anticheat.checks.player.*;
import com.anticheat.checks.other.*;
import com.anticheat.data.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class CheckManager {

    private final AnticheatPlugin plugin;
    private final List<Check> checks;

    public CheckManager(AnticheatPlugin plugin) {
        this.plugin = plugin;
        this.checks = new ArrayList<>();
        registerChecks();
        startTickTask();
    }

    private void registerChecks() {
        checks.add(new FlyCheck(plugin));
        checks.add(new SpeedCheck(plugin));
        checks.add(new NoFallCheck(plugin));
        checks.add(new VelocityCheck(plugin));
        checks.add(new StepCheck(plugin));

        checks.add(new KillAuraCheck(plugin));
        checks.add(new ReachCheck(plugin));
        checks.add(new AutoClickerCheck(plugin));
        checks.add(new CriticalsCheck(plugin));

        checks.add(new TimerCheck(plugin));
        checks.add(new ScaffoldCheck(plugin));
        checks.add(new NoSlowCheck(plugin));
        checks.add(new FastEatCheck(plugin));
        checks.add(new InventoryCheck(plugin));

        checks.add(new PacketCheck(plugin));
        checks.add(new BadPacketsCheck(plugin));

        plugin.getLogger().info("Caricati " + checks.size() + " checks.");
    }

    public void performChecks(Player player, PlayerData data) {
        if (data == null) return;
        if (player.hasPermission("anticheat.bypass")) return;

        for (Check check : checks) {
            if (!check.isEnabled()) continue;
            try {
                check.check(player, data);
            } catch (Exception e) {
                plugin.getLogger().warning("Errore check " + check.getName() + " per " + player.getName() + ": " + e.getMessage());
            }
        }
    }

    private void startTickTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    PlayerData data = plugin.getPlayerDataManager().getData(player);
                    if (data != null) {
                        if (player.isOnGround()) {
                            data.setGroundTicks(data.getGroundTicks() + 1);
                            data.setAirTicks(0);
                        } else {
                            data.setAirTicks(data.getAirTicks() + 1);
                            data.setGroundTicks(0);
                        }

                        if (player.isInWater() || player.isInLava()) {
                            data.setLiquidTicks(data.getLiquidTicks() + 1);
                        } else {
                            data.setLiquidTicks(0);
                        }

                        if (data.getSinceVelocityTicks() < 100) {
                            data.incrementSinceVelocityTicks();
                        }

                        performChecks(player, data);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    public void disable() {}

    public int getCheckCount() {
        return checks.size();
    }

    public List<Check> getChecks() {
        return new ArrayList<>(checks);
    }

    public Check getCheck(String name) {
        for (Check check : checks) {
            if (check.getName().equalsIgnoreCase(name)) {
                return check;
            }
        }
        return null;
    }
}