package org.cleroux.passwdservice.model;

import java.util.List;

/**
 * Represents a group as defined in the group file.
 */
public class Group extends Principal {

    /**
     * Group ID.
     */
    private Integer gid = null;

    /**
     * List of member names.
     */
    private List<String> members = null;

    /**
     * Creates a Group with the given values.
     * @param name Group name
     * @param gid Group ID
     * @param members List of user names
     */
    public Group(final String name, final Integer gid,
                 final List<String> members) {
        this.name = name;
        this.gid = gid;
        this.members = members;
    }

    /**
     * Get this group's ID.
     * @return Group ID
     */
    public final Integer getGid() {
        return gid;
    }

    /**
     * Get a list of user names in this group.
     * @return List of user names in the group
     */
    public final List<String> getMembers() {
        return members;
    }

    /**
     * Determine if this group object matches the given arguments.
     * @param name Group name
     * @param gid Group ID
     * @param members List of user names
     * @return true if all non-null arguments match exactly or if the members
     *         list argument is a subset of this group's members. false
     *         otherwise.
     */
    public final boolean matches(final String name, final Integer gid,
                                 final List<String> members) {

        return compare(this.name, name)
               && compare(this.gid, gid)
               && compareList(this.members, members);
    }

}
