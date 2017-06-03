package com.jonnymatts.prometheus.jmx;

import io.prometheus.client.Collector;
import io.prometheus.client.Histogram;

import java.util.List;

class JmxMetricHistogram {
    private final Histogram histogram;

    public JmxMetricHistogram() {
        this.histogram = Histogram.build()
                .name("jmx_metric_histogram")
                .help("JMX metrics backed by a histogram")
                .labelNames("bean_name", "attribute_name")
                .create()
                .register();
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
}