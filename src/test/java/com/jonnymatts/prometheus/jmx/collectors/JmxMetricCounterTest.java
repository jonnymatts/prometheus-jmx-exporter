package com.jonnymatts.prometheus.jmx.collectors;

import io.prometheus.client.Counter;
import io.prometheus.client.Counter.Child;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JmxMetricCounterTest {

    private static final String BEAN_NAME = "bean_name";
    private static final String ATTRIBUTE_NAME = "attribute_name";

    @Mock
    private Counter counter;
    @Mock
    private Child counterChild;

    private JmxMetricCounter jmxMetricCounter;

    @Before
    public void setUp() throws Exception {
        jmxMetricCounter = new JmxMetricCounter(counter);

        when(jmxMetricCounter.labels(BEAN_NAME, ATTRIBUTE_NAME)).thenReturn(counterChild);
    }

    @Test
    public void incCallsIncWithTheCorrectLabels() throws Exception {
        jmxMetricCounter.inc(BEAN_NAME, ATTRIBUTE_NAME);

        verify(counterChild).inc(1.0);
    }

    @Test
    public void getCallsGetWithTheCorrectLabels() throws Exception {
        when(counterChild.get()).thenReturn(100d);

        final double got = jmxMetricCounter.get(BEAN_NAME, ATTRIBUTE_NAME);

        assertThat(got).isEqualTo(100d);
    }
}