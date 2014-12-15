package com.github.itimur.callbacks;

import com.github.itimur.downloader.Download;
import com.github.itimur.events.DownloadErrorEvent;
import com.github.itimur.events.DownloadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.FutureCallback;

import java.util.Objects;

/**
 * @author Timur
 */
public class FutureDownloadCallback implements FutureCallback<Download> {

    private final EventBus eventBus;

    public FutureDownloadCallback(EventBus eventBus) {
        this.eventBus = Objects.requireNonNull(eventBus, "eventBus");
    }

    @Override
    public void onSuccess(Download result) {
        eventBus.post(new DownloadEvent(result));
    }

    @Override
    public void onFailure(Throwable t) {
        eventBus.post(new DownloadErrorEvent(t));
    }

}
