package com.jonnymatts.prometheus.jmx;

import com.jonnymatts.prometheus.jmx.configuration.Bean;

import java.util.List;

public class HandleMetricsRunner implements Runnable {
    private final JmxMetricHandler metricHandler;
    private final List<Bean> beans;

    public HandleMetricsRunner(JmxMetricHandler metricHandler, List<Bean> beans) {
        this.metricHandler = metricHandler;
        this.beans = beans;
    }

    @Override
    public void run() {
        metricHandler.handle(beans);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HandleMetricsRunner that = (HandleMetricsRunner) o;

        return beans != null ? beans.equals(that.beans) : that.beans == null;
    }

    @Override
    public int hashCode() {
        return beans != null ? beans.hashCode() : 0;
    }
}