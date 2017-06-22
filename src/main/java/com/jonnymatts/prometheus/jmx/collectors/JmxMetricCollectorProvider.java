package com.jonnymatts.prometheus.jmx.collectors;

import com.jonnymatts.prometheus.jmx.configuration.BeanAttribute;
import com.jonnymatts.prometheus.jmx.configuration.BeanAttributeMetricCollectorReference;
import com.jonnymatts.prometheus.jmx.configuration.Configuration;
import com.jonnymatts.prometheus.jmx.configuration.MetricCollectorType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

public class JmxMetricCollectorProvider {

    private JmxMetricHistogram defaultHistogram;
    private JmxMetricCounter defaultCounter;
    private JmxMetricGauge defaultGauge;
    private JmxMetricSummary defaultSummary;
    private Map<String, JmxMetricHistogram> customHistograms;
    private Map<String, JmxMetricSummary> customSummaries;

    public JmxMetricCollectorProvider() {
        this(
                JmxMetricCounter::new,
                JmxMetricGauge::new,
                JmxMetricHistogram::new,
                JmxMetricSummary::new,
                new HashMap<>(),
                new HashMap<>()
        );
    }

    public JmxMetricCollectorProvider(Supplier<JmxMetricCounter> defaultCounterSupplier,
                                      Supplier<JmxMetricGauge> defaultGaugeSupplier,
                                      Supplier<JmxMetricHistogram> defaultHistogramSupplier,
                                      Supplier<JmxMetricSummary> defaultSummarySupplier,
                                      Map<String, JmxMetricHistogram> customHistograms,
                                      Map<String, JmxMetricSummary> customSummaries) {
        this.defaultCounter = defaultCounterSupplier.get();
        this.defaultGauge = defaultGaugeSupplier.get();
        this.defaultHistogram = defaultHistogramSupplier.get();
        this.defaultSummary = defaultSummarySupplier.get();
        this.customHistograms = customHistograms;
        this.customSummaries = customSummaries;
    }

    public void createCollectors(Configuration configuration) {
        registerRequiredDefaultMetricCollectors(configuration);

        ofNullable(configuration.getHistograms())
                .ifPresent(histograms -> histograms.forEach(this::createNewHistogram));
        ofNullable(configuration.getSummaries())
                .ifPresent(summaries -> summaries.forEach(this::createNewSummary));
    }

    public JmxMetricCounter counter() {
        return defaultCounter;
    }

    public JmxMetricGauge gauge() {
        return defaultGauge;
    }

    public JmxMetricHistogram histogram() {
        return defaultHistogram;
    }

    public JmxMetricHistogram histogram(String name) {
        final JmxMetricHistogram jmxMetricHistogram = customHistograms.get(name);
        return jmxMetricHistogram;
    }

    public JmxMetricSummary summary(String name) {
        final JmxMetricSummary jmxMetricSummary = customSummaries.get(name);
        return jmxMetricSummary;
    }

    public JmxMetricSummary summary() {
        return defaultSummary;
    }

    private void registerRequiredDefaultMetricCollectors(Configuration configuration) {
        final List<BeanAttributeMetricCollectorReference> collectorReferences = configuration.getBeans().stream()
                .flatMap(bean ->
                        bean.getAttributes().stream().map(BeanAttribute::getCollectorReference)
                ).collect(toList());

        if(referenceExistsSuchThat(collectorReferences, ref -> ref.getType().equals(MetricCollectorType.COUNTER))) {
            defaultCounter.register();
        }
        if(referenceExistsSuchThat(collectorReferences, ref -> ref.getType().equals(MetricCollectorType.GAUGE))) {
            defaultGauge.register();
        }
        if(referenceExistsSuchThat(collectorReferences, ref -> ref.getType().equals(MetricCollectorType.HISTOGRAM) && ref.getName() == null)) {
            defaultHistogram.register();
        }
        if(referenceExistsSuchThat(collectorReferences, ref -> ref.getType().equals(MetricCollectorType.SUMMARY) && ref.getName() == null)) {
            defaultSummary.register();
        }
    }

    private boolean referenceExistsSuchThat(List<BeanAttributeMetricCollectorReference> collectorReferences,
                                            Predicate<BeanAttributeMetricCollectorReference> predicate) {
        return collectorReferences.stream().anyMatch(predicate);
    }

    private void createNewHistogram(HistogramMetricConfiguration histogram) {
        final JmxMetricHistogram newHistogram = new JmxMetricHistogram(histogram);
        newHistogram.register();
        customHistograms.put(histogram.getName(), newHistogram);
    }

    private void createNewSummary(SummaryMetricConfiguration summary) {
        final JmxMetricSummary newSummary = new JmxMetricSummary(summary);
        newSummary.register();
        customSummaries.put(summary.getName(), newSummary);
    }
}