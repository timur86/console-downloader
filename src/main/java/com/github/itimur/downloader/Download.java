package com.github.itimur.downloader;

import com.google.common.base.MoreObjects;

import java.net.URL;
import java.util.Objects;

/**
 * @author Timur
 */
public class Download {

    private final URL url;
    private final String filename;
    private long bytesRead = 0L;

    public Download(URL url, String filename) {
        this.url = Objects.requireNonNull(url, "url");
        this.filename = Objects.requireNonNull(filename, "filename");
    }

    public URL getUrl() {
        return url;
    }

    public String getFilename() {
        return filename;
    }

    public long getBytesRead() {
        return bytesRead;
    }

    public void setBytesRead(long bytesRead) {
        this.bytesRead = bytesRead;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that)
            return true;
        if (that == null || getClass() != that.getClass())
            return false;

        Download download = (Download) that;

        return Objects.equals(url, download.url) &&
                Objects.equals(filename, download.filename) &&
                Objects.equals(bytesRead, download.bytesRead);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, filename, bytesRead);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("url", url)
                .add("filename", filename)
                .add("bytesRead", bytesRead).toString();
    }

}
