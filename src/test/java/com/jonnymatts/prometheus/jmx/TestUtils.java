package com.jonnymatts.prometheus.jmx;

import com.jonnymatts.prometheus.jmx.configuration.Bean;
import com.jonnymatts.prometheus.jmx.configuration.BeanAttribute;

import java.util.ArrayList;
import java.util.List;

import static com.jonnymatts.prometheus.jmx.configuration.MetricCollector.*;
import static com.jonnymatts.prometheus.jmx.configuration.MetricType.BOOLEAN;
import static com.jonnymatts.prometheus.jmx.configuration.MetricType.NUMBER;
import static java.util.Arrays.asList;

public class TestUtils {

    public final static List<Bean> DEFAULT_BEANS = new ArrayList<>();
    static {
        DEFAULT_BEANS.addAll(
                asList(
                        new Bean(
                                "java.lang:type=Threading",
                                asList(
                                        new BeanAttribute(
                                                "peakThreadCount",
                                                NUMBER,
                                                GAUGE
                                        ),
                                        new BeanAttribute(
                                                "totalStartedThreadCount",
                                                NUMBER,
                                                GAUGE
                                        )
                                )
                        ),
                        new Bean(
                                "com.jonnymatts:type=Blah",
                                asList(
                                        new BeanAttribute(
                                                "histogram",
                                                NUMBER,
                                                HISTOGRAM
                                        ),
                                        new BeanAttribute(
                                                "counter",
                                                NUMBER,
                                                COUNTER
                                        ),
                                        new BeanAttribute(
                                                "summary",
                                                NUMBER,
                                                SUMMARY
                                        ),
                                        new BeanAttribute(
                                                "boolean",
                                                BOOLEAN,
                                                GAUGE
                                        )
                                )
                        )
                )
        );
    }
}