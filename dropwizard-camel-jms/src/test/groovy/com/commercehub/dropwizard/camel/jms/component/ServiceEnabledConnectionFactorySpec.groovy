package com.commercehub.dropwizard.camel.jms.component

import org.springframework.jms.connection.SingleConnectionFactory
import spock.lang.Specification

import javax.jms.Connection

class ServiceEnabledConnectionFactorySpec extends Specification {

    private static final USERNAME = 'username'
    private static final PASSWORD = 'password'

    def connection = Stub(Connection)
    def wrappedConnectionFactory = Mock(SingleConnectionFactory)

    def connectionFactory = new ServiceEnabledConnectionFactory(wrappedConnectionFactory)

    def setup() {
        wrappedConnectionFactory.createConnection(*_) >> {
            return connection
        }
    }

    def "resets wrapped ConnectionFactory's connection while stopping if it is a SingleConnectionFactory"() {
        when:
        connectionFactory.stop()

        then:
        1 * wrappedConnectionFactory.resetConnection()
    }

    def "creates connection while active"() {
        given:
        connectionFactory.start()

        when:
        def returnedConnection = connectionFactory.createConnection()

        then:
        returnedConnection == connection

        when:
        returnedConnection = connectionFactory.createConnection(USERNAME, PASSWORD)

        then:
        returnedConnection == connection
    }

    def "doesn't create connection while stopped"() {
        given:
        connectionFactory.stop()

        when:
        def returnedConnection = connectionFactory.createConnection()

        then:
        returnedConnection == null

        when:
        returnedConnection = connectionFactory.createConnection(USERNAME, PASSWORD)

        then:
        returnedConnection == null
    }

}