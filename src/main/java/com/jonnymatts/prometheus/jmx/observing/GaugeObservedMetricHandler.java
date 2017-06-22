package com.jonnymatts.prometheus.jmx.observing;

import com.jonnymatts.prometheus.jmx.collectors.JmxMetricGauge;

public class GaugeObservedMetricHandler extends ObservedMetricHandler {
    private final String beanName;
    private final String attributeName;
    private final JmxMetricGauge gauge;

    public GaugeObservedMetricHandler(String beanName, String attributeName, JmxMetricGauge gauge) {
        this.beanName = beanName;
        this.attributeName = attributeName;
        this.gauge = gauge;
    }

    @Override
    public void accept(Double observedMetric) {
        gauge.set(beanName, attributeName, observedMetric);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GaugeObservedMetricHandler that = (GaugeObservedMetricHandler) o;

        if (beanName != null ? !beanName.equals(that.beanName) : that.beanName != null) return false;
        if (attributeName != null ? !attributeName.equals(that.attributeName) : that.attributeName != null)
            return false;
        return gauge != null ? gauge.equals(that.gauge) : that.gauge == null;
    }

    @Override
    public int hashCode() {
        int result = beanName != null ? beanName.hashCode() : 0;
        result = 31 * result + (attributeName != null ? attributeName.hashCode() : 0);
        result = 31 * result + (gauge != null ? gauge.hashCode() : 0);
        return result;
    }
}