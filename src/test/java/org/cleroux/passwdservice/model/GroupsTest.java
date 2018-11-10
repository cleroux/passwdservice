package org.cleroux.passwdservice.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.*;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import static org.junit.Assert.*;
import org.junit.*;

/**
 * Unit tests for the Groups class.
 */
public class GroupsTest {

    private static Path orig = Paths.get("src/test/resources/group_orig");
    private static Path changed =
        Paths.get("src/test/resources/group_changed");
    private static Path dest = Paths.get("src/test/resources/group");

    @BeforeClass
    public static void init() {
        Properties sysProps = System.getProperties();
        sysProps.setProperty(Groups.PROP_GROUP_PATH, "src/test/resources/group");
        System.setProperties(sysProps);
    }

    @Before
    public void beforeTest()
        throws IOException {

        Files.copy(orig, dest, REPLACE_EXISTING);
        waitForMonitor();
    }

    /**
     * Test file changed.
     */
    @Test
    public void fileChanged()
        throws IOException {
        List<Group> groups = Groups.getGroups();
        assertEquals("Loaded original group file", 5, groups.size());
        Files.copy(changed, dest, REPLACE_EXISTING);
        waitForMonitor();
        groups = Groups.getGroups();
        assertEquals("Loaded changed group file", 4, groups.size());
    }

    /**
     * Test getGroups method.
     */
    @Test
    public void testGetGroups() {
        List<Group> groups = Groups.getGroups();
        assertEquals("Loaded test group file", 5, groups.size());
    }

    /**
     * Test getGroups method with arguments.
     */
    @Test
    public void testGetGroupsArgs() {
        List<Group> groups = Groups.getGroups("sudo", 27,
                                              Arrays.asList("cleroux"));
        assertEquals("Query all fields of group file", 1, groups.size());
    }

    @Test
    public void testGetGroupsName() {
        List<Group> groups = Groups.getGroups("sudo", null, null);
        assertEquals("Query name field of group file", 1, groups.size());
    }

    @Test
    public void testGetGroupsGid() {
        List<Group> groups = Groups.getGroups(null, 27, null);
        assertEquals("Query group ID field of group file", 1, groups.size());
    }

    @Test
    public void testGetGroupsMembers() {
        List<Group> groups = Groups.getGroups(null, null,
                                              Arrays.asList("cleroux"));
        assertEquals("Query members field of group file", 1, groups.size());
    }

    @Test
    public void testGetGroupsNullArgs() {
        List<Group> groups = Groups.getGroups(null, null, null);
        assertEquals("Query group file with no arguments", 5, groups.size());
    }

    @Test
    public void testGetGroupsNoMatches() {
        List<Group> groups = Groups.getGroups("noname", null, null);
        assertEquals("No matched groups", 0, groups.size());
    }

    /**
     * Test getGroup method.
     */
    @Test
    public void testGetGroupWithMembers() {
        Group group = Groups.getGroup(27);
        assertEquals("Get group by ID", "sudo", group.getName());
        assertTrue(group.getMembers().contains("cleroux"));
        assertTrue(group.getMembers().contains("dude"));
    }

    @Test
    public void testGetGroupNoMembers() {
        Group group = Groups.getGroup(1001);
        assertEquals("Get group by ID", "dude", group.getName());
        assertEquals(0, group.getMembers().size());
    }

    @Test
    public void testGetGroupNone() {
        Group group = Groups.getGroup(999);
        assertNull("Get non-existent group by ID", group);
    }

    // Allow a some time for the Monitor to reload the file after a change.
    private void waitForMonitor() {
        try {Thread.sleep(100);} catch (Exception e) {}
    }

}
