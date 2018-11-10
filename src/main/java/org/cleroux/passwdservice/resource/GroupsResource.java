package org.cleroux.passwdservice.resource;

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

/**
 * Provides all functionality of the /groups endpoint.
 */
@Path("groups")
public class GroupsResource {

    /**
     * Return a list of all groups on the system.
     * @return List of Group
     * @throws ExceptionResponse with error 500 Internal Server Error.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public final List<Group> getGroups() {
        List<Group> groups = null;
        try {
            groups = Groups.getGroups();
        } catch (Exception e) {
            throw new ExceptionResponse();
        }
        return groups;
    }

    /**
     * Return a list of groups matching all of the specified fields.
     * @param name Group name
     * @param gid Group ID
     * @param members List of group members
     * @return List of Group
     * @throws ExceptionResponse with error 500 Internal Server Error.
     */
    @GET
    @Path("query")
    @Produces(MediaType.APPLICATION_JSON)
    public final List<Group> queryGroups(@QueryParam("name") final String name,
        @QueryParam("gid") final Integer gid,
        @QueryParam("member") final List<String> members) {

        List<Group> groups = null;
        try {
            groups = Groups.getGroups(name, gid, members);
        } catch (Exception e) {
            throw new ExceptionResponse();
        }
        return groups;
    }

    /**
     * Return a single group with the specified group ID.
     * @param gid Group ID
     * @return Group with the specified ID
     * @throws ExceptionResponse with error 404 Not Found if the group ID was
     *         not found or 500 Internal Server Error on any other error.
     */
    @GET
    @Path("{gid}")
    @Produces(MediaType.APPLICATION_JSON)
    public final Group getGroup(@PathParam("gid") final Integer gid) {
        Group group = null;
        try {
            group = Groups.getGroup(gid);
            if (group == null) {
                throw new ExceptionResponse(new NotFoundException());
            }
        } catch (ExceptionResponse e) {
            throw e;
        } catch (Exception e) {
            throw new ExceptionResponse();
        }
        return group;
    }

}
