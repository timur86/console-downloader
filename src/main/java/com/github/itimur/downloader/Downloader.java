package com.github.itimur.downloader;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.ListeningExecutorService;

/**
 * @author Timur
 */
public interface Downloader {

    void startAllAsync(ImmutableList<DownloadTask> tasks,
                       ListeningExecutorService executor,
                       EventBus eventBus);
}
