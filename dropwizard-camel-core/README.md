# THIS PROJECT IS DEPRECATED
dropwizard-camel is no longer maintained, and this repository will be removed from GitHub on or after April 24, 2018. Published release artifacts will continue to be available indefinitely via
[Bintray JCenter](https://bintray.com/commercehub-oss/main/dropwizard-camel-core).

# dropwizard-camel-core

[![Build Status](https://travis-ci.org/commercehub-oss/dropwizard-camel.svg?branch=master)](https://travis-ci.org/commercehub-oss/dropwizard-camel)
[![Code Coverage](https://img.shields.io/codecov/c/github/commercehub-oss/dropwizard-camel.svg)](https://codecov.io/gh/commercehub-oss/dropwizard-camel)
[![Download](https://api.bintray.com/packages/commercehub-oss/main/dropwizard-camel-core/images/download.svg)](https://bintray.com/commercehub-oss/main/dropwizard-camel-core/_latestVersion)

A library that supports using [Apache Camel™](http://camel.apache.org/) core in
[Dropwizard](http://dropwizard.io/) applications.

Currently, it includes a [Managed](http://www.dropwizard.io/1.0.0/docs/manual/core.html#managed-objects)
[CamelContext](http://camel.apache.org/camelcontext.html), a Managed
[ProducerTemplate](http://camel.apache.org/producertemplate.html), and a
[health check](http://www.dropwizard.io/1.0.0/docs/manual/core.html#health-checks).

# Usage

First, add a dependency to your build file.  Releases are published to
[Bintray JCenter](https://bintray.com/bintray/jcenter).  See the [changelog](../CHANGES.md) for the latest version.

Gradle:

```groovy
dependencies {
    compile "com.commercehub.dropwizard:dropwizard-camel-core:VERSION"
}
```

Maven:

```xml
<dependency>
  <groupId>com.commercehub.dropwizard</groupId>
  <artifactId>dropwizard-camel-core</artifactId>
  <version>VERSION</version>
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
        CamelContext camelContext = ...;
        HealthCheck camelHealthCheck = new CamelHealthCheck(camelContext);
        environment.healthChecks().register("camel", camelHealthCheck);
    }
    
}
```

# License

This library is available under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).

Copyright © 2014-2016 Commerce Technologies, LLC
