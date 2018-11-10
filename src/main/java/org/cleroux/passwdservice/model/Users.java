package org.cleroux.passwdservice.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.cleroux.passwdservice.fsmon.Monitor;
import org.cleroux.passwdservice.fsmon.MonitorHandler;

/**
 * Singleton object provides a searchable data structure of User objects.
 * The passwd file is monitored for changes and reloaded automatically.
 */
public final class Users extends PrincipalsFile {

    /**
     * Field number for the name field.
     */
    private static final int FIELD_NAME = 0;

    /**
     * Field number for the user ID field.
     */
    private static final int FIELD_UID = 2;

    /**
     * Field number for the group ID field.
     */
    private static final int FIELD_GID = 3;

    /**
     * Field number for the comment field.
     */
    private static final int FIELD_COMMENT = 4;

    /**
     * Field number for the home directory field.
     */
    private static final int FIELD_HOME = 5;

    /**
     * Field number for the executable shell field.
     */
    private static final int FIELD_SHELL = 6;

    /**
     * The property name used to store the path to the passwd file.
     */
    protected static final String PROP_PASSWD_PATH = "passwdFile";

    /**
     * The default path to the passwd file.
     */
    protected static final String DEFAULT_PASSWD_PATH = "/etc/passwd";

    /**
     * Map of users indexed by user ID.
     */
    private static Map<Integer, User> users = null;

    /**
     * Log utility.
     */
    private static final Logger LOG = Logger.getLogger(Users.class);

    static {
        String passwdFilePath = System.getProperties()
            .getProperty(PROP_PASSWD_PATH, DEFAULT_PASSWD_PATH);
        loadFile(passwdFilePath);

        MonitorHandler handler = new MonitorHandler() {
            public void changed(final String filePath) {
                loadFile(filePath);
            }
        };

        Monitor.monitor(passwdFilePath, handler);
    }

    /**
     * Private constructor prevents instantiation of this class.
     */
    private Users() { }

    /**
     * List all users.
     * @return List of all User objects.
     */
    public static List<User> getUsers() {
        return new ArrayList<User>(users.values());
    }

    /**
     * Get a list of users that match the given values.
     * If an argument is null, it will not be used to eliminate matches.
     * @param name String user login name.
     * @param uid Integer user id.
     * @param gid Integer primary group id.
     * @param comment String comment (also referred to as the GECOS field).
     * @param home String user's home directory.
     * @param shell String path to shell program to run at login.
     * @return List of User that match all non-null arguments exactly.
     */
    public static List<User> getUsers(final String name, final Integer uid,
                                      final Integer gid, final String comment,
                                      final String home, final String shell) {

        List<User> filteredUsers = new ArrayList<User>();

        for (User user : users.values()) {
            if (user.matches(name, uid, gid, comment, home, shell)) {
                filteredUsers.add(user);
            }
        }

        return filteredUsers;
    }

    /**
     * Get a specific user by their user id.
     * @param uid Integer user id of the desired user.
     * @return User with the specified user id. Return null if the ID is not
     *         found.
     */
    public static User getUser(final Integer uid) {
        return users.get(uid);
    }

    /**
     * Parse the specified file.
     * Updates the users data used by the application.
     * The users Map is only updated if the file is parsed without errors.
     * @param filePath path to file to load.
     */
    private static void loadFile(final String filePath) {

        Map<Integer, User> newUsers = new HashMap<Integer, User>();

        try {

            for (String line : readLines(filePath)) {

                String[] fields = line.split(":");

                String name = fields[FIELD_NAME];
                Integer uid = Integer.valueOf(fields[FIELD_UID]);
                Integer gid = Integer.valueOf(fields[FIELD_GID]);
                String comment = fields[FIELD_COMMENT];
                String home = fields[FIELD_HOME];
                String shell = fields[FIELD_SHELL];

                User user = new User(name, uid, gid, comment, home, shell);
                newUsers.put(uid, user);
            }

            users = newUsers;

        } catch (Exception e) {
            LOG.error("Failed to read " + filePath + ": " + e);
        }

    }

}
