package org.cleroux.passwdservice.model;

/**
 * Response object used by Jersey to provide errors in the MediaType specified
 * by the Produces annotation.
 */
public class ErrorResponse {

    /**
     * Error message.
     */
    private final String message;

    /**
     * Error status code.
     */
    private final int status;

    /**
     * Constructs an ErrorResponse.
     * @param status Status code
     * @param message Error message
     */
    public ErrorResponse(final int status, final String message) {
        this.message = message;
        this.status = status;
    }

    /**
     * Get this error response's error message.
     * @return Error message
     */
    public final String getMessage() {
        return message;
    }

    /**
     * Get this error response's status code.
     * @return Status code
     */
    public final int getStatus() {
        return status;
    }
}
