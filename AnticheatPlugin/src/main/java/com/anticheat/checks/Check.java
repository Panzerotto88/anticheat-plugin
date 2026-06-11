package com.anticheat.checks;

import com.anticheat.AnticheatPlugin;
import com.anticheat.data.PlayerData;
import org.bukkit.entity.Player;

/**
 * Classe astratta base per tutti i check dell'anti-cheat.
 */
public abstract class Check {

    protected final AnticheatPlugin plugin;
    protected final String name;
    protected final CheckType type;
    protected boolean enabled;
    protected int maxViolations;
    protected String punishment;

    public Check(AnticheatPlugin plugin, String name, CheckType type) {
        this.plugin = plugin;
        this.name = name;
        this.type = type;
        this.enabled = plugin.getConfig().getBoolean("checks." + type.name().toLowerCase() + "." + name + ".enabled", true);
        this.maxViolations = plugin.getConfig().getInt("checks." + type.name().toLowerCase() + "." + name + ".max-violations", 20);
        this.punishment = plugin.getConfig().getString("checks." + type.name().toLowerCase() + "." + name + ".punishment", "kick");
    }

    public abstract void check(Player player, PlayerData data);

    /**
     * Gestisce la flag di un giocatore per questo check.
     */
    public void flag(Player player, String details) {
        if (!enabled) return;

        plugin.getViolationManager().addViolation(player, this, details);
    }

    /**
     * Verifica se un giocatore ha il permesso di bypassare questo check.
     */
    public boolean canBypass(Player player) {
        return player.hasPermission("anticheat.bypass") || player.isOp();
    }

    public String getName() { return name; }
    public CheckType getType() { return type; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public int getMaxViolations() { return maxViolations; }
    public String getPunishment() { return punishment; }

    public enum CheckType {
        COMBAT,
        MOVEMENT,
        PLAYER,
        OTHER
    }
}