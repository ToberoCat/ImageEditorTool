package io.github.toberocat.utils;

public class Mathf {
    public static int clamp(int value, int max) {
        return Math.max(Math.min(value, max), 0);
    }

    public static float clamp(float value, float min, float max) {
        return Math.max(Math.min(value, max), min);
    }

    public static int clampRound(float value, float min, float max) {
        return Math.round(Math.max(Math.min(value, max), min));
    }
}
