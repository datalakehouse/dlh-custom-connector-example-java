package io.datalakehouse.helpers;

/**
 * @author Nisarg Raval
 * A tiny "require" validator helper (kills repeated null/blank checks)
 */
public final class Req {
    private Req() {
    }

    public static String required(String v, String msg) {
        if (v == null || v.isBlank()) {
            throw new IllegalArgumentException(msg);
        }
        return v;
    }

    public static boolean has(String v) {
        return v != null && !v.isBlank();
    }
}