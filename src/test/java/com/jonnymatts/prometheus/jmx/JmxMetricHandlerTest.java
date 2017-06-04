package com.jonnymatts.prometheus.jmx;

import com.jonnymatts.prometheus.jmx.collectors.JmxMetricCounter;
import com.jonnymatts.prometheus.jmx.collectors.JmxMetricGauge;
import com.jonnymatts.prometheus.jmx.collectors.JmxMetricHistogram;
import com.jonnymatts.prometheus.jmx.collectors.JmxMetricSummary;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import static com.jonnymatts.prometheus.jmx.TestUtils.DEFAULT_BEANS;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JmxMetricHandlerTest {

    @Mock private MBeanServer mBeanServer;
    @Mock private JmxMetricCounter counter;
    @Mock private JmxMetricGauge gauge;
    @Mock private JmxMetricHistogram histogram;
    @Mock private JmxMetricSummary summary;

    private JmxMetricHandler handler;

    @Before
    public void setUp() throws Exception {
        handler = new JmxMetricHandler(mBeanServer, () -> counter, () -> gauge, () -> histogram, () -> summary);
    }

    @Test
    public void handleGetsMetricValueAndSetsCollectorValue() throws Exception {
        when(counter.get("com.jonnymatts:type=Blah", "counter")).thenReturn(1d);
        when(mBeanServer.getAttribute(new ObjectName("java.lang:type=Threading"), "peakThreadCount")).thenReturn(1);
        when(mBeanServer.getAttribute(new ObjectName("java.lang:type=Threading"), "totalStartedThreadCount")).thenReturn(2);
        when(mBeanServer.getAttribute(new ObjectName("com.jonnymatts:type=Blah"), "histogram")).thenReturn(3);
        when(mBeanServer.getAttribute(new ObjectName("com.jonnymatts:type=Blah"), "counter")).thenReturn(4);
        when(mBeanServer.getAttribute(new ObjectName("com.jonnymatts:type=Blah"), "summary")).thenReturn(5);
        when(mBeanServer.getAttribute(new ObjectName("com.jonnymatts:type=Blah"), "boolean")).thenReturn("true");

        handler.handle(DEFAULT_BEANS);

        verify(gauge).set("java.lang:type=Threading", "peakThreadCount", 1);
        verify(gauge).set("java.lang:type=Threading", "totalStartedThreadCount", 2);
        verify(histogram).observe("com.jonnymatts:type=Blah", "histogram", 3);
        verify(counter).inc("com.jonnymatts:type=Blah", "counter", 3);
        verify(summary).observe("com.jonnymatts:type=Blah","summary", 5);
        verify(gauge).set("com.jonnymatts:type=Blah","boolean", 1);
    }
}