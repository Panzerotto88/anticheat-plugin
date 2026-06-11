package com.anticheat.listeners;

import com.anticheat.AnticheatPlugin;
import com.anticheat.checks.combat.CriticalsCheck;
import com.anticheat.checks.combat.ReachCheck;
import com.anticheat.data.PlayerData;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;
import org.bukkit.util.Vector;

public class PlayerListener implements Listener {

    private final AnticheatPlugin plugin;

    public PlayerListener(AnticheatPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.getPlayerDataManager().createData(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        plugin.getViolationManager().resetViolations(player);
        plugin.getPlayerDataManager().removeData(player);
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        plugin.getViolationManager().resetViolations(player);
        plugin.getPlayerDataManager().removeData(player);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        PlayerData data = plugin.getPlayerDataManager().getData(player);
        if (data == null) return;

        Location from = event.getFrom();
        Location to = event.getTo();
        if (to == null) return;

        double deltaX = to.getX() - from.getX();
        double deltaY = to.getY() - from.getY();
        double deltaZ = to.getZ() - from.getZ();
        double deltaXZ = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);

        data.setLastLocation(to);
        data.setLastDeltaXZ(deltaXZ);
        data.setLastDeltaY(deltaY);
        data.setWasOnGround(player.isOnGround());

        float yawDelta = Math.abs(to.getYaw() - from.getYaw());
        float pitchDelta = Math.abs(to.getPitch() - from.getPitch());
        data.setLastYawDelta(yawDelta);
        data.setLastPitchDelta(pitchDelta);

        if (yawDelta > 0) {
            data.setYawTicks(data.getYawTicks() + 1);
        } else {
            data.setYawTicks(0);
        }

        if (pitchDelta > 0) {
            data.setPitchTicks(data.getPitchTicks() + 1);
        } else {
            data.setPitchTicks(0);
        }

        data.setLastPacketTime(System.currentTimeMillis());

        if (player.isInWater() || player.isInLava()) {
            data.setLiquidTicks(data.getLiquidTicks() + 1);
        } else {
            data.setLiquidTicks(0);
        }

        if (player.getLocation().getBlock().getType().name().contains("COBWEB")) {
            data.setWebTicks(data.getWebTicks() + 1);
        } else {
            data.setWebTicks(0);
        }

        boolean climbing = player.isClimbing();
        data.setWasClimbing(climbing);
        if (climbing) {
            data.setClimbTicks(data.getClimbTicks() + 1);
        } else {
            data.setClimbTicks(0);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player attacker)) return;
        if (!(event.getEntity() instanceof LivingEntity target)) return;

        PlayerData data = plugin.getPlayerDataManager().getData(attacker);
        if (data == null) return;

        data.setLastAttackTime(System.currentTimeMillis());

        ReachCheck reachCheck = (ReachCheck) plugin.getCheckManager().getCheck("Reach");
        if (reachCheck != null && reachCheck.isEnabled()) {
            reachCheck.checkReach(attacker, target, data);
        }

        CriticalsCheck criticalsCheck = (CriticalsCheck) plugin.getCheckManager().getCheck("Criticals");
        if (criticalsCheck != null && criticalsCheck.isEnabled()) {
            criticalsCheck.checkCrit(attacker, data);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        PlayerData data = plugin.getPlayerDataManager().getData(player);
        if (data == null) return;
        data.setLastInteractTime(System.currentTimeMillis());
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        PlayerData data = plugin.getPlayerDataManager().getData(player);
        if (data == null) return;
        data.setLastEatTime(System.currentTimeMillis());
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        PlayerData data = plugin.getPlayerDataManager().getData(player);
        if (data == null) return;

        data.setPlacingBlocks(true);

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            PlayerData d = plugin.getPlayerDataManager().getData(player);
            if (d != null) d.setPlacingBlocks(false);
        }, 3L);
    }

    @EventHandler
    public void onPlayerVelocity(PlayerVelocityEvent event) {
        Player player = event.getPlayer();
        PlayerData data = plugin.getPlayerDataManager().getData(player);
        if (data == null) return;

        Vector velocity = event.getVelocity();
        data.setVelocityTaken(true);
        data.setVelocityX(velocity.getX());
        data.setVelocityY(velocity.getY());
        data.setVelocityZ(velocity.getZ());
        data.setSinceVelocityTicks(0);
        data.setLastVelocity(velocity);
    }

    @EventHandler
    public void onPlayerSwing(PlayerSwingEvent event) {
        Player player = event.getPlayer();
        PlayerData data = plugin.getPlayerDataManager().getData(player);
        if (data == null) return;

        long now = System.currentTimeMillis();
        long lastSwing = data.getLastSwingTime();

        if (lastSwing > 0) {
            double delta = (now - lastSwing) / 1000.0;
            data.addClickDelta(delta);
        }

        data.setLastSwingTime(now);
    }
}
