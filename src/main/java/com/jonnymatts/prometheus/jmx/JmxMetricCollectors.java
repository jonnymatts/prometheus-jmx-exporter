package com.jonnymatts.prometheus.jmx;

public class JmxMetricCollectors {

    private static JmxMetricCounter jmxMetricCounter;
    private static JmxMetricGauge jmxMetricGauge;
    private static JmxMetricHistogram jmxMetricHistogram;
    private static JmxMetricSummary jmxMetricSummary;

    public static JmxMetricCounter counter() {
        if(jmxMetricCounter == null)
            createJmxMetricCounter();
        return jmxMetricCounter;
    }

    public static JmxMetricGauge gauge() {
        if(jmxMetricGauge == null)
            createJmxMetricGauge();
        return jmxMetricGauge;
    }

    public static JmxMetricHistogram histogram() {
        if(jmxMetricHistogram == null)
            createJmxMetricHistogram();
        return jmxMetricHistogram;
    }

    public static JmxMetricSummary summary() {
        if(jmxMetricSummary == null)
            createJmxMetricSummary();
        return jmxMetricSummary;
    }

    private static void createJmxMetricCounter() {
        jmxMetricCounter = new JmxMetricCounter();
    }

    private static void createJmxMetricGauge() {
        jmxMetricGauge = new JmxMetricGauge();
    }

    private static void createJmxMetricHistogram() {
        jmxMetricHistogram = new JmxMetricHistogram();
    }

    private static void createJmxMetricSummary() {
        jmxMetricSummary = new JmxMetricSummary();
    }
}