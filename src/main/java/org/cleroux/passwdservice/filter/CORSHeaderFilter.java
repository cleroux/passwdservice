package org.cleroux.passwdservice.filter;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

/**
 * Sets Cross-Origin Resource Sharing HTTP headers for all API requests.
 * This allows the API to be used by clients (mainly web browsers using AJAX)
 * that disallow Cross-Origin requests by default for security reasons.
 */
@Provider
public final class CORSHeaderFilter implements ContainerResponseFilter {

    @Override
    public void filter(final ContainerRequestContext request,
                       final ContainerResponseContext response)
        throws IOException {

        response.getHeaders().add("Access-Control-Allow-Origin", "*");
        //response.getHeaders().add("Access-Control-Allow-Methods", "GET");
        response.getHeaders().add("Vary", "accept");
    }
}
