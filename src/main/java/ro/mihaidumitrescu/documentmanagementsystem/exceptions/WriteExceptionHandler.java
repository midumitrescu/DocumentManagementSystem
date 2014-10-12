package ro.mihaidumitrescu.documentmanagementsystem.exceptions;

import org.slf4j.Logger;

import java.io.IOException;

public class WriteExceptionHandler {

    private final IOException nested;

    public WriteExceptionHandler(IOException nested) {
        this.nested = nested;
    }

    public void handle(Logger logger) {
        CanNotWriteException canNotWriteException = new CanNotWriteException("Can't write content", nested);
        logger.error("Exception on write operation", canNotWriteException);
        throw canNotWriteException;
    }
}
