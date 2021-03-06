package com.jonnymatts.prometheus.jmx.verification;

import static java.lang.String.format;

public class MBeanDoesNotExistException extends MBeanVerificationException {
    public MBeanDoesNotExistException(String beanName) {
        super(format("MBean '%s' does not exist", beanName));
    }
}