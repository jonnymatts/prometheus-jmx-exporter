package com.jonnymatts.prometheus.jmx.verification;

import com.jonnymatts.prometheus.jmx.collectors.HistogramMetricConfiguration;
import com.jonnymatts.prometheus.jmx.collectors.SummaryMetricConfiguration;
import com.jonnymatts.prometheus.jmx.configuration.Bean;
import com.jonnymatts.prometheus.jmx.configuration.BeanAttribute;
import com.jonnymatts.prometheus.jmx.configuration.BeanAttributeMetricCollectorReference;
import com.jonnymatts.prometheus.jmx.configuration.Configuration;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class ConfigurationVerifier {
    private final MBeanServer mBeanServer;

    public ConfigurationVerifier() {
        this.mBeanServer = ManagementFactory.getPlatformMBeanServer();
    }

    public ConfigurationVerifier(MBeanServer mBeanServer) {
        this.mBeanServer = mBeanServer;
    }

    public void verify(Configuration configuration) {
        configuration.getBeans()
                .forEach(bean -> verifyBean(bean, configuration));
    }

    private void verifyBean(Bean bean, Configuration configuration) {
        try {
            final Set<ObjectInstance> objectInstances = mBeanServer.queryMBeans(null, null);

            final boolean mBeanDoesNotExist = objectInstances
                    .stream()
                    .noneMatch(instance -> instance.getObjectName().toString().equals(bean.getName()));

            if (mBeanDoesNotExist)
                throw new MBeanDoesNotExistException(bean.getName());

            final MBeanInfo mBeanInfo = mBeanServer.getMBeanInfo(new ObjectName(bean.getName()));
            final List<String> foundAttributes = stream(mBeanInfo.getAttributes())
                    .map(MBeanAttributeInfo::getName)
                    .collect(toList());

            bean.getAttributes().forEach(attribute -> verifyAttribute(attribute, bean, foundAttributes, configuration));
        } catch (MBeanVerificationException e) {
            throw e;
        } catch (Exception e) {
            throw new MBeanVerificationException(format("Exception occurred verifying MBean '%s'", bean.getName()), e);
        }
    }

    private void verifyAttribute(BeanAttribute attribute, Bean bean, List<String> foundAttributes, Configuration configuration) {
        final boolean attributeDoesNotExist = foundAttributes.stream()
                .noneMatch(info -> info.equals(attribute.getName()));

        if (attributeDoesNotExist)
            throw new MBeanAttributeDoesNotExistException(bean.getName(), attribute.getName());

        final String metricCollectorName = attribute.getCollectorReference().getName();
        if(metricCollectorName != null) {
            verifyMetricCollectorReference(attribute.getCollectorReference(), configuration);
        }
    }

    private void verifyMetricCollectorReference(BeanAttributeMetricCollectorReference reference, Configuration configuration) {
        final String referenceName = reference.getName();
        switch (reference.getType()) {
            case HISTOGRAM:
                final List<String> histogramNames = configuration.getHistograms().stream()
                        .map(HistogramMetricConfiguration::getName)
                        .collect(toList());
                if(!histogramNames.contains(referenceName))
                    throw new MetricCollectorDoesNotExistException(referenceName);
                break;
            case SUMMARY:
                final List<String> summaryNames = configuration.getSummaries().stream()
                        .map(SummaryMetricConfiguration::getName)
                        .collect(toList());
                if(!summaryNames.contains(referenceName))
                    throw new MetricCollectorDoesNotExistException(referenceName);
                break;
            default:
                break;
        }
    }
}