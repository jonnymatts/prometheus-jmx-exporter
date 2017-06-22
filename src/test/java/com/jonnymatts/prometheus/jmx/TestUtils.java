package com.jonnymatts.prometheus.jmx;

import com.jonnymatts.prometheus.jmx.collectors.HistogramMetricConfiguration;
import com.jonnymatts.prometheus.jmx.collectors.SummaryMetricConfiguration;
import com.jonnymatts.prometheus.jmx.configuration.*;

import java.util.ArrayList;
import java.util.List;

import static com.jonnymatts.prometheus.jmx.configuration.MetricCollectorType.*;
import static com.jonnymatts.prometheus.jmx.configuration.MetricType.BOOLEAN;
import static com.jonnymatts.prometheus.jmx.configuration.MetricType.NUMBER;
import static java.util.Arrays.asList;

public class TestUtils {

    public final static List<HistogramMetricConfiguration> DEFAULT_HISTOGRAMS = new ArrayList<>();
    public final static List<SummaryMetricConfiguration> DEFAULT_SUMMARIES = new ArrayList<>();
    public final static List<Bean> DEFAULT_BEANS = new ArrayList<>();
    static {
        DEFAULT_HISTOGRAMS.add(
                new HistogramMetricConfiguration(
                        "histogram1",
                        asList(0.001d, 0.01d, 0.1d, 1d),
                        new ExponentialBucketConfiguration(0.001, 10, 5),
                        new LinearBucketConfiguration(0.001, 10, 5)
                )
        );
        DEFAULT_SUMMARIES.add(
                new SummaryMetricConfiguration(
                        "summary1",
                        asList(
                                new QuantileConfiguration(0.001d, 1d),
                                new QuantileConfiguration(0.01d, 2d),
                                new QuantileConfiguration(0.1d, 3d),
                                new QuantileConfiguration(1d, 4d)
                        ),
                        10,
                        100L

                )
        );
        DEFAULT_BEANS.addAll(
                asList(
                        new Bean(
                                "java.lang:type=Threading",
                                asList(
                                        new BeanAttribute(
                                                "peakThreadCount",
                                                NUMBER,
                                                new BeanAttributeMetricCollectorReference(GAUGE)
                                        ),
                                        new BeanAttribute(
                                                "totalStartedThreadCount",
                                                NUMBER,
                                                new BeanAttributeMetricCollectorReference(GAUGE, null)
                                        )
                                )
                        ),
                        new Bean(
                                "com.jonnymatts:type=Blah",
                                asList(
                                        new BeanAttribute(
                                                "histogram",
                                                NUMBER,
                                                new BeanAttributeMetricCollectorReference(HISTOGRAM, "histogram1")
                                        ),
                                        new BeanAttribute(
                                                "counter",
                                                NUMBER,
                                                new BeanAttributeMetricCollectorReference(COUNTER, null)
                                        ),
                                        new BeanAttribute(
                                                "summary",
                                                NUMBER,
                                                new BeanAttributeMetricCollectorReference(SUMMARY, "summary1")
                                        ),
                                        new BeanAttribute(
                                                "boolean",
                                                BOOLEAN,
                                                new BeanAttributeMetricCollectorReference(GAUGE, null)
                                        )
                                )
                        )
                )
        );
    }
}