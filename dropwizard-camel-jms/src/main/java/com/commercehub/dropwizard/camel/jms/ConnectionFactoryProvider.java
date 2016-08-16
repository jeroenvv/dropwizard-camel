package com.commercehub.dropwizard.camel.jms;

import com.commercehub.dropwizard.camel.jms.component.ServiceEnabledConnectionFactory;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.jms.connection.CachingConnectionFactory;

import javax.inject.Provider;
import javax.jms.ConnectionFactory;
import javax.naming.Context;
import javax.naming.NamingException;

@JsonIgnoreProperties(ignoreUnknown = true)
class ConnectionFactoryProvider implements Provider<ConnectionFactory> {
    @NotBlank
    private String name;

    ConnectionFactory get(Context context) throws NamingException {
        ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup(name);
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(connectionFactory);
        return new ServiceEnabledConnectionFactory(cachingConnectionFactory);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public ConnectionFactory get() {
        return null;
    }

}
