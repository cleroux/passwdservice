package org.cleroux.passwdservice.model;

import java.util.List;

/**
 * Represents a user or group.
 * This class could implement java.security.Principal but not necessary now
 */
public abstract class Principal {

    /**
     * The name of the principal object.
     */
    protected String name = null;

    /**
     * Get the name of the user or group.
     * @return name of the user or group
     */
    public final String getName() {
        return name;
    }

    /**
     * Compare values according to the query requirements.
     * @param thiss The reference object to compare
     * @param other The target object to compare against the reference object
     * @return true if the other value is null, (it wasn't specified as a
     *         search term) or
     *         this value is null and other value is null or
     *         this value == other value.
     *         Returns false if this value is null and other value is not
     *         null or this value is not null and not equal to other value.
     */
    protected static boolean compare(final Object thiss, final Object other) {
        if (other == null) {
            return true;
        }
        if (thiss == null) {
            return false;
        }
        return thiss.equals(other);
    }

    /**
     * Compare lists according to the query requirements.
     * @param thiss The reference list to compare
     * @param other The target list to compare against the reference list
     * @return true if the other list is null (it wasn't specified as a search
     *         term or this list is null and other list is null or
     *         this list contains all items in the other list.
     *         Returns false if this list is null and the other list is not
     *         null or this list is not null and does not contain all items in
     *         the other list.
     */
    protected static boolean compareList(final List<String> thiss,
                                         final List<String> other) {
        if (other == null) {
            return true;
        }
        if (thiss == null) {
            return false;
        }
        return thiss.containsAll(other);
    }

}
