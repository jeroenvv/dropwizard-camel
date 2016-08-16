package com.commercehub.dropwizard.camel.jms.component

import org.apache.camel.component.jms.JmsConfiguration
import spock.lang.Specification

import javax.jms.ConnectionFactory

class ServiceEnabledConnectionFactoryAwareJmsComponentSpec extends Specification {

    def connectionFactory = Mock(ServiceEnabledConnectionFactory)
    def configuration = new JmsConfiguration((ConnectionFactory)connectionFactory)
    def jmsComponent = new ServiceEnabledConnectionFactoryAwareJmsComponent(configuration)

    def "stops ConnectionFactory while stopping if the ConnectionFactory implements the Service interface"() {
        when:
        jmsComponent.stop()

        then:
        1 * connectionFactory.stop()
    }

}
