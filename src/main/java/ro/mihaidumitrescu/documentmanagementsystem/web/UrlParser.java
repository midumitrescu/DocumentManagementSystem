package ro.mihaidumitrescu.documentmanagementsystem.web;

import ro.mihaidumitrescu.general.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class UrlParser {
    public static final String STORAGE_PATH = "/storage/documents";
    private static final String[] storageUrlAtoms = determineUrlAtoms(STORAGE_PATH);

    public boolean hasDeepPath(String requestURL) {
        String[] requestUrlAtoms = determineUrlAtoms(requestURL);
        if (requestUrlAtoms.length == 0) {
            return false;
        }
        return (requestUrlAtoms.length != storageUrlAtoms.length || !validFirstAtoms(requestUrlAtoms));
    }

    /**
     * Returns document name from a link like {@link ro.mihaidumitrescu.documentmanagementsystem.web.UrlParser#STORAGE_PATH}/{documentName}.
     * Empty string if context path does not coincide.
     */
    public String findDocumentNamePathParameter(String requestURL) {
        if (!StringUtils.hasText(requestURL)) {
            return "";
        }
        String[] parsedRequest = determineUrlAtoms(requestURL);
        int numberOfAtoms = parsedRequest.length;

        if (acceptableSize(numberOfAtoms)) {
            if (numberOfAtoms == 1) {
                return parsedRequest[0];
            }
            if (numberOfAtoms == expectedSize()) {
                if (validFirstAtoms(parsedRequest)) {
                    return parsedRequest[2];
                }
            }
        }
        return "";
    }

    private static String[] determineUrlAtoms(String requestURL) {
        String[] emptyStringArray = new String[0];
        if (!StringUtils.hasText(requestURL)) {
            return emptyStringArray;
        }
        String[] initialSplit = requestURL.split("/");

        List<String> result = getOnlyNonEmptyAtoms(initialSplit);
        return result.toArray(emptyStringArray);
    }

    private static List<String> getOnlyNonEmptyAtoms(String[] initialSplit) {
        List<String> result = new ArrayList<String>(initialSplit.length);
        for (String currentAtom : initialSplit) {
            if (StringUtils.hasText(currentAtom)) {
                result.add(currentAtom);
            }
        }
        return result;
    }

    private int expectedSize() {
        return storageUrlAtoms.length + 1;
    }

    private boolean acceptableSize(int numberOfAtoms) {
        return numberOfAtoms > 0 && numberOfAtoms <= expectedSize();
    }

    private boolean validFirstAtoms(String[] parsedRequest) {
        for (int i = 0; i < storageUrlAtoms.length; i++) {
            String standard = storageUrlAtoms[i];
            String value = parsedRequest[i];
            if (!standard.equalsIgnoreCase(value)) {
                return false;
            }
        }
        return true;
    }
}
