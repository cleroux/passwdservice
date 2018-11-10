package org.cleroux.passwdservice.model;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Superclass providing utility methods for system files.
 */
public abstract class PrincipalsFile {

    /**
     * Log utility.
     */
    private static final Logger LOG = Logger.getLogger(PrincipalsFile.class);

    /**
     * Read the contents of the specified file.
     * @param filePath String path to the file to be read.
     * @return List of String containing each line of the file.
     * @throws Exception on any error while reading the file.
     */
    protected static List<String> readLines(final String filePath)
        throws Exception {

        LOG.debug("Reading " + filePath);

        Path path = FileSystems.getDefault().getPath(filePath);
        List<String> lines = null;
        lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        return lines;
    }

}
