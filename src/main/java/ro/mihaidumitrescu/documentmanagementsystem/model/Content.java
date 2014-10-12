package ro.mihaidumitrescu.documentmanagementsystem.model;

import javax.servlet.http.HttpServletResponse;

public interface Content<T> {
    void writeContent(HttpServletResponse response);
}
