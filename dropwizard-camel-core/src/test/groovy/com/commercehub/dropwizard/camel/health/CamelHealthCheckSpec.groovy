package com.commercehub.dropwizard.camel.health

import org.apache.camel.CamelContext
import org.apache.camel.Component
import org.apache.camel.Endpoint
import org.apache.camel.Route
import org.apache.camel.ServiceStatus
import org.apache.camel.StatefulService
import spock.lang.Specification

@SuppressWarnings("GroovyAssignabilityCheck")
class CamelHealthCheckSpec extends Specification {

    def camelContext = Mock(CamelContext)
    def healthCheck = new CamelHealthCheck(camelContext)

    def setup() {
        camelContext.getName() >> "camel-1"
        camelContext.status >> ServiceStatus.Started
        camelContext.getEndpointMap() >> [:]
    }

    @SuppressWarnings("UnnecessaryObjectReferences")
    def "healthy only when CamelContext is Started"() {
        when:
            def result = healthCheck.execute()

        then:
            1 * camelContext.status >> status
            result.healthy == healthy
            result.message == message

        where:
            status                   | healthy | message
            ServiceStatus.Starting   | false   | "CamelContext [camel-1] is not running"
            ServiceStatus.Started    | true    | null
            ServiceStatus.Stopping   | false   | "CamelContext [camel-1] is not running"
            ServiceStatus.Stopped    | false   | "CamelContext [camel-1] is not running"
            ServiceStatus.Suspending | false   | "CamelContext [camel-1] is not running"
            ServiceStatus.Suspended  | false   | "CamelContext [camel-1] is not running"
    }

    def "healthy only when all Routes implementing StatefulService are Started"() {
        given:
            def route1 = Mock(StatefulServiceRoute)
            route1.getId() >> 'route-1'
            route1.isStarted() >> route1Started

            def route2 = Mock(StatefulServiceRoute)
            route2.getId() >> 'route-2'
            route2.isStarted() >> route2Started

            camelContext.getRoutes() >> [route1, route2]

        when:
            def result = healthCheck.execute()

        then:
            result.healthy == healthy
            result.message == message

        where:
            route1Started | route2Started | healthy | message
            true          | true          | true    | null
            true          | false         | false   | "Route [route-2] is not running"
            false         | true          | false   | "Route [route-1] is not running"
            false         | false         | false   | "Route [route-1] is not running"
    }

    def "healthy only when all Components implementing StatefulService are Started"() {
        given:
            def component1Name = "component-1"
            def component1 = Mock(StatefulServiceComponent)
            component1.isStarted() >> component1Started

            def component2Name = "component-2"
            def component2 = Mock(StatefulServiceComponent)
            component2.isStarted() >> component2Started

            camelContext.getComponentNames() >> [component1Name, component2Name]
            camelContext.getComponent(component1Name) >> component1
            camelContext.getComponent(component2Name) >> component2

        when:
            def result = healthCheck.execute()

        then:
            result.healthy == healthy
            result.message == message

        where:
            component1Started | component2Started | healthy | message
            true              | true              | true    | null
            true              | false             | false   | "Component [component-2] is not running"
            false             | true              | false   | "Component [component-1] is not running"
            false             | false             | false   | "Component [component-1] is not running"
    }

    def "healthy only when all Endpoints implementing StatefulService are Started"() {
        given:
            def endpoint1 = Mock(StatefulServiceEndpoint)
            endpoint1.isStarted() >> endpoint1Started

            def endpoint2 = Mock(StatefulServiceEndpoint)
            endpoint2.isStarted() >> endpoint2Started

        when:
            def result = healthCheck.execute()

        then:
            1 * camelContext.getEndpointMap() >> ["endpoint-1": endpoint1, "endpoint-2": endpoint2]
            result.healthy == healthy
            result.message == message

        where:
            endpoint1Started | endpoint2Started | healthy | message
            true             | true             | true    | null
            true             | false            | false   | "Endpoint [endpoint-2] is not running"
            false            | true             | false   | "Endpoint [endpoint-1] is not running"
            false            | false            | false   | "Endpoint [endpoint-1] is not running"
    }

    interface StatefulServiceRoute extends Route, StatefulService {}
    interface StatefulServiceComponent extends Component, StatefulService {}
    interface StatefulServiceEndpoint extends Endpoint, StatefulService {}

}
