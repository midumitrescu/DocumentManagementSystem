package ro.mihaidumitrescu.documentmanagementsystem.web;

import org.junit.Assert;
import ro.mihaidumitrescu.documentmanagementsystem.JaxRsUtils;
import ro.mihaidumitrescu.documentmanagementsystem.StreamingUtils;
import ro.mihaidumitrescu.documentmanagementsystem.model.AbstractContent;
import ro.mihaidumitrescu.documentmanagementsystem.model.Content;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;

public class DocumentManagementCommandExecutor {

    private final Client client;
    private final WebTarget documentsPath;

    public DocumentManagementCommandExecutor(String localAppUrl, String path) {
        client = ClientBuilder.newClient();
        documentsPath = client.target(localAppUrl).path(path);
    }

    public Response createStringDocument(String content) {
        return documentsPath.request(MediaType.TEXT_PLAIN).post(Entity.entity(content, MediaType.TEXT_PLAIN));
    }

    public String findCreatedDocumentName(Response post) {
        return post.readEntity(String.class);
    }

    public Response createDocument(String localFile) {
        InputStream fileOnDisk = new StreamingUtils().inputStreamForFile(localFile);
        return documentsPath.request(MediaType.APPLICATION_OCTET_STREAM).post(Entity.entity(fileOnDisk, MediaType.APPLICATION_OCTET_STREAM_TYPE));
    }

    public Response updateDocument(String documentName, String localFile) {
        InputStream fileOnDisk = new StreamingUtils().inputStreamForFile(localFile);
        return updateDocument(documentName, fileOnDisk, MediaType.APPLICATION_OCTET_STREAM);
    }

    public Response updateStringDocument(String documentName, String content) {
        InputStream streamedContent = new ByteArrayInputStream(content.getBytes());
        return updateDocument(documentName, streamedContent, MediaType.TEXT_PLAIN);
    }

    public Response updateDocument(String documentName, InputStream stream, String mediaType) {
        return documentsPath.path(documentName).request(mediaType).put(Entity.entity(stream, MediaType.APPLICATION_OCTET_STREAM_TYPE));
    }

    public Response executeDeleteFor(String aDocumentName) {
        return documentsPath.path(aDocumentName).request(MediaType.TEXT_PLAIN).delete();
    }

    public Response executeGetFor(String documentName) {
        return documentsPath.path(documentName).request("text/plain").get();
    }

    public Entity<InputStream> entityFromFile(String fileName) {
        InputStream testFile1 = new StreamingUtils().inputStreamForFile(fileName);
        return Entity.entity(testFile1, MediaType.APPLICATION_OCTET_STREAM_TYPE);
    }

    public InputStream getInputStreamPerReflection(Content content) {
        Method getContentMethod;
        try {
            getContentMethod = AbstractContent.class.getDeclaredMethod("streamContent");
            getContentMethod.setAccessible(true);
            return (InputStream) getContentMethod.invoke(content);
        } catch (Exception e) {

            Assert.fail("Could not get content per reflection from " + content);
            return null;
        }
    }

    public WebTarget path(String path) {
        return documentsPath.path(path);
    }

    public void closeClient() {
        JaxRsUtils.closeClient(client);
    }
}
