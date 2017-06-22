package com.jonnymatts.prometheus.jmx.collectors;

import io.prometheus.client.Gauge;
import io.prometheus.client.Gauge.Child;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JmxMetricGaugeTest {

    private static final String BEAN_NAME = "bean_name";
    private static final String ATTRIBUTE_NAME = "attribute_name";

    @Mock private Gauge gauge;
    @Mock private Child gaugeChild;

    private JmxMetricGauge jmxMetricGauge;

    @Before
    public void setUp() throws Exception {
        jmxMetricGauge = new JmxMetricGauge(gauge);

        when(jmxMetricGauge.labels(BEAN_NAME, ATTRIBUTE_NAME)).thenReturn(gaugeChild);
    }

    @Test
    public void registerCallsRegister() throws Exception {
        final JmxMetricGauge got = jmxMetricGauge.register();

        assertThat(got).isEqualTo(jmxMetricGauge);

        verify(gauge).register();
    }

    @Test
    public void incCallsIncWithTheCorrectLabels() throws Exception {
        jmxMetricGauge.inc(BEAN_NAME, ATTRIBUTE_NAME);

        verify(gaugeChild).inc(1.0);
    }

    @Test
    public void decCallsDecWithTheCorrectLabels() throws Exception {
        jmxMetricGauge.dec(BEAN_NAME, ATTRIBUTE_NAME);

        verify(gaugeChild).dec(1.0);
    }

    @Test
    public void setCallsSetWithTheCorrectLabels() throws Exception {
        jmxMetricGauge.set(BEAN_NAME, ATTRIBUTE_NAME, 100);

        verify(gaugeChild).set(100);
    }
}