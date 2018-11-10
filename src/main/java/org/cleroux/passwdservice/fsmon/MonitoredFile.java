package org.cleroux.passwdservice.fsmon;

/**
 * Stores information about a file being monitored by the Monitor.
 */
public class MonitoredFile {

    /**
     * Path to the file being monitored.
     */
    private String filePath;

    /**
     * Class providing callback to be executed for file events.
     */
    private MonitorHandler handler;

    /**
     * Create a MonitoredFile with the given values.
     * @param filePath Path to the file being monitored
     * @param handler Object providing methods to run when file events occur.
     */
    public MonitoredFile(final String filePath, final MonitorHandler handler) {
        this.filePath = filePath;
        this.handler = handler;
    }

    /**
     * Execute the callback function when the file is changed.
     */
    public final void changed() {
        handler.changed(filePath);
    }

    /**
     * @return String file path for this monitored file.
     */
    public final String getFilePath() {
        return filePath;
    }

}
