package com.jonnymatts.prometheus.jmx;

public class MBeanVerificationException extends RuntimeException {
    public MBeanVerificationException(String message) {
        super(message);
    }

    public MBeanVerificationException(String message, Throwable cause) {
        super(message, cause);
    }
}