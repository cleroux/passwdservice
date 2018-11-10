package org.cleroux.passwdservice.fsmon;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * Thread that monitors individual files and executes a callback when files
 * are modified.
 */
public final class Monitor implements Runnable {

    /**
     * Delay time in milliseconds to allow file system events to accumulate.
     */
    private static final int MONITOR_EVENT_DELAY = 50;

    /**
     * Persistent watch service monitors file system events.
     */
    private static WatchService watcher;

    /**
     * A list of files being monitored.
     */
    private static List<MonitoredFile> monitoredFiles =
        new ArrayList<MonitoredFile>();

    /**
     * Paths monitored by the watch service indexed by their watch keys.
     */
    private static Map<WatchKey, Path> keys = new HashMap<WatchKey, Path>();

    /**
     * Keys monitored by the watch service indexed by their paths.
     */
    private static Map<Path, WatchKey> keysByPath =
        new HashMap<Path, WatchKey>();

    /**
     * Log utility.
     */
    private static final Logger LOG = Logger.getLogger(Monitor.class);

    static {
        try {
            watcher = FileSystems.getDefault().newWatchService();

            Thread monitorThread = new Thread(new Monitor(),
                "Passwd as a Service File Monitor");
            monitorThread.start();
        } catch (IOException e) {
            LOG.error("Failed to create watch service.");
        }
    }

    /**
     * Private constructor prevents instantiation.
     */
    private Monitor() { }

    /**
     * Add a file to the monitoring thread.
     * @param filePath Path to the file to be monitored
     * @param handler A class providing the callback
     */
    public static synchronized void monitor(final String filePath,
                                            final MonitorHandler handler) {

        try {
            Path watchPath = getDirectory(Paths.get(filePath));

            // Check if already watching given directory, reuse watchKeys
            WatchKey watchKey = keysByPath.get(watchPath);
            if (watchKey == null) {
                watchKey = watchPath.register(watcher, ENTRY_MODIFY);
                keys.put(watchKey, watchPath);
                keysByPath.put(watchPath, watchKey);
            }

            MonitoredFile f = new MonitoredFile(filePath, handler);
            monitoredFiles.add(f);
            LOG.info("Monitoring " + filePath);
        } catch (Exception e) {
            LOG.error("Failed to monitor " + filePath + ": " + e);
        }
    }

    /**
     * Main loop of the monitoring thread.
     */
    public void run() {

        try {
            for (;;) {
                WatchKey key;
                try {
                    Thread.sleep(MONITOR_EVENT_DELAY); // Filter duplicates
                    key = watcher.take();
                } catch (InterruptedException e) {
                    break;
                }

                Path dir = keys.get(key);
                if (dir == null) {
                    continue;
                }

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind kind = event.kind();
                    if (kind == OVERFLOW) {
                        LOG.warn("File system event overflow");
                        continue;
                    }

                    WatchEvent<Path> ev = cast(event);
                    Path filename = ev.context();
                    Path child = dir.resolve(filename);

                    if (kind == ENTRY_MODIFY) {
                        // Compare filename to our list of MonitoredFiles
                        for (MonitoredFile f : monitoredFiles) {
                            if (f.getFilePath().equals(child.toString())) {
                                f.changed();
                            }
                        }
                    }

                }

                boolean valid = key.reset();
                if (!valid) {
                    keys.remove(key);
                }
            }
        } catch (Exception e) {
            LOG.error("Watcher error: " + e);
        } finally {
            try {
                watcher.close();
            } catch (Exception e) { }
        }

    }

    /**
     * Return the directory path of the given path.
     * If the path is a directory, return it unchanged.
     * @param path A Path to a file or directory
     * @return Directory path
     */
    private static Path getDirectory(final Path path) {
        if (path.toFile().isDirectory()) {
            return path;
        }
        return path.getParent();
    }

    /**
     * Utility function to cast event to their proper type.
     * @param <T> Type of the event.
     * @param event Event to be cast.
     * @return Properly cast event
     */
    @SuppressWarnings("unchecked")
    private static <T> WatchEvent<T> cast(final WatchEvent<?> event) {
        return (WatchEvent<T>) event;
    }

}
