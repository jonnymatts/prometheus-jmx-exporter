package com.jonnymatts.prometheus.jmx;

import java.util.ArrayList;
import java.util.List;

import static com.jonnymatts.prometheus.jmx.MetricType.*;
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
                                                GAUGE
                                        ),
                                        new BeanAttribute(
                                                "totalStartedThreadCount",
                                                GAUGE
                                        )
                                )
                        ),
                        new Bean(
                                "com.jonnymatts:type=Blah",
                                asList(
                                        new BeanAttribute(
                                                "histogram",
                                                HISTOGRAM
                                        ),
                                        new BeanAttribute(
                                                "counter",
                                                COUNTER
                                        ),
                                        new BeanAttribute(
                                                "summary",
                                                SUMMARY
                                        )
                                )
                        )
                )
        );
    }
}