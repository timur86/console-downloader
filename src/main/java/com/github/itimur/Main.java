package com.github.itimur;

import com.github.itimur.utils.ParseUtils;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.ParserProperties;

import java.nio.file.Path;

import static org.kohsuke.args4j.OptionHandlerFilter.REQUIRED;

/**
 * @author Timur
 */
public class Main {

    @Option(name = "-n", usage = "number of simultaneous downloads", required = true)
    private int numberOfSimultaneousDownloads;

    @Option(name = "-l", usage = "total downloads rate", required = true)
    private String totalDownloadsRate;

    @Option(name = "-o", usage = "downloads folder path", required = true)
    private Path downloadsFolderPath;

    @Option(name = "-f", usage = "links file path", required = true)
    private Path filePath;

    public static void main(String[] args) {
        new Main().doMain(args);
    }

    private void doMain(String[] args) {
        CmdLineParser parser = new CmdLineParser(this, ParserProperties.defaults().withOptionSorter(null));
        try {
            parser.parseArgument(args);
            go();
        } catch (CmdLineException e) {
            System.err.println("Error: " + e.getMessage());
            System.out.printf("usage: %s%s%n", "java -jar console-downloader.jar", parser.printExample(REQUIRED));
            parser.printUsage(System.out);
        }
    }

    private void go() {
        DownloadManager dm = new DownloadManager(numberOfSimultaneousDownloads,
                ParseUtils.parseTotalDownloadsRate(totalDownloadsRate), // TODO impl. OptionHandler
                downloadsFolderPath);
        dm.addDownloadsFromFile(filePath);
        dm.start();
    }

}
