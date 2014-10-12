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

/**
 * Created by Mihai Dumitrescu on 12.10.2014.
 *
 * @author <a href="mailto:dumitrescu.mihai2002@yahoo.com">Mihai Dumitrescu</a>
 */
public class BinaryContent extends AbstractContent<byte[]> {
    private final static Logger classLogger = LoggerFactory.getLogger(BinaryContent.class);

    private final byte[] content;
    private BodyReader bodyReader;

    public BinaryContent(HttpServletRequest request) {
        this.bodyReader = new BodyReader(request);
        this.content = getBodyReader().readAllContent();
    }

    @Override
    public void writeContent(HttpServletResponse response) {
        try {
            response.setContentLength(content.length);
//            response.setHeader("Transfer-Encoding", "chunked");
//            response.setContentType("application/octet-stream");

            response.getOutputStream().write(content);


//            new BufferedWriter(content).writeBuffered(response);
        } catch (IOException e) {
             new WriteExceptionHandler(e).handle(classLogger);
        }
    }

    @Override
    protected InputStream getContent() {
        return new ByteArrayInputStream(content);
    }

    public BodyReader getBodyReader() {
        return bodyReader;
    }
}
