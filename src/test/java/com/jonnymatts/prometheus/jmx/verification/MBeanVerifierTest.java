package com.jonnymatts.prometheus.jmx.verification;

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
import static java.util.Collections.emptySet;
import static java.util.Collections.singletonList;
import static org.assertj.core.util.Sets.newHashSet;
import static org.hamcrest.CoreMatchers.isA;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MBeanVerifierTest {

    @Rule public ExpectedException expectedException = ExpectedException.none();

    @Mock private MBeanServer mBeanServer;
    @Mock private MBeanInfo threadingMBeanInfo;
    @Mock private MBeanAttributeInfo peakThreadCountMBeanAttributeInfo;
    @Mock private MBeanAttributeInfo totalStartedThreadCountMBeanAttributeInfo;

    private MBeanVerifier mBeanVerifier;

    @Before
    public void setUp() throws Exception {
        mBeanVerifier = new MBeanVerifier(mBeanServer);

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

        mBeanVerifier.verify(DEFAULT_BEANS.get(0));
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

        mBeanVerifier.verify(DEFAULT_BEANS.get(0));
    }

    @Test
    public void verifyThrowsExceptionIfAnyOtherExceptionIsThrown() throws Exception {
        when(mBeanServer.queryMBeans(null, null)).thenThrow(new RuntimeException());

        expectedException.expect(MBeanVerificationException.class);
        expectedException.expectCause(isA(RuntimeException.class));
        expectedException.expectMessage("Exception occurred");

        mBeanVerifier.verify(DEFAULT_BEANS.get(0));
    }

    @Test
    public void verifyDoesNotThrowExceptionIfMBeanAndItsAttributesSpecifiedInConfigurationExist() throws Exception {
        mBeanVerifier.verify(DEFAULT_BEANS.get(0));
    }
}