package com.github.itimur;

import com.github.itimur.downloader.AsyncDownloader;
import com.github.itimur.downloader.Download;
import com.github.itimur.downloader.DownloadTask;
import com.github.itimur.events.DownloadErrorEvent;
import com.github.itimur.events.DownloadEvent;
import com.github.itimur.utils.CommonUtils;
import com.github.itimur.utils.IOUtils;
import com.github.itimur.utils.ParseUtils;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.RateLimiter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Timur
 */
public class DownloadManager {

    private final ImmutableList.Builder<DownloadTask> downloadsTaskBuilder = ImmutableList.builder();
    private final ListeningExecutorService executor;
    private final Path downloadsFolderPath;
    private final RateLimiter rateLimiter;
    private final EventBus eventBus;
    private long totalDownloadedBytes = 0L;
    private int remainingTasksCounter = 0;

    public DownloadManager(int numberOfSimultaneousDownloads, long totalDownloadsRate, Path downloadsFolderPath) {
        CommonUtils.requirePositive(numberOfSimultaneousDownloads, "numberOfSimultaneousDownloads");
        this.downloadsFolderPath = downloadsFolderPath;
        executor = MoreExecutors.listeningDecorator(MoreExecutors.getExitingExecutorService((ThreadPoolExecutor)
                Executors.newFixedThreadPool(numberOfSimultaneousDownloads)));
        rateLimiter = RateLimiter.create(totalDownloadsRate);
        eventBus = new EventBus(DownloadManager.class.getName());
    }

    public void addDownloadsFromFile(Path filePath) {
        try {
            for (String line : IOUtils.readAllLinesFromFile(filePath)) {
                addDownloadTask(ParseUtils.lineToDownload(line));
            }
        } catch (IOException e) {
            System.err.println("An I/O error has occurred while opening the file: " + e);
        }
    }

    public void start() {
        ImmutableList<DownloadTask> tasks = downloadsTaskBuilder.build();
        remainingTasksCounter = tasks.size();
        eventBus.register(this);
        Stopwatch stopwatch = Stopwatch.createStarted();
        new AsyncDownloader().startAllAsync(tasks, executor, eventBus);
        awaitStop(stopwatch);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void completed(DownloadEvent downloadEvent) {
        System.out.printf("\r%s%n", downloadEvent);
        totalDownloadedBytes += downloadEvent.getBytesRead();
        decrementRemainingTasks();
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void failed(DownloadErrorEvent downloadErrorEvent) {
        System.err.printf("\r%s%n", downloadErrorEvent);
        decrementRemainingTasks();
    }

    private void decrementRemainingTasks() {
        remainingTasksCounter--;
    }

    private void awaitStop(Stopwatch stopwatch) {
        try {
            showProgressBar();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            eventBus.unregister(this);
            System.out.printf("\rElapsed Time: %s%nTotal Downloaded Bytes: %,d%n",
                    stopwatch.stop(), totalDownloadedBytes);
        }
    }

    private void addDownloadTask(Download download) {
        downloadsTaskBuilder.add(new DownloadTask(download, downloadsFolderPath, rateLimiter));
    }

    private void showProgressBar() throws InterruptedException {
        char[] chars = {'|', '/', '-', '\\'};
        for (int i = 0; remainingTasksCounter > 0; i++) {
            System.out.printf("\r%c", chars[i % chars.length]);
            TimeUnit.MILLISECONDS.sleep(100L);
        }
    }

}
