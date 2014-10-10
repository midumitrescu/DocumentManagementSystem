package ro.mihaidumitrescu.web;

import org.apache.http.entity.StringEntity;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.mihaidumitrescu.application.ApplicationModes;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.*;

public class DocumentManagementServletTest extends AbstractJettyBasedServletTest<DocumentManagementServlet> {
    private final static Logger classLogger = LoggerFactory.getLogger(DocumentManagementServletTest.class);
    public static final String localhost = "http://localhost:";

    private static Client client;

    @BeforeClass
    public static void startJetty() {
        client = ClientBuilder.newClient();
    }

    @Override
    protected Class<DocumentManagementServlet> getTestedServlet() {
        return DocumentManagementServlet.class;
    }

    private static void closeClient() {
        try {
            if (client == null) {
                classLogger.warn("Close jax-rs client was called, but it was already null");
            } else {
                client.close();
                classLogger.warn("Closed jax-rs client ");
            }
        } catch (Exception e) {
            classLogger.error("Error while jax-rs client", e);
        }
    }

    @Test
    public void testDoGet() {
        WebTarget dmsEndpoint = client.target(localAppUrl());

        Response response = dmsEndpoint.request(MediaType.TEXT_PLAIN).get();
        Assert.assertEquals("Should be successful!", 200, response.getStatus());
        String helloWorld = response.readEntity(String.class);
        Assert.assertEquals("Hello World!", helloWorld);
    }

    private String localAppUrl() {
        return localhost + jettyRunningPort();
    }
}