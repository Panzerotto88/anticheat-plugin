package com.anticheat.violation;

import com.anticheat.AnticheatPlugin;
import com.anticheat.checks.Check;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

public class ViolationManager {

    private final AnticheatPlugin plugin;
    private final Map<UUID, Map<String, Integer>> violations;
    private final Set<UUID> alertToggle;

    public ViolationManager(AnticheatPlugin plugin) {
        this.plugin = plugin;
        this.violations = new HashMap<>();
        this.alertToggle = new HashSet<>();
    }

    public void addViolation(Player player, Check check, String details) {
        UUID uuid = player.getUniqueId();
        String checkKey = check.getType().name() + "_" + check.getName();

        Map<String, Integer> playerViolations = violations.computeIfAbsent(uuid, k -> new HashMap<>());
        int vl = playerViolations.getOrDefault(checkKey, 0) + 1;
        playerViolations.put(checkKey, vl);

        sendAlert(player, check, details, vl);

        if (vl >= check.getMaxViolations()) {
            applyPunishment(player, check);
            playerViolations.put(checkKey, 0);
        }
    }

    public void resetViolations(Player player) {
        violations.remove(player.getUniqueId());
    }

    public void resetViolations(Player player, Check check) {
        Map<String, Integer> playerViolations = violations.get(player.getUniqueId());
        if (playerViolations != null) {
            playerViolations.remove(check.getType().name() + "_" + check.getName());
        }
    }

    public int getViolations(Player player, Check check) {
        Map<String, Integer> playerViolations = violations.get(player.getUniqueId());
        if (playerViolations == null) return 0;
        return playerViolations.getOrDefault(check.getType().name() + "_" + check.getName(), 0);
    }

    private void sendAlert(Player player, Check check, String details, int vl) {
        String alert = ChatColor.translateAlternateColorCodes('&',
                "&8[&cAnticheat&8] &7" + player.getName() +
                " &cflagged &7" + check.getType().name().toLowerCase() + ":" + check.getName() +
                " &8(&7VL: " + vl + "&8) &8- &7" + details);

        for (Player online : Bukkit.getOnlinePlayers()) {
            if (online.hasPermission("anticheat.alerts") && isAlertEnabled(online)) {
                online.sendMessage(alert);
            }
        }

        plugin.getLogger().warning(player.getName() + " flagged " + check.getName() + " (VL: " + vl + ") - " + details);
    }

    private void applyPunishment(Player player, Check check) {
        String punishment = check.getPunishment();
        String message = ChatColor.translateAlternateColorCodes('&',
                "&cSei stato punito per cheating! (&7" + check.getType().name().toLowerCase() + ":" + check.getName() + "&c)");

        switch (punishment.toLowerCase()) {
            case "kick" -> Bukkit.getScheduler().runTask(plugin, () -> player.kickPlayer(message));
            case "ban" -> Bukkit.getScheduler().runTask(plugin, () -> player.banPlayerIP(message, null, "Anticheat"));
            case "warn" -> player.sendMessage(message);
            default -> Bukkit.getScheduler().runTask(plugin, () -> player.kickPlayer(message));
        }

        String broadcast = ChatColor.translateAlternateColorCodes('&',
                "&8[&cAnticheat&8] &7" + player.getName() + " &cè stato punito per &7" +
                check.getType().name().toLowerCase() + ":" + check.getName());

        for (Player online : Bukkit.getOnlinePlayers()) {
            if (online.hasPermission("anticheat.admin")) {
                online.sendMessage(broadcast);
            }
        }
    }

    public void toggleAlerts(Player player) {
        if (alertToggle.contains(player.getUniqueId())) {
            alertToggle.remove(player.getUniqueId());
            player.sendMessage(ChatColor.GREEN + "Alert anti-cheat: attivati");
        } else {
            alertToggle.add(player.getUniqueId());
            player.sendMessage(ChatColor.RED + "Alert anti-cheat: disattivati");
        }
    }

    public boolean isAlertEnabled(Player player) {
        return !alertToggle.contains(player.getUniqueId());
    }
}