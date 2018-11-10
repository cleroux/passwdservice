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
 * Unit tests for the Users class.
 */
public class UsersTest {

    private static Path orig = Paths.get("src/test/resources/passwd_orig");
    private static Path changed =
        Paths.get("src/test/resources/passwd_changed");
    private static Path dest = Paths.get("src/test/resources/passwd");

    @BeforeClass
    public static void init() {
        Properties sysProps = System.getProperties();
        sysProps.setProperty(Users.PROP_PASSWD_PATH,
                             "src/test/resources/passwd");
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
        List<User> users = Users.getUsers();
        assertEquals("Loaded original user file", 4, users.size());
        Files.copy(changed, dest, REPLACE_EXISTING);
        waitForMonitor();
        users = Users.getUsers();
        assertEquals("Loaded changed user file", 3, users.size());
    }

    /**
     * Test getUsers method.
     */
    @Test
    public void testGetUsers() {
        List<User> users = Users.getUsers();
        assertEquals("Loaded test user file", 4, users.size());
    }

    /**
     * Test getUsers method with arguments.
     */
    @Test
    public void testGetUsersArgs() {
        List<User> users = Users.getUsers("dude", 1001, 1001, "The Dude",
                                          "/home/dude", "/bin/bash");
        assertEquals("Query all fields of passwd file", 1, users.size());
    }

    @Test
    public void testGetUsersName() {
        List<User> users = Users.getUsers("dude", null, null, null, null,
                                          null);
        assertEquals("Query name field of passwd file", 1, users.size());
    }

    @Test
    public void testGetUsersUid() {
        List<User> users = Users.getUsers(null, 1001, null, null, null, null);
        assertEquals("Query user ID field of passwd file", 1, users.size());
    }

    @Test
    public void testGetUsersGid() {
        List<User> users = Users.getUsers(null, null, 1001, null, null, null);
        assertEquals("Query group ID field of passwd file", 1, users.size());
    }

    @Test
    public void testGetUsersComment() {
        List<User> users = Users.getUsers(null, null, null, "The Dude", null,
                                          null);
        assertEquals("Query comment field of passwd file", 1, users.size());
    }

    @Test
    public void testGetUsersHome() {
        List<User> users = Users.getUsers(null, null, null, null, "/home/dude",
                                          null);
        assertEquals("Query home directory field of passwd file", 1,
                     users.size());
    }

    @Test
    public void testGetUsersShell() {
        List<User> users = Users.getUsers(null, null, null, null, null,
                                          "/bin/bash");
        assertEquals("Query shell field of passwd file", 2, users.size());
    }

    @Test
    public void testGetUsersNullArgs() {
        List<User> users = Users.getUsers(null, null, null, null, null, null);
        assertEquals("Query passwd file with no arguments", 4, users.size());
    }

    @Test
    public void testGetUsersNoMatches() {
        List<User> users = Users.getUsers("noname", null, null, null, null,
                                          null);
        assertEquals("No matched users", 0, users.size());
    }

    /**
     * Test getUser method.
     */
    @Test
    public void testGetUser() {
        User user = Users.getUser(1001);
        assertEquals("Get user by ID", "dude", user.getName());
    }

    @Test
    public void testGetUserNone() {
        User user = Users.getUser(999);
        assertNull("Get non-existent user by ID", user);
    }

    /**
     * Test the readLines method.
     */
    @Test
    public void testReadLines()
        throws Exception {

        List<String> lines = Users.readLines("src/test/resources/passwd_orig");
        assertEquals("Read from original passwd", 4, lines.size());
    }

    // Allow a small amount of time for the Monitor to reload the file after a change.
    private void waitForMonitor() {
        try {Thread.sleep(100);} catch (Exception e) {}
    }

}
