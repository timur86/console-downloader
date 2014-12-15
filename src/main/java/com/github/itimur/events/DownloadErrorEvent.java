package com.github.itimur.events;

import com.google.common.base.MoreObjects;

/**
 * @author Timur
 */
public class DownloadErrorEvent {

    private final Throwable cause;

    public DownloadErrorEvent(Throwable cause) {
        this.cause = cause;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("cause", cause).toString();
    }
}
