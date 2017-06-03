package com.jonnymatts.prometheus.jmx;

import io.prometheus.client.Histogram;
import io.prometheus.client.Histogram.Child;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JmxMetricHistogramTest {

    private static final String BEAN_NAME = "bean_name";
    private static final String ATTRIBUTE_NAME = "attribute_name";

    @Mock private Histogram histogram;
    @Mock private Child histogramChild;
    @Mock private Runnable runnable;

    private JmxMetricHistogram jmxMetricHistogram;

    @Before
    public void setUp() throws Exception {
        jmxMetricHistogram = new JmxMetricHistogram(histogram);

        when(jmxMetricHistogram.labels(BEAN_NAME, ATTRIBUTE_NAME)).thenReturn(histogramChild);
    }

    @Test
    public void observeCallsObserveWithTheCorrectLabels() throws Exception {
        jmxMetricHistogram.observe(BEAN_NAME, ATTRIBUTE_NAME, 100d);

        verify(histogramChild).observe(100d);
    }

    @Test
    public void startTimerCallsGetWithTheCorrectLabels() throws Exception {
        jmxMetricHistogram.startTimer(BEAN_NAME, ATTRIBUTE_NAME);

        verify(histogramChild).startTimer();
    }

    @Test
    public void timeCallsGetWithTheCorrectLabels() throws Exception {
        jmxMetricHistogram.time(BEAN_NAME, ATTRIBUTE_NAME, runnable);

        verify(histogramChild).time(runnable);
    }
}