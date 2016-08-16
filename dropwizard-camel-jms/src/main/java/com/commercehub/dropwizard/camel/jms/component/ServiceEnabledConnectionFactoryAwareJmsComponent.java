package com.commercehub.dropwizard.camel.jms.component;

import org.apache.camel.Service;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.jms.JmsConfiguration;

import javax.jms.ConnectionFactory;

/**
 * A {@link JmsComponent} that stops its {@link ConnectionFactory} as appropriate. Currently, it supports stopping
 * ConnectionFactory instances that implement {@link Service}.
 */
public class ServiceEnabledConnectionFactoryAwareJmsComponent extends JmsComponent {
    public ServiceEnabledConnectionFactoryAwareJmsComponent(JmsConfiguration configuration) {
        super(configuration);
    }

    @SuppressWarnings("Instanceof")
    @Override
    protected void doStop() throws Exception {
        Object connectionFactory = this.getConfiguration().getConnectionFactory();
        if (connectionFactory instanceof Service) {
            ((Service)connectionFactory).stop();
        }

    }

}
