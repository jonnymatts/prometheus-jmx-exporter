package com.jonnymatts.prometheus.jmx;

import com.jonnymatts.prometheus.jmx.collectors.JmxMetricCollectorProvider;
import com.jonnymatts.prometheus.jmx.configuration.Configuration;
import com.jonnymatts.prometheus.jmx.configuration.ConfigurationParser;
import com.jonnymatts.prometheus.jmx.verification.ConfigurationVerifier;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.nio.charset.Charset.defaultCharset;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class JmxExporter {
    private final ConfigurationParser parser;
    private final ConfigurationVerifier verifier;
    private final JmxMetricHandler metricHandler;
    private final JmxMetricCollectorProvider metricCollectorProvider;
    private final ScheduledExecutorService scheduledExecutorService;

    public JmxExporter() {
        this.parser = new ConfigurationParser();
        this.verifier = new ConfigurationVerifier();
        this.metricCollectorProvider = new JmxMetricCollectorProvider();
        this.metricHandler = new JmxMetricHandler(metricCollectorProvider);
        this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    }

    public JmxExporter(ConfigurationParser parser,
                       ConfigurationVerifier verifier,
                       JmxMetricHandler metricHandler,
                       JmxMetricCollectorProvider metricCollectorProvider,
                       ScheduledExecutorService scheduledExecutorService) {
        this.parser = parser;
        this.verifier = verifier;
        this.metricHandler = metricHandler;
        this.metricCollectorProvider = metricCollectorProvider;
        this.scheduledExecutorService = scheduledExecutorService;
    }

    public void register(String configFileLocation) {
        try {
            final String configBody = IOUtils.toString(new FileInputStream(configFileLocation), defaultCharset());
            final Configuration configuration = parser.parse(configBody);
            verifier.verify(configuration);
            metricCollectorProvider.createCollectors(configuration);
            metricHandler.register(configuration.getBeans());
            scheduledExecutorService.scheduleAtFixedRate(new HandleMetricsRunner(metricHandler, configuration.getBeans()), 0, configuration.getScrapeInterval().toNanos(), NANOSECONDS);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}