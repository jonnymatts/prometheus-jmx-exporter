package com.jonnymatts.prometheus.jmx.collectors;

import com.jonnymatts.prometheus.jmx.configuration.QuantileConfiguration;
import io.prometheus.client.Collector;
import io.prometheus.client.Summary;
import io.prometheus.client.Summary.Builder;

import java.util.List;

public class JmxMetricSummary {
    private final Summary summary;

    public JmxMetricSummary() {
        this(new SummaryMetricConfiguration(null, null, null, null));
    }

    public JmxMetricSummary(SummaryMetricConfiguration configuration) {
        this(Summary.build(), configuration);
    }

    public JmxMetricSummary(Builder builder,
                            SummaryMetricConfiguration configuration) {

        final String summaryBaseName = "jmx_metric_summary";
        final String summaryName = configuration.getName() != null ?
                summaryBaseName + "_" + configuration.getName() :
                summaryBaseName;

        builder.name(summaryName)
                .help("JMX metrics backed by a summary")
                .labelNames("bean_name", "attribute_name");

        final List<QuantileConfiguration> quantiles = configuration.getQuantiles();
        if(quantiles != null) {
            quantiles.forEach(q -> builder.quantile(q.getQuantile(), q.getError()));
        }

        final Integer ageBuckets = configuration.getAgeBuckets();
        if(ageBuckets != null) {
            builder.ageBuckets(ageBuckets);
        }

        final Long maxAgesSeconds = configuration.getMaxAgeSeconds();
        if(maxAgesSeconds != null) {
            builder.maxAgeSeconds(maxAgesSeconds);
        }

        this.summary = builder.create();
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

    public JmxMetricSummary register() {
        summary.register();
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JmxMetricSummary that = (JmxMetricSummary) o;

        return summary != null ? summary.equals(that.summary) : that.summary == null;
    }

    @Override
    public int hashCode() {
        return summary != null ? summary.hashCode() : 0;
    }
}