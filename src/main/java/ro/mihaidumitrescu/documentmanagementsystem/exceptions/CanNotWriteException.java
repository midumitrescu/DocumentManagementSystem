package ro.mihaidumitrescu.documentmanagementsystem.exceptions;

import java.io.IOException;

public class CanNotWriteException extends RuntimeException {

    public CanNotWriteException(String message, IOException exception) {
        super(message, exception);
    }
}
