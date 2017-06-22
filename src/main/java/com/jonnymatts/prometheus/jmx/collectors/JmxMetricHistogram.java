package com.jonnymatts.prometheus.jmx.collectors;

import com.jonnymatts.prometheus.jmx.configuration.ExponentialBucketConfiguration;
import com.jonnymatts.prometheus.jmx.configuration.LinearBucketConfiguration;
import io.prometheus.client.Collector;
import io.prometheus.client.Histogram;

import java.util.List;

public class JmxMetricHistogram {
    private final Histogram histogram;

    public JmxMetricHistogram() {
        this(new HistogramMetricConfiguration(null, null, null, null));
    }

    public JmxMetricHistogram(HistogramMetricConfiguration configuration) {
        this(Histogram.build(), configuration);
    }

    public JmxMetricHistogram(Histogram.Builder builder,
                              HistogramMetricConfiguration configuration) {
        final String histogramBaseName = "jmx_metric_histogram";
        final String histogramName = configuration.getName() != null ?
                histogramBaseName + "_" + configuration.getName() :
                histogramBaseName;

        final List<Double> configurationBuckets = configuration.getBuckets();
        if(configurationBuckets != null) {
            configureBucketsForBuilder(builder, configurationBuckets);
        }

        final ExponentialBucketConfiguration exponentialBucketConfiguration = configuration.getExponentialBuckets();
        if(exponentialBucketConfiguration != null) {
            builder.exponentialBuckets(
                    exponentialBucketConfiguration.getStart(),
                    exponentialBucketConfiguration.getFactor(),
                    exponentialBucketConfiguration.getCount()
            );
        }

        final LinearBucketConfiguration linearBucketConfiguration = configuration.getLinearBuckets();
        if(linearBucketConfiguration != null) {
            builder.linearBuckets(
                    linearBucketConfiguration.getStart(),
                    linearBucketConfiguration.getWidth(),
                    linearBucketConfiguration.getCount()
            );
        }

        builder.name(histogramName)
                .help("JMX metrics backed by a histogram")
                .labelNames("bean_name", "attribute_name");

        this.histogram = builder
                .create();
    }

    public JmxMetricHistogram(Histogram histogram) {
        this.histogram = histogram;
    }

    public Histogram.Child labels(String... labels) {
        return histogram.labels(labels);
    }

    public void startTimer(String beanName, String attributeName) {
        labels(beanName, attributeName).startTimer();
    }

    public void time(String beanName, String attributeName, Runnable runnable) {
        labels(beanName, attributeName).time(runnable);
    }

    public void observe(String beanName, String attributeName, double amount) {
        labels(beanName, attributeName).observe(amount);
    }

    public List<Collector.MetricFamilySamples> collect() {
        return histogram.collect();
    }

    public List<Collector.MetricFamilySamples> describe() {
        return histogram.describe();
    }

    public JmxMetricHistogram register() {
        histogram.register();
        return this;
    }

    private void configureBucketsForBuilder(Histogram.Builder builder, List<Double> configurationBuckets) {
        builder.buckets(
                configurationBuckets.stream()
                        .mapToDouble(Double::doubleValue)
                        .toArray()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JmxMetricHistogram that = (JmxMetricHistogram) o;

        return histogram != null ? histogram.equals(that.histogram) : that.histogram == null;
    }

    @Override
    public int hashCode() {
        return histogram != null ? histogram.hashCode() : 0;
    }
}