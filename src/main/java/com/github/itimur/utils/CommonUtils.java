package com.github.itimur.utils;

import java.util.Collection;

/**
 * @author Timur
 */
public final class CommonUtils {

    private CommonUtils() {}

    public static int requirePositive(int n, String message) {
        if (n <= 0) {
            throw new IllegalArgumentException(message + " can't be negative or zero");
        }
        return n;
    }

    public static String requireNonEmpty(String argument, String message) {
        if (argument == null) {
            throw new IllegalArgumentException(message + " can't be null");
        }
        if (argument.isEmpty()) {
            throw new IllegalArgumentException(message + " can't be empty");
        }
        return argument;
    }

    public static <E, T extends Collection<E>> T requireNonEmpty(T argument, String message) {
        if (argument == null) {
            throw new IllegalArgumentException(message + " can't be null");
        }
        if (argument.isEmpty()) {
            throw new IllegalArgumentException(message + " can't be empty");
        }
        return argument;
    }

}
