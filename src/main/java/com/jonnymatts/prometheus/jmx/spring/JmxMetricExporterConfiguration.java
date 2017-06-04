package com.jonnymatts.prometheus.jmx.spring;

import com.jonnymatts.prometheus.jmx.JmxExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

@Configuration
public class JmxMetricExporterConfiguration {

    @Autowired
    private Environment environment;

    @PostConstruct
    public void setUp() {
        final String configFileLocation = environment.getProperty("jmx.metric.exporter.config.path", "/config/config.yaml");
        jmxExporter().register(configFileLocation);
    }

    @Bean
    public JmxExporter jmxExporter() {
        return new JmxExporter();
    }
}