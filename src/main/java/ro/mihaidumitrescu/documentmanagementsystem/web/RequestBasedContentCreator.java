package ro.mihaidumitrescu.documentmanagementsystem.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.mihaidumitrescu.documentmanagementsystem.exceptions.MediaTypeNotSupportedException;
import ro.mihaidumitrescu.documentmanagementsystem.model.BinaryContent;
import ro.mihaidumitrescu.documentmanagementsystem.model.Content;
import ro.mihaidumitrescu.documentmanagementsystem.model.StringContent;

import javax.servlet.http.HttpServletRequest;

public class RequestBasedContentCreator implements ContentCreator {
    private final static Logger classLogger = LoggerFactory.getLogger(RequestBasedContentCreator.class);

    public Content extract(HttpServletRequest requestInfo) {
        SupportedMediaTypes detected = SupportedMediaTypes.detectMediaType(requestInfo);
        logMediaTypeDetection(detected, requestInfo);

        if(SupportedMediaTypes.Binary == detected) {
            return new BinaryContent(requestInfo);
        } else if(SupportedMediaTypes.Text == detected) {
            return new StringContent(requestInfo);
        }
        throw new MediaTypeNotSupportedException(detected);
    }

    private void logMediaTypeDetection(SupportedMediaTypes detected, HttpServletRequest requestInfo) {
        if(classLogger.isDebugEnabled()) {
            classLogger.debug("For request " + requestInfo + ", creating content type of type " + detected);
        }
    }
}
