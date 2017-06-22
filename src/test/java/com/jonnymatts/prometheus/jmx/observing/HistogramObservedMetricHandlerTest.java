package com.jonnymatts.prometheus.jmx.observing;


import com.jonnymatts.prometheus.jmx.collectors.JmxMetricHistogram;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class HistogramObservedMetricHandlerTest {

    @Mock private JmxMetricHistogram histogram;

    private HistogramObservedMetricHandler handler;

    @Before
    public void setUp() throws Exception {
        handler = new HistogramObservedMetricHandler("bean", "attribute", histogram);
    }

    @Test
    public void acceptIncrementsHistogramByValueRequired() throws Exception {
        handler.accept(10d);
        
        verify(histogram).observe("bean", "attribute", 10d);
    }
}