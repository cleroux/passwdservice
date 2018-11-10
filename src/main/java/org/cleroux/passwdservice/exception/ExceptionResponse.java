package org.cleroux.passwdservice.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import org.cleroux.passwdservice.model.ErrorResponse;

/**
 * An exception type that provides a response that will be serialized using
 * Jersey's media type providers.
 * Ie. if an endpoint produces MediaType.APPLICATION_JSON, the response body
 * will be valid JSON containing the error status and message.
 */
public class ExceptionResponse extends WebApplicationException {

    /**
     * Constructs a default error response.
     * Defaults to Status 500: Internal Server Error
     */
    public ExceptionResponse() {
        super(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
            .entity(new ErrorResponse(
                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase()))
            .build());
    }

    /**
     * Constructs an error response from a WebApplicationException.
     * @param e A WebApplicationException to use as the basis for the response
     */
    public ExceptionResponse(final WebApplicationException e) {

        super(Response.status(e.getResponse().getStatus())
            .entity(new ErrorResponse(e.getResponse().getStatus(),
                                      e.getMessage()))
            .build());
    }
}
