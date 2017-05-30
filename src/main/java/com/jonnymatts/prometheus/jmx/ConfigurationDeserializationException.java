package com.jonnymatts.prometheus.jmx;

import static java.lang.String.format;

public class ConfigurationDeserializationException extends RuntimeException {

    public ConfigurationDeserializationException(String failedConfigBody, Exception e) {
        super(format("Could not deserialize config. Given config: \n %s", failedConfigBody), e);
    }
}
