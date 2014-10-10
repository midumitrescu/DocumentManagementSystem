package ro.mihaidumitrescu.web;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.mihaidumitrescu.application.ApplicationModes;

import javax.servlet.Servlet;

public abstract class AbstractJettyBasedServletTest<T extends Servlet> {
    private final static Logger classLogger = LoggerFactory.getLogger(AbstractJettyBasedServletTest.class);

    private Server server;
    private final int jettyRunningPort = 9999;

    /** Return servlet class under test */
    protected abstract Class<T> getTestedServlet();

    @Before
    public void prepareJetty() {
        startUpJetty();
    }

    private void startUpJetty() {
        server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(jettyRunningPort);
        server.setStopAtShutdown(true);
        server.addConnector(connector);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        ServletHolder servletHolder = new ServletHolder(getTestedServlet());
        servletHolder.setInitParameter(ApplicationModes.configurationParameterName, ApplicationModes.TEST.name());
        servletHolder.setInitOrder(0);
        servletHolder.setEnabled(true);
        context.addServlet(servletHolder, "/*");
        server.setHandler(context);

        try {
            server.start();
        } catch (Exception e) {
            classLogger.error("Error while starting jetty on " + jettyRunningPort, e);
            closeJetty();
        }
    }

    @After
    public void alwaysCloseJetty() {
        closeJetty();
    }

    private void closeJetty() {
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

    protected int jettyRunningPort() {
        return jettyRunningPort;
    }
}
