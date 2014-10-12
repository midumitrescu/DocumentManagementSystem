package ro.mihaidumitrescu.documentmanagementsystem;

import ro.mihaidumitrescu.documentmanagementsystem.exceptions.FileDoesNotExistException;

import java.io.*;

public class FileSystemUtils {
    private final String testFolderPrefix = "src" + File.separator + "test" + File.separator + "resources";

    public InputStream inputStreamForFile(String fileName) {
        String fileReference = ensureSeparatorAtBeginning(fileName);
        if(!fileReference.contains(testFolderPrefix)) {
            fileReference = testFolderPrefix + fileReference;
        }

        File desiredFile = new File(fileReference);
        if(!desiredFile.exists()) {
            throw new FileDoesNotExistException("File with final reference " + fileReference + " does not exist");
        }
        try {
            return new BufferedInputStream(new FileInputStream(desiredFile));
        } catch (FileNotFoundException e) {
            throw new FileDoesNotExistException("File with final reference " + fileReference + " does not exist", e);
        }
    }

    private String ensureSeparatorAtBeginning(String fileName) {
        if(!fileName.startsWith(File.separator)) {
            return File.separator + fileName;
        }
        return fileName;
    }
}
