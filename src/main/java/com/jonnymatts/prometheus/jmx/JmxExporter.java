package com.jonnymatts.prometheus.jmx;

import com.jonnymatts.prometheus.jmx.configuration.Configuration;
import com.jonnymatts.prometheus.jmx.configuration.ConfigurationParser;
import com.jonnymatts.prometheus.jmx.verification.MBeanVerifier;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.nio.charset.Charset.defaultCharset;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class JmxExporter {
    private final ConfigurationParser parser;
    private final MBeanVerifier verifier;
    private final JmxMetricHandler metricHandler;
    private final ScheduledExecutorService scheduledExecutorService;

    public JmxExporter() {
        this.parser = new ConfigurationParser();
        this.verifier = new MBeanVerifier();
        this.metricHandler = new JmxMetricHandler();
        this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    }

    public JmxExporter(ConfigurationParser parser,
                       MBeanVerifier verifier,
                       JmxMetricHandler metricHandler,
                       ScheduledExecutorService scheduledExecutorService) {
        this.parser = parser;
        this.verifier = verifier;
        this.metricHandler = metricHandler;
        this.scheduledExecutorService = scheduledExecutorService;
    }

    public void register(String configFileLocation) {
        try {
            final String configBody = IOUtils.toString(new FileInputStream(configFileLocation), defaultCharset());
            final Configuration configuration = parser.parse(configBody);
            configuration.getBeans().forEach(verifier::verify);
            scheduledExecutorService.scheduleAtFixedRate(new HandleMetricsRunner(metricHandler, configuration.getBeans()), 0, configuration.getScrapeInterval().toNanos(), NANOSECONDS);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}