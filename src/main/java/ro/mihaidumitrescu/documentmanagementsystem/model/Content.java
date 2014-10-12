package ro.mihaidumitrescu.documentmanagementsystem.model;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;

public interface Content<T> {
    void writeContent(HttpServletResponse response);
}
