package ro.mihaidumitrescu.documentmanagementsystem.repository;

import ro.mihaidumitrescu.documentmanagementsystem.exceptions.CanNotWriteException;
import ro.mihaidumitrescu.documentmanagementsystem.model.Content;
import ro.mihaidumitrescu.documentmanagementsystem.model.Document;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileSystemDocumentsRepository implements Repository<Document> {

    private final static String repositoryName = "citrix";
    private final static Path repositoryFolder = getTempDirectory();

    private static Path getTempDirectory() {
        try {
            return Files.createTempDirectory(repositoryName);
        } catch (IOException e) {
            throw new CanNotWriteException(" Can not create temp folder ",e);
        }
    }


    @Override
    public boolean exists(Document item) {
        return false;
    }

    @Override
    public Document create(Content content) {
        return null;
    }

    @Override
    public Document delete(Document item) {
            return null;
    }

    @Override
    public boolean exists(String documentName) {
        return false;
    }

    @Override
    public Document read(String documentName) {
        return null;
    }

    @Override
    public Document delete(String documentName) {
            return null;
    }

    @Override
    public void update(Document item) {

    }
}
