package ro.mihaidumitrescu.documentmanagementsystem.web;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.mihaidumitrescu.application.ApplicationSettings;
import ro.mihaidumitrescu.documentmanagementsystem.FileSystemUtils;
import ro.mihaidumitrescu.general.StringUtils;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

import static ro.mihaidumitrescu.application.ApplicationSettings.documentNameLength;

public class DocumentManagementServletTest extends AbstractJettyBasedServletTest<DocumentManagementServlet> {
    private final static Logger classLogger = LoggerFactory.getLogger(DocumentManagementServletTest.class);
    public static final String localhost = "http://localhost:";

    private static Client client;
    private static WebTarget defaultTarget;

    @BeforeClass
    public static void startJetty() {
        client = ClientBuilder.newClient();
        defaultTarget = client.target(localAppUrl());
    }

    @AfterClass
    public static void releaseJaxrsClient() {
        try {
            if(client == null) {
                classLogger.warn("Close jaxrs client was called, but server was already null");
            } else {
                client.close();
                classLogger.info("Closed jax rs client");
            }
        } catch (Exception e) {
            classLogger.error("Error while closing jaxrs client", e);
        }
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
        Response response = defaultTarget.path(UrlParser.storagePath).request(MediaType.TEXT_PLAIN).get();
        Assert.assertEquals("Should be successful!", 200, response.getStatus());
        String helloWorld = response.readEntity(String.class);
        Assert.assertEquals("Hello World!", helloWorld);
    }

    @Test
    public void testCreateDocument_Successfully() {
        InputStream testFile1 = new FileSystemUtils().inputStreamForFile("testFile1.txt");

        Response post = defaultTarget.path(UrlParser.storagePath).request(MediaType.APPLICATION_OCTET_STREAM).post(Entity.entity(testFile1, MediaType.APPLICATION_OCTET_STREAM_TYPE));
        Assert.assertEquals("Status should be crated", 201, post.getStatus());

        String documentName = post.readEntity(String.class);
        Assert.assertTrue("Document Name must be given as response body", StringUtils.hasText(documentName));
        Assert.assertEquals("Document Name size must be " + documentNameLength, documentNameLength, documentName.length());
    }

    @Test
    public void testPostMapping_UnexpectedAtoms() {
        InputStream testFile1 = new FileSystemUtils().inputStreamForFile("testFile1.txt");

        Response post = defaultTarget.path(UrlParser.storagePath).path("somethingExtra").request(MediaType.APPLICATION_OCTET_STREAM).post(Entity.entity(testFile1, MediaType.APPLICATION_OCTET_STREAM_TYPE));
        Assert.assertEquals("Besides servlet configured storage path, post should not be accepted", 405, post.getStatus());
    }

    @Test
    public void test404WhenDocumentDoesNotExist() {
        Response response = defaultTarget.path("aDocumentName").request(MediaType.TEXT_PLAIN).get();
        Assert.assertEquals("Document does not exist", 404, response.getStatus());
    }

    private static String localAppUrl() {
        return localhost + AbstractJettyBasedServletTest.jettyRunningPort;
    }
}