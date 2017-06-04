package com.jonnymatts.prometheus.jmx.collectors;

import io.prometheus.client.Summary;
import io.prometheus.client.Summary.Child;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JmxMetricSummaryTest {

    private static final String BEAN_NAME = "bean_name";
    private static final String ATTRIBUTE_NAME = "attribute_name";

    @Mock private Summary summary;
    @Mock private Child summaryChild;
    @Mock private Runnable runnable;

    private JmxMetricSummary jmxMetricSummary;

    @Before
    public void setUp() throws Exception {
        jmxMetricSummary = new JmxMetricSummary(summary);

        when(jmxMetricSummary.labels(BEAN_NAME, ATTRIBUTE_NAME)).thenReturn(summaryChild);
    }

    @Test
    public void observeCallsObserveWithTheCorrectLabels() throws Exception {
        jmxMetricSummary.observe(BEAN_NAME, ATTRIBUTE_NAME, 100d);

        verify(summaryChild).observe(100d);
    }

    @Test
    public void startTimerCallsGetWithTheCorrectLabels() throws Exception {
        jmxMetricSummary.startTimer(BEAN_NAME, ATTRIBUTE_NAME);

        verify(summaryChild).startTimer();
    }

    @Test
    public void timeCallsGetWithTheCorrectLabels() throws Exception {
        jmxMetricSummary.time(BEAN_NAME, ATTRIBUTE_NAME, runnable);

        verify(summaryChild).time(runnable);
    }
}