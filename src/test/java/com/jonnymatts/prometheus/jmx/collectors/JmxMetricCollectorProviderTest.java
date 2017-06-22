package com.jonnymatts.prometheus.jmx.collectors;

import com.jonnymatts.prometheus.jmx.configuration.Configuration;
import com.jonnymatts.prometheus.jmx.configuration.ExponentialBucketConfiguration;
import com.jonnymatts.prometheus.jmx.configuration.LinearBucketConfiguration;
import com.jonnymatts.prometheus.jmx.configuration.QuantileConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;
import java.util.function.Consumer;

import static com.jonnymatts.prometheus.jmx.TestUtils.DEFAULT_BEANS;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JmxMetricCollectorProviderTest {

    @Mock private Configuration configuration;
    @Mock private JmxMetricCounter counter;
    @Mock private JmxMetricGauge gauge;
    @Mock private JmxMetricHistogram histogram;
    @Mock private JmxMetricHistogram customHistogram;
    @Mock private JmxMetricSummary summary;
    @Mock private JmxMetricSummary customSummary;

    @Mock private Map<String, JmxMetricHistogram> histogramMap;
    @Mock private Map<String, JmxMetricSummary> summaryMap;

    private JmxMetricCollectorProvider provider;

    @Before
    public void setUp() throws Exception {
        when(gauge.register()).thenReturn(gauge);

        provider = new JmxMetricCollectorProvider(
                () -> counter,
                () -> gauge,
                () -> histogram,
                () -> summary,
                histogramMap,
                summaryMap
        );
    }

    @Test
    public void createCollectorsCreatesAndRegistersRequiredDefaultCollectorsAndAnyCustomCollectors() throws Exception {
        when(configuration.getHistograms()).thenReturn(
                singletonList(
                        new HistogramMetricConfiguration(
                                "histogram",
                                singletonList(1d),
                                new ExponentialBucketConfiguration(1d, 1d, 1),
                                new LinearBucketConfiguration(1d, 1d, 1)
                        )
                )
        );
        when(configuration.getSummaries()).thenReturn(
                singletonList(
                        new SummaryMetricConfiguration(
                                "summary",
                                singletonList(new QuantileConfiguration(1d, 1d)),
                                1,
                                1L
                        )
                )
        );
        when(configuration.getBeans()).thenReturn(singletonList(DEFAULT_BEANS.get(0)));

        provider.createCollectors(configuration);

        verify(summaryMap).put(eq("summary"), any(JmxMetricSummary.class));
        verify(histogramMap).put(eq("histogram"), any(JmxMetricHistogram.class));
        verify(gauge).register();
        verifyZeroInteractions(counter, histogram, summary);
    }

    @Test
    public void createIgnoresCustomHistogramsAndSummariesIfTheyAreNullInConfiguration() throws Exception {
        when(configuration.getSummaries()).thenReturn(null);
        when(configuration.getHistograms()).thenReturn(null);

        provider.createCollectors(configuration);

        verifyZeroInteractions(histogramMap);
        verifyZeroInteractions(summaryMap);
    }

    @Test
    public void counterAlwaysReturnsDefaultCounter() throws Exception {
        repeatComputation(
                10,
                p -> {
                    final JmxMetricCounter got = p.counter();
                    assertThat(got).isEqualTo(counter);
                }
        );
    }

    @Test
    public void gaugeAlwaysReturnsDefaultGauge() throws Exception {
        repeatComputation(
                10,
                p -> {
                    final JmxMetricGauge got = p.gauge();
                    assertThat(got).isEqualTo(gauge);
                }
        );
    }

    @Test
    public void histogramAlwaysReturnsDefaultHistogramIfNoConfigurationIsGiven() throws Exception {
        repeatComputation(
                10,
                p -> {
                    final JmxMetricHistogram got = p.histogram();
                    assertThat(got).isEqualTo(histogram);
                }
        );
    }

    @Test
    public void summaryAlwaysReturnsDefaultSummaryIfNoConfigurationIsGiven() throws Exception {
        repeatComputation(
                10,
                p -> {
                    final JmxMetricSummary got = p.summary();
                    assertThat(got).isEqualTo(summary);
                }
        );
    }

    @Test
    public void histogramReturnsCustomHistogramIfCalledWithAName() throws Exception {
        when(histogramMap.get("histogram")).thenReturn(customHistogram);

        final JmxMetricHistogram got = provider.histogram("histogram");

        assertThat(got).isEqualTo(customHistogram);
    }

    @Test
    public void summaryReturnsCustomSummaryIfCalledWithAName() throws Exception {
        when(summaryMap.get("summary")).thenReturn(customSummary);

        final JmxMetricSummary got = provider.summary("summary");

        assertThat(got).isEqualTo(customSummary);
    }

    private void repeatComputation(int times, Consumer<JmxMetricCollectorProvider> consumer) {
        for (int i = 0; i < times; i++) {
            consumer.accept(provider);
        }
    }
}