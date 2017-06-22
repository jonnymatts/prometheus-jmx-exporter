package com.jonnymatts.prometheus.jmx;

import com.jonnymatts.prometheus.jmx.collectors.JmxMetricCollectorProvider;
import com.jonnymatts.prometheus.jmx.collectors.JmxMetricHistogram;
import com.jonnymatts.prometheus.jmx.collectors.JmxMetricSummary;
import com.jonnymatts.prometheus.jmx.configuration.Bean;
import com.jonnymatts.prometheus.jmx.configuration.BeanAttribute;
import com.jonnymatts.prometheus.jmx.configuration.BeanAttributeMetricCollectorReference;
import com.jonnymatts.prometheus.jmx.observing.*;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

public class JmxMetricHandler {

    private final MBeanServer mBeanServer;
    private final JmxMetricCollectorProvider jmxMetricCollectorProvider;
    private final ArrayList<JmxMetricCollector> collectors;

    public JmxMetricHandler(JmxMetricCollectorProvider jmxMetricCollectorProvider) {
        this(ManagementFactory.getPlatformMBeanServer(), jmxMetricCollectorProvider, new ArrayList<>());
    }

    public JmxMetricHandler(MBeanServer mBeanServer,
                            JmxMetricCollectorProvider jmxMetricCollectorProvider,
                            ArrayList<JmxMetricCollector> collectors) {
        this.mBeanServer = mBeanServer;
        this.jmxMetricCollectorProvider = jmxMetricCollectorProvider;
        this.collectors = collectors;
    }

    public void register(List<Bean> beans) {
        beans.forEach(this::registerMetricCollectorsForBean);
    }

    private void registerMetricCollectorsForBean(Bean bean) {
        final List<BeanAttribute> attributes = bean.getAttributes();
        attributes.forEach(attribute -> registerMetricCollectorsForBean(bean, attribute));
    }

    private void registerMetricCollectorsForBean(Bean bean, BeanAttribute attribute) {
        final ObservedMetricHandler observedMetricHandler = getObservedMetricHandler(bean.getName(), attribute);
        collectors.add(new JmxMetricCollector(bean, attribute, observedMetricHandler));
    }

    private ObservedMetricHandler getObservedMetricHandler(String beanName, BeanAttribute attribute) {
        final BeanAttributeMetricCollectorReference reference = attribute.getCollectorReference();
        switch (reference.getType()) {
            case COUNTER:
                return new CounterObservedMetricHandler(beanName, attribute.getName(), jmxMetricCollectorProvider.counter());
            case GAUGE:
                return new GaugeObservedMetricHandler(beanName, attribute.getName(), jmxMetricCollectorProvider.gauge());
            case HISTOGRAM:
                final JmxMetricHistogram histogram = reference.getName() == null ? jmxMetricCollectorProvider.histogram() : jmxMetricCollectorProvider.histogram(reference.getName());
                return new HistogramObservedMetricHandler(beanName, attribute.getName(), histogram);
            default:
                final JmxMetricSummary summary = reference.getName() == null ? jmxMetricCollectorProvider.summary() : jmxMetricCollectorProvider.summary(reference.getName());
                return new SummaryObservedMetricHandler(beanName, attribute.getName(), summary);
        }
    }

    public void handle() {
        collectors.forEach(collector -> {
            final double observedValue = getObservedValue(collector.getBean(), collector.getAttribute());
            collector.getConsumer().accept(observedValue);
        });
    }

    private double getObservedValue(Bean bean, BeanAttribute attribute) {
        try {
            final Object observedValue = mBeanServer.getAttribute(new ObjectName(bean.getName()), attribute.getName());
            switch (attribute.getType()) {
                case NUMBER:
                    return ((Number) observedValue).doubleValue();
                default:
                    return observedValue.equals("true") ? 1d : 0d;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}