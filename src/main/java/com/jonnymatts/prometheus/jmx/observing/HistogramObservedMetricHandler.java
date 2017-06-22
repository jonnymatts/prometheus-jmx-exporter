package com.jonnymatts.prometheus.jmx.observing;

import com.jonnymatts.prometheus.jmx.collectors.JmxMetricHistogram;

public class HistogramObservedMetricHandler extends ObservedMetricHandler {
    private final String beanName;
    private final String attributeName;
    private final JmxMetricHistogram histogram;

    public HistogramObservedMetricHandler(String beanName, String attributeName, JmxMetricHistogram histogram) {
        this.beanName = beanName;
        this.attributeName = attributeName;
        this.histogram = histogram;
    }

    @Override
    public void accept(Double observedMetric) {
        histogram.observe(beanName, attributeName, observedMetric);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HistogramObservedMetricHandler that = (HistogramObservedMetricHandler) o;

        if (beanName != null ? !beanName.equals(that.beanName) : that.beanName != null) return false;
        if (attributeName != null ? !attributeName.equals(that.attributeName) : that.attributeName != null)
            return false;
        return histogram != null ? histogram.equals(that.histogram) : that.histogram == null;
    }

    @Override
    public int hashCode() {
        int result = beanName != null ? beanName.hashCode() : 0;
        result = 31 * result + (attributeName != null ? attributeName.hashCode() : 0);
        result = 31 * result + (histogram != null ? histogram.hashCode() : 0);
        return result;
    }
}