package com.jonnymatts.prometheus.jmx;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.jonnymatts.prometheus.jmx.TestUtils.DEFAULT_BEANS;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class HandleMetricsRunnerTest {

    @Mock private JmxMetricHandler jmxMetricHandler;

    private HandleMetricsRunner handleMetricsRunner;

    @Before
    public void setUp() throws Exception {
        handleMetricsRunner = new HandleMetricsRunner(jmxMetricHandler, DEFAULT_BEANS);
    }

    @Test
    public void runCallsMetricHandlerWithBeans() throws Exception {
        handleMetricsRunner.run();

        verify(jmxMetricHandler).handle(DEFAULT_BEANS);
    }
}