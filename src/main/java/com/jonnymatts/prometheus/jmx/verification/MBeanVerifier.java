package com.jonnymatts.prometheus.jmx.verification;

import com.jonnymatts.prometheus.jmx.configuration.Bean;
import com.jonnymatts.prometheus.jmx.configuration.BeanAttribute;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class MBeanVerifier {
    private final MBeanServer mBeanServer;

    public MBeanVerifier() {
        this.mBeanServer = ManagementFactory.getPlatformMBeanServer();
    }

    public MBeanVerifier(MBeanServer mBeanServer) {
        this.mBeanServer = mBeanServer;
    }

    public void verify(Bean bean) {
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

            bean.getAttributes().forEach(attribute -> verifyAttribute(attribute, bean, foundAttributes));
        } catch (MBeanVerificationException e) {
            throw e;
        } catch (Exception e) {
            throw new MBeanVerificationException(format("Exception occurred verifying MBean '%s'", bean.getName()), e);
        }
    }

    private void verifyAttribute(BeanAttribute attribute, Bean bean, List<String> foundAttributes) {
        final boolean attributeDoesNotExist = foundAttributes.stream()
                .noneMatch(info -> info.equals(attribute.getName()));
        if (attributeDoesNotExist)
            throw new MBeanAttributeDoesNotExistException(bean.getName(), attribute.getName());
    }
}