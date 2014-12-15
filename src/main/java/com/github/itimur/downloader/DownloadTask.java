package com.github.itimur.downloader;

import com.github.itimur.utils.IOUtils;
import com.google.common.util.concurrent.RateLimiter;

import java.nio.file.Path;
import java.util.concurrent.Callable;

/**
 * @author Timur
 */
public class DownloadTask implements Callable<Download> {

    private final Download download;
    private final Path path;
    private final RateLimiter rateLimiter;

    public DownloadTask(Download download, Path path, RateLimiter rateLimiter) {
        this.download = download;
        this.path = path;
        this.rateLimiter = rateLimiter;
    }

    @Override
    public Download call() throws Exception {
        IOUtils.downloadFile(download, path, rateLimiter);
        return download;
    }

}
