package com.anticheat.data;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.LinkedList;
import java.util.Queue;

public class PlayerData {

    private final Player player;
    private final Queue<Double> clickDeltas;
    private final Queue<Long> packetTimes;
    private final Queue<Double> moveDeltas;

    private Location lastLocation;
    private Location lastLastLocation;
    private Vector lastVelocity;
    private Vector lastLastVelocity;

    private long lastAttackTime;
    private long lastPacketTime;
    private long lastMoveTime;
    private long joinTime;
    private long lastEatTime;
    private long lastInteractTime;
    private long lastSwingTime;

    private int airTicks;
    private int groundTicks;
    private int iceTicks;
    private int webTicks;
    private int climbTicks;
    private int liquidTicks;
    private int sinceVelocityTicks;

    private double lastDeltaXZ;
    private double lastLastDeltaXZ;
    private double lastDeltaY;
    private double lastLastDeltaY;
    private double velocityX, velocityY, velocityZ;

    private boolean wasOnGround;
    private boolean wasLastOnGround;
    private boolean wasInLiquid;
    private boolean wasClimbing;
    private boolean velocityTaken;
    private boolean placingBlocks;

    private float lastPitchDelta;
    private float lastYawDelta;
    private int pitchTicks;
    private int yawTicks;

    private int buffer;

    public PlayerData(Player player) {
        this.player = player;
        this.clickDeltas = new LinkedList<>();
        this.packetTimes = new LinkedList<>();
        this.moveDeltas = new LinkedList<>();
        this.lastLocation = player.getLocation();
        this.lastLastLocation = player.getLocation();
        this.lastVelocity = new Vector(0, 0, 0);
        this.lastLastVelocity = new Vector(0, 0, 0);
        this.joinTime = System.currentTimeMillis();
        this.lastPacketTime = System.currentTimeMillis();
        this.lastMoveTime = System.currentTimeMillis();
    }

    public Player getPlayer() { return player; }

    public Location getLastLocation() { return lastLocation; }
    public void setLastLocation(Location loc) { this.lastLastLocation = this.lastLocation; this.lastLocation = loc; }

    public Location getLastLastLocation() { return lastLastLocation; }

    public Vector getLastVelocity() { return lastVelocity; }
    public void setLastVelocity(Vector vel) { this.lastLastVelocity = this.lastVelocity; this.lastVelocity = vel; }

    public Vector getLastLastVelocity() { return lastLastVelocity; }

    public long getLastAttackTime() { return lastAttackTime; }
    public void setLastAttackTime(long t) { this.lastAttackTime = t; }

    public long getLastPacketTime() { return lastPacketTime; }
    public void setLastPacketTime(long t) { this.lastPacketTime = t; }

    public long getLastMoveTime() { return lastMoveTime; }
    public void setLastMoveTime(long t) { this.lastMoveTime = t; }

    public long getJoinTime() { return joinTime; }

    public long getLastEatTime() { return lastEatTime; }
    public void setLastEatTime(long t) { this.lastEatTime = t; }

    public long getLastInteractTime() { return lastInteractTime; }
    public void setLastInteractTime(long t) { this.lastInteractTime = t; }

    public long getLastSwingTime() { return lastSwingTime; }
    public void setLastSwingTime(long t) { this.lastSwingTime = t; }

    public int getAirTicks() { return airTicks; }
    public void setAirTicks(int t) { this.airTicks = t; }
    public void incrementAirTicks() { this.airTicks++; }

    public int getGroundTicks() { return groundTicks; }
    public void setGroundTicks(int t) { this.groundTicks = t; }
    public void incrementGroundTicks() { this.groundTicks++; }

    public int getIceTicks() { return iceTicks; }
    public void setIceTicks(int t) { this.iceTicks = t; }

    public int getWebTicks() { return webTicks; }
    public void setWebTicks(int t) { this.webTicks = t; }

    public int getClimbTicks() { return climbTicks; }
    public void setClimbTicks(int t) { this.climbTicks = t; }

    public int getLiquidTicks() { return liquidTicks; }
    public void setLiquidTicks(int t) { this.liquidTicks = t; }

    public int getSinceVelocityTicks() { return sinceVelocityTicks; }
    public void setSinceVelocityTicks(int t) { this.sinceVelocityTicks = t; }
    public void incrementSinceVelocityTicks() { this.sinceVelocityTicks++; }

