package ro.mihaidumitrescu.documentmanagementsystem.web;

import ro.mihaidumitrescu.documentmanagementsystem.model.Content;

import javax.servlet.http.HttpServletRequest;

public interface ContentExtractor {
    Content extract(HttpServletRequest requestInfo);
}
