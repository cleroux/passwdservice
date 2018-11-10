package org.cleroux.passwdservice.resource;

import java.util.Arrays;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.cleroux.passwdservice.exception.ExceptionResponse;
import org.cleroux.passwdservice.model.Group;
import org.cleroux.passwdservice.model.Groups;
import org.cleroux.passwdservice.model.User;
import org.cleroux.passwdservice.model.Users;

/**
 * Provides all functionality of the /users endpoint.
 */
@Path("users")
public class UsersResource {

    /**
     * Return a list of all users on the system.
     * @return List of User
     * @throws ExceptionResponse with error 500 Internal Server Error.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public final List<User> getUsers() {
        List<User> users = null;
        try {
            users = Users.getUsers();
        } catch (Exception e) {
            throw new ExceptionResponse();
        }
        return users;
    }

    /**
     * Return a list of users matching all of the specified fields.
     * @param name User name
     * @param uid User ID
     * @param gid Group ID
     * @param comment Comment
     * @param home Home directory
     * @param shell Shell executable
     * @return List of User
     * @throws ExceptionResponse with error 500 Internal Server Error.
     */
    @GET
    @Path("query")
    @Produces(MediaType.APPLICATION_JSON)
    public final List<User> queryUsers(@QueryParam("name") final String name,
        @QueryParam("uid") final Integer uid,
        @QueryParam("gid") final Integer gid,
        @QueryParam("comment") final String comment,
        @QueryParam("home") final String home,
        @QueryParam("shell") final String shell) {

        List<User> users = null;
        try {
            users = Users.getUsers(name, uid, gid, comment, home, shell);
        } catch (Exception e) {
            throw new ExceptionResponse();
        }
        return users;
    }

    /**
     * Return a single user with the specified user ID.
     * @param uid User ID
     * @return User with the specified ID
     * @throws ExceptionResponse with error 404 Not Found if the ID was not
     *         found or 500 Internal Server Error on any other error.
     */
    @GET
    @Path("{uid}")
    @Produces(MediaType.APPLICATION_JSON)
    public final User getUser(@PathParam("uid") final Integer uid) {

        User user = null;
        try {
            user = Users.getUser(uid);
            if (user == null) {
                throw new ExceptionResponse(new NotFoundException());
            }
        } catch (ExceptionResponse e) {
            throw e;
        } catch (Exception e) {
            throw new ExceptionResponse();
        }
        return user;
    }

    /**
     * Return a list of groups for the specified user ID.
     * @param uid User ID
     * @return List of Group
     * @throws ExceptionResponse with error 404 Not Found if the ID was not
     *         found or 500 Internal Server Error on any other error.
     */
    @GET
    @Path("{uid}/groups")
    @Produces(MediaType.APPLICATION_JSON)
    public final List<Group> getUserGroups(
        @PathParam("uid") final Integer uid) {

        List<Group> groups = null;
        try {
            User user = Users.getUser(uid);
            if (user == null) {
                throw new ExceptionResponse(new NotFoundException());
            }
            groups = Groups.getGroups(null, null,
                                      Arrays.asList(user.getName()));
        } catch (ExceptionResponse e) {
            throw e;
        } catch (Exception e) {
            throw new ExceptionResponse();
        }
        return groups;
    }

}
