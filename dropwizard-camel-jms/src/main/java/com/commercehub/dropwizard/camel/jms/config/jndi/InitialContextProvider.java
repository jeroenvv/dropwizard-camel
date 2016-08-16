package com.commercehub.dropwizard.camel.jms.config.jndi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotBlank;

import javax.inject.Provider;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InitialContextProvider implements Provider<InitialContext> {

    @NotBlank
    private String initialContextFactory;

    @NotBlank
    private String providerUrl;

    @NotBlank
    private String urlPkgPrefixes;

    public InitialContext get() {
        Properties environment = new Properties();
        environment.setProperty(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
        environment.setProperty(Context.PROVIDER_URL, providerUrl);
        environment.setProperty(Context.URL_PKG_PREFIXES, urlPkgPrefixes);
        try {
            return new InitialContext(environment);
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setInitialContextFactory(String initialContextFactory) {
        this.initialContextFactory = initialContextFactory;
    }

    public String getInitialContextFactory() {
        return initialContextFactory;
    }

    public void setProviderUrl(String providerUrl) {
        this.providerUrl = providerUrl;
    }

    public String getProviderUrl() {
        return providerUrl;
    }

    public void setUrlPkgPrefixes(String urlPkgPrefixes) {
        this.urlPkgPrefixes = urlPkgPrefixes;
    }

    public String getUrlPkgPrefixes() {
        return urlPkgPrefixes;
    }

}
