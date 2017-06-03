package com.jonnymatts.prometheus.jmx;

import io.prometheus.client.Collector;
import io.prometheus.client.Gauge;
import io.prometheus.client.Gauge.Child;

import java.util.List;

class JmxMetricGauge {
    private final Gauge gauge;

    public JmxMetricGauge() {
        this.gauge = Gauge.build()
                .name("jmx_metric_gauge")
                .help("JMX metrics backed by a gauge")
                .labelNames("bean_name", "attribute_name")
                .create()
                .register();
    }

    public JmxMetricGauge(Gauge gauge) {
        this.gauge = gauge;
    }

    public void inc(String beanName, String attributeName) {
        inc(beanName, attributeName, 1);
    }

    public void inc(String beanName, String attributeName, double amount) {
        labels(beanName, attributeName).inc(amount);
    }

    public void dec(String beanName, String attributeName) {
        dec(beanName, attributeName, 1);
    }

    public void dec(String beanName, String attributeName, double amount) {
        labels(beanName, attributeName).dec(amount);
    }

    public void set(String beanName, String attributeName, double amount) {
        labels(beanName, attributeName).set(amount);
    }

    public Child labels(String... labels) {
        return gauge.labels(labels);
    }

    public List<Collector.MetricFamilySamples> collect() {
        return gauge.collect();
    }

    public List<Collector.MetricFamilySamples> describe() {
        return gauge.describe();
    }
}