package org.cleroux.passwdservice.resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.*;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;
import org.cleroux.passwdservice.model.Users;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.junit.*;

/**
 * Integration tests for the UsersResource class.
 */
public class UsersResourceIT {

	private static final Path orig = Paths.get("src/test/resources/passwd_orig");
	private static final Path changed = Paths.get("src/test/resources/passwd_changed");
	private static final Path malformed = Paths.get("src/test/resources/passwd_malformed");
	private static final Path dest = Paths.get("src/test/resources/passwd");

	private static final String ENDPOINT_USERS = "/users";
	private static final String ENDPOINT_USERS_QUERY = "/users/query";
	private static final String ENDPOINT_USER_ID = "/users/{uid}";
	private static final String ENDPOINT_USER_ID_GROUPS = "/users/{uid}/groups";

	@BeforeClass
	public static void init() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 8080;

		get(ENDPOINT_USERS); // Initialize the passwd file and monitoring
		waitForMonitor();
	}

	@Before
	public void beforeTest()
		throws IOException {

		Files.copy(orig, dest, REPLACE_EXISTING);
		waitForMonitor();
	}

	@Test
	public void testGetUsers() {

		get(ENDPOINT_USERS)
			.then()
			.body("name", hasItems("root", "cleroux", "dude", "dudette"))
			.body("uid", hasItems(0, 1000, 1001, 1002))
			.body("gid", hasItems(0, 1000, 1001, 1002))
			.body("comment", hasItems("root", "cleroux,,,", "The Dude", "The Dudette"))
			.body("home", hasItems("/root", "/home/cleroux", "/home/dude", "/home/dudette"))
			.body("shell", hasItems("/bin/bash", "/bin/zsh", "/bin/bash", "/bin/sh"));
	}

	@Test
	public void testGetUsersPasswdChanged()
		throws IOException {

		Files.copy(changed, dest, REPLACE_EXISTING);
		waitForMonitor();
	
		get(ENDPOINT_USERS)
			.then()
			.body("name", hasItems("root", "cleroux", "dude"))
			.body("uid", hasItems(0, 1000, 1001))
			.body("gid", hasItems(0, 1000, 1001))
			.body("comment", hasItems("root", "cleroux,,,", "The Dude"))
			.body("home", hasItems("/root", "/home/cleroux", "/home/dude"))
			.body("shell", hasItems("/bin/bash", "/bin/zsh", "/bin/bash"));
	}

	@Test
	public void testGetUsersPasswdMalformed()
		throws IOException {

		Files.copy(malformed, dest, REPLACE_EXISTING);
		waitForMonitor();

		get(ENDPOINT_USERS)
			.then()
			.body("name", hasItems("root", "cleroux", "dude", "dudette"))
			.body("uid", hasItems(0, 1000, 1001, 1002))
			.body("gid", hasItems(0, 1000, 1001, 1002))
			.body("comment", hasItems("root", "cleroux,,,", "The Dude", "The Dudette"))
			.body("home", hasItems("/root", "/home/cleroux", "/home/dude", "/home/dudette"))
			.body("shell", hasItems("/bin/bash", "/bin/zsh", "/bin/bash", "/bin/sh"));
	}

	@Test
	public void testUsersQueryNoParams() {

		get(ENDPOINT_USERS_QUERY)
			.then()
			.body("name", hasItems("root", "cleroux", "dude", "dudette"))
			.body("uid", hasItems(0, 1000, 1001, 1002))
			.body("gid", hasItems(0, 1000, 1001, 1002))
			.body("comment", hasItems("root", "cleroux,,,", "The Dude", "The Dudette"))
			.body("home", hasItems("/root", "/home/cleroux", "/home/dude", "/home/dudette"))
			.body("shell", hasItems("/bin/bash", "/bin/zsh", "/bin/bash", "/bin/sh"));
	}

	@Test
	public void testUsersQueryAllparams() {
		given()
			.queryParam("name", "cleroux")
			.queryParam("uid", "1000")
			.queryParam("gid", "1000")
			.queryParam("comment", "cleroux,,,")
			.queryParam("home", "/home/cleroux")
			.queryParam("shell", "/bin/zsh")
			.when()
			.get(ENDPOINT_USERS_QUERY)
			.then()
			.body("name", hasItem("cleroux"))
			.body("uid", hasItem(1000))
			.body("gid", hasItem(1000))
			.body("comment", hasItem("cleroux,,,"))
			.body("home", hasItem("/home/cleroux"))
			.body("shell", hasItem("/bin/zsh"));
	}

	@Test
	public void testUsersQueryName() {

		given()
			.queryParam("name", "root")
			.when()
			.get(ENDPOINT_USERS_QUERY)
			.then()
			.body("name", hasItem("root"))
			.body("uid", hasItem(0))
			.body("gid", hasItem(0))
			.body("comment", hasItem("root"))
			.body("home", hasItem("/root"))
			.body("shell", hasItem("/bin/bash"));
	}

	@Test
	public void testUsersQueryUid() {

		given()
			.queryParam("uid", 1000)
			.when()
			.get(ENDPOINT_USERS_QUERY)
			.then()
			.body("name", hasItem("cleroux"))
			.body("uid", hasItem(1000))
			.body("gid", hasItem(1000))
			.body("comment", hasItem("cleroux,,,"))
			.body("home", hasItem("/home/cleroux"))
			.body("shell", hasItem("/bin/zsh"));
	}

	@Test
	public void testUsersQueryGid() {

		given()
			.queryParam("gid", 1000)
			.when()
			.get(ENDPOINT_USERS_QUERY)
			.then()
			.body("name", hasItem("cleroux"))
			.body("uid", hasItem(1000))
			.body("gid", hasItem(1000))
			.body("comment", hasItem("cleroux,,,"))
			.body("home", hasItem("/home/cleroux"))
			.body("shell", hasItem("/bin/zsh"));
	}

	@Test
	public void testUsersQueryComment() {

		given()
			.queryParam("comment", "root")
			.when()
			.get(ENDPOINT_USERS_QUERY)
			.then()
			.body("name", hasItem("root"))
			.body("uid", hasItem(0))
			.body("gid", hasItem(0))
			.body("comment", hasItem("root"))
			.body("home", hasItem("/root"))
			.body("shell", hasItem("/bin/bash"));
	}

	@Test
	public void testUsersQueryHome() {

		given()
			.queryParam("home", "/root")
			.when()
			.get(ENDPOINT_USERS_QUERY)
			.then()
			.body("name", hasItem("root"))
			.body("uid", hasItem(0))
			.body("gid", hasItem(0))
			.body("comment", hasItem("root"))
			.body("home", hasItem("/root"))
			.body("shell", hasItem("/bin/bash"));
	}

	@Test
	public void testUsersQueryShell() {
		given()
			.queryParam("shell", "/bin/bash")
			.when()
			.get(ENDPOINT_USERS_QUERY)
			.then()
			.body("name", hasItem("root"))
			.body("uid", hasItem(0))
			.body("gid", hasItem(0))
			.body("comment", hasItem("root"))
			.body("home", hasItem("/root"))
			.body("shell", hasItem("/bin/bash"));
	}

	@Test
	public void testUserId() {
		given()
			.pathParam("uid", 1000)
			.when()
			.get(ENDPOINT_USER_ID)
			.then()
			.body("name", equalTo("cleroux"))
			.body("uid", equalTo(1000))
			.body("gid", equalTo(1000))
			.body("comment", equalTo("cleroux,,,"))
			.body("home", equalTo("/home/cleroux"))
			.body("shell", equalTo("/bin/zsh"));
	}

	@Test
	public void testUserIdNotFound() {
		given()
			.pathParam("uid", 999)
			.expect()
			.statusCode(404)
			.when()
			.get(ENDPOINT_USER_ID);
	}

	@Test
	public void testUserGroups() {
		given()
			.pathParam("uid", 1000)
			.when()
			.get(ENDPOINT_USER_ID_GROUPS)
			.then()
			.body("name", hasItem("sudo"))
			.body("gid", hasItem(27));
	}

	@Test
	public void testUserGroupsNotFound() {
		given()
			.pathParam("uid", 999)
			.expect()
			.statusCode(404)
			.when()
			.get(ENDPOINT_USER_ID_GROUPS);
	}

	@Test
	public void testUserGroupsInvalidUid() {
		given()
			.pathParam("uid", "abc")
			.expect()
			.statusCode(404)
			.when()
			.get(ENDPOINT_USER_ID_GROUPS);
	}

	// Allow some time for the Monitor to reload the file after a change.
	private static void waitForMonitor() {
		try {Thread.sleep(100);} catch (Exception e) {}
	}

}
