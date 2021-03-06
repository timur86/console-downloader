package com.github.itimur;

import com.github.itimur.jimfs.JimfsURLStreamHandler;
import com.google.common.collect.ImmutableList;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Formatter;

/**
 * @author Timur
 */
public final class TestUtils {

    public static final int FIFTY_KB = 50 * 1024;
    public static final int FIVE_MB = 5 * 1024 * 1024;
    public static final ImmutableList<String> EXPECTED_LINES = ImmutableList.of(
            "http://mirror.internode.on.net/pub/test/50meg.test 50meg.test",
            "http://test.online.kz/download/swf.rar example.rar",
            "http://test.online.kz/download/swf.rar swf.rar");

    private static final String FILE_FORMAT = "%s %s%n";

    private TestUtils() {}

    public static void emptyFile(Path path) throws IOException {
        Files.write(path, new byte[0]);
    }

    public static URL write50KB_TestFile(Path path) throws IOException {
        Path testFile = Files.write(path.resolve("50KB.test"), new byte[FIFTY_KB]);
        return pathToURL(testFile);
    }

    public static URL write5MB_TestFile(Path path) throws IOException {
        Path testFile = Files.write(path.resolve("50MB.test"), new byte[FIVE_MB]);
        return pathToURL(testFile);
    }

    public static byte[] textLinksInBytes() {
        Formatter formatter = new Formatter();
        formatter.format(FILE_FORMAT, "http://mirror.internode.on.net/pub/test/50meg.test", "50meg.test").
                format(FILE_FORMAT, "http://test.online.kz/download/swf.rar", "example.rar").
                format(FILE_FORMAT, "http://test.online.kz/download/swf.rar", "swf.rar");
        return formatter.toString().getBytes(StandardCharsets.UTF_8);
    }

    /**
     * @see <a href="https://github.com/google/jimfs/issues/13">Issue #13</a>
     */
    private static URL pathToURL(Path path) throws MalformedURLException {
        return new URL(null, path.toUri().toString(), new JimfsURLStreamHandler());
    }
}
