package ro.mihaidumitrescu.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DocumentManagementServlet extends HttpServlet {

    private final static Logger classLogger = LoggerFactory.getLogger(DocumentManagementServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.getWriter().write("Hello World!");
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
