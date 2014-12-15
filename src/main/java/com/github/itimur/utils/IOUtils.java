package com.github.itimur.utils;

import com.github.itimur.downloader.Download;
import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.RateLimiter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static com.github.itimur.utils.CommonUtils.requireNonEmpty;
import static com.google.common.collect.ImmutableList.builder;
import static java.lang.String.format;
import static java.nio.file.Files.newBufferedReader;
import static java.nio.file.Files.newOutputStream;

/**
 * @author Timur
 */
public final class IOUtils {

    private static final int BUFFER_SIZE = 8192;
    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:33.0) Gecko/20100101 Firefox/33.0";
    private static final OpenOption[] OPEN_OPTIONS = {StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE};
    private static final int EOF = -1;

    private IOUtils() {}

    public static ImmutableList<String> readAllLinesFromFile(Path path) throws IOException {
        try (BufferedReader reader = newBufferedReader(path, StandardCharsets.UTF_8)) {
            ImmutableList.Builder<String> listBuilder = builder();
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                listBuilder.add(line);
            }
            return requireNonEmpty(listBuilder.build(), path.toString());
        }
    }

    public static void downloadFile(Download download, Path path, RateLimiter rateLimiter) throws IOException {
        HttpURLConnection connection = newHttpURLConnection(download.getUrl());
        long contentLength = connection.getContentLengthLong();
        long bytesRead;
        try (InputStream in = connection.getInputStream();
             OutputStream out = newOutputStream(path.resolve(download.getFilename()), OPEN_OPTIONS)) {
            bytesRead = copy(in, out, rateLimiter);
        }
        verifyBytesRead(bytesRead, contentLength);
        download.setBytesRead(bytesRead);
    }

    private static HttpURLConnection newHttpURLConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // some servers send 403 Forbidden with default Java user agent
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.setConnectTimeout(15_000);
        connection.setReadTimeout(30_000);
        return connection;
    }

    private static long copy(InputStream in, OutputStream out, RateLimiter rateLimiter) throws IOException {
        long bytesRead = 0L;
        byte[] buf = new byte[BUFFER_SIZE];
        for (int n = in.read(buf); n != EOF; n = in.read(buf)) {
            out.write(buf, 0, n);
            bytesRead += n;
            rateLimiter.acquire(n);
        }
        return bytesRead;
    }

    static void verifyBytesRead(long bytesRead, long contentLength) throws IOException {
        if (bytesRead != contentLength)
            throw new IOException(format("Only read %d bytes but expected %d bytes%n", bytesRead, contentLength));
    }

}
