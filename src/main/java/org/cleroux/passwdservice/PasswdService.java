package org.cleroux.passwdservice;

import org.glassfish.jersey.server.ResourceConfig;

/**
 * Resource Configuration for the Passwd as a Service application.
 */
public class PasswdService extends ResourceConfig {

    /**
     * Constructs the Resource Configuration to include Jersey provider
     * packages.
     */
    public PasswdService() {
        packages("org.cleroux.passwdservice.resource",
            "org.cleroux.passwdservice.filter");
    }
}
