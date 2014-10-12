package ro.mihaidumitrescu.documentmanagementsystem.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.IOUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class BodyReader {
    private final static Logger classLogger = LoggerFactory.getLogger(DocumentManagementServlet.class);

    private final HttpServletRequest readFrom;

    public BodyReader(HttpServletRequest readFrom) {
        this.readFrom = readFrom;
    }

    public byte[] readAllContent() {
        byte[] result;
        try {
            result = IOUtils.readFully(readFrom.getInputStream(), Integer.MAX_VALUE, true);
            logReadContent(result.length);
            return result;
        } catch (IOException e) {

            throw new RuntimeException("Could not read from serlver input stream", e);
        }
    }

    private void logReadContent(int bytesRead) {
        if (bytesRead == readFrom.getContentLength()) {
            if (classLogger.isDebugEnabled()) {
                classLogger.debug(String.format("A total number of %d were read from request (content lenth matched)", bytesRead));
            }
        } else {
            classLogger.warn(String.format("Request has a content length of %d, but only %d were read", readFrom.getContentLength(), bytesRead));
        }
    }
}
