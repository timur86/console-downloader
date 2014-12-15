console-downloader
==================

[![Build Status](https://travis-ci.org/itimur/console-downloader.svg)](https://travis-ci.org/itimur/console-downloader)
[![Dependency Status](https://www.versioneye.com/user/projects/548f0876dd709d3ee20000f6/badge.svg?style=flat)](https://www.versioneye.com/user/projects/548f0876dd709d3ee20000f6)

**Build an executable JAR**

To build a single executable JAR file that contains all the necessary dependencies. On Windows platforms, type this command:

```sh
> gradlew.bat build
```

On Mac OS and Linux platforms, type these commands:

```sh
$ chmod +x gradlew
$ ./gradlew build
```

Then you can run the JAR file:

```sh
java -jar build/libs/console-downloader-1.0.jar -n 5 -l 2000k -o output_folder -f links.txt
```
