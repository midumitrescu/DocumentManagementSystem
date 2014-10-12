package ro.mihaidumitrescu.documentmanagementsystem.web;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.mihaidumitrescu.documentmanagementsystem.TestStreamingUtils;
import ro.mihaidumitrescu.documentmanagementsystem.model.AbstractContent;
import ro.mihaidumitrescu.documentmanagementsystem.model.Content;
import ro.mihaidumitrescu.documentmanagementsystem.model.Document;
import ro.mihaidumitrescu.documentmanagementsystem.repository.InMemoryDocumentsRepository;
import ro.mihaidumitrescu.general.StringUtils;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

import static ro.mihaidumitrescu.application.ApplicationSettings.documentNameLength;

public class DocumentManagementServletIntegrationTest extends AbstractJettyBasedServletTest<DocumentManagementServlet> {
    private final static Logger classLogger = LoggerFactory.getLogger(DocumentManagementServletIntegrationTest.class);
    public static final String localhost = "http://localhost:";
    public static final String testFile1 = "testFile1.txt";
    public static final String testFile2 = "testPic1.jpg";
    public static final String testFile3 = "testPic2.jpg";
    public static final String smallTestFile = "small.png";
    public static final String mediumTestFile = "medium.png";

    private static Client client;
    private static WebTarget documentsPath;
    private final TestStreamingUtils fileSystemUtils = new TestStreamingUtils();

    @BeforeClass
    public static void startJetty() {
        client = ClientBuilder.newClient();
        documentsPath = client.target(localAppUrl()).path(UrlParser.storagePath);
    }

    @AfterClass
    public static void releaseJaxrsClient() {
        closeClient();
    }

    @Override
    protected Class<DocumentManagementServlet> getTestedServlet() {
        return DocumentManagementServlet.class;
    }

    @Test
    public void requirementsTest() {
        String initialText = "hello world";
        String updateText = "goodbye world";

        Response createCommand = createStringDocument(initialText);
        Assert.assertEquals("Status is 201 created", 201, createCommand.getStatus());
        Assert.assertEquals("ID Length is 20", 20, createCommand.getLength());
        Assert.assertEquals("text/plain content type", "text/plain; charset=us-ascii", createCommand.getMediaType().toString());
        String documentName = findCreatedDocumentName(createCommand);
        Assert.assertTrue("document id is initialized", StringUtils.hasText(documentName));

        Response getCommand = executeGetFor(documentName);
        Assert.assertEquals("Status is 200 OK", 200, getCommand.getStatus());
        Assert.assertEquals("Length is 11", initialText.length(), getCommand.getLength());
        Assert.assertEquals("Text is predefined", initialText, getCommand.readEntity(String.class));




    }

    @Test
    public void testGet_LargeFile1() throws IOException {
        testSaveAndRead(testFile2);
    }

    @Test
    public void testGet_LargeFile2() throws IOException {
        testSaveAndRead(testFile3);
    }

    @Test
    public void testDoGetAfterCreation_SmallFile() throws IOException {
        testSaveAndRead(smallTestFile);
    }

    @Test
    public void testDoGetAfterCreation_MediumFile() throws IOException {
        testSaveAndRead(mediumTestFile);
    }

    private void testSaveAndRead(String smallTestFile1) throws IOException {
        Response documentCreation = createDocument(smallTestFile1);
        String documentName = findCreatedDocumentName(documentCreation);
        Response response = executeGetFor(documentName);
        Assert.assertEquals("Should be successful!", 200, response.getStatus());

        assertDocumentContentEqualsIncomingContent("Content should come from " + smallTestFile1, smallTestFile1, response);
    }

    private Response executeGetFor(String documentName) {
        return documentsPath.path(documentName).request("text/plain").get();
    }

    @Test
    public void testCreateDocument_Successfully_file1() throws IOException {
        testCreationFor(testFile1);
    }

    @Test
    public void testCreateDocument_Successfully_file2() throws IOException {
        testCreationFor(testFile2);
    }

    @Test
    public void testCreateDocument_Successfully_file3() throws IOException {
        testCreationFor(testFile3);
    }

    @Test
    public void testCreateDocument_Successfully_smallPng() throws IOException {
        testCreationFor(smallTestFile);
    }

    private void testCreationFor(String testedFile) throws IOException {
        Response post = createDocument(testedFile);
        Assert.assertEquals("Status should be created", 201, post.getStatus());

        String documentName = findCreatedDocumentName(post);
        Assert.assertTrue("Document Name must be given as response body", StringUtils.hasText(documentName));
        Assert.assertEquals("Document Name size must be " + documentNameLength, documentNameLength, documentName.length());

        assertDocumentContentEqualsTestFile("Saved content should match content from file " + testedFile, testedFile, documentName);
    }

