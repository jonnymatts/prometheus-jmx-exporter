package com.jonnymatts.prometheus.jmx;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import static com.jonnymatts.prometheus.jmx.MetricType.*;
import static java.nio.charset.Charset.defaultCharset;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class ObjectMapperTest {

    private static final Configuration configuration = new Configuration(
            asList(
                    new Bean(
                            "java.lang:type=Threading",
                            asList(
                                    new BeanAttribute(
                                         "peakThreadCount",
                                            GAUGE
                                    ),
                                    new BeanAttribute(
                                         "totalStartedThreadCount",
                                            GAUGE
                                    )
                            )
                    ),
                    new Bean(
                            "com.jonnymatts:type=Blah",
                            asList(
                                    new BeanAttribute(
                                            "histogram",
                                            HISTOGRAM
                                    ),
                                    new BeanAttribute(
                                            "counter",
                                            COUNTER
                                    ),
                                    new BeanAttribute(
                                            "summary",
                                            SUMMARY
                                    )
                            )
                    )
            )
    );

    private ObjectMapper objectMapper;
    private String configBody;

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper(new YAMLFactory());
        configBody = IOUtils.toString(getClass().getResourceAsStream("/test-config.yaml"), defaultCharset());
    }

    @Test
    public void objectMapperDeserializesConfigurationCorrectly() throws Exception {
        final Configuration got = objectMapper.readValue(configBody, Configuration.class);

        assertThat(got).isEqualTo(configuration);
    }
}
