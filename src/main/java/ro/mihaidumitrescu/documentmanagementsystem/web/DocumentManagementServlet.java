package ro.mihaidumitrescu.documentmanagementsystem.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.mihaidumitrescu.documentmanagementsystem.model.BinaryContent;
import ro.mihaidumitrescu.documentmanagementsystem.model.Content;
import ro.mihaidumitrescu.documentmanagementsystem.model.Document;
import ro.mihaidumitrescu.documentmanagementsystem.repository.DocumentsRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public class DocumentManagementServlet extends HttpServlet {

    private final static Logger classLogger = LoggerFactory.getLogger(DocumentManagementServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.getWriter().write("Hello World!");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest servletRequest, HttpServletResponse resp) throws ServletException, IOException {
        if(new UrlParser().hasDeepPath(servletRequest.getRequestURI())) {
            resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
        Document newDocument = createNewDocument(servletRequest);

        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.setCharacterEncoding("us-ascii");
        resp.setContentType(SupportedMediaTypes.Text.produces());
        resp.getWriter().write(newDocument.getName());
    }

    private Document createNewDocument(HttpServletRequest servletRequest) {
        String newDocumentName = UUID.randomUUID().toString();
        byte[] requestContent = new BodyReader(servletRequest).readAllContent();
        Content<?> content = new BinaryContent(requestContent);
        Document newlyCreatedDocument = DocumentsRepository.INSTANCE.createDocument(content);
        return newlyCreatedDocument;
    }
}
