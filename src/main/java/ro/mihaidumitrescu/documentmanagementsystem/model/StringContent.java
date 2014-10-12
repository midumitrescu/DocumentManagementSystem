package ro.mihaidumitrescu.documentmanagementsystem.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.mihaidumitrescu.documentmanagementsystem.exceptions.WriteExceptionHandler;
import ro.mihaidumitrescu.documentmanagementsystem.web.BodyReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class StringContent extends AbstractContent<String> {
    private final static Logger classLogger = LoggerFactory.getLogger(StringContent.class);

    private final String content;

    public StringContent(HttpServletRequest requestInfo) {
        byte[] bytes = new BodyReader(requestInfo).readAllContent();
        content = new String(bytes);
    }

    @Override
    public void writeContent(HttpServletResponse response) {
        try {
            response.getWriter().write(content);
        } catch (IOException e) {
             new WriteExceptionHandler(e).handle(classLogger);
        }
    }

    @Override
    protected InputStream getContent() {
        return  new ByteArrayInputStream(content.getBytes());
    }

    @Override
    public String toString() {
        return "StringContent{" +
                "contentLength='" + content.length() + '\'' +
                '}';
    }
}
