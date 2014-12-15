package com.github.itimur.utils;

/**
 * @author Timur
 */
public enum ByteUnit {

    BYTE(1), KILOBYTE(1024), MEGABYTE(1024 * 1024);

    private final long size;

    ByteUnit(long size) {
        this.size = size;
    }

    public long size() {
        return size;
    }

    public String allowedSuffixes() {
        return "\"b\", \"k\", \"m\"";
    }

}
