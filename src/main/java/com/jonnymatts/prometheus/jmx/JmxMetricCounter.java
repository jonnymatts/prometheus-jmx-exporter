package com.jonnymatts.prometheus.jmx;

import io.prometheus.client.Collector;
import io.prometheus.client.Counter;
import io.prometheus.client.Counter.Child;

import java.util.List;

class JmxMetricCounter {
    private final Counter counter;

    public JmxMetricCounter() {
        this.counter = Counter.build()
                .name("jmx_metric_counter")
                .help("JMX metrics backed by a counter")
                .labelNames("bean_name", "attribute_name")
                .create()
                .register();
    }

    public JmxMetricCounter(Counter counter) {
        this.counter = counter;
    }

    public void inc(String beanName, String attributeName) {
        inc(beanName, attributeName, 1);
    }

    public void inc(String beanName, String attributeName, double amount) {
        labels(beanName, attributeName).inc(amount);
    }

    public double get(String beanName, String attributeName) {
        return labels(beanName, attributeName).get();
    }

    public Child labels(String... labels) {
        return counter.labels(labels);
    }

    public List<Collector.MetricFamilySamples> collect() {
        return counter.collect();
    }

    public List<Collector.MetricFamilySamples> describe() {
        return counter.describe();
    }
}