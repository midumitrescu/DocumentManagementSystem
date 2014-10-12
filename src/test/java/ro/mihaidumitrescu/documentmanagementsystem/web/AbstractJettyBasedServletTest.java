package ro.mihaidumitrescu.documentmanagementsystem.web;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractJettyBasedServletTest {
    private final static Logger classLogger = LoggerFactory.getLogger(AbstractJettyBasedServletTest.class);

    public static final String localhost = "http://localhost:";

    private static Server server;
    protected static final int jettyRunningPort = 9999;

    @BeforeClass
    public static void prepareJetty() {
        startUpJetty();
    }

    private static void startUpJetty() {
        server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(jettyRunningPort);
        server.setStopAtShutdown(true);
        server.addConnector(connector);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        ServletHolder servletHolder = new ServletHolder(DocumentManagementServlet.class);
        servletHolder.setInitOrder(0);
        servletHolder.setEnabled(true);
        context.addServlet(servletHolder, UrlParser.storagePath + "/*");
        server.setHandler(context);

        try {
            server.start();
        } catch (Exception e) {
            classLogger.error("Error while starting jetty on " + jettyRunningPort, e);
            closeJetty();
        }
    }

    @AfterClass
    public static void alwaysCloseJetty() {
        closeJetty();
    }

    private static void closeJetty() {
        try {
            if(server == null) {
                classLogger.warn("Close jetty on port " + jettyRunningPort + " was called, but server was already null");
            } else {
                server.stop();
                classLogger.warn("Closed jetty on port " + jettyRunningPort);
            }
        } catch (Exception e) {
            classLogger.error("Error while closing jetty on port "  + jettyRunningPort, e);
        }
    }

    protected static String localAppUrl() {
        return localhost + AbstractJettyBasedServletTest.jettyRunningPort;
    }
}
