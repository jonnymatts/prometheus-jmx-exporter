package com.jonnymatts.prometheus.jmx.collectors;

import com.jonnymatts.prometheus.jmx.configuration.QuantileConfiguration;
import io.prometheus.client.Summary;
import io.prometheus.client.Summary.Builder;
import io.prometheus.client.Summary.Child;
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
public class JmxMetricSummaryTest {

    private static final String BEAN_NAME = "bean_name";
    private static final String ATTRIBUTE_NAME = "attribute_name";

    @Mock private Summary summary;
    @Mock private Builder summaryBuilder;
    @Mock private Child summaryChild;
    @Mock private Runnable runnable;

    private JmxMetricSummary jmxMetricSummary;

    @Before
    public void setUp() throws Exception {
        jmxMetricSummary = new JmxMetricSummary(summary);

        when(jmxMetricSummary.labels(BEAN_NAME, ATTRIBUTE_NAME)).thenReturn(summaryChild);
    }

    @Test
    public void registerCallsRegister() throws Exception {
        final JmxMetricSummary got = jmxMetricSummary.register();

        assertThat(got).isEqualTo(jmxMetricSummary);

        verify(summary).register();
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

    @Test
    public void constructorAppliesConfigurationCorrectly() throws Exception {
        when(summaryBuilder.name("jmx_metric_summary_my_summary")).thenReturn(summaryBuilder);
        when(summaryBuilder.help(any())).thenReturn(summaryBuilder);
        when(summaryBuilder.labelNames(any())).thenReturn(summaryBuilder);
        when(summaryBuilder.quantile(0.001d, 1)).thenReturn(summaryBuilder);
        when(summaryBuilder.quantile(0.01d, 2)).thenReturn(summaryBuilder);
        when(summaryBuilder.quantile(0.1d, 3)).thenReturn(summaryBuilder);
        when(summaryBuilder.ageBuckets(100)).thenReturn(summaryBuilder);
        when(summaryBuilder.maxAgeSeconds(200)).thenReturn(summaryBuilder);
        when(summaryBuilder.create()).thenReturn(summary);

        final JmxMetricSummary got = new JmxMetricSummary(
                summaryBuilder,
                new SummaryMetricConfiguration(
                        "my_summary",
                        asList(
                                new QuantileConfiguration(0.001d, 1d),
                                new QuantileConfiguration(0.01d, 2d),
                                new QuantileConfiguration(0.1d, 3d)
                        ),
                        100,
                        200L
                )
        );

        assertThat(got).isEqualTo(new JmxMetricSummary(summary));

        verify(summaryBuilder).quantile(0.001d, 1);
        verify(summaryBuilder).quantile(0.01d, 2);
        verify(summaryBuilder).quantile(0.1d, 3);
        verify(summaryBuilder).ageBuckets(100);
        verify(summaryBuilder).maxAgeSeconds(200);
    }
}