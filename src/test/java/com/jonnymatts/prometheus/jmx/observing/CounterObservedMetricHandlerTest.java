package com.jonnymatts.prometheus.jmx.observing;


import com.jonnymatts.prometheus.jmx.collectors.JmxMetricCounter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CounterObservedMetricHandlerTest {

    @Mock private JmxMetricCounter counter;

    private CounterObservedMetricHandler handler;

    @Before
    public void setUp() throws Exception {
        handler = new CounterObservedMetricHandler("bean", "attribute", counter);
    }

    @Test
    public void acceptIncrementsCounterByValueRequired() throws Exception {
        when(counter.get("bean", "attribute")).thenReturn(3d);

        handler.accept(10d);

        verify(counter).inc("bean", "attribute", 7d);
    }
}