package com.senzer.enhancedrecyclerview.util;

public class FloatUtil {
    // epsilon: min value more than zero
    private final static float EPSILON = 0.00000001F;

    public static boolean compareFloats(float f1, float f2) {
        return Math.abs(f1 - f2) <= EPSILON;
    }
}
