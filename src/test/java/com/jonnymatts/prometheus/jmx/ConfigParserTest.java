package com.jonnymatts.prometheus.jmx;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.isA;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConfigParserTest {

    private static final String CONFIG_BODY = "configBody";

    @Rule public ExpectedException expectedException = ExpectedException.none();

    @Mock private ObjectMapper objectMapper;
    @Mock private Configuration configuration;

    private ConfigParser configParser;

    @Before
    public void setUp() throws Exception {
        configParser = new ConfigParser(objectMapper);
    }

    @Test
    public void parseReturnsCorrectConfig() throws Exception {
        when(objectMapper.readValue(CONFIG_BODY, Configuration.class)).thenReturn(configuration);

        final Configuration got = configParser.parse(CONFIG_BODY);

        assertThat(got).isEqualTo(configuration);
    }

    @Test
    public void parseThrowsConfigurationDeserializationExceptionIfObjectMapperThrowsException() throws Exception {
        final String errorMessage = "error!";
        when(objectMapper.readValue(CONFIG_BODY, Configuration.class)).thenThrow(new RuntimeException(errorMessage));

        expectedException.expect(ConfigurationDeserializationException.class);
        expectedException.expectCause(isA(RuntimeException.class));
        expectedException.expectMessage("Could not");
        expectedException.expectMessage(CONFIG_BODY);

        configParser.parse(CONFIG_BODY);
    }
}