    public double getLastDeltaXZ() { return lastDeltaXZ; }
    public void setLastDeltaXZ(double d) { this.lastLastDeltaXZ = this.lastDeltaXZ; this.lastDeltaXZ = d; }

    public double getLastLastDeltaXZ() { return lastLastDeltaXZ; }

    public double getLastDeltaY() { return lastDeltaY; }
    public void setLastDeltaY(double d) { this.lastLastDeltaY = this.lastDeltaY; this.lastDeltaY = d; }

    public double getLastLastDeltaY() { return lastLastDeltaY; }

    public double getVelocityX() { return velocityX; }
    public void setVelocityX(double v) { this.velocityX = v; }

    public double getVelocityY() { return velocityY; }
    public void setVelocityY(double v) { this.velocityY = v; }

    public double getVelocityZ() { return velocityZ; }
    public void setVelocityZ(double v) { this.velocityZ = v; }

    public boolean isWasOnGround() { return wasOnGround; }
    public void setWasOnGround(boolean b) { this.wasLastOnGround = this.wasOnGround; this.wasOnGround = b; }

    public boolean isWasLastOnGround() { return wasLastOnGround; }

    public boolean isWasInLiquid() { return wasInLiquid; }
    public void setWasInLiquid(boolean b) { this.wasInLiquid = b; }

    public boolean isWasClimbing() { return wasClimbing; }
    public void setWasClimbing(boolean b) { this.wasClimbing = b; }

    public boolean isVelocityTaken() { return velocityTaken; }
    public void setVelocityTaken(boolean b) { this.velocityTaken = b; }

    public boolean isPlacingBlocks() { return placingBlocks; }
    public void setPlacingBlocks(boolean b) { this.placingBlocks = b; }

    public float getLastPitchDelta() { return lastPitchDelta; }
    public void setLastPitchDelta(float d) { this.lastPitchDelta = d; }

    public float getLastYawDelta() { return lastYawDelta; }
    public void setLastYawDelta(float d) { this.lastYawDelta = d; }

    public int getPitchTicks() { return pitchTicks; }
    public void setPitchTicks(int t) { this.pitchTicks = t; }

    public int getYawTicks() { return yawTicks; }
    public void setYawTicks(int t) { this.yawTicks = t; }

    public int getBuffer() { return buffer; }
    public void setBuffer(int b) { this.buffer = b; }
    public void incrementBuffer() { this.buffer++; }
    public void decrementBuffer() { this.buffer = Math.max(0, this.buffer - 1); }

    // Click analysis
    public void addClickDelta(double delta) {
        clickDeltas.add(delta);
        if (clickDeltas.size() > 20) {
            clickDeltas.poll();
        }
    }

    public double getAverageClickDelta() {
        if (clickDeltas.isEmpty()) return 0;
        return clickDeltas.stream().mapToDouble(d -> d).average().orElse(0);
    }

    public Queue<Double> getClickDeltas() {
        return clickDeltas;
    }

    public double getClickCps() {
        if (clickDeltas.isEmpty()) return 0;
        long total = clickDeltas.stream().mapToLong(d -> (long) (d * 1000)).sum();
        if (total == 0) return 0;
        return 1000.0 / (total / (double) clickDeltas.size());
    }

    // Packet timing analysis
    public void addPacketTime(long time) {
        packetTimes.add(time);
        if (packetTimes.size() > 40) {
            packetTimes.poll();
        }
    }

    public Queue<Long> getPacketTimes() {
        return packetTimes;
    }

    public double getAveragePacketDelta() {
        if (packetTimes.size() < 2) return 0;
        long[] arr = packetTimes.stream().mapToLong(l -> l).toArray();
        long totalDelta = 0;
        for (int i = 1; i < arr.length; i++) {
            totalDelta += (arr[i] - arr[i-1]);
        }
        return totalDelta / (double) (arr.length - 1);
    }

    // Move delta analysis
    public void addMoveDelta(double delta) {
        moveDeltas.add(delta);
        if (moveDeltas.size() > 20) {
            moveDeltas.poll();
        }
    }

    public double getAverageMoveDelta() {
        if (moveDeltas.isEmpty()) return 0;
        return moveDeltas.stream().mapToDouble(d -> d).average().orElse(0);
    }
}