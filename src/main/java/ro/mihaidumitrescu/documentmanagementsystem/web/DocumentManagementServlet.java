package ro.mihaidumitrescu.documentmanagementsystem.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.mihaidumitrescu.documentmanagementsystem.model.Content;
import ro.mihaidumitrescu.documentmanagementsystem.model.Document;
import ro.mihaidumitrescu.documentmanagementsystem.repository.InMemoryDocumentsRepository;
import ro.mihaidumitrescu.documentmanagementsystem.repository.Repository;
import ro.mihaidumitrescu.general.StringUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ConcurrentModificationException;

public class DocumentManagementServlet extends HttpServlet {
    private final static Logger classLogger = LoggerFactory.getLogger(DocumentManagementServlet.class);

    private UrlParser parser;
    private Repository<Document> documentsRepository;
    private ContentExtractor contentExtractor;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        initDependencies();
    }

    private void initDependencies() {
        logStartInit();
        setParser(new UrlParser());
        setDocumentsRepository(InMemoryDocumentsRepository.INSTANCE);
        setContentExtractor(new RequestBasedContentExtractor());
        logEndInit();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logMethodEntry("GET", request);
        String documentName = parser.findDocumentNamePathParameter(request.getRequestURI());
        if(emptyDocumentName(response, documentName)) {
            return;
        }

        Document document = documentsRepository.read(documentName);
        if(document == null) {
            replyNotFound(response);
        } else {
            document.getContent().writeContent(response);
            replyOK(response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logMethodEntry("POST", request);
        if(parser.hasDeepPath(request.getRequestURI())) {
            replyNotAcceptable(response);
            return;
        }

        Document newDocument = createNewDocument(request);
        replyCreated(response);
        response.setCharacterEncoding("us-ascii");
        response.setContentType(SupportedMediaTypes.Text.produces());
        response.getWriter().write(newDocument.getName());
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logMethodEntry("DELETE", request);
        String documentName = parser.findDocumentNamePathParameter(request.getRequestURI());
        if(emptyDocumentName(response, documentName)) {
            return;
        }

        Document document = documentsRepository.delete(documentName);
        if(document == null) {
            replyNotFound(response);
        } else {
            replyNoContent(response);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logMethodEntry("PUT", request);
        String documentName = parser.findDocumentNamePathParameter(request.getRequestURI());
        if(emptyDocumentName(response, documentName)) {
            return;
        }

        Document document = documentsRepository.read(documentName);
        if(!documentsRepository.exists(documentName)) {
            replyNotFound(response);
        } else {
            try {
                Content<?> updatedContent = extractContent(request);
                Document updated = new Document(document.getName(), updatedContent);
                documentsRepository.update(updated);
                replyNoContent(response);
            } catch (ConcurrentModificationException e) {
                replyNotFound(response);
            }
        }
    }

    private void replyOK(HttpServletResponse resp) {
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private void replyCreated(HttpServletResponse resp) {
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }

    private void replyNotFound(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    private void replyNotAcceptable(HttpServletResponse resp) {
        resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    private void replyNoContent(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    private Document createNewDocument(HttpServletRequest servletRequest) {
        Content<?> content = extractContent(servletRequest);
        return documentsRepository.create(content);
    }

    protected Content extractContent(HttpServletRequest servletRequest) {
        return contentExtractor.extract(servletRequest);
    }

    private boolean emptyDocumentName(HttpServletResponse response, String documentName) {
        if(!StringUtils.hasText(documentName)) {
            replyNotAcceptable(response);
            return true;
        }
        return false;
    }

    public void setParser(UrlParser parser) {
        this.parser = parser;
    }

    public void setDocumentsRepository(Repository<Document> documentsRepository) {
        this.documentsRepository = documentsRepository;
    }

    public void setContentExtractor(ContentExtractor contentExtractor) {
        this.contentExtractor = contentExtractor;
    }

    private void logEndInit() {
        if(classLogger.isDebugEnabled()) {
            classLogger.debug("Init for servlet " + getServletName() + " ended successfully");
        }
    }

    private void logStartInit() {
        if(classLogger.isDebugEnabled()) {
            classLogger.debug("Called init for servlet " + getServletName());
        }
    }

    private void logMethodEntry(String method, HttpServletRequest request) {
        if(classLogger.isDebugEnabled()) {
            classLogger.debug("Doing " + method + " for " + request.getRequestURI());
        }
    }
}
