package com.commercehub.dropwizard.camel.component.api;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.jackson.Discoverable;
import org.apache.camel.Component;
import javax.inject.Provider;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface CamelComponentProvider extends Discoverable, Provider<Component> {

    String getName();

    Component get();

}