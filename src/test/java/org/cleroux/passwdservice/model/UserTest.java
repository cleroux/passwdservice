package org.cleroux.passwdservice.model;

import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.*;

/**
 * Unit tests for the User class.
 */
public class UserTest {

    private static String testName;
    private static Integer testUid;
    private static Integer testGid;
    private static String testComment;
    private static String testHome;
    private static String testShell;
    private static User testUser;

    @BeforeClass
    public static void init() {
        testName = "testuser";
        testUid = new Integer(1);
        testGid = new Integer(2);
        testComment = "Test Comment";
        testHome = "/home/testuser";
        testShell = "/bin/sh";
        testUser = new User(testName, testUid, testGid, testComment, testHome,
                            testShell);
    }

    /**
     * Test object creation.
     */
    @Test
    public void testUserName() {
        assertEquals("User.getName() ", testName, testUser.getName());
    }

    @Test
    public void testUserUid() {
        assertEquals("User.getUid() ", testUid, testUser.getUid());
    }

    @Test
    public void testUserGid() {
        assertEquals("User.getGid() ", testGid, testUser.getGid());
    }

    @Test
    public void testUserComment() {
        assertEquals("User.getComment() ", testComment, testUser.getComment());
    }

    @Test
    public void testUserHome() {
        assertEquals("User.getHome() ", testHome, testUser.getHome());
    }

    @Test
    public void testUserShell() {
        assertEquals("User.getShell() ", testShell, testUser.getShell());
    }

    /**
     * Test object matches query arguments.
     */
    @Test
    public void testUserMatchesAllArgs() {
        assertTrue("User matches all arguments",
                   testUser.matches(testName, testUid, testGid, testComment,
                                    testHome, testShell));
    }

    @Test
    public void testUserMatchesName() {
        assertTrue("User matches name argument",
                   testUser.matches(testName, null, null, null, null, null));
    }

    @Test
    public void testUserMatchesUid() {
        assertTrue("User matches uid argument",
                   testUser.matches(null, testUid, null, null, null, null));
    }

    @Test
    public void testUserMatchesGid() {
        assertTrue("User matches gid argument",
                   testUser.matches(null, null, testGid, null, null, null));
    }

    @Test
    public void testUserMatchesComment() {
        assertTrue("User matches comment argument",
                   testUser.matches(null, null, null, testComment, null,
                                    null));
    }

    @Test
    public void testUserMatchesHome() {
        assertTrue("User matches home argument",
                   testUser.matches(null, null, null, null, testHome, null));
    }

    @Test
    public void testUserMatchesShell() {
        assertTrue("User matches shell argument",
                   testUser.matches(null, null, null, null, null, testShell));
    }

    @Test
    public void testUserMatchesNullArgs() {
        assertTrue("User matches if all arguments null",
                   testUser.matches(null, null, null, null, null, null));
    }

    @Test
    public void testUserNoMatchName() {
        assertFalse("User does not match name argument",
                    testUser.matches("otheruser", null, null, null, null,
                                     null));
    }

    @Test
    public void testUserNoMatchUid() {
        assertFalse("User does not match uid argument",
                    testUser.matches(null, new Integer(3), null, null, null,
                                     null));
    }

    @Test
    public void testUserNoMatchGid() {
        assertFalse("User does not match gid argument",
                    testUser.matches(null, null, new Integer(4), null, null,
                                     null));
    }

    @Test
    public void testUserNoMatchComment() {
        assertFalse("User does not match comment argument",
                    testUser.matches(null, null, null, "", null, null));
    }

    @Test
    public void testUserNoMatchHome() {
        assertFalse("User does not match home argument",
                    testUser.matches(null, null, null, null, "", null));
    }

    @Test
    public void testUserNoMatchShell() {
        assertFalse("User does not match shell argument",
                    testUser.matches(null, null, null, null, null, ""));
    }

    /**
     * Test compare method.
     */
    @Test
    public void testCompareIntegersEqual() {
        assertTrue("Integers are equal",
                   User.compare(new Integer(1), new Integer(1)));
    }

    @Test
    public void testCompareOtherIntegerNull() {
        assertTrue("Other integer is null",
                   User.compare(new Integer(1), null));
    }

    @Test
    public void testCompareBothIntegersNull() {
        assertTrue("Both integers are null",
                   User.compare(null, null));
    }

    @Test
    public void testCompareIntegersNotEqual() {
        assertFalse("Integers are not equal",
                    User.compare(new Integer(1), new Integer(2)));
    }

    @Test
    public void testCompareIntegerNullOtherNotNull() {
        assertFalse("Integer is null and other integer is not null",
                    User.compare(null, new Integer(1)));
    }

    @Test
    public void testCompareStringsEqual() {
        assertTrue("Strings are equal", User.compare("Test", "Test"));
    }

    @Test
    public void testCompareOtherStringNull() {
        assertTrue("Other string is null", User.compare("Test", null));
    }

    @Test
    public void testCompareBothStringsNull() {
        assertTrue("Both strings are null", User.compare(null, null));
    }

    @Test
    public void testCompareStringsNotEqual() {
        assertFalse("Strings are not equal", User.compare("Test", "nope"));
    }

    @Test
    public void testCompareStringNullOtherNotNull() {
        assertFalse("String is null and other string is not null",
                    User.compare(null, "Test"));
    }

    /**
     * Test compareList method.
     */
    @Test
    public void testCompareList() {
        List<String> list1 = Arrays.asList("one", "two", "three");
        List<String> list2 = Arrays.asList("one", "two", "three");

        assertTrue("Both lists contain the same items",
                   User.compareList(list1, list2));
    }

    @Test
    public void testCompareListOtherListNull() {
        List<String> list1 = Arrays.asList("one", "two", "three");
        assertTrue("Other list is null", User.compareList(list1, null));
    }

    @Test
    public void testCompareListBothListsNull() {
        assertTrue("Both lists are null", User.compareList(null, null));
    }

    @Test
    public void testCompareListOtherListSubset() {
        List<String> list1 = Arrays.asList("one", "two", "three");
        assertTrue("Other list is a subset",
                   User.compareList(list1, Arrays.asList("one")));
    }

    @Test
    public void testCompareListOtherListNotSubset() {
        List<String> list1 = Arrays.asList("one", "two", "three");
        assertFalse("Other list is not a subset",
                    User.compareList(list1, Arrays.asList("nope")));
    }

    @Test
    public void testCompareListOtherSuperset() {
        List<String> list1 = Arrays.asList("one", "two", "three");
        assertFalse("List does not contain all other items",
                    User.compareList(list1, Arrays.asList("one", "two",
                                                          "three", "four")));
    }

    @Test
    public void testCompareListNullOtherNotNull() {
        List<String> list1 = Arrays.asList("one", "two", "three");
        assertFalse("List is null and other list is not null",
                    User.compareList(null, list1));
    }
}
