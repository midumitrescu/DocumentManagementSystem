package ro.mihaidumitrescu.documentmanagementsystem.exceptions;

public class FileDoesNotExistException extends RuntimeException {

    public FileDoesNotExistException(String detailedMessage) {
        super(detailedMessage);
    }
    public FileDoesNotExistException(String detailedMessage, Throwable cause) {
        super(detailedMessage, cause);
    }
}
