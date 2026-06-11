package com.anticheat;

import com.anticheat.checks.Check;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AnticheatCommand implements CommandExecutor {

    private final AnticheatPlugin plugin;

    public AnticheatCommand(AnticheatPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "alerts":
            case "alert":
                handleAlerts(sender);
                break;
            case "info":
                handleInfo(sender);
                break;
            case "reload":
                handleReload(sender);
                break;
            case "reset":
                handleReset(sender, args);
                break;
            case "checks":
                handleChecks(sender, args);
                break;
            default:
                sendHelp(sender);
                break;
        }
        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&8&m----------------------------------------"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&c&lAnticheat &8- &7Comandi disponibili"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&8&m----------------------------------------"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&7/anticheat &calerts &8- &7Attiva/disattiva alert"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&7/anticheat &cinfo &8- &7Mostra info sul plugin"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&7/anticheat &creload &8- &7Ricarica la configurazione"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&7/anticheat &creset <player> &8- &7Resetta violazioni"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&7/anticheat &cchecks &8- &7Mostra stato checks"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&8&m----------------------------------------"));
    }

    private void handleAlerts(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Comando solo per giocatori.");
            return;
        }
        if (!player.hasPermission("anticheat.alerts")) {
            player.sendMessage(ChatColor.RED + "Non hai il permesso.");
            return;
        }
        plugin.getViolationManager().toggleAlerts(player);
    }

    private void handleInfo(CommandSender sender) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&8&m----------------------------------------"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&c&lAnticheat Plugin &8v" + plugin.getDescription().getVersion()));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&7Checks caricati: &c" + plugin.getChecksLoaded()));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&7Giocatori tracciati: &c" + plugin.getPlayerDataManager().size()));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&8&m----------------------------------------"));
    }

    private void handleReload(CommandSender sender) {
        if (!sender.hasPermission("anticheat.admin")) {
            sender.sendMessage(ChatColor.RED + "Non hai il permesso.");
            return;
        }
        plugin.getConfigManager().reload();
        sender.sendMessage(ChatColor.GREEN + "Configurazione ricaricata.");
    }

    private void handleReset(CommandSender sender, String[] args) {
        if (!sender.hasPermission("anticheat.admin")) {
            sender.sendMessage(ChatColor.RED + "Non hai il permesso.");
            return;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Uso: /anticheat reset <giocatore>");
            return;
        }

        Player target = plugin.getServer().getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Giocatore non trovato.");
            return;
        }

        plugin.getViolationManager().resetViolations(target);
        sender.sendMessage(ChatColor.GREEN + "Violazioni resettate per " + target.getName());
    }

    private void handleChecks(CommandSender sender, String[] args) {
        if (!sender.hasPermission("anticheat.admin")) {
            sender.sendMessage(ChatColor.RED + "Non hai il permesso.");
            return;
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&8&m----------------------------------------"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&c&lStato Checks"));

        for (Check.CheckType type : Check.CheckType.values()) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&8- &7" + type.name() + ":"));
            for (Check check : plugin.getCheckManager().getChecks()) {
                if (check.getType() == type) {
                    String status = check.isEnabled() ? "&a✔" : "&c✘";
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "  " + status + " &7" + check.getName()));
                }
            }
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&8&m----------------------------------------"));
    }
}