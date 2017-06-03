package com.jonnymatts.prometheus.jmx;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static com.jonnymatts.prometheus.jmx.TestUtils.DEFAULT_BEANS;
import static java.nio.charset.Charset.defaultCharset;
import static org.assertj.core.api.Assertions.assertThat;

public class ObjectMapperTest {

    private static final Configuration configuration = new Configuration(
            Duration.of(200, ChronoUnit.MILLIS),
            DEFAULT_BEANS
    );

    private ObjectMapper objectMapper;
    private String configBody;

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper(new YAMLFactory()).registerModule(new JavaTimeModule()).registerModule(new Jdk8Module());
        configBody = IOUtils.toString(getClass().getResourceAsStream("/test-config.yaml"), defaultCharset());
    }

    @Test
    public void objectMapperDeserializesConfigurationCorrectly() throws Exception {
        final Configuration got = objectMapper.readValue(configBody, Configuration.class);

        assertThat(got).isEqualTo(configuration);
    }

    @Test
    public void configHasDefaultScrapeIntervalIfNotPresentInConfig() throws Exception {
        configuration.setScrapeInterval(Duration.ofSeconds(15));
        configBody = IOUtils.toString(getClass().getResourceAsStream("/test-config-without-scrape-interval.yaml"), defaultCharset());

        final Configuration got = objectMapper.readValue(configBody, Configuration.class);

        assertThat(got).isEqualTo(configuration);
    }
}