    private String findCreatedDocumentName(Response post) {
        return post.readEntity(String.class);
    }

    private Response createDocument(String localFile)  {
        InputStream fileOnDisk = new TestStreamingUtils().inputStreamForFile(localFile);
        return documentsPath.request(MediaType.APPLICATION_OCTET_STREAM).post(Entity.entity(fileOnDisk, MediaType.APPLICATION_OCTET_STREAM_TYPE));
    }

    private Response updateDocument(String documentName, String localFile)  {
        InputStream fileOnDisk = new TestStreamingUtils().inputStreamForFile(localFile);
        return updateDocument(documentName, fileOnDisk, MediaType.APPLICATION_OCTET_STREAM);
    }

    private Response updateStringDocument(String documentName, String content)  {
        InputStream streamedContent = new ByteArrayInputStream(content.getBytes());
        return updateDocument(documentName, streamedContent, MediaType.TEXT_PLAIN);
    }

    private Response updateDocument(String documentName, InputStream stream, String mediaType)  {
        return documentsPath.path(documentName).request(mediaType).put(Entity.entity(stream, MediaType.APPLICATION_OCTET_STREAM_TYPE));
    }

    private Response createStringDocument(String content)  {
        return documentsPath.request(MediaType.TEXT_PLAIN).post(Entity.entity(content, MediaType.TEXT_PLAIN));
    }

    @Test
    public void testPostMapping_UnexpectedAtoms()  {
        Entity<InputStream> fileEntity = entityFromFile(testFile1);
        Response post = documentsPath.path("somethingExtra").request(MediaType.APPLICATION_OCTET_STREAM).post(fileEntity);
        Assert.assertEquals("Besides servlet configured storage path, post should not be accepted", 405, post.getStatus());
    }

    private Entity<InputStream> entityFromFile(String fileName)  {
        InputStream testFile1 = new TestStreamingUtils().inputStreamForFile(fileName);
        return Entity.entity(testFile1, MediaType.APPLICATION_OCTET_STREAM_TYPE);
    }

    @Test
    public void test404WhenDocumentDoesNotExist_Get() {
        Response response = documentsPath.path("aDocumentName").request(MediaType.TEXT_PLAIN).get();
        Assert.assertEquals("Document does not exist", 404, response.getStatus());
    }

    @Test
    public void test404WhenDocumentDoesNotExist_Delete() {
        Response response = documentsPath.path("aDocumentName").request(MediaType.TEXT_PLAIN).delete();
        Assert.assertEquals("Document does not exist", 404, response.getStatus());
    }

    @Test
    public void test404WhenDocumentDoesNotExist_Put() {
        Response response = documentsPath.path("aDocumentName").request(MediaType.TEXT_PLAIN).put(entityFromFile(testFile1));
        Assert.assertEquals("Document does not exist", 404, response.getStatus());
    }

    @Test
    public void testPut_validUpdateSequence() {
        Response createCommand = createDocument(testFile1);
        String documentName = findCreatedDocumentName(createCommand);

        testUpdate(documentName, testFile1);
        testUpdate(documentName, testFile2);
        testUpdate(documentName, testFile3);
        testUpdate(documentName, smallTestFile);
        testUpdate(documentName, mediumTestFile);
    }

    private void testUpdate(String documentName, String fileOnDisk) {
        Response updateCommand = updateDocument(documentName, fileOnDisk);
        Assert.assertEquals("Saved successfully. No Content", 204, updateCommand.getStatus());

        Response getCommand = executeGetFor(documentName);
        assertDocumentContentEqualsIncomingContent("Updated with " + fileOnDisk, fileOnDisk, getCommand);
    }

    private static String localAppUrl() {
        return localhost + AbstractJettyBasedServletTest.jettyRunningPort;
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

    private void assertDocumentContentEqualsTestFile(String message, String fileOnDisk, String documentName) {
        Document document = InMemoryDocumentsRepository.INSTANCE.read(documentName);
        Content content = document.getContent();

        InputStream obtainedStream = getInputStreamPerRelection(content);
        assertContentsEqual(message, fileOnDisk, obtainedStream);
    }

    private InputStream getInputStreamPerRelection(Content content)   {
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

    private void assertDocumentContentEqualsIncomingContent(String message, String fileOnDisk, Response response) {
        InputStream content = response.readEntity(InputStream.class);
        assertContentsEqual(message, fileOnDisk, content);
    }

    private void assertContentsEqual(String message, String fileOnDisk, InputStream stream)  {
        Assert.assertArrayEquals(message, fileSystemUtils.readFully(fileOnDisk), fileSystemUtils.readFully(stream));
    }
}