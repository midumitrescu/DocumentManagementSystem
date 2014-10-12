package ro.mihaidumitrescu.documentmanagementsystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;

public class JaxRsUtils {

    private final static Logger classLogger = LoggerFactory.getLogger(JaxRsUtils.class);

    public static void closeClient(Client client) {
        try {
            if (client == null) {
                classLogger.warn("Close jax-rs client was called, but it was already null");
            } else {
                client.close();
                classLogger.warn("Closed jax-rs client ");
            }
        } catch (Exception e) {
            classLogger.error("Error while jax-rs client", e);
        }
    }

}
