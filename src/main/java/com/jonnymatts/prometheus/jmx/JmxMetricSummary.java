package com.jonnymatts.prometheus.jmx;

import io.prometheus.client.Collector;
import io.prometheus.client.Summary;

import java.util.List;

class JmxMetricSummary {
    private final Summary summary;

    public JmxMetricSummary() {
        this.summary = Summary.build()
                .name("jmx_metric_summary")
                .help("JMX metrics backed by a summary")
                .labelNames("bean_name", "attribute_name")
                .create()
                .register();
    }

    public JmxMetricSummary(Summary summary) {
        this.summary = summary;
    }

    public Summary.Child labels(String... labels) {
        return summary.labels(labels);
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
        return summary.collect();
    }

    public List<Collector.MetricFamilySamples> describe() {
        return summary.describe();
    }
}