package com.github.itimur.utils;

import com.github.itimur.downloader.Download;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.MissingFormatArgumentException;

import static com.github.itimur.utils.CommonUtils.requireNonEmpty;
import static java.util.Objects.requireNonNull;

/**
 * @author Timur
 */
public final class ParseUtils {

    private static final int MIN_FILENAME_LENGTH = 2;

    private ParseUtils() {}

    public static long parseTotalDownloadsRate(String totalDownloadsRate) {
        requireNonNull(totalDownloadsRate, "totalDownloadsRate");
        requireNonEmpty(totalDownloadsRate, "totalDownloadsRate");

        int suffixPosition = totalDownloadsRate.length() - 1;
        ByteUnit byteUnit = ByteUnit.BYTE;
        switch (totalDownloadsRate.charAt(suffixPosition)) {
            case 'b':
                break;
            case 'k':
                byteUnit = ByteUnit.KILOBYTE;
                break;
            case 'm':
                byteUnit = ByteUnit.MEGABYTE;
                break;
            default:
                throw new IllegalArgumentException("Allowed unit suffixes are: " + byteUnit.allowedSuffixes());
        }
        return Long.parseLong(totalDownloadsRate.substring(0, suffixPosition)) * byteUnit.size();
    }

    public static Download lineToDownload(String line) {
        String[] parts = splitLineByUrlAndFilename(requireNonNull(line, "line"));
        return new Download(parseURL(parts[0]), parts[1]);
    }

    static String[] splitLineByUrlAndFilename(String line) {
        String[] parts = line.split("\\s");
        if (parts.length < 2 || parts[1].isEmpty())
            throw new MissingFormatArgumentException("<HTTP url><space><filename>");
        return parts;
    }

    static URL parseURL(String httpUrl) {
        try {
            URL url = new URL(httpUrl);
            if (!"http".equalsIgnoreCase(url.getProtocol()))
                throw new MalformedURLException("Only HTTP protocol supported");
            else if (url.getFile().length() < MIN_FILENAME_LENGTH)
                throw new IllegalArgumentException("Url doesn't contain filename");
            return url;
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
