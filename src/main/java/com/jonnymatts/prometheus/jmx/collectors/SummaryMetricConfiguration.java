package com.jonnymatts.prometheus.jmx.collectors;

import com.jonnymatts.prometheus.jmx.configuration.QuantileConfiguration;

import java.util.List;

public class SummaryMetricConfiguration {
    private String name;
    private List<QuantileConfiguration> quantiles;
    private Integer ageBuckets;
    private Long maxAgeSeconds;

    public SummaryMetricConfiguration() {}

    public SummaryMetricConfiguration(String name, List<QuantileConfiguration> quantiles, Integer ageBuckets, Long maxAgeSeconds) {
        this.name = name;
        this.quantiles = quantiles;
        this.ageBuckets = ageBuckets;
        this.maxAgeSeconds = maxAgeSeconds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<QuantileConfiguration> getQuantiles() {
        return quantiles;
    }

    public void setQuantiles(List<QuantileConfiguration> quantiles) {
        this.quantiles = quantiles;
    }

    public Integer getAgeBuckets() {
        return ageBuckets;
    }

    public void setAgeBuckets(int ageBuckets) {
        this.ageBuckets = ageBuckets;
    }

    public Long getMaxAgeSeconds() {
        return maxAgeSeconds;
    }

    public void setMaxAgeSeconds(long maxAgeSeconds) {
        this.maxAgeSeconds = maxAgeSeconds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SummaryMetricConfiguration that = (SummaryMetricConfiguration) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (quantiles != null ? !quantiles.equals(that.quantiles) : that.quantiles != null) return false;
        if (ageBuckets != null ? !ageBuckets.equals(that.ageBuckets) : that.ageBuckets != null) return false;
        return maxAgeSeconds != null ? maxAgeSeconds.equals(that.maxAgeSeconds) : that.maxAgeSeconds == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (quantiles != null ? quantiles.hashCode() : 0);
        result = 31 * result + (ageBuckets != null ? ageBuckets.hashCode() : 0);
        result = 31 * result + (maxAgeSeconds != null ? maxAgeSeconds.hashCode() : 0);
        return result;
    }
}