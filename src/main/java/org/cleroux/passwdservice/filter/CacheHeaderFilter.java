package org.cleroux.passwdservice.filter;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

/**
 * Sets cache HTTP headers for all API requests.
 */
@Provider
public class CacheHeaderFilter implements ContainerResponseFilter {

    @Override
    public final void filter(final ContainerRequestContext request,
                             final ContainerResponseContext response)
        throws IOException {

        response.getHeaders().add("Cache-Control", "no-cache,max-age=0");
    }

}
