package com.jonnymatts.prometheus.jmx.collectors;

import com.jonnymatts.prometheus.jmx.configuration.ExponentialBucketConfiguration;
import com.jonnymatts.prometheus.jmx.configuration.LinearBucketConfiguration;

import java.util.List;

public class HistogramMetricConfiguration {
    private String name;
    private List<Double> buckets;
    private ExponentialBucketConfiguration exponentialBuckets;
    private LinearBucketConfiguration linearBuckets;

    public HistogramMetricConfiguration() {}

    public HistogramMetricConfiguration(String name,
                                        List<Double> buckets,
                                        ExponentialBucketConfiguration exponentialBuckets,
                                        LinearBucketConfiguration linearBuckets) {
        this.name = name;
        this.buckets = buckets;
        this.exponentialBuckets = exponentialBuckets;
        this.linearBuckets = linearBuckets;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Double> getBuckets() {
        return buckets;
    }

    public void setBuckets(List<Double> buckets) {
        this.buckets = buckets;
    }

    public ExponentialBucketConfiguration getExponentialBuckets() {
        return exponentialBuckets;
    }

    public void setExponentialBuckets(ExponentialBucketConfiguration exponentialBuckets) {
        this.exponentialBuckets = exponentialBuckets;
    }

    public LinearBucketConfiguration getLinearBuckets() {
        return linearBuckets;
    }

    public void setLinearBuckets(LinearBucketConfiguration linearBuckets) {
        this.linearBuckets = linearBuckets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HistogramMetricConfiguration that = (HistogramMetricConfiguration) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (buckets != null ? !buckets.equals(that.buckets) : that.buckets != null) return false;
        if (exponentialBuckets != null ? !exponentialBuckets.equals(that.exponentialBuckets) : that.exponentialBuckets != null)
            return false;
        return linearBuckets != null ? linearBuckets.equals(that.linearBuckets) : that.linearBuckets == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (buckets != null ? buckets.hashCode() : 0);
        result = 31 * result + (exponentialBuckets != null ? exponentialBuckets.hashCode() : 0);
        result = 31 * result + (linearBuckets != null ? linearBuckets.hashCode() : 0);
        return result;
    }
}
