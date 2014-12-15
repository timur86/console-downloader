package com.github.itimur.utils;

import com.github.itimur.downloader.Download;
import com.google.common.base.Stopwatch;
import com.google.common.jimfs.Jimfs;
import com.google.common.util.concurrent.RateLimiter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import static com.github.itimur.TestUtils.*;
import static com.github.itimur.utils.IOUtils.*;
import static com.github.itimur.utils.ParseUtils.parseTotalDownloadsRate;
import static com.google.common.truth.Truth.assertThat;
import static com.google.common.util.concurrent.RateLimiter.create;

public class IOUtilsTest {

    private static final RateLimiter RATE_50KB_PER_SECOND = create(parseTotalDownloadsRate("50k"));
    private static final RateLimiter RATE_2MB_PER_SECOND = create(parseTotalDownloadsRate("2m"));
    private FileSystem fileSystem;
    private Path downloadsPath;
    private Path filePath;

    @Before
    public void setUp() throws IOException {
        fileSystem = Jimfs.newFileSystem("test-file-system");
        downloadsPath = fileSystem.getPath("Downloads");
        Files.createDirectory(downloadsPath);
        filePath = Files.write(downloadsPath.resolve("links.txt"), textLinksInBytes());
    }

    @After
    public void tearDown() throws IOException {
        fileSystem.close();
    }

    @Test
    public void itShouldReadAllLinesFromFile() throws IOException {
        assertThat(readAllLinesFromFile(filePath)).isEqualTo(EXPECTED_LINES);
    }

    @Test(expected = IllegalArgumentException.class)
    public void itShouldThrowIAE_WhenFileIsEmpty() throws IOException {
        emptyFile(filePath);
        readAllLinesFromFile(filePath);
    }

    @Test(expected = NoSuchFileException.class)
    public void itShouldThrowNoSuchFileExceptionWhenFileDoesNotExist() throws IOException {
        readAllLinesFromFile(downloadsPath.resolve("non_existing_file.txt"));
    }

    @Test(timeout = 1100L)
    public void itShouldDownload50KB_FileWithGivenRateAtMostIn1Second() throws Exception {
        URL url = write50KB_TestFile(downloadsPath);
        Download download = new Download(url, "50KB.file");
        Stopwatch stopwatch = Stopwatch.createStarted();

        downloadFile(download, downloadsPath, RATE_50KB_PER_SECOND);
        assertThat(Files.size(downloadsPath.resolve("50KB.file"))).is(FIFTY_KB);
        assertDownloadTookAtMost(stopwatch, 1, TimeUnit.SECONDS);
    }

    @Test(timeout = 2100L)
    public void itShouldDownload5MB_FileWithGivenRateAtMostIn2Seconds() throws Exception {
        URL url = write5MB_TestFile(downloadsPath);
        Download download = new Download(url, "5MB.file");
        Stopwatch stopwatch = Stopwatch.createStarted();

        downloadFile(download, downloadsPath, RATE_2MB_PER_SECOND);
        assertThat(Files.size(downloadsPath.resolve("5MB.file"))).is(FIVE_MB);
        assertDownloadTookAtMost(stopwatch, 2, TimeUnit.SECONDS);
    }

    @Test(expected = IOException.class)
    public void itShouldThrowIOE_WhenContentLengthMismatchWithBytesRead() throws IOException {
        verifyBytesRead(FIVE_MB, FIFTY_KB);
    }

    private static void assertDownloadTookAtMost(Stopwatch stopwatch, long atMost, TimeUnit timeUnit) {
        stopwatch.stop();
        assertThat(stopwatch.elapsed(timeUnit)).isAtMost(atMost);
    }
}