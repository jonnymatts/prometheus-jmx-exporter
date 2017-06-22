package com.jonnymatts.prometheus.jmx;

import com.jonnymatts.prometheus.jmx.collectors.*;
import com.jonnymatts.prometheus.jmx.observing.CounterObservedMetricHandler;
import com.jonnymatts.prometheus.jmx.observing.GaugeObservedMetricHandler;
import com.jonnymatts.prometheus.jmx.observing.HistogramObservedMetricHandler;
import com.jonnymatts.prometheus.jmx.observing.SummaryObservedMetricHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.util.ArrayList;

import static com.jonnymatts.prometheus.jmx.TestUtils.DEFAULT_BEANS;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JmxMetricHandlerTest {

    @Mock private MBeanServer mBeanServer;
    @Mock private JmxMetricCollectorProvider provider;
    @Mock private JmxMetricGauge gauge;
    @Mock private JmxMetricCounter counter;
    @Mock private JmxMetricHistogram histogram;
    @Mock private JmxMetricHistogram customHistogram;
    @Mock private JmxMetricSummary summary;
    @Mock private JmxMetricSummary customSummary;
    @Mock private CounterObservedMetricHandler counterObservedMetricHandler;
    @Mock private GaugeObservedMetricHandler gaugeObservedMetricHandler;
    @Mock private HistogramObservedMetricHandler histogramObservedMetricHandler;
    @Mock private SummaryObservedMetricHandler summaryObservedMetricHandler;

    private JmxMetricHandler handler;
    private ArrayList<JmxMetricCollector> collectors;

    @Before
    public void setUp() throws Exception {
        collectors = new ArrayList<>();
        when(provider.gauge()).thenReturn(gauge);
        when(provider.counter()).thenReturn(counter);
        when(provider.histogram(any(String.class))).thenReturn(customHistogram);
        when(provider.summary(any(String.class))).thenReturn(customSummary);

        handler = new JmxMetricHandler(mBeanServer, provider, collectors);
    }

    @Test
    public void registerCreatesAndRegistersMetricsForEachBeanProvided() throws Exception {
        handler.register(DEFAULT_BEANS);

        assertThat(collectors).containsExactlyInAnyOrder(
                new JmxMetricCollector(DEFAULT_BEANS.get(0), DEFAULT_BEANS.get(0).getAttributes().get(0), new GaugeObservedMetricHandler("java.lang:type=Threading", "peakThreadCount", gauge)),
                new JmxMetricCollector(DEFAULT_BEANS.get(0), DEFAULT_BEANS.get(0).getAttributes().get(1), new GaugeObservedMetricHandler("java.lang:type=Threading", "totalStartedThreadCount", gauge)),
                new JmxMetricCollector(DEFAULT_BEANS.get(1), DEFAULT_BEANS.get(1).getAttributes().get(0), new HistogramObservedMetricHandler("com.jonnymatts:type=Blah", "histogram", customHistogram)),
                new JmxMetricCollector(DEFAULT_BEANS.get(1), DEFAULT_BEANS.get(1).getAttributes().get(1), new CounterObservedMetricHandler("com.jonnymatts:type=Blah", "counter", counter)),
                new JmxMetricCollector(DEFAULT_BEANS.get(1), DEFAULT_BEANS.get(1).getAttributes().get(2), new SummaryObservedMetricHandler("com.jonnymatts:type=Blah", "summary", customSummary)),
                new JmxMetricCollector(DEFAULT_BEANS.get(1), DEFAULT_BEANS.get(1).getAttributes().get(3), new GaugeObservedMetricHandler("com.jonnymatts:type=Blah", "boolean", gauge))
        );
    }

    @Test
    public void handleGetsMetricValueAndSetsCollectorValue() throws Exception {
        when(mBeanServer.getAttribute(new ObjectName("java.lang:type=Threading"), "peakThreadCount")).thenReturn(1);
        when(mBeanServer.getAttribute(new ObjectName("java.lang:type=Threading"), "totalStartedThreadCount")).thenReturn(2);
        when(mBeanServer.getAttribute(new ObjectName("com.jonnymatts:type=Blah"), "histogram")).thenReturn(3);
        when(mBeanServer.getAttribute(new ObjectName("com.jonnymatts:type=Blah"), "counter")).thenReturn(4);

        collectors.addAll(
          asList(
                  new JmxMetricCollector(DEFAULT_BEANS.get(0), DEFAULT_BEANS.get(0).getAttributes().get(0), gaugeObservedMetricHandler),
                  new JmxMetricCollector(DEFAULT_BEANS.get(0), DEFAULT_BEANS.get(0).getAttributes().get(1), counterObservedMetricHandler),
                  new JmxMetricCollector(DEFAULT_BEANS.get(1), DEFAULT_BEANS.get(1).getAttributes().get(0), summaryObservedMetricHandler),
                  new JmxMetricCollector(DEFAULT_BEANS.get(1), DEFAULT_BEANS.get(1).getAttributes().get(1), histogramObservedMetricHandler)
          )
        );

        handler.handle();

        verify(gaugeObservedMetricHandler).accept(1d);
        verify(counterObservedMetricHandler).accept(2d);
        verify(summaryObservedMetricHandler).accept(3d);
        verify(histogramObservedMetricHandler).accept(4d);
    }
}