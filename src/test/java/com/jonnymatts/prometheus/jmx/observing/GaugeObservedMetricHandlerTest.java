package com.jonnymatts.prometheus.jmx.observing;


import com.jonnymatts.prometheus.jmx.collectors.JmxMetricGauge;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class GaugeObservedMetricHandlerTest {

    @Mock private JmxMetricGauge gauge;

    private GaugeObservedMetricHandler handler;

    @Before
    public void setUp() throws Exception {
        handler = new GaugeObservedMetricHandler("bean", "attribute", gauge);
    }

    @Test
    public void acceptIncrementsGaugeByValueRequired() throws Exception {
        handler.accept(10d);
        
        verify(gauge).set("bean", "attribute", 10d);
    }
}