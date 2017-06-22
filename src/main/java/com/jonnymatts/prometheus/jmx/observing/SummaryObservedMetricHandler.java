package com.jonnymatts.prometheus.jmx.observing;

import com.jonnymatts.prometheus.jmx.collectors.JmxMetricSummary;

public class SummaryObservedMetricHandler extends ObservedMetricHandler {
    private final String beanName;
    private final String attributeName;
    private final JmxMetricSummary summary;

    public SummaryObservedMetricHandler(String beanName, String attributeName, JmxMetricSummary summary) {
        this.beanName = beanName;
        this.attributeName = attributeName;
        this.summary = summary;
    }

    @Override
    public void accept(Double observedMetric) {
        summary.observe(beanName, attributeName, observedMetric);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SummaryObservedMetricHandler that = (SummaryObservedMetricHandler) o;

        if (beanName != null ? !beanName.equals(that.beanName) : that.beanName != null) return false;
        if (attributeName != null ? !attributeName.equals(that.attributeName) : that.attributeName != null)
            return false;
        return summary != null ? summary.equals(that.summary) : that.summary == null;
    }

    @Override
    public int hashCode() {
        int result = beanName != null ? beanName.hashCode() : 0;
        result = 31 * result + (attributeName != null ? attributeName.hashCode() : 0);
        result = 31 * result + (summary != null ? summary.hashCode() : 0);
        return result;
    }
}