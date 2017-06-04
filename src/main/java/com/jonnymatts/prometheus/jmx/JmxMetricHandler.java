package com.jonnymatts.prometheus.jmx;

import com.jonnymatts.prometheus.jmx.collectors.*;
import com.jonnymatts.prometheus.jmx.configuration.Bean;
import com.jonnymatts.prometheus.jmx.configuration.BeanAttribute;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.function.Supplier;

public class JmxMetricHandler {

    private final MBeanServer mBeanServer;
    private final Supplier<JmxMetricCounter> counterSupplier;
    private final Supplier<JmxMetricGauge> gaugeSupplier;
    private final Supplier<JmxMetricHistogram> histogramSupplier;
    private final Supplier<JmxMetricSummary> summarySupplier;

    public JmxMetricHandler() {
        this.mBeanServer = ManagementFactory.getPlatformMBeanServer();
        this.counterSupplier = JmxMetricCollectors::counter;
        this.gaugeSupplier = JmxMetricCollectors::gauge;
        this.histogramSupplier = JmxMetricCollectors::histogram;
        this.summarySupplier = JmxMetricCollectors::summary;
    }

    public JmxMetricHandler(MBeanServer mBeanServer,
                            Supplier<JmxMetricCounter> counterSupplier,
                            Supplier<JmxMetricGauge> gaugeSupplier,
                            Supplier<JmxMetricHistogram> histogramSupplier,
                            Supplier<JmxMetricSummary> summarySupplier) {

        this.mBeanServer = mBeanServer;
        this.counterSupplier = counterSupplier;
        this.gaugeSupplier = gaugeSupplier;
        this.histogramSupplier = histogramSupplier;
        this.summarySupplier = summarySupplier;
    }

    public void handle(List<Bean> beans) {
        beans.forEach(bean -> {
            final List<BeanAttribute> attributes = bean.getAttributes();
            attributes.forEach(attribute -> {
                double observedValue = getObservedValue(bean, attribute);
                switch (attribute.getCollector()) {
                    case COUNTER:
                        handleCounterMetric(bean, attribute, observedValue);
                        break;
                    case GAUGE:
                        handleGaugeMetric(bean, attribute, observedValue);
                        break;
                    case HISTOGRAM:
                        handleHistogramMetric(bean, attribute, observedValue);
                        break;
                    case SUMMARY:
                        handleSummaryMetric(bean, attribute, observedValue);
                        break;
                }
            });
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

    private void handleCounterMetric(Bean bean, BeanAttribute attribute, double observedValue) {
        final JmxMetricCounter jmxMetricCounter = counterSupplier.get();
        final double valueToIncreaseBy = observedValue - jmxMetricCounter.get(bean.getName(), attribute.getName());
        jmxMetricCounter.inc(bean.getName(), attribute.getName(), valueToIncreaseBy);
    }

    private void handleGaugeMetric(Bean bean, BeanAttribute attribute, double observedValue) {
        final JmxMetricGauge jmxMetricGauge = gaugeSupplier.get();
        jmxMetricGauge.set(bean.getName(), attribute.getName(), observedValue);
    }

    private void handleHistogramMetric(Bean bean, BeanAttribute attribute, double observedValue) {
        final JmxMetricHistogram jmxMetricHistogram = histogramSupplier.get();
        jmxMetricHistogram.observe(bean.getName(), attribute.getName(), observedValue);
    }

    private void handleSummaryMetric(Bean bean, BeanAttribute attribute, double observedValue) {
        final JmxMetricSummary jmxMetricSummary = summarySupplier.get();
        jmxMetricSummary.observe(bean.getName(), attribute.getName(), observedValue);
    }
}