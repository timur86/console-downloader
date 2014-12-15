package com.github.itimur.downloader;

import com.github.itimur.callbacks.FutureDownloadCallback;
import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;

/**
 * @author Timur
 */
public class AsyncDownloader implements Downloader {

    @Override
    public void startAllAsync(ImmutableList<DownloadTask> tasks,
                              ListeningExecutorService executor,
                              EventBus evenBus) {
        FutureDownloadCallback downloadCallback = new FutureDownloadCallback(evenBus);
        for (ListenableFuture<Download> future : submitTasks(tasks, executor)) {
            Futures.addCallback(future, downloadCallback, executor);
        }
    }

    private ImmutableList<ListenableFuture<Download>> submitTasks(ImmutableList<DownloadTask> tasks,
                                                                  ListeningExecutorService executor) {
        ImmutableList.Builder<ListenableFuture<Download>> futures = ImmutableList.builder();
        for (DownloadTask task : tasks) {
            futures.add(executor.submit(task));
        }
        return futures.build();
    }
}
