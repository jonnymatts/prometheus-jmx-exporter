package com.jonnymatts.prometheus.jmx.observing;

import com.jonnymatts.prometheus.jmx.collectors.JmxMetricCounter;

public class CounterObservedMetricHandler extends ObservedMetricHandler {
    private final String beanName;
    private final String attributeName;
    private final JmxMetricCounter counter;

    public CounterObservedMetricHandler(String beanName, String attributeName, JmxMetricCounter counter) {
        this.beanName = beanName;
        this.attributeName = attributeName;
        this.counter = counter;
    }

    @Override
    public void accept(Double observedMetric) {
        final double valueToIncreaseBy = observedMetric - counter.get(beanName, attributeName);
        counter.inc(beanName, attributeName, valueToIncreaseBy);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CounterObservedMetricHandler that = (CounterObservedMetricHandler) o;

        if (beanName != null ? !beanName.equals(that.beanName) : that.beanName != null) return false;
        if (attributeName != null ? !attributeName.equals(that.attributeName) : that.attributeName != null)
            return false;
        return counter != null ? counter.equals(that.counter) : that.counter == null;
    }

    @Override
    public int hashCode() {
        int result = beanName != null ? beanName.hashCode() : 0;
        result = 31 * result + (attributeName != null ? attributeName.hashCode() : 0);
        result = 31 * result + (counter != null ? counter.hashCode() : 0);
        return result;
    }
}