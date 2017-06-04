package com.jonnymatts.prometheus.jmx;

import com.jonnymatts.prometheus.jmx.configuration.Configuration;
import com.jonnymatts.prometheus.jmx.configuration.ConfigurationParser;
import com.jonnymatts.prometheus.jmx.verification.MBeanVerifier;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.FileWriter;
import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.jonnymatts.prometheus.jmx.TestUtils.DEFAULT_BEANS;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JmxExporterTest {

    private static final String CONFIG_BODY = "CONFIG_BODY";
    private static final Duration SCRAPE_INTERVAL = Duration.ofSeconds(1);

    @Rule public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Mock private ConfigurationParser parser;
    @Mock private MBeanVerifier verifier;
    @Mock private JmxMetricHandler metricHandler;
    @Mock private ScheduledExecutorService scheduledExecutorService;

    @Mock private Configuration configuration;

    private File configFile;

    private JmxExporter jmxExporter;

    @Before
    public void setUp() throws Exception {
        when(parser.parse(CONFIG_BODY)).thenReturn(configuration);
        when(configuration.getBeans()).thenReturn(DEFAULT_BEANS);
        when(configuration.getScrapeInterval()).thenReturn(SCRAPE_INTERVAL);

        jmxExporter = new JmxExporter(parser, verifier, metricHandler, scheduledExecutorService);

        configFile = temporaryFolder.newFile("config.yaml");

        final FileWriter fileWriter = new FileWriter(configFile);
        fileWriter.write(CONFIG_BODY);
        fileWriter.close();
    }

    @Test
    public void register() throws Exception {
        jmxExporter.register(configFile.getAbsolutePath());

        DEFAULT_BEANS.forEach(bean -> verify(verifier).verify(bean));

        verify(scheduledExecutorService).scheduleAtFixedRate(new HandleMetricsRunner(metricHandler, DEFAULT_BEANS), 0, 1_000_000_000, TimeUnit.NANOSECONDS);
    }
}