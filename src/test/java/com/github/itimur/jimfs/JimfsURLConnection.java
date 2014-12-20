package com.github.itimur.jimfs;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * @author Timur
 * @see <a href="https://github.com/google/jimfs/issues/13">Issue #13</a>
 */
public class JimfsURLConnection extends HttpURLConnection {

    private final URL url;
    private final Path path;

    public JimfsURLConnection(URL url, Path path) {
        super(url);
        this.url = url;
        this.path = path;
    }

    @Override
    public void connect() throws IOException {
        throw new IOException("Cannot connect to " + url);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return Files.newInputStream(path, StandardOpenOption.READ);
    }

    @Override
    public long getContentLengthLong() {
        try {
            return Files.size(path);
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public void disconnect() {}

    @Override
    public boolean usingProxy() {
        return false;
    }
}
