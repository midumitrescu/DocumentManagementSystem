package ro.mihaidumitrescu.documentmanagementsystem.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ro.mihaidumitrescu.documentmanagementsystem.model.BinaryContent;
import ro.mihaidumitrescu.documentmanagementsystem.model.Document;
import ro.mihaidumitrescu.documentmanagementsystem.repository.Repository;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ConcurrentModificationException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DocumentManagementServletTest {

    @Mock
    Repository<Document> repository;
    @Mock
    BinaryContent binaryContent;
    @Mock
    BodyReader bodyReader;
    @Mock
    ContentCreator contentCreator;

    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    ServletInputStream servletInputStream;
    @Mock
    PrintWriter outputWriter;

    private DocumentManagementServlet testTarget;

     @Before
     public void prepare() throws IOException {
         testTarget = new DocumentManagementServlet();

         when(repository.exists("a")).thenReturn(true);
         when(repository.exists("b")).thenReturn(false);

         when(repository.read("a")).thenReturn(aDocument());
         when(repository.read("b")).thenReturn(null);

         when(repository.delete("a")).thenReturn(aDocument());
         when(repository.delete("b")).thenReturn(null);

         when(repository.create(binaryContent)).thenReturn(aDocument());

         when(contentCreator.extract(any(HttpServletRequest.class))).thenReturn(binaryContent);

         when(response.getWriter()).thenReturn(outputWriter);
         testTarget.setDocumentsRepository(repository);
         testTarget.setContentCreator(contentCreator);
     }

    private Document aDocument() {
        return new Document("a", binaryContent);
    }

    @Test
    public void testDoGet() throws Exception {

    }

    @Test
    public void testDoPost_Acceptable() throws Exception {
        mockCallOn("/");
        testTarget.doPost(request, response);
        verify(repository).create(binaryContent);
        verify(outputWriter).write(aDocument().getName());
        verifyCreated();
    }

    @Test
    public void testDoPost_DeepPathPresent() throws Exception {
        mockCallOnA();
        testTarget.doPost(request, response);
        verifyNotAllowed();
    }

    @Test
     public void testDoDelete_A() throws Exception {
        mockCallOnA();
        testTarget.doDelete(request, response);
        verify(repository).delete("a");
        verifyNoContent();
    }

    @Test
    public void testDoDelete_B() throws Exception {
        mockCallOnB();
        testTarget.doDelete(request, response);
        verifyNotFound();
    }

    @Test
    public void testDoPut_empty() throws Exception {
        mockCallOn(UrlParser.storagePath);
        testTarget.doPut(request, response);
        verifyNotAllowed();
    }



    @Test
     public void testDoPut_B() throws Exception {
        mockCallOnB();
        testTarget.doPut(request, response);
        verifyNotFound();
    }

    @Test
    public void testDoPut_ConcurrencyProblem() throws Exception {
        mockCallOnC();
        doThrow(new ConcurrentModificationException()).when(repository).update(any(Document.class));
        testTarget.doPut(request, response);
        verifyNotFound();
    }

    @Test
    public void testDoPut_A() throws Exception {
        mockCallOnA();
        testTarget.doPut(request, response);
        verify(repository).update(aDocument());
        verifyNoContent();
    }

    private void mockCallOnA() {
        mockCallOn("/a");
    }

    private void mockCallOnB() {
        mockCallOn("/b");
    }

    private void mockCallOnC() {
        mockCallOn("/c");
    }

    private void mockCallOn(String value) {
        when(request.getRequestURI()).thenReturn(value);
    }

    private void verifyCreated() {
        verifyStatus(HttpServletResponse.SC_CREATED);
    }

    private void verifyNoContent() {
        verifyStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    private void verifyNotAllowed() {
        verifyStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    private void verifyNotFound() {
        verifyStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    private void verifyStatus(int scNotFound) {
        verify(response).setStatus(scNotFound);
    }
}