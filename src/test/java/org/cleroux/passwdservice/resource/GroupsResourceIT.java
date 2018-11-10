package org.cleroux.passwdservice.resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.*;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;
import org.cleroux.passwdservice.model.Groups;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.junit.*;

/**
 * Integration tests for the GroupssResource class.
 */
public class GroupsResourceIT {

    private static final Path orig = Paths.get("src/test/resources/group_orig");
    private static final Path changed =
        Paths.get("src/test/resources/group_changed");
    private static final Path malformed =
        Paths.get("src/test/resources/group_malformed");
    private static final Path dest = Paths.get("src/test/resources/group");

    private static final String ENDPOINT_GROUPS = "/groups";
    private static final String ENDPOINT_GROUPS_QUERY = "/groups/query";
    private static final String ENDPOINT_GROUP_ID = "/groups/{gid}";

    @BeforeClass
    public static void init() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;

        get(ENDPOINT_GROUPS); // Initialize the group file and monitoring
        waitForMonitor();
    }

    @Before
    public void beforeTest()
        throws IOException {

        Files.copy(orig, dest, REPLACE_EXISTING);
        waitForMonitor();
    }

    @Test
    public void testGetGroups() {

        get(ENDPOINT_GROUPS)
            .then()
            .body("name", hasItems("root", "cleroux", "dude", "dudette",
                                   "sudo"))
            .body("gid", hasItems(0, 1000, 1001, 1002, 27));
    }

    @Test
    public void testGetGroupsGroupChanged()
        throws IOException {

        Files.copy(changed, dest, REPLACE_EXISTING);
        waitForMonitor();
    
        get(ENDPOINT_GROUPS)
            .then()
            .body("name", hasItems("root", "cleroux", "dude"))
            .body("gid", hasItems(0, 1000, 1001));
    }

    @Test
    public void testGetGroupsGroupMalformed()
        throws IOException {

        Files.copy(malformed, dest, REPLACE_EXISTING);
        waitForMonitor();

        get(ENDPOINT_GROUPS)
            .then()
            .body("name", hasItems("root", "cleroux", "dude", "dudette",
                                   "sudo"))
            .body("gid", hasItems(0, 1000, 1001, 1002, 27));
    }

    @Test
    public void testGroupsQueryNoParams() {

        get(ENDPOINT_GROUPS_QUERY)
            .then()
            .body("name", hasItems("root", "cleroux", "dude", "dudette",
                                   "sudo"))
            .body("gid", hasItems(0, 1000, 1001, 1002, 27));
    }

    @Test
    public void testGroupsQueryAllparams() {
        given()
            .queryParam("name", "sudo")
            .queryParam("gid", "27")
            .queryParam("member", "cleroux")
            .when()
            .get(ENDPOINT_GROUPS_QUERY)
            .then()
            .body("name", hasItem("sudo"))
            .body("gid", hasItem(27));
    }

    @Test
    public void testGroupsQueryName() {

        given()
            .queryParam("name", "root")
            .when()
            .get(ENDPOINT_GROUPS_QUERY)
            .then()
            .body("name", hasItem("root"))
            .body("gid", hasItem(0));
    }

    @Test
    public void testGroupsQueryGid() {

        given()
            .queryParam("gid", 1000)
            .when()
            .get(ENDPOINT_GROUPS_QUERY)
            .then()
            .body("name", hasItem("cleroux"))
            .body("gid", hasItem(1000));
    }

    @Test
    public void testGroupId() {
        given()
            .pathParam("gid", 1000)
            .when()
            .get(ENDPOINT_GROUP_ID)
            .then()
            .body("name", equalTo("cleroux"))
            .body("gid", equalTo(1000));
    }

    @Test
    public void testGroupIdNotFound() {
        given()
            .pathParam("gid", 999)
            .expect()
            .statusCode(404)
            .when()
            .get(ENDPOINT_GROUP_ID);
    }

    // Allow some time for the Monitor to reload the file after a change.
    private static void waitForMonitor() {
        try {Thread.sleep(100);} catch (Exception e) {}
    }

}
