package com.anticheat.config;

import com.anticheat.AnticheatPlugin;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    private final AnticheatPlugin plugin;
    private FileConfiguration config;

    public ConfigManager(AnticheatPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        loadDefaultConfig();
    }

    private void loadDefaultConfig() {
        config.addDefault("prefix", "&8[&cAnticheat&8]");
        config.addDefault("alerts-to-console", true);

        config.addDefault("checks.movement.fly.enabled", true);
        config.addDefault("checks.movement.fly.max-violations", 20);
        config.addDefault("checks.movement.fly.punishment", "kick");

        config.addDefault("checks.movement.speed.enabled", true);
        config.addDefault("checks.movement.speed.max-violations", 25);
        config.addDefault("checks.movement.speed.punishment", "kick");

        config.addDefault("checks.movement.nofall.enabled", true);
        config.addDefault("checks.movement.nofall.max-violations", 10);
        config.addDefault("checks.movement.nofall.punishment", "kick");

        config.addDefault("checks.movement.velocity.enabled", true);
        config.addDefault("checks.movement.velocity.max-violations", 15);
        config.addDefault("checks.movement.velocity.punishment", "warn");

        config.addDefault("checks.movement.step.enabled", true);
        config.addDefault("checks.movement.step.max-violations", 10);
        config.addDefault("checks.movement.step.punishment", "kick");

        config.addDefault("checks.combat.killaura.enabled", true);
        config.addDefault("checks.combat.killaura.max-violations", 20);
        config.addDefault("checks.combat.killaura.punishment", "kick");

        config.addDefault("checks.combat.reach.enabled", true);
        config.addDefault("checks.combat.reach.max-violations", 20);
        config.addDefault("checks.combat.reach.punishment", "kick");
        config.addDefault("checks.combat.reach.max-reach", 3.5);

        config.addDefault("checks.combat.autoclicker.enabled", true);
        config.addDefault("checks.combat.autoclicker.max-violations", 20);
        config.addDefault("checks.combat.autoclicker.punishment", "kick");
        config.addDefault("checks.combat.autoclicker.max-cps", 18);

        config.addDefault("checks.combat.criticals.enabled", true);
        config.addDefault("checks.combat.criticals.max-violations", 10);
        config.addDefault("checks.combat.criticals.punishment", "kick");

        config.addDefault("checks.player.timer.enabled", true);
        config.addDefault("checks.player.timer.max-violations", 20);
        config.addDefault("checks.player.timer.punishment", "kick");

        config.addDefault("checks.player.scaffold.enabled", true);
        config.addDefault("checks.player.scaffold.max-violations", 15);
        config.addDefault("checks.player.scaffold.punishment", "kick");

        config.addDefault("checks.player.noslow.enabled", true);
        config.addDefault("checks.player.noslow.max-violations", 10);
        config.addDefault("checks.player.noslow.punishment", "warn");

        config.addDefault("checks.player.fasteat.enabled", true);
        config.addDefault("checks.player.fasteat.max-violations", 10);
        config.addDefault("checks.player.fasteat.punishment", "kick");

        config.addDefault("checks.player.inventory.enabled", true);
        config.addDefault("checks.player.inventory.max-violations", 10);
        config.addDefault("checks.player.inventory.punishment", "kick");

        config.addDefault("checks.other.packet.enabled", true);
        config.addDefault("checks.other.packet.max-violations", 20);
        config.addDefault("checks.other.packet.punishment", "kick");

        config.addDefault("checks.other.badpackets.enabled", true);
        config.addDefault("checks.other.badpackets.max-violations", 10);
        config.addDefault("checks.other.badpackets.punishment", "kick");

        config.options().copyDefaults(true);
        plugin.saveConfig();
    }

    public void reload() {
        plugin.reloadConfig();
        this.config = plugin.getConfig();
    }

    public String getPrefix() {
        return config.getString("prefix", "&8[&cAnticheat&8]");
    }

    public boolean isCheckEnabled(String category, String checkName) {
        return config.getBoolean("checks." + category + "." + checkName + ".enabled", true);
    }

    public int getMaxViolations(String category, String checkName) {
        return config.getInt("checks." + category + "." + checkName + ".max-violations", 20);
    }

    public String getPunishment(String category, String checkName) {
        return config.getString("checks." + category + "." + checkName + ".punishment", "kick");
    }

    public double getDouble(String path, double def) {
        return config.getDouble(path, def);
    }
}