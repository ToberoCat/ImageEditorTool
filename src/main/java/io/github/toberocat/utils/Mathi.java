package io.github.toberocat.utils;

public class Mathi {
    public static int clamp(int value, int min, int max) {
        return Math.max(Math.min(value, max), min);
    }
}
