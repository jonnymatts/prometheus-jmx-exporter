package com.jonnymatts.prometheus.jmx.observing;


import com.jonnymatts.prometheus.jmx.collectors.JmxMetricSummary;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SummaryObservedMetricHandlerTest {

    @Mock private JmxMetricSummary summary;

    private SummaryObservedMetricHandler handler;

    @Before
    public void setUp() throws Exception {
        handler = new SummaryObservedMetricHandler("bean", "attribute", summary);
    }

    @Test
    public void acceptIncrementsSummaryByValueRequired() throws Exception {
        handler.accept(10d);
        
        verify(summary).observe("bean", "attribute", 10d);
    }
}