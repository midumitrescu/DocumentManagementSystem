package ro.mihaidumitrescu.documentmanagementsystem.web;

import ro.mihaidumitrescu.general.StringUtils;

import java.net.URI;
import java.net.URL;

/**
 * Created by Mihai Dumitrescu on 11.10.2014.
 *
 * @author <a href="mailto:dumitrescu.mihai2002@yahoo.com">Mihai Dumitrescu</a>
 */
public class UrlParser {
    public static final String storagePath = "/storage/documents";

    public boolean hasDeepPath(String requestURI) {
        String comparedValue = requestURI;
        if (emptyOrSlash(comparedValue)) {
            return false;
        }
        if(!comparedValue.startsWith(storagePath)) {
            return true;
        }

        return !emptyOrSlash(eliminateStoragePath(comparedValue));
    }

    private String eliminateStoragePath(String comparedValue) {
        return comparedValue.substring(storagePath.length());
    }

    private boolean emptyOrSlash(String comparedValue) {
        return !StringUtils.hasText(comparedValue) || "/".equals(comparedValue);
    }
}
