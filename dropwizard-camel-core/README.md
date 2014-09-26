# Overview

A library that supports using [Apache Camel™](http://camel.apache.org/) core in
[Dropwizard](http://dropwizard.io/) applications.

Currently, it includes a [Managed](http://dropwizard.io/manual/core.html#managed-objects)
[CamelContext](http://camel.apache.org/camelcontext.html), a Managed
[ProducerTemplate](http://camel.apache.org/producertemplate.html), and a
[health check](http://dropwizard.io/manual/core.html#health-checks).


# Usage

First, add a dependency to your build file.  Releases are published to
[Bintray JCenter](https://bintray.com/bintray/jcenter).  See the [changelog](../CHANGES.md) for the latest version.

Gradle:

```groovy
dependencies {
    compile "com.commercehub.dropwizard:dropwizard-camel-core:1.0.0"
}
```

Maven:

```xml
<dependency>
  <groupId>com.commercehub.dropwizard</groupId>
  <artifactId>dropwizard-camel-core</artifactId>
  <version>1.0.0</version>
</dependency>
```

Now you can wrap your `CamelContext` in a `ManagedCamelContext` and wrap your `ProducerTemplate` in a
`ManagedProducerTemplate`:

```java
public class App extends Application<AppConfiguration> {

    @Override
    public void run(AppConfiguration config, Environment environment) {
        CamelContext camelContext = new DefaultCamelContext();
        environment.lifecycle().manage(ManagedCamelContext.of(camelContext));
        
        ProducerTemplate producerTemplate = camelContext.createProducerTemplate();
        environment.lifecycle().manage(ManagedProducerTemplate.of(producerTemplate));
    }
    
}
```

And you can register a `CamelHealthCheck`:

```java
public class App extends Application<AppConfiguration> {

    @Override
    public void run(AppConfiguration config, Environment environment) {
        CamelContext camelContext = ...
        HealthCheck camelHealthCheck = new CamelHealthCheck(camelContext);
        environment.healthChecks().register("camel", camelHealthCheck);
    }
    
}
```

# License
This library is available under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).

(c) All rights reserved Commerce Technologies, Inc.
