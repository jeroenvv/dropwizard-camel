package com.commercehub.dropwizard.camel.jms;

import com.commercehub.dropwizard.camel.component.api.CamelComponentProvider;
import com.commercehub.dropwizard.camel.jms.component.ServiceEnabledConnectionFactoryAwareJmsComponent;
import com.commercehub.dropwizard.camel.jms.config.jndi.InitialContextProvider;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.apache.camel.Component;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.component.jms.JmsConfiguration;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import javax.jms.ConnectionFactory;
import javax.naming.NamingException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@JsonTypeName("jms")
@JsonIgnoreProperties(ignoreUnknown = true)
public class JmsComponentProvider implements CamelComponentProvider {

    @NotNull
    private String name = "jms";

    @Valid
    @NotNull
    private InitialContextProvider context = new InitialContextProvider();

    @Valid
    @NotNull
    private ConnectionFactoryProvider connectionFactory = new ConnectionFactoryProvider();

    @Override
    public Component get() {
        ConnectionFactory connectionFactory;
        try {
            connectionFactory = this.connectionFactory.get(context.get());
        } catch (NamingException e) {
            throw new RuntimeCamelException(e);
        }
        JmsConfiguration jmsConfig = new JmsConfiguration(connectionFactory);
        jmsConfig.setTransactionManager(new JmsTransactionManager(connectionFactory));
        jmsConfig.setTransacted(true);
        jmsConfig.setCacheLevel(DefaultMessageListenerContainer.CACHE_CONSUMER);
        jmsConfig.setTestConnectionOnStartup(true);
        return new ServiceEnabledConnectionFactoryAwareJmsComponent(jmsConfig);
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InitialContextProvider getContext() {
        return context;
    }

    public void setContext(InitialContextProvider context) {
        this.context = context;
    }

    public ConnectionFactoryProvider getConnectionFactory() {
        return connectionFactory;
    }

    public void setConnectionFactory(ConnectionFactoryProvider connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

}
