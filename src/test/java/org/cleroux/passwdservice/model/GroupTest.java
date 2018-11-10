package org.cleroux.passwdservice.model;

import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.*;

/**
 * Unit tests for the Group class.
 */
public class GroupTest {

    private static String testName;
    private static Integer testGid;
    private static List<String> testMembers;
    private static Group testGroup;

    @BeforeClass
    public static void init() {
        testName = "testgroup";
        testGid = new Integer(1);
        testMembers = Arrays.asList("usera", "userb", "userc");
        testGroup = new Group(testName, testGid, testMembers);
    }

    /**
     * Test object creation.
     */
    @Test
    public void testGroupName() {
        assertEquals("Group.getName() ", testName, testGroup.getName());
    }

    @Test
    public void testGroupGid() {
        assertEquals("Group.getGid() ", testGid, testGroup.getGid());
    }

    @Test
    public void testGroupMembers() {
        assertArrayEquals("Group.getMembers() ", testMembers.toArray(),
                          testGroup.getMembers().toArray());
    }

    /**
     * Test object matches query arguments.
     */
    @Test
    public void testGroupMatchesAllArgs() {
        assertTrue("Group matches all arguments",
                   testGroup.matches(testName, testGid, testMembers));
    }

    @Test
    public void testGroupMatchesName() {
        assertTrue("Group matches name argument",
                   testGroup.matches(testName,null, null));
    }

    @Test
    public void testGroupMatchesGid() {
        assertTrue("Group matches gid argument",
                   testGroup.matches(null, testGid, null));
    }

    @Test
    public void testGroupMatchesMembers() {
        assertTrue("Group matches members argument",
                   testGroup.matches(null, null, testMembers));
    }

    @Test
    public void testGroupMatchesMembersSubset() {
        assertTrue("Group matches members subset",
                   testGroup.matches(null, null,
                                     Arrays.asList("usera", "userc")));
    }

    @Test
    public void testGroupMatchesNulLArgs() {
        assertTrue("Group matches if all arguments null",
                   testGroup.matches(null, null, null));
    }

    @Test
    public void testGroupNoMatchName() {
        assertFalse("Group does not match name argument",
                    testGroup.matches("othergroup", null, null));
    }

    @Test
    public void testGroupNoMatchGid() {
        assertFalse("Group does not match gid argument",
                    testGroup.matches(null, new Integer(2), null));
    }

    @Test
    public void testGroupNoMatchMembers() {
        assertFalse("Group does not contain all members",
                    testGroup.matches(null, null, Arrays.asList("userd")));
    }

}
