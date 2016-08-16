package com.commercehub.dropwizard.camel.jms.component;

import org.apache.camel.Service;
import org.apache.log4j.Logger;
import org.springframework.jms.connection.SingleConnectionFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A {@link ConnectionFactory} adapter that refuses to create a connection after it has been stopped. Useful for
 * wrapping a {@link SingleConnectionFactory} for use with Camel.
 */
public class ServiceEnabledConnectionFactory implements ConnectionFactory, Service  {
    static private Logger log = Logger.getLogger("ServiceEnabledConnectionFactory");

    private final AtomicBoolean active = new AtomicBoolean(true);

    private ConnectionFactory connectionFactory;

    public ServiceEnabledConnectionFactory(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public void start() {
        log.info("Starting");
        active.set(true);
        log.info("Started");
    }

    @SuppressWarnings("Instanceof")
    @Override
    public void stop() {
        log.info("Stopping");
        active.set(false);
        if (connectionFactory instanceof SingleConnectionFactory) {
            ((SingleConnectionFactory)connectionFactory).resetConnection();
        }
        log.info("Stopped");
    }

    @Override
    public Connection createConnection() throws JMSException {
        return active.get()
                ? connectionFactory.createConnection()
                : null;
    }

    @Override
    public Connection createConnection(String userName, String password) throws JMSException {
        return active.get()
            ? connectionFactory.createConnection(userName, password)
            : null;
    }

}
