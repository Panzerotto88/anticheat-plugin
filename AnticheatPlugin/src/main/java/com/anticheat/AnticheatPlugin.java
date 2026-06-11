package com.anticheat;

import com.anticheat.checks.CheckManager;
import com.anticheat.config.ConfigManager;
import com.anticheat.data.PlayerDataManager;
import com.anticheat.listeners.PlayerListener;
import com.anticheat.violation.ViolationManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class AnticheatPlugin extends JavaPlugin {

    private static AnticheatPlugin instance;
    private CheckManager checkManager;
    private ViolationManager violationManager;
    private PlayerDataManager playerDataManager;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        instance = this;

        // Salva configurazione default
        saveDefaultConfig();

        // Inizializza managers
        this.configManager = new ConfigManager(this);
        this.violationManager = new ViolationManager(this);
        this.playerDataManager = new PlayerDataManager();
        this.checkManager = new CheckManager(this);

        // Registra listeners
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        // Registra comandi
        getCommand("anticheat").setExecutor(new AnticheatCommand(this));

        getLogger().info("AnticheatPlugin abilitato con successo! (" + getChecksLoaded() + " checks caricati)");
    }

    @Override
    public void onDisable() {
        if (checkManager != null) {
            checkManager.disable();
        }
        if (playerDataManager != null) {
            playerDataManager.clear();
        }
        getLogger().info("AnticheatPlugin disabilitato.");
    }

    public int getChecksLoaded() {
        return checkManager != null ? checkManager.getCheckCount() : 0;
    }

    public static AnticheatPlugin getInstance() {
        return instance;
    }

    public CheckManager getCheckManager() {
        return checkManager;
    }

    public ViolationManager getViolationManager() {
        return violationManager;
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}