package ro.mihaidumitrescu.documentmanagementsystem.web;

import ro.mihaidumitrescu.documentmanagementsystem.model.Content;

import javax.servlet.http.HttpServletRequest;

public interface ContentCreator {
    Content extract(HttpServletRequest requestInfo);
}
