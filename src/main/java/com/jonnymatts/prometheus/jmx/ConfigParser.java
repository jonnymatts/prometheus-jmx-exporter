package com.jonnymatts.prometheus.jmx;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ConfigParser {
    private final ObjectMapper objectMapper;

    public ConfigParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Configuration parse(String configBody) {
        try {
            return objectMapper.readValue(configBody, Configuration.class);
        } catch (Exception e) {
            throw new ConfigurationDeserializationException(configBody, e);
        }
    }
}