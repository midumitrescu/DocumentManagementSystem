package ro.mihaidumitrescu.documentmanagementsystem;

import ro.mihaidumitrescu.documentmanagementsystem.exceptions.FileDoesNotExistException;
import sun.misc.IOUtils;

import java.io.*;

public class TestStreamingUtils {
    private final String testFolderPrefix = "src" + File.separator + "test" + File.separator + "resources";

    public InputStream inputStreamForFile(String fileName) throws FileDoesNotExistException {
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

    public byte[] readFully(String filename) throws FileDoesNotExistException {
        try {
            return IOUtils.readFully(inputStreamForFile(filename), Integer.MAX_VALUE, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] readFully(InputStream stream)  {
        try {
            return IOUtils.readFully(stream, Integer.MAX_VALUE, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String ensureSeparatorAtBeginning(String fileName) {
        if(!fileName.startsWith(File.separator)) {
            return File.separator + fileName;
        }
        return fileName;
    }
}
