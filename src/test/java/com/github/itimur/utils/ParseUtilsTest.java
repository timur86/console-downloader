package com.github.itimur.utils;

import com.github.itimur.downloader.Download;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.MissingFormatArgumentException;

import static com.github.itimur.utils.ParseUtils.*;
import static com.google.common.truth.Truth.assertThat;

/**
 * @author Timur
 */
public class ParseUtilsTest {

    private static final String HTTP_STR_URL = "http://example.com/archive.zip";
    private static final int TWO_THOUSAND_KB = 2000 * 1024;
    private static final int ONE_MB = 1024 * 1024;

    @Test
    public void itShouldParseUrlStringToURL() throws Exception {
        URL expectedUrl = new URL(HTTP_STR_URL);
        assertThat(parseURL(HTTP_STR_URL)).isEqualTo(expectedUrl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void itShouldThrowIAE_IfProtocolIsNotHttp() {
        parseURL("https://example.com/archive.zip");
    }

    @Test(expected = IllegalArgumentException.class)
    public void itShouldThrowIAE_WithUrlThatDoesNotContainFilename() {
        parseURL("http://example.com/");
    }

    @Test(expected = IllegalArgumentException.class)
    public void itShouldThrowIAE_WhenUrlArgumentIsEmpty() {
        parseURL("");
    }

    @Test
    public void itShouldParseTotalDownloadsRateInBytes() {
        assertThat(parseTotalDownloadsRate("2000b")).is(2000);
    }

    @Test
    public void itShouldParseTotalDownloadsRateInKilobytes() {
        assertThat(parseTotalDownloadsRate("2000k")).is(TWO_THOUSAND_KB);
    }

    @Test
    public void itShouldParseTotalDownloadsRateInMegabytes() {
        assertThat(parseTotalDownloadsRate("1m")).is(ONE_MB);
    }

    @Test(expected = IllegalArgumentException.class)
    public void itShouldThrowIAE_WithNotAllowedUnitSuffix() {
        parseTotalDownloadsRate("10g");
    }

    @Test(expected = IllegalArgumentException.class)
    public void itShouldThrowIAE_WithoutUnitSuffix() {
        parseTotalDownloadsRate("150");
    }

    @Test(expected = IllegalArgumentException.class)
    public void itShouldThrowIAE_WhenRateArgumentIsEmpty() {
        parseTotalDownloadsRate("");
    }

    @Test
    public void itShouldSplitLineByUrlAndFilename() {
        String[] expectedParts = {"http://example.com/image.jpg", "image.jpg"};
        assertThat(splitLineByUrlAndFilename("http://example.com/image.jpg image.jpg")).isEqualTo(expectedParts);
    }

    @Test(expected = MissingFormatArgumentException.class)
    public void itShouldThrowMFAE_WhenLineArgumentIsEmpty() {
        splitLineByUrlAndFilename("");
    }

    @Test(expected = MissingFormatArgumentException.class)
    public void itShouldThrowMFAE_WithoutURL() {
        splitLineByUrlAndFilename("image.jpg");
    }

    @Test(expected = MissingFormatArgumentException.class)
    public void itShouldThrowMFAE_WithBlankURL() {
        splitLineByUrlAndFilename("  image.jpg");
    }

    @Test(expected = MissingFormatArgumentException.class)
    public void itShouldThrowMFAE_WithoutFilename() {
        splitLineByUrlAndFilename("http://example.com/image.jpg");
    }

    @Test(expected = MissingFormatArgumentException.class)
    public void itShouldThrowMFAE_WithBlankFilename() {
        splitLineByUrlAndFilename("http://example.com/image.jpg  ");
    }

    @Test
    public void itShouldConvertLineToDownload() throws MalformedURLException {
        Download expectedDownload = new Download(new URL("http://example.com/image.jpg"), "image.jpg");
        assertThat(lineToDownload("http://example.com/image.jpg image.jpg")).isEqualTo(expectedDownload);
    }
}
