package com.github.itimur.events;

import com.github.itimur.downloader.Download;

/**
 * @author Timur
 */
public class DownloadEvent {

    private final Download download;

    public DownloadEvent(Download download) {
        this.download = download;
    }

    public long getBytesRead() {
        return download.getBytesRead();
    }

    @Override
    public String toString() {
        return download.toString();
    }
}
