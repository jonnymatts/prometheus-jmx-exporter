package com.jonnymatts.prometheus.jmx;

import com.jonnymatts.prometheus.jmx.configuration.Bean;
import com.jonnymatts.prometheus.jmx.configuration.BeanAttribute;
import com.jonnymatts.prometheus.jmx.observing.ObservedMetricHandler;

public class JmxMetricCollector {
    private final Bean beanName;
    private final BeanAttribute attributeName;
    private ObservedMetricHandler consumer;

    public JmxMetricCollector(Bean beanName, BeanAttribute attributeName, ObservedMetricHandler metricHandler) {
        this.beanName = beanName;
        this.attributeName = attributeName;
        this.consumer = metricHandler;
    }

    public Bean getBean() {
        return beanName;
    }

    public BeanAttribute getAttribute() {
        return attributeName;
    }

    public ObservedMetricHandler getConsumer() {
        return consumer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JmxMetricCollector that = (JmxMetricCollector) o;

        if (beanName != null ? !beanName.equals(that.beanName) : that.beanName != null) return false;
        if (attributeName != null ? !attributeName.equals(that.attributeName) : that.attributeName != null)
            return false;
        return consumer != null ? consumer.equals(that.consumer) : that.consumer == null;
    }

    @Override
    public int hashCode() {
        int result = beanName != null ? beanName.hashCode() : 0;
        result = 31 * result + (attributeName != null ? attributeName.hashCode() : 0);
        result = 31 * result + (consumer != null ? consumer.hashCode() : 0);
        return result;
    }
}