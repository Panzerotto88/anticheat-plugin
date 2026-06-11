package com.anticheat;

/**
 * Utility matematiche per i checks dell'anti-cheat.
 */
public final class MathUtils {

    private MathUtils() {}

    /**
     * Calcola la distanza orizzontale al quadrato tra due punti.
     */
    public static double horizontalDistanceSquared(double x1, double z1, double x2, double z2) {
        double dx = x2 - x1;
        double dz = z2 - z1;
        return dx * dx + dz * dz;
    }

    /**
     * Calcola la distanza orizzontale tra due punti.
     */
    public static double horizontalDistance(double x1, double z1, double x2, double z2) {
        return Math.sqrt(horizontalDistanceSquared(x1, z1, x2, z2));
    }

    /**
     * Calcola la distanza 3D tra due punti.
     */
    public static double distance3D(double x1, double y1, double z1, double x2, double y2, double z2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        double dz = z2 - z1;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    /**
     * Verifica se un valore è quasi zero.
     */
    public static boolean almostZero(double value, double threshold) {
        return Math.abs(value) < threshold;
    }

    /**
     * Calcola l'accelerazione da tre campioni di velocità.
     */
    public static double acceleration(double v1, double v2, double v3) {
        return v3 - (2 * v2) + v1;
    }

    /**
     * Verifica se un numero è finito e valido.
     */
    public static boolean isFinite(double value) {
        return Double.isFinite(value) && !Double.isNaN(value) && Math.abs(value) < 1e8;
    }

    /**
     * Arrotonda alla precisione specificata.
     */
    public static double round(double value, int precision) {
        double scale = Math.pow(10, precision);
        return Math.round(value * scale) / scale;
    }

    /**
     * Calcola la differenza angolare minima tra due angoli (in gradi).
     */
    public static double angleDifference(float a1, float a2) {
        double diff = Math.abs(a1 - a2) % 360.0;
        if (diff > 180.0) {
            diff = 360.0 - diff;
        }
        return diff;
    }

    /**
     * Calcola la probabilità usando la distribuzione normale.
     */
    public static double gaussianProbability(double value, double mean, double stdDev) {
        if (stdDev == 0) return 0;
        double exponent = -((value - mean) * (value - mean)) / (2 * stdDev * stdDev);
        return (1.0 / (stdDev * Math.sqrt(2 * Math.PI))) * Math.exp(exponent);
    }
}