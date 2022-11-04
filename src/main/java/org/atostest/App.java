package org.atostest;

import org.atostest.services.DocumentService;
import org.atostest.services.ProfileService;
import org.atostest.services.impl.DocumentServiceImpl;
import org.atostest.services.impl.ProfileServiceImpl;
import org.atostest.web.resources.DocumentsResource;
import org.atostest.web.resources.ProfilesResource;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletContainer;

public class App {

    public static void main(String[] args) throws Exception {
        ServletContextHandler handler = createHandler();

        Server server = new Server(8080);
        server.setHandler(handler);

        try {
            server.start();
            server.join();
        } finally {
            server.destroy();
        }
    }

    static ServletContextHandler createHandler() {
        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        handler.setContextPath("/");

        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.register(ProfilesResource.class);
        resourceConfig.register(DocumentsResource.class);
        resourceConfig.register(JacksonFeature.class);
        resourceConfig.register(MultiPartFeature.class);
        resourceConfig.property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);

        resourceConfig.register(getAbstractBinder());

        handler.addServlet(new ServletHolder(new ServletContainer(resourceConfig)), "/api/*");
        return handler;
    }

    private static AbstractBinder getAbstractBinder() {
        return new AbstractBinder() {
            @Override
            protected void configure() {
                bind(ProfileServiceImpl.class).to(ProfileService.class);
                bind(DocumentServiceImpl.class).to(DocumentService.class);
            }
        };
    }

}