package org.cleroux.passwdservice.model;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.cleroux.passwdservice.fsmon.Monitor;
import org.cleroux.passwdservice.fsmon.MonitorHandler;

/**
 * Singleton object provides a searchable data structure of Group objects.
 * The group file is monitored for changes and reloaded automatically.
 */
public final class Groups extends PrincipalsFile {

    /**
     * Field number for the name field.
     */
    private static final int FIELD_NAME = 0;

    /**
     * Field number for the group ID field.
     */
    private static final int FIELD_GID = 2;

    /**
     * Field number for the members field.
     */
    private static final int FIELD_MEMBERS = 3;

    /**
     * The property name used to store the path to the group file.
     */
    protected static final String PROP_GROUP_PATH = "groupFile";

    /**
     * The default path to the group file.
     */
    protected static final String DEFAULT_GROUP_PATH = "/etc/group";

    /**
     * Map of groups index by group ID.
     */
    private static Map<Integer, Group> groups = null;

    /**
     * Log utility.
     */
    private static final Logger LOG = Logger.getLogger(Groups.class);

    static {
        String groupFilePath = System.getProperties()
            .getProperty(PROP_GROUP_PATH, DEFAULT_GROUP_PATH);
        loadFile(groupFilePath);

        MonitorHandler handler = new MonitorHandler() {
            public void changed(final String filePath) {
                loadFile(filePath);
            }
        };

        Monitor.monitor(groupFilePath, handler);
    }

    /**
     * Private constructor prevents instantiation of this class.
     */
    private Groups() { }

    /**
     * List all groups.
     * @return List of all Group objects.
     */
    public static List<Group> getGroups() {
        return new ArrayList<Group>(groups.values());
    }

    /**
     * Get a list of groups that match the given values.
     * If an argument is null, it will not be used to eliminate matches.
     * @param name Group name.
     * @param gid Group ID.
     * @param members List of user names.
     * @return List of Group that match all non-null arguments exactly. However
     *         the members argument may be a subset of the members list.
     */
    public static List<Group> getGroups(final String name, final Integer gid,
                                        final List<String> members) {

        List<Group> filteredGroups = new ArrayList<Group>();

        for (Group group : groups.values()) {
            if (group.matches(name, gid, members)) {
                filteredGroups.add(group);
            }
        }

        return filteredGroups;
    }

    /**
     * Get a specific group by its group ID.
     * @param gid Group ID of the desired group.
     * @return Group with the specified group ID. Return null if the ID is not
     *         found.
     */
    public static Group getGroup(final Integer gid) {
        return groups.get(gid);
    }

    /**
     * Parse the specified group file.
     * Updates the groups data used by the application.
     * The groups Map is only updated if the file is parsed without errors.
     * @param filePath Path to the file to load.
     */
    private static void loadFile(final String filePath) {

        Map<Integer, Group> newGroups = new HashMap<Integer, Group>();

        try {

            for (String line : readLines(filePath)) {

                String[] fields = line.split(":");

                String name = fields[FIELD_NAME];
                Integer gid = Integer.valueOf(fields[FIELD_GID]);
                String[] members = {};
                if (fields.length > FIELD_MEMBERS) {
                    members = fields[FIELD_MEMBERS].split(",");
                }

                Group group = new Group(name, gid, Arrays.asList(members));
                newGroups.put(gid, group);
            }

            groups = newGroups;

        } catch (Exception e) {
            LOG.error("Failed to read " + filePath + ": " + e);
        }

    }

}
