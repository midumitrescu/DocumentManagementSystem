package ro.mihaidumitrescu.documentmanagementsystem.exceptions;

import ro.mihaidumitrescu.documentmanagementsystem.web.SupportedMediaTypes;

public class MediaTypeNotSupportedException extends RuntimeException {

    public MediaTypeNotSupportedException(SupportedMediaTypes mediaType) {
        super("Media type " + mediaType + " present, but no suitable BodyReader found");
    }
}
