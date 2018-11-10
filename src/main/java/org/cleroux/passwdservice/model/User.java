package org.cleroux.passwdservice.model;

/**
 * Represents a user as defined in the passwd file.
 */
public class User extends Principal {

    /**
     * User ID.
     */
    private Integer uid = null;

    /**
     * Group ID.
     */
    private Integer gid = null;

    /**
     * Comment.
     */
    private String comment = null;

    /**
     * Path to user's home directory.
     */
    private String home = null;

    /**
     * Path to shell executable.
     */
    private String shell = null;

    /**
     * Creates a User with the given values.
     * @param name User name
     * @param uid User ID
     * @param gid Group ID
     * @param comment Comment
     * @param home Path of user's home directory
     * @param shell Path to user's shell executable
     */
    public User(final String name, final Integer uid, final Integer gid,
                final String comment, final String home, final String shell) {
        this.name = name;
        this.uid = uid;
        this.gid = gid;
        this.comment = comment;
        this.home = home;
        this.shell = shell;
    }

    /**
     * Get this user's ID.
     * @return User ID
     */
    public final Integer getUid() {
        return uid;
    }

    /**
     * Get this user's group ID.
     * @return Group ID
     */
    public final Integer getGid() {
        return gid;
    }

    /**
     * Get this user's comment.
     * @return Comment
     */
    public final String getComment() {
        return comment;
    }

    /**
     * Get this user's home directory.
     * @return Path of this user's home directory.
     */
    public final String getHome() {
        return home;
    }

    /**
     * Get this user's shell.
     * @return Path to this user's shell executable.
     */
    public final String getShell() {
        return shell;
    }

    /**
     * Determine if this user object matches the given arguments.
     * @param name User name
     * @param uid User ID
     * @param gid Group ID
     * @param comment Comment
     * @param home Path to the user's home directory
     * @param shell Path to the user's shell executable
     * @return true if all non-null arguments match exactly, false otherwise.
     */
    public final boolean matches(final String name, final Integer uid,
                                 final Integer gid, final String comment,
                                 final String home, final String shell) {

        return compare(this.name, name)
               && compare(this.uid, uid)
               && compare(this.gid, gid)
            && compare(this.comment, comment)
            && compare(this.home, home)
            && compare(this.shell, shell);
    }

}
