package com.jonnymatts.prometheus.jmx.verification;

import static java.lang.String.format;

public class MetricCollectorDoesNotExistException extends MBeanVerificationException {
    public MetricCollectorDoesNotExistException(String collectorName) {
        super(format("Metric collector '%s' does not exist", collectorName));
    }
}