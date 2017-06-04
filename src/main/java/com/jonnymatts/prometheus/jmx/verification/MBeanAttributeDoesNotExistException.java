package com.jonnymatts.prometheus.jmx.verification;

import static java.lang.String.format;

public class MBeanAttributeDoesNotExistException extends MBeanVerificationException {
    public MBeanAttributeDoesNotExistException(String beanName, String attributeName) {
        super(format("MBean attribute '%s' from MBean '%s' does not exist", attributeName, beanName));
    }
}