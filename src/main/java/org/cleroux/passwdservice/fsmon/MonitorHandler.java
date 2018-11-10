package org.cleroux.passwdservice.fsmon;

/**
 * Defines the interface for an object that handles file changed events.
 */
public interface MonitorHandler {

    /**
     * Implement this method to be called by the Monitor when a file changes.
     * @param filePath Path of the changed file.
     */
    void changed(String filePath);
}
