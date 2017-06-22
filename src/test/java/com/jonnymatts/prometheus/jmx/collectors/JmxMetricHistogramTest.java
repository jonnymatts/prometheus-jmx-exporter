package com.jonnymatts.prometheus.jmx.collectors;

import com.jonnymatts.prometheus.jmx.configuration.ExponentialBucketConfiguration;
import com.jonnymatts.prometheus.jmx.configuration.LinearBucketConfiguration;
import io.prometheus.client.Histogram;
import io.prometheus.client.Histogram.Builder;
import io.prometheus.client.Histogram.Child;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JmxMetricHistogramTest {

    private static final String BEAN_NAME = "bean_name";
    private static final String ATTRIBUTE_NAME = "attribute_name";

    @Mock private Histogram histogram;
    @Mock private Child histogramChild;
    @Mock private Builder histogramBuilder;
    @Mock private Runnable runnable;

    private JmxMetricHistogram jmxMetricHistogram;

    @Before
    public void setUp() throws Exception {
        jmxMetricHistogram = new JmxMetricHistogram(histogram);

        when(jmxMetricHistogram.labels(BEAN_NAME, ATTRIBUTE_NAME)).thenReturn(histogramChild);
    }

    @Test
    public void registerCallsRegister() throws Exception {
        final JmxMetricHistogram got = jmxMetricHistogram.register();

        assertThat(got).isEqualTo(jmxMetricHistogram);

        verify(histogram).register();
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

    @Test
    public void constructorAppliesConfigurationCorrectly() throws Exception {
        when(histogramBuilder.name("jmx_metric_histogram_my_histogram")).thenReturn(histogramBuilder);
        when(histogramBuilder.help(any())).thenReturn(histogramBuilder);
        when(histogramBuilder.labelNames(any())).thenReturn(histogramBuilder);
        when(histogramBuilder.buckets(0.001d, 0.01d, 0.1d, 1d)).thenReturn(histogramBuilder);
        when(histogramBuilder.exponentialBuckets(0.001d, 10d, 5)).thenReturn(histogramBuilder);
        when(histogramBuilder.linearBuckets(0.01d, 30d, 10)).thenReturn(histogramBuilder);
        when(histogramBuilder.create()).thenReturn(histogram);

        final JmxMetricHistogram got = new JmxMetricHistogram(
                histogramBuilder,
                new HistogramMetricConfiguration(
                        "my_histogram",
                        asList(0.001d, 0.01d, 0.1d, 1d),
                        new ExponentialBucketConfiguration(0.001d, 10d, 5),
                        new LinearBucketConfiguration(0.01d, 30d, 10)
                )
        );

        assertThat(got).isEqualTo(new JmxMetricHistogram(histogram));

        verify(histogramBuilder).buckets(0.001d, 0.01d, 0.1d, 1d);
        verify(histogramBuilder).exponentialBuckets(0.001d, 10d, 5);
        verify(histogramBuilder).linearBuckets(0.01d, 30d, 10);
    }
}