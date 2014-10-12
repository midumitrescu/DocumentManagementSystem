package ro.mihaidumitrescu.documentmanagementsystem.web;

import ro.mihaidumitrescu.general.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public enum SupportedMediaTypes {

    Text("text/plain"),
    Binary("application/octet-stream");

    private final Set<String> acceptedNamed;
    private final String produces;

    SupportedMediaTypes(String produces, String... headerAcceptedValues) {
        this.produces = produces;
        acceptedNamed = new TreeSet<String>();
        acceptedNamed.addAll(Arrays.asList(headerAcceptedValues));
    }

    public String produces() {
        return produces;
    }

    SupportedMediaTypes(String singleHeaderValue) {
        this.produces = singleHeaderValue;
        acceptedNamed = new TreeSet<String>();
        acceptedNamed.add(singleHeaderValue);
    }

    public static SupportedMediaTypes fromHeaderValue(String value) {
        if(StringUtils.hasText(value)) {
            for (SupportedMediaTypes mediaType : values()) {
                if (mediaType.supports(value)) {
                    return mediaType;
                }
            }
        }
        return defaultMediaType();
    }

    public static SupportedMediaTypes detectMediaType(HttpServletRequest request) {
        return fromHeaderValue(request.getContentType());
    }

    private boolean supports(String value) {
        return this.acceptedNamed.contains(value);
    }

    private static SupportedMediaTypes defaultMediaType() {
        return Binary;
    }
}
