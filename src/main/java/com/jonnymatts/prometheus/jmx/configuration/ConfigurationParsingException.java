package com.jonnymatts.prometheus.jmx.configuration;

import static java.lang.String.format;

public class ConfigurationParsingException extends RuntimeException {

    public ConfigurationParsingException(String failedConfigBody, Exception e) {
        super(format("Could not parse config. Given config: \n %s", failedConfigBody), e);
    }
}