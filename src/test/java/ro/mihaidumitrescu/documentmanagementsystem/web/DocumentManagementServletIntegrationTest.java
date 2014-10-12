package ro.mihaidumitrescu.documentmanagementsystem.web;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ro.mihaidumitrescu.documentmanagementsystem.StreamingUtils;
import ro.mihaidumitrescu.documentmanagementsystem.model.Content;
import ro.mihaidumitrescu.documentmanagementsystem.model.Document;
import ro.mihaidumitrescu.documentmanagementsystem.repository.InMemoryDocumentsRepository;
import ro.mihaidumitrescu.general.StringUtils;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;

import static ro.mihaidumitrescu.application.ApplicationSettings.DOCUMENT_NAME__DEFAULT_LENGTH;
import static ro.mihaidumitrescu.documentmanagementsystem.AvailableFiles.mediumTestFile;
import static ro.mihaidumitrescu.documentmanagementsystem.AvailableFiles.smallTestFile;
import static ro.mihaidumitrescu.documentmanagementsystem.AvailableFiles.testFile1;
import static ro.mihaidumitrescu.documentmanagementsystem.AvailableFiles.testFile2;
import static ro.mihaidumitrescu.documentmanagementsystem.AvailableFiles.testFile3;

public class DocumentManagementServletIntegrationTest extends AbstractJettyBasedServletTest {

    private final StreamingUtils fileSystemUtils = new StreamingUtils();
    private static DocumentManagementCommandExecutor executor;

    @BeforeClass
    public static void startJetty() {
        executor = new DocumentManagementCommandExecutor(localAppUrl(), UrlParser.storagePath);
    }

    @AfterClass
    public static void releaseJaxrsClient() {
        executor.closeClient();
    }

    @Test
    public void requirementsTest() {
        String initialText = "hello world";
        String updateText = "goodbye world";

        Response createCommand = executor.createStringDocument(initialText);
        Assert.assertEquals("Status is 201 created", 201, createCommand.getStatus());
        Assert.assertEquals("ID Length is 20", 20, createCommand.getLength());
        Assert.assertEquals("text/plain content type", "text/plain; charset=us-ascii", createCommand.getMediaType().toString());
        String documentName = executor.findCreatedDocumentName(createCommand);
        Assert.assertTrue("document id is initialized", StringUtils.hasText(documentName));

        Response getCommand = executor.executeGetFor(documentName);
        Assert.assertEquals("Status is 200 OK", 200, getCommand.getStatus());
        Assert.assertEquals("Length is 11", initialText.length(), getCommand.getLength());
        Assert.assertEquals("Text is predefined", initialText, getCommand.readEntity(String.class));

        Response putCommand = executor.updateStringDocument(documentName, updateText);
        Assert.assertEquals("Status is 204 NO Content", 204, putCommand.getStatus());

        Response firstDeleteCommand = executor.executeDeleteFor(documentName);
        Assert.assertEquals("Status is 204 NO Content", 204, firstDeleteCommand.getStatus());

        Response secondDeleteCommand = executor.executeDeleteFor(documentName);
        Assert.assertEquals("Already Deleted", 404, secondDeleteCommand.getStatus());
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
        Response documentCreation = executor.createDocument(smallTestFile1);
        String documentName = executor.findCreatedDocumentName(documentCreation);
        Response response = executor.executeGetFor(documentName);
        Assert.assertEquals("Should be successful!", 200, response.getStatus());
        assertDocumentContentEqualsIncomingContent("Content should come from " + smallTestFile1, smallTestFile1, response);
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
        Response post = executor.createDocument(testedFile);
        Assert.assertEquals("Status should be created", 201, post.getStatus());

        String documentName = executor.findCreatedDocumentName(post);
        Assert.assertTrue("Document Name must be given as response body", StringUtils.hasText(documentName));
        Assert.assertEquals("Document Name size must be " + DOCUMENT_NAME__DEFAULT_LENGTH, DOCUMENT_NAME__DEFAULT_LENGTH, documentName.length());
        assertDocumentContentEqualsTestFile("Saved content should match content from file " + testedFile, testedFile, documentName);
    }

    @Test
    public void testPostMapping_UnexpectedAtoms()  {
        Entity<InputStream> fileEntity = executor.entityFromFile(testFile1);
        Response post = executor.path("somethingExtra").request(MediaType.APPLICATION_OCTET_STREAM).post(fileEntity);
        Assert.assertEquals("Unconfigured storage path, post should not be accepted", 405, post.getStatus());
    }

    @Test
    public void test404WhenDocumentDoesNotExist_Get() {
        Response response = executor.path("aDocumentName").request(MediaType.TEXT_PLAIN).get();
        Assert.assertEquals("Document does not exist", 404, response.getStatus());
    }

    @Test
    public void test404WhenDocumentDoesNotExist_Delete() {
        String aDocumentName = "aDocumentName";
        Response response = executor.executeDeleteFor(aDocumentName);
        Assert.assertEquals("Document does not exist", 404, response.getStatus());
    }

    @Test
    public void test404WhenDocumentDoesNotExist_Put() {
        Entity<InputStream> entity = executor.entityFromFile(testFile1);
        Response response = executor.path("aDocumentName").request(MediaType.TEXT_PLAIN).put(entity);
        Assert.assertEquals("Document does not exist", 404, response.getStatus());
    }

    @Test
    public void testPut_validUpdateSequence() {
        Response createCommand = executor.createDocument(testFile1);
        String documentName = executor.findCreatedDocumentName(createCommand);

        testUpdate(documentName, testFile1);
        testUpdate(documentName, testFile2);
        testUpdate(documentName, testFile3);
        testUpdate(documentName, smallTestFile);
        testUpdate(documentName, mediumTestFile);
    }

    private void testUpdate(String documentName, String fileOnDisk) {
        Response updateCommand = executor.updateDocument(documentName, fileOnDisk);
        Assert.assertEquals("Saved successfully. No Content", 204, updateCommand.getStatus());

        Response getCommand = executor.executeGetFor(documentName);
        assertDocumentContentEqualsIncomingContent("Updated with " + fileOnDisk, fileOnDisk, getCommand);
    }

    private void assertDocumentContentEqualsTestFile(String message, String fileOnDisk, String documentName) {
        Document document = InMemoryDocumentsRepository.INSTANCE.read(documentName);
        Content content = document.getContent();

        InputStream obtainedStream = executor.getInputStreamPerReflection(content);
        assertContentsEqual(message, fileOnDisk, obtainedStream);
    }

    private void assertDocumentContentEqualsIncomingContent(String message, String fileOnDisk, Response response) {
        InputStream content = response.readEntity(InputStream.class);
        assertContentsEqual(message, fileOnDisk, content);
    }

    private void assertContentsEqual(String message, String fileOnDisk, InputStream stream)  {
        Assert.assertArrayEquals(message, fileSystemUtils.readFully(fileOnDisk), fileSystemUtils.readFully(stream));
    }
}