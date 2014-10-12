package ro.mihaidumitrescu.general;

import com.sun.deploy.util.ArrayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Mihai Dumitrescu on 12.10.2014.
 *
 * @author <a href="mailto:dumitrescu.mihai2002@yahoo.com">Mihai Dumitrescu</a>
 */
public class BufferedWriter {

    private final static Logger classLogger = LoggerFactory.getLogger(BufferedWriter.class);

    public final static int defaultBufferSize = 16384;

    private final int bufferSize;
    private final byte[] content;

    public BufferedWriter(byte[] content) {
        this(content, content.length);
    }

    public BufferedWriter(byte[] content, int bufferSize) {
        this.content = content;
        this.bufferSize = bufferSize;
    }

    public void writeBuffered(ServletResponse response) throws IOException {

        ServletOutputStream outputStream = response.getOutputStream();
        response.setBufferSize(content.length);

        for(int i = 0; i < content.length; i += bufferSize) {
            classLogger.debug("Writing from position " + i);
            byte[] chunk = nextChunk(content, i);
            outputStream.write(chunk);
//            response.flushBuffer();
        }
    }

    private byte[] nextChunk(byte[] content, int cursor) {
        try {
            int chunkSize = lessThanAFullBuffer(content, cursor) ? remainingContentSize(content, cursor) : bufferSize;
            return Arrays.copyOfRange(content, cursor, cursor + chunkSize);
        } catch (Exception e) {
            classLogger.error("Exception" ,e);
            throw new RuntimeException(e);
        }

    }

    private boolean lessThanAFullBuffer(byte[] content, int cursor) {
        return cursor + bufferSize > content.length;
    }

    private int remainingContentSize(byte[] content, int cursor) {
        return content.length - cursor;
    }
}
