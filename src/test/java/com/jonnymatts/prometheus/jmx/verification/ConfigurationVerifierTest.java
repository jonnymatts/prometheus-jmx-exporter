package com.jonnymatts.prometheus.jmx.verification;

import com.jonnymatts.prometheus.jmx.configuration.Bean;
import com.jonnymatts.prometheus.jmx.configuration.BeanAttribute;
import com.jonnymatts.prometheus.jmx.configuration.BeanAttributeMetricCollectorReference;
import com.jonnymatts.prometheus.jmx.configuration.Configuration;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.management.*;
import java.util.stream.Stream;

import static com.jonnymatts.prometheus.jmx.TestUtils.DEFAULT_BEANS;
import static com.jonnymatts.prometheus.jmx.configuration.MetricCollectorType.HISTOGRAM;
import static com.jonnymatts.prometheus.jmx.configuration.MetricCollectorType.SUMMARY;
import static com.jonnymatts.prometheus.jmx.configuration.MetricType.NUMBER;
import static java.util.Collections.emptySet;
import static java.util.Collections.singletonList;
import static org.assertj.core.util.Sets.newHashSet;
import static org.hamcrest.CoreMatchers.isA;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConfigurationVerifierTest {

    @Rule public ExpectedException expectedException = ExpectedException.none();

    @Mock private MBeanServer mBeanServer;
    @Mock private Configuration configuration;
    @Mock private MBeanInfo threadingMBeanInfo;
    @Mock private MBeanAttributeInfo peakThreadCountMBeanAttributeInfo;
    @Mock private MBeanAttributeInfo totalStartedThreadCountMBeanAttributeInfo;

    private ConfigurationVerifier configurationVerifier;

    @Before
    public void setUp() throws Exception {
        configurationVerifier = new ConfigurationVerifier(mBeanServer);

        when(configuration.getBeans()).thenReturn(singletonList(DEFAULT_BEANS.get(0)));
        when(mBeanServer.queryMBeans(null, null)).thenReturn(newHashSet(singletonList(
                new ObjectInstance("java.lang:type=Threading", "sun.management.ThreadImpl")
        )));
        when(mBeanServer.getMBeanInfo(new ObjectName("java.lang:type=Threading"))).thenReturn(threadingMBeanInfo);
        when(threadingMBeanInfo.getAttributes()).thenReturn(
                Stream.of(peakThreadCountMBeanAttributeInfo, totalStartedThreadCountMBeanAttributeInfo)
                        .toArray(MBeanAttributeInfo[]::new)
        );
        when(peakThreadCountMBeanAttributeInfo.getName()).thenReturn("peakThreadCount");
        when(totalStartedThreadCountMBeanAttributeInfo.getName()).thenReturn("totalStartedThreadCount");
    }

    @Test
    public void verifyThrowsExceptionIfMBeanDoesNotExist() throws Exception {
        when(mBeanServer.queryMBeans(null, null)).thenReturn(emptySet());

        expectedException.expect(MBeanDoesNotExistException.class);
        expectedException.expectMessage("MBean");
        expectedException.expectMessage("not exist");
        expectedException.expectMessage("Threading");

        configurationVerifier.verify(configuration);
    }

    @Test
    public void verifyThrowsExceptionIfAttributeDoesNotExistForMBean() throws Exception {
        when(threadingMBeanInfo.getAttributes()).thenReturn(
                Stream.of(peakThreadCountMBeanAttributeInfo)
                        .toArray(MBeanAttributeInfo[]::new)
        );

        expectedException.expect(MBeanAttributeDoesNotExistException.class);
        expectedException.expectMessage("MBean attribute");
        expectedException.expectMessage("not exist");
        expectedException.expectMessage("Threading");
        expectedException.expectMessage("totalStartedThreadCount");

        configurationVerifier.verify(configuration);
    }

    @Test
    public void verifyThrowsExceptionIfAnyOtherExceptionIsThrown() throws Exception {
        when(mBeanServer.queryMBeans(null, null)).thenThrow(new RuntimeException());

        expectedException.expect(MBeanVerificationException.class);
        expectedException.expectCause(isA(RuntimeException.class));
        expectedException.expectMessage("Exception occurred");

        configurationVerifier.verify(configuration);
    }

    @Test
    public void verifyDoesNotThrowExceptionIfMBeanAndItsAttributesSpecifiedInConfigurationExist() throws Exception {
        configurationVerifier.verify(configuration);
    }

    @Test
    public void verifyThrowsExceptionIfHistogramMetricCollectorReferencedDoesNotExist() throws Exception {
        final Bean bean = new Bean("java.lang:type=Threading", singletonList(
                new BeanAttribute(
                        "peakThreadCount",
                        NUMBER,
                        new BeanAttributeMetricCollectorReference(HISTOGRAM, "histogram1")
                )
        ));
        when(configuration.getBeans()).thenReturn(singletonList(bean));

        expectedException.expect(MetricCollectorDoesNotExistException.class);
        expectedException.expectMessage("histogram1");

        configurationVerifier.verify(configuration);
    }

    @Test
    public void verifyThrowsExceptionIfSummaryMetricCollectorReferencedDoesNotExist() throws Exception {
        final Bean bean = new Bean("java.lang:type=Threading", singletonList(
                new BeanAttribute(
                        "peakThreadCount",
                        NUMBER,
                        new BeanAttributeMetricCollectorReference(SUMMARY, "summary1")
                )
        ));
        when(configuration.getBeans()).thenReturn(singletonList(bean));

        expectedException.expect(MetricCollectorDoesNotExistException.class);
        expectedException.expectMessage("summary1");

        configurationVerifier.verify(configuration);
    }
}