package com.github.itimur.jimfs;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.nio.file.Paths;

/**
 * @author Timur
 */
public class JimfsURLStreamHandler extends URLStreamHandler {

    @Override
    protected URLConnection openConnection(URL url) throws IOException {
        try {
            return new JimfsURLConnection(url, Paths.get(url.toURI()));
        } catch (URISyntaxException e) {
            throw new AssertionError(e);
        }
    }

}